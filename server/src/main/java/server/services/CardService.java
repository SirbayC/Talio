package server.services;

import commons.dto.CardDto;
import commons.dto.CardTagDto;
import commons.entities.Card;
import commons.mappers.CardMapper;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;
import server.database.CardRepository;

@Service
@AllArgsConstructor
public class CardService {

    private final CardListRepository cardListRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    /**
     * Update an existing or create a new card on a board based on a DTO
     *
     * @param boardId board key
     * @param dto     card DTO
     * @return card
     */
    public Optional<Card> updateOrCreateFromDto(long boardId, CardDto dto) {
        Card card = cardRepository.findCardByBoard_IdAndId(boardId, dto.getId())
            .flatMap(c -> cardListRepository.findCardListByBoard_IdAndId(
                boardId,
                dto.getCardListId()
            ).map(cardList -> {
                cardMapper.partialUpdate(dto, c);
                return c.setCardList(cardList);
            })).orElseGet(() -> fromDto(boardId, dto));

        if(card != null) {
            cardRepository.save(card);
            dto.setId(card.getId());
        }

        return Optional.ofNullable(card);
    }

    /**
     * Get a new Card on a board based on a DTO
     *
     * @param boardId board key
     * @param dto     card DTO
     * @return card
     */
    public Card fromDto(long boardId, CardDto dto) {
        return cardListRepository.findCardListByBoard_IdAndId(boardId, dto.getCardListId())
            .map(cardList -> {
                Card card = cardMapper.toEntity(dto);
                card.setIndexInCardList(cardList.getCards().size());
                card.setCardList(cardList);

                dto.setIndexInCardList(card.getIndexInCardList());
                return card;
            }).orElse(null);
    }

    /**
     * Delete card on board
     *
     * @param boardId board key
     * @param cardId  card key
     * @return whether it got deleted or not
     */
    public boolean delete(long boardId, long cardId) {
        return cardRepository.findCardByBoard_IdAndId(
            boardId,
            cardId
        ).map(card -> {
            try {
                cardRepository.unlinkCard(cardId);
                card.getTags().clear();
                cardRepository.delete(card);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).orElse(false);
    }

    /**
     * Link/Unlink Card to/from Tag
     *
     * @param dto card tag dto
     * @return whether it was successful or not
     */
    public boolean linkCardTagService(CardTagDto dto) {
        int i;
        if(dto.isLink()) {
            i = cardRepository.linkCardToTag(dto.getCardId(), dto.getTagId());
        } else {
            i = cardRepository.unlinkCardFromTag(dto.getCardId(), dto.getTagId());
        }

        return i > 0;
    }

}