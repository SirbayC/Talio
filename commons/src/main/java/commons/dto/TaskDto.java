package commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link commons.entities.Task} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TaskDto implements Serializable{

    private long id;
    private String title;
    private Integer indexInCard;
    private Long cardId;
    private Boolean completed;

    /**
     * Trasnform the DTO in a set of differences between its start state and the provided DTO
     * @param taskDto task dto
     * @return same instance
     */
    public TaskDto diff(TaskDto taskDto){
        id = taskDto.id;
        cardId = taskDto.getCardId();
        if(Objects.equals(taskDto.title, title))
            title = null;

        if(Objects.equals(taskDto.title, title))
            title = null;

        if(Objects.equals(taskDto.indexInCard, indexInCard))
            indexInCard = null;

        return this;
    }
}
