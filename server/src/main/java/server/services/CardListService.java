package server.services;

import commons.dto.CardListDto;
import commons.entities.CardList;
import commons.mappers.CardListMapper;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardListRepository;

@Service
@AllArgsConstructor
public class CardListService {

    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;
    private final CardListMapper cardListMapper;

    /**
     * Update an existing or create a new card list on a board based on a DTO
     *
     * @param boardId board key
     * @param dto     card list DTO
     * @return card list
     */
    public Optional<CardList> updateOrCreateFromDto(long boardId, CardListDto dto) {
        CardList cardList = cardListRepository.findCardListByBoard_IdAndId(boardId, dto.getId())
            .map(cl -> cardListMapper.partialUpdate(dto, cl))
            .orElseGet(() -> fromDto(boardId, dto));

        if(cardList != null) {
            cardListRepository.save(cardList);
            dto.setId(cardList.getId());
        }

        return Optional.ofNullable(cardList);
    }

    /**
     * Get a new CardList on a board based on a DTO
     *
     * @param boardId board key
     * @param dto     card list DTO
     * @return card list
     */
    public CardList fromDto(long boardId, CardListDto dto) {
        return boardRepository.findById(boardId).map(board -> {
            CardList cardList = cardListMapper.toEntity(dto);
            cardList.setIndexInBoard(board.getCardLists().size());
            cardList.setBoard(board);

            dto.setIndexInBoard(cardList.getIndexInBoard());
            return cardList;
        }).orElse(null);
    }

    /**
     * Delete card list on board
     *
     * @param boardId    board key
     * @param cardListId card list key
     * @return whether it got deleted or not
     */
    public boolean delete(long boardId, long cardListId) {
        return cardListRepository.findCardListByBoard_IdAndId(
            boardId,
            cardListId
        ).map(cardList -> {
            try {
                cardListRepository.delete(cardList);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).orElse(false);
    }

}