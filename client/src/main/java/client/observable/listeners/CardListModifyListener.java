package client.observable.listeners;

import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.CardList;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardListModifyListener implements MapChangeListener<CardList> {

    private final ObservableList<CardList> list;

    /**
     * Modify the Board's collection of CardLists based on the ChangeType
     *
     * @param value      current value
     * @param changeType type of the change
     */
    @Override
    public void listen(CardList value, ChangeType changeType) {
        if(changeType == ChangeType.UPDATE)
            return;

        switch(changeType) {
            case ADD -> {
                if(value.getIndexInBoard() == null)
                    value.setIndexInBoard(list.size());

                list.add(value.getIndexInBoard(), value);
            }
            case DELETE -> list.remove(value);
        }
    }

}
