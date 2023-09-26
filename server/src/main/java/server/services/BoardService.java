package server.services;

import commons.dto.BoardDto;
import commons.entities.Board;
import commons.mappers.BoardMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final List<DeferredResult<List<BoardDto>>> results = new ArrayList<>();
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    /**
     * Poll for an updated list
     *
     * @param duration in seconds, after how much time of no activity should the server timeout
     * @return differed result
     */
    public synchronized DeferredResult<List<BoardDto>> poll(long duration) {
        DeferredResult<List<BoardDto>> result = new DeferredResult<>(TimeUnit.SECONDS.toMillis(duration));
        results.add(result);

        return result;
    }

    /**
     * Get or create board by name
     *
     * @param boardDto boardDto
     * @return board
     */
    public Board getOrCreate(BoardDto boardDto) {
        return boardRepository.findBoardByName(boardDto.getName())
            .orElseGet(() -> createFromBoardDto(boardDto));
    }

    /**
     * Create board from name
     *
     * @param dto board dto
     * @return board
     */
    public Board createFromBoardDto(BoardDto dto) {
        Board b = boardMapper.toEntity(dto)
            .setCards(new ArrayList<>())
            .setCardLists(new ArrayList<>());

        boardRepository.save(b);
        resolve();
        return b;
    }

    /**
     * Update board from DTO (notifies clients of deletion using long polling)
     *
     * @param dto DTO
     * @return whether it was updated or not
     */
    public boolean updateFromDto(BoardDto dto) {
        return boardRepository.findById(dto.getId()).map(board -> {
            board = boardMapper.partialUpdate(dto, board);
            boardRepository.save(board);

            resolve();
            return true;
        }).orElse(false);
    }

    /**
     * Delete board by id (notifies clients of deletion using long polling)
     *
     * @param boardId board key
     * @return whether it was successfully deleted
     */
    public boolean delete(long boardId) {
        return boardRepository.findById(boardId)
            .map(board -> {
                try {
                    boardRepository.delete(board);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }).map(board -> {
                resolve();
                return board;
            }).orElse(false);
    }

    /**
     * In case of board addition, triggers an update to the client
     */
    public synchronized void resolve() {
        List<BoardDto> boards = boardRepository.findAll()
            .stream()
            .map(boardMapper::toDto)
            .toList();

        results.forEach(result -> {
            if(result.isSetOrExpired())
                return;

            result.setResult(boards);
        });

        results.clear();
    }

}
