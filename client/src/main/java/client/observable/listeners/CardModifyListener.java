package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.Card;
import commons.entities.CardList;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardModifyListener implements MapChangeListener<Card> {

    private final ObservableBoard board;

    /**
     * Modify the CardList's collection of Cards based on the ChangeType
     *
     * @param value      current value
     * @param changeType type of the change
     */
    @Override
    public void listen(Card value, ChangeType changeType) {
        if(changeType == ChangeType.UPDATE)
            return;

        CardList cardList = board.getCardLists().get(value.getCardList().getId());
        if(cardList == null)
            return;

        switch(changeType) {
            case ADD -> cardList.getCards().add(value.getIndexInCardList(), value);
            case DELETE -> cardList.getCards().remove(value);
        }

        for(int i = 0; i < cardList.getCards().size(); i++)
            cardList.getCards().get(i).setIndexInCardList(i);

        board.triggerCardList(cardList.getId());
    }

}
