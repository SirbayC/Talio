package client.observable.listeners;

import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.CardList;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardListOrderListener implements MapChangeListener<CardList> {

    private final ObservableList<CardList> list;

    /**
     * Listen to index changes of card lists
     *
     * @param value      current value
     * @param changeType type of the change
     */
    @Override
    public void listen(CardList value, ChangeType changeType) {
        if(changeType != ChangeType.UPDATE)
            return;

        int current = list.indexOf(value);
        if(current == -1)
            return;

        if(value.getIndexInBoard() == null || current == value.getIndexInBoard())
            return;

        List<CardList> cardLists = new ArrayList<>(list);
        cardLists.remove(value);
        cardLists.add(value.getIndexInBoard(), value);

        list.setAll(cardLists);
    }

}
