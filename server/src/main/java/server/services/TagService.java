package server.services;

import commons.dto.TagDto;
import commons.entities.Tag;
import commons.mappers.TagMapper;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.TagRepository;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final BoardRepository boardRepository;
    private final TagMapper tagMapper;

    /**
     * Update an existing or create a new card on a board based on a DTO
     *
     * @param boardId board key
     * @param dto     tag DTO
     * @return tag
     */
    public Optional<Tag> updateOrCreateFromDto(long boardId, TagDto dto) {
        Tag tag = tagRepository.findTagByBoard_IdAndId(boardId, dto.getId())
            .map(tag1 -> tagMapper.partialUpdate(dto, tag1))
            .orElseGet(() -> fromDto(boardId, dto));

        if(tag != null) {
            tagRepository.save(tag);
            dto.setId(tag.getId());
        }

        return Optional.ofNullable(tag);
    }

    /**
     * Get a new Tag on a board based on a DTO
     *
     * @param boardId board key
     * @param dto     tag DTO
     * @return tag
     */
    public Tag fromDto(long boardId, TagDto dto) {
        return boardRepository.findById(boardId)
            .map(board -> tagMapper.toEntity(dto).setBoard(board))
            .orElse(null);
    }

    /**
     * Delete tag on board
     *
     * @param boardId board key
     * @param tagId   tag key
     * @return whether it got deleted or not
     */
    public boolean delete(long boardId, long tagId) {
        return tagRepository.findTagByBoard_IdAndId(
            boardId,
            tagId
        ).map(tag -> {
            try {
                tagRepository.unlinkTag(tagId);
                tag.getCards().clear();
                tagRepository.delete(tag);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).orElse(false);

    }
}
