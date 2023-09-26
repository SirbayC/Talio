package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.Card;
import commons.entities.CardList;
import java.util.Comparator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardOrderListener implements MapChangeListener<Card> {

    private final ObservableBoard board;

    /**
     * Listen to index/list changes of cards
     *
     * @param card       current value
     * @param changeType type of the change
     */
    @Override
    public void listen(Card card, ChangeType changeType) {
        if(changeType != ChangeType.UPDATE)
            return;

        if(card.getIndexInCardList() == null)
            return;

        CardList cardList = board.getCardLists().get(card.getCardList().getId());
        if(cardList.getCards().contains(card)) {
            cardList.getCards().sort(Comparator.comparing(Card::getIndexInCardList));
            board.triggerCardList(cardList.getId());
            return;
        }

        switchList(card, cardList);
    }

    private void switchList(Card card, CardList cardList) {
        board.getCardLists()
            .values()
            .stream()
            .filter(cl -> cl.getCards().remove(card))
            .findAny()
            .ifPresent(cl -> {
                cardList.getCards().add(card.getIndexInCardList(), card);

                board.triggerCardList(cl.getId());
                board.triggerCardList(cardList.getId());
            });
    }

}
