package commons.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A DTO for the {@link commons.entities.CardList} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CardListDto implements Serializable {

    private long id;
    private String title;
    private Integer indexInBoard;
    private List<CardDto> cards;
    private String backgroundColor;
    private String borderColor;

    /**
     * Returns the differences between this and the dto
     *
     * @param dto cardList dto
     * @return some instance
     */
    public CardListDto diff(CardListDto dto) {
        if(dto == null)
            return this;

        id = dto.id;
        if(Objects.equals(dto.title, title))
            title = null;

        if(Objects.equals(dto.getCards(), cards))
            cards = null;

        if(Objects.equals(dto.getIndexInBoard(), indexInBoard))
            indexInBoard = null;

        if(Objects.equals(dto.getBackgroundColor(), backgroundColor))
            backgroundColor = null;

        if(Objects.equals(dto.getBorderColor(), borderColor))
            borderColor = null;

        return this;
    }

}