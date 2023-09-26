package server.database;

import commons.entities.Board;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * Find a board by its name
     *
     * @param name board name
     * @return optional board
     */
    Optional<Board> findBoardByName(String name);

}
