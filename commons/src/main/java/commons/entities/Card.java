package commons.entities;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.OneToMany;
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
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Board board;

    @ManyToOne
    private CardList cardList;

    private String title;

    private Integer indexInCardList;

    private String description;

    private LocalDate date;

    private String color;

    @ManyToMany(
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(
        mappedBy = "card",
        cascade = CascadeType.REMOVE,
        fetch = FetchType.EAGER
    )
    private List<Task> taskList = new ArrayList<>();

    /**
     * Change board that card belongs to
     *
     * @param board board
     * @return same instance
     */
    public Card setBoard(Board board) {
        if(board == null)
            return this;

        this.board = board;
        return this;
    }

    /**
     * Change card list that card belongs to
     *
     * @param cardList card list
     * @return same instance
     */
    public Card setCardList(CardList cardList) {
        if(cardList == null)
            return this;

        setBoard(cardList.getBoard());
        this.cardList = cardList;
        return this;
    }

    /**
     * Check whether the card has a description
     *
     * @return whether it has a description
     */
    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    /**
     * Check whether the card has a date
     *
     * @return whether it has a date
     */
    public boolean hasDate() {
        return date != null && !date.equals(LocalDate.of(2000, 1, 1));
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

        if(!(obj instanceof Card c))
            return false;

        return Objects.equals(id, c.id);
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
