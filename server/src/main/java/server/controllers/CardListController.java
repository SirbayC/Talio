package server.controllers;

import commons.dto.CardListDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.CardListService;

@RestController
@AllArgsConstructor
@RequestMapping("boards/{boardId}/cardlists")
public class CardListController {

    private final CardListService cardListService;

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param dto     card list data transfer model
     * @return the card list dto
     */
    @MessageMapping("/boards/{boardId}/cardlists")
    @SendTo("/out/boards/{boardId}/cardlists")
    public CardListDto sendUpdate(@DestinationVariable long boardId, CardListDto dto) {
        return cardListService.updateOrCreateFromDto(boardId, dto)
            .map(cl -> dto)
            .orElse(null);
    }

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param key     card list key
     * @return the card list key to delete
     */
    @MessageMapping("/boards/{boardId}/cardlists/delete")
    @SendTo("/out/boards/{boardId}/cardlists/delete")
    public long sendUpdate(@DestinationVariable long boardId, long key) {
        return cardListService.delete(boardId, key) ? key : -1;
    }

}
