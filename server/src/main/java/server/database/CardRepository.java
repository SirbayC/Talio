package server.database;

import commons.entities.Card;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Get card from board and own id
     *
     * @param boardId board key
     * @param id      own identifier
     * @return card instance
     */
    Optional<Card> findCardByBoard_IdAndId(long boardId, long id);

    /**
     * Add a link between a card and a tag
     *
     * @param cardId card identifier
     * @param tagId  tag identifier
     * @return number of rows changed
     */
    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO CARD_TAGS(CARDS_ID, TAGS_ID) VALUES (:cardId,:tagId)",
        countQuery = "SELECT count(*) FROM CARD_TAGS",
        nativeQuery = true
    )
    int linkCardToTag(@Param("cardId") long cardId, @Param("tagId") long tagId);

    /**
     * Remove a link between a card and a tag
     *
     * @param cardId card identifier
     * @param tagId  tag identifier
     * @return number of rows changed
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM CARD_TAGS WHERE CARDS_ID=:cardId AND TAGS_ID=:tagId",
        countQuery = "SELECT count(*) FROM CARD_TAGS",
        nativeQuery = true
    )
    int unlinkCardFromTag(@Param("cardId") long cardId, @Param("tagId") long tagId);

    /**
     * Remove all links between a card and its tags
     *
     * @param cardId card identifier
     * @return number of rows changed
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM CARD_TAGS WHERE CARDS_ID=:cardId",
        countQuery = "SELECT count(*) FROM CARD_TAGS",
        nativeQuery = true
    )
    int unlinkCard(@Param("cardId") long cardId);

}
