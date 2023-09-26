package commons.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String colour;

    private String font;

    @ManyToMany(
        mappedBy = "tags",
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE
    )
    private List<Card> cards = new ArrayList<>();

    @ManyToOne
    private Board board;

    /**
     * Sets the board of the Tag
     *
     * @param board the board of the tag
     * @return the Tag
     */
    public Tag setBoard(Board board) {
        if(board != null)
            this.board = board;

        return this;
    }

    /**
     * Check whether an object is equal to this object
     *
     * @param obj object to compare
     * @return whether it is equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(!(obj instanceof Tag t))
            return false;

        return Objects.equals(id, t.id);
    }
}
