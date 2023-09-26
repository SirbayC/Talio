package commons.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.Objects;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Board board;

    @ManyToOne
    private Card card;

    private String title;

    private Integer indexInCard;

    private boolean completed;


    /**
     * Set card for a task
     * @param card card
     * @return same instance
     */
    public Task setCard(Card card){
        if(card == null)
            return this;

        setBoard(card.getBoard());
        this.card = card;
        return this;
    }

    /**
     * Change board that card belongs to
     *
     * @param board board
     * @return same instance
     */
    public Task setBoard(Board board) {
        if(board == null)
            return this;

        this.board = board;
        return this;
    }


    /**
     * Equals method
     * @param obj - compared object
     * @return true/false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(!(obj instanceof Task t))
            return false;

        return Objects.equals(id, t.id);
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
        return this.getTitle();
    }

}
