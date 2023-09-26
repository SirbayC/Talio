package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.Card;
import commons.entities.Task;
import java.util.Comparator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskOrderListener implements MapChangeListener<Task> {

    private final ObservableBoard board;

    /**
     * Listen to task reordering
     *
     * @param value      current value
     * @param changeType type of the change
     */
    @Override
    public void listen(Task value, ChangeType changeType) {
        if(changeType != ChangeType.UPDATE)
            return;

        if(value.getIndexInCard() == null)
            return;

        Card card = board.getCards().get(value.getCard().getId());
        if(card.getTaskList().contains(value)) {
            card.getTaskList().sort(Comparator.comparing(Task::getIndexInCard));
            board.triggerCard(card.getId());
        }

    }
}
