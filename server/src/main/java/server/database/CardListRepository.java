package server.database;

import commons.entities.CardList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardListRepository extends JpaRepository<CardList, Long> {

    /**
     * Get a specific card list from a board
     *
     * @param boardId board key
     * @param id      id of the desired card list
     * @return cardlist (if found)
     */
    Optional<CardList> findCardListByBoard_IdAndId(long boardId, long id);
    
}
