package server.controllers;

import commons.dto.TagDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.TagService;

@RestController
@AllArgsConstructor
@RequestMapping("boards/{boardId}/tags")
public class TagController {

    private final TagService tagService;

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param dto     tag data transfer model
     * @return the tag list dto
     */
    @MessageMapping("/boards/{boardId}/tags")
    @SendTo("/out/boards/{boardId}/tags")
    public TagDto sendUpdate(@DestinationVariable long boardId, TagDto dto) {
        return tagService.updateOrCreateFromDto(boardId, dto)
            .map(tag -> dto)
            .orElse(null);
    }

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param key     tag key
     * @return the tag key to delete
     */
    @MessageMapping("/boards/{boardId}/tags/delete")
    @SendTo("/out/boards/{boardId}/tags/delete")
    public long sendUpdate(@DestinationVariable long boardId, long key) {
        return tagService.delete(boardId, key) ? key : -1;
    }
}
