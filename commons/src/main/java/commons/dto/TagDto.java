package commons.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TagDto implements Serializable {

    private long id;
    private String name;
    private String colour;
    private List<Long> cardIds;
    private String font;

    /**
     * Transform the DTO in a set of differences between its start state and the provided DTO
     *
     * @param dto tag dto
     * @return same instance
     */
    public TagDto diff(TagDto dto) {
        if(dto == null)
            return this;

        id = dto.id;
        if(Objects.equals(dto.name, name))
            name = null;

        if(Objects.equals(dto.colour, colour))
            colour = null;

        if(Objects.equals(dto.cardIds, cardIds))
            cardIds = null;

        if(Objects.equals(dto.font, font))
            font = null;

        return this;
    }

}
