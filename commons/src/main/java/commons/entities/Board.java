package commons.entities;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(
        mappedBy = "board",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.EAGER
    )
    private List<CardList> cardLists = new ArrayList<>();

    @OneToMany(
        mappedBy = "board",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.EAGER
    )
    private List<Card> cards = new ArrayList<>();

    @OneToMany(
        mappedBy = "board",
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE
    )
    private List<Tag> tags = new ArrayList<>();

    private String outerColor;

    private String innerColor;

    private String cardListBorderColor;

    private String cardListBackgroundColor;

    private String font;

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

        if(!(obj instanceof Board b))
            return false;

        return Objects.equals(id, b.id);
    }

    /**
     * Get hashcode of object
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * String representation of an object
     *
     * @return string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}
