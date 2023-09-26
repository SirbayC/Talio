package server.database;

import commons.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Get task from card and id
     * @param cardId card id of task
     * @param id id of task
     * @return task instance
     */
    @SuppressWarnings("unused")
    Optional<Task> findTaskByCardIdAndId(long cardId, Long id);

    /**
     * Get task from board id and id
     * @param boardId board id of task
     * @param id if od task
     * @return task instance
     */
    @SuppressWarnings("unused")
    Optional<Task> findTaskByBoardIdAndId(long boardId, Long id);

}
