package server.services;

import commons.dto.TaskDto;
import commons.entities.Task;
import commons.mappers.TaskMapper;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import server.database.CardRepository;
import server.database.TaskRepository;

@Service
@AllArgsConstructor
public class TaskService {

    private final CardRepository cardRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Update an existing or create a new task on a board based on a DTO
     *
     * @param boardId board key
     * @param taskDto task DTO
     * @return card
     */
    public Optional<Task> updateOrCreateFromDto(long boardId, TaskDto taskDto) {
        Task task = taskRepository.findTaskByCardIdAndId(taskDto.getCardId(), taskDto.getId())
            .map(t -> taskMapper.partialUpdate(taskDto, t))
            .orElseGet(() -> fromDto(boardId, taskDto));

        if(task != null) {
            taskRepository.save(task);
            taskDto.setId(task.getId());
        }

        return Optional.ofNullable(task);
    }

    /**
     * Get a new Task on a board based on a DTO
     *
     * @param boardId board key
     * @param taskDto task DTO
     * @return task
     */
    public Task fromDto(long boardId, TaskDto taskDto) {
        return cardRepository.findCardByBoard_IdAndId(boardId, taskDto.getCardId())
            .map(card -> {
                Task task = taskMapper.toEntity(taskDto);
                task.setIndexInCard(card.getTaskList().size());
                task.setCard(card);

                taskDto.setCompleted(false);
                taskDto.setIndexInCard(task.getIndexInCard());
                return task;
            }).orElse(null);
    }

    /**
     * Delete task on card
     *
     * @param boardId task key
     * @param taskId task key
     * @return whether it got deleted or not
     */
    public boolean delete(long boardId, long taskId) {
        return taskRepository.findTaskByBoardIdAndId(
            boardId,
            taskId
        ).map(task -> {
            try {
                taskRepository.delete(task);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).orElse(false);
    }
}
