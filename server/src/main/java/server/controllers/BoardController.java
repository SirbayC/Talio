package server.controllers;

import commons.dto.BoardDto;
import commons.mappers.BoardMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardRepository;
import server.services.BoardService;

@RestController
@AllArgsConstructor
@RequestMapping("boards")
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final BoardService boardService;

    /**
     * Get all boards
     *
     * @return list of boards
     */
    @GetMapping({"", "/"})
    public List<BoardDto> all() {
        return boardRepository.findAll()
            .stream()
            .map(boardMapper::toDto)
            .peek(dto -> dto.setCardLists(null).setTags(null)) //do not return full board, only name and ID
            .toList();
    }

    /**
     * @param boardDto new board with name
     * @return a board with the specified name
     */
    @PostMapping({"", "/"})
    public BoardDto createBoard(@RequestBody BoardDto boardDto) {
        return boardMapper.toDto(boardService.getOrCreate(boardDto));
    }

    /**
     * Long poll board list
     *
     * @return deferred result (actually waits until it has something to return)
     */
    @GetMapping("poll")
    public DeferredResult<List<BoardDto>> poll() {
        return boardService.poll(30);
    }

    /**
     * Get board by id
     *
     * @param boardId board identity
     * @return board dto
     */
    @GetMapping("{boardId}")
    public ResponseEntity<BoardDto> get(@PathVariable long boardId) {
        return boardRepository.findById(boardId)
            .map(boardMapper::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Sends notification to the client that there has been an update on a board
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param dto     the updated board dto
     * @return the updated board dto (null if the database update failed)
     */
    @MessageMapping("/boards/{boardId}")
    @SendTo("/out/boards/{boardId}")
    public BoardDto sendUpdate(@DestinationVariable long boardId, BoardDto dto) {
        return boardService.updateFromDto(dto.setId(boardId)) ? dto : null;
    }

    /**
     * Sends notification to the client that a board was deleted
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @return the boardId, or -1 if the deletion on the database side has failed
     */
    @MessageMapping("/boards/{boardId}/delete")
    @SendTo("/out/boards/{boardId}/delete")
    public long sendDeleteUpdate(@DestinationVariable long boardId) {
        return boardService.delete(boardId) ? boardId : -1;
    }

}
