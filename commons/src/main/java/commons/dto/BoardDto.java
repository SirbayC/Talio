package commons.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A DTO for the {@link commons.entities.Board} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BoardDto implements Serializable {

    private long id;
    private String name;
    private List<CardListDto> cardLists = new ArrayList<>();
    private String outerColor;
    private String innerColor;
    private String cardListBorderColor;
    private String cardListBackgroundColor;
    private String font;
    private List<TagDto> tags = new ArrayList<>();

}