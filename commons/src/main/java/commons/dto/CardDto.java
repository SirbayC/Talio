package commons.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A DTO for the {@link commons.entities.Card} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CardDto implements Serializable {

    private long id;
    private Long cardListId;
    private String title;
    private String description;
    private LocalDate date;
    private Integer indexInCardList;
    private List<TaskDto> taskList;
    private List<TagDto> tags;

    private String color;

    /**
     * Transform the DTO in a set of differences between its start state and the provided DTO
     *
     * @param dto card dto
     * @return same instance
     */
    public CardDto diff(CardDto dto) {
        id = dto.id;
        cardListId = dto.getCardListId();
        if(Objects.equals(dto.title, title))
            title = null;

        if(Objects.equals(dto.description, description))
            description = null;

        if(Objects.equals(dto.date, date))
            date = null;

        if(Objects.equals(dto.indexInCardList, indexInCardList))
            indexInCardList = null;

        if(Objects.equals(dto.taskList, taskList))
            taskList = null;

        if(Objects.equals(dto.tags, tags))
            tags = null;

        if(Objects.equals(dto.color, color))
            color = null;

        return this;
    }

}