package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.Card;
import commons.entities.Task;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskModifyListener implements MapChangeListener<Task> {

    private final ObservableBoard board;

    /**
     * Listen to task changes
     *
     * @param value      current value
     * @param changeType type of the change
     */
    @Override
    public void listen(Task value, ChangeType changeType) {
        if(changeType == ChangeType.UPDATE)
            return;

        Card card = board.getCards().get(value.getCard().getId());
        if(card == null)
            return;

        switch(changeType) {
            case ADD -> card.getTaskList().add(value.getIndexInCard(), value);
            case DELETE -> card.getTaskList().remove(value);
        }

        for(int i = 0; i < card.getTaskList().size(); i++) {
            card.getTaskList().get(i).setIndexInCard(i);
        }

        board.triggerCard(card.getId());
    }
}
