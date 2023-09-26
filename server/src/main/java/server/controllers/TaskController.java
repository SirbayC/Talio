package server.controllers;

import commons.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.TaskService;

@RestController
@AllArgsConstructor
@RequestMapping("boards/{boardId}/cards/{cardId}/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId board id, identifier
     * @param taskDto task dto
     * @return task dto
     */
    @MessageMapping("/boards/{boardId}/tasks")
    @SendTo("/out/boards/{boardId}/tasks")
    public TaskDto sendUpdate(@DestinationVariable long boardId, TaskDto taskDto) {
        return taskService.updateOrCreateFromDto(boardId, taskDto)
            .map(t -> taskDto)
            .orElse(null);
    }

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the card, used as an identifier for the connection
     * @param taskId  task id
     * @return key to delete
     */
    @MessageMapping("/boards/{boardId}/tasks/delete")
    @SendTo("/out/boards/{boardId}/tasks/delete")
    public long sendUpdate(@DestinationVariable long boardId, long taskId) {
        return taskService.delete(boardId, taskId) ? taskId : -1;
    }

}
