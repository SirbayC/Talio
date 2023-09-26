package server.database;

import commons.entities.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Get card from board and own id
     *
     * @param boardId board key
     * @param id      own identifier
     * @return card instance
     */
    Optional<Tag> findTagByBoard_IdAndId(long boardId, long id);

    /**
     * Remove all links between a tag and its cards
     *
     * @param tagId tag identifier
     * @return number of rows changed
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM CARD_TAGS WHERE TAGS_ID=:tagId",
        countQuery = "SELECT count(*) FROM CARD_TAGS",
        nativeQuery = true
    )
    int unlinkTag(@Param("tagId") long tagId);

}
