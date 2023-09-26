package commons.mappers;

import commons.dto.BoardDto;
import commons.entities.Board;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import commons.entities.CardList;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {CardListMapper.class, TagMapper.class}
)
public interface BoardMapper {

    // Global Instance
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    /**
     * Map DTO to Entity
     *
     * @param boardDto DTO
     * @return Entity
     */
    Board toEntity(BoardDto boardDto);

    /**
     * Map Entity to DTO
     *
     * @param board Entity
     * @return DTO
     */
    BoardDto toDto(Board board);

    /**
     * Partially update Entity with DTO
     *
     * @param boardDto DTO
     * @param board    Entity
     * @return Updated Entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Board partialUpdate(BoardDto boardDto, @MappingTarget Board board);

    /**
     * Link card lists after mapping from DTO to Entity
     *
     * @param board Entity
     */
    @AfterMapping
    default void linkCardLists(@MappingTarget Board board) {
        if(board.getCardLists() == null)
            board.setCardLists(new ArrayList<>());

        board.getCardLists().sort(Comparator.comparing(CardList::getIndexInBoard));
        board.getCardLists().forEach(cardList -> cardList.setBoard(board));
    }

    /**
     * Link cards after mapping from DTO to Entity
     *
     * @param board Entity
     */
    @AfterMapping
    default void linkCards(@MappingTarget Board board) {
        if(board.getCardLists() == null)
            board.setCardLists(new ArrayList<>());

        board.getCardLists().forEach(cardList -> cardList.getCards().forEach(card -> {
            card.setBoard(board);
            board.getCards().add(card);
        }));
    }

    /**
     * Link tags after mapping from DTO to Entity
     *
     * @param board Entity
     */
    @AfterMapping
    default void linkTags(@MappingTarget Board board) {
        if(board.getTags() == null)
            board.setTags(new ArrayList<>());

        board.getTags().forEach(tag -> tag.setBoard(board));
    }

    /**
     * Links tags to the cards on the board after mapping
     *
     * @param board - the tags' board
     */
    @AfterMapping
    default void linkTagsToCards(@MappingTarget Board board) {
        board.getCards().forEach(card -> {
            if(card.getTags() == null)
                card.setTags(new ArrayList<>());

            List.copyOf(card.getTags()).forEach(tag -> board.getTags()
                .stream()
                .filter(t -> t.getId() == tag.getId())
                .findAny()
                .ifPresent(t -> {
                    card.getTags().remove(tag);
                    card.getTags().add(t);
                    t.getCards().add(card);
                }));
        });
    }


}