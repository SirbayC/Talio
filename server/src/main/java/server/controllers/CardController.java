package server.controllers;

import commons.dto.CardDto;
import commons.dto.CardTagDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.CardService;

@AllArgsConstructor
@RestController
@RequestMapping("boards/{boardId}/cards")
public class CardController {

    private final CardService cardService;

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param dto     card list data transfer model
     * @return the card list dto
     */
    @MessageMapping("/boards/{boardId}/cards")
    @SendTo("/out/boards/{boardId}/cards")
    public CardDto sendUpdate(@DestinationVariable long boardId, CardDto dto) {
        return cardService.updateOrCreateFromDto(boardId, dto)
            .map(cl -> dto)
            .orElse(null);
    }

    /**
     * Sends notification to the client that there has been an update on the
     *
     * @param dto     card tag dto entity
     * @return the card tag dto entity
     */
    @MessageMapping("/boards/{boardId}/linkCardTag")
    @SendTo("/out/boards/{boardId}/linkCardTag")
    public CardTagDto linkCards(CardTagDto dto) {
        return cardService.linkCardTagService(dto) ? dto : null;
    }

    /**
     * Sends notification to the client that there has been an update
     *
     * @param boardId the id of the board, used as an identifier for the connection
     * @param key     card list key
     * @return the card list key to delete
     */
    @MessageMapping("/boards/{boardId}/cards/delete")
    @SendTo("/out/boards/{boardId}/cards/delete")
    public long sendUpdate(@DestinationVariable long boardId, long key) {
        return cardService.delete(boardId, key) ? key : -1;
    }

}
