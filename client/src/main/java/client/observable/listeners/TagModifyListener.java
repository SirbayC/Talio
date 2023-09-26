package client.observable.listeners;

import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import commons.entities.Tag;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TagModifyListener implements MapChangeListener<Tag> {

    private final ObservableList<Tag> tags;

    /**
     * Modify the TagList's collection of Cards based on the ChangeType
     *
     * @param value      current value
     * @param changeType type of the change
     */
    @Override
    public void listen(Tag value, ChangeType changeType) {
        if(changeType == ChangeType.UPDATE)
            return;

        switch(changeType) {
            case ADD -> {tags.add(value);}
            case DELETE -> {tags.remove(value);}
        }
    }
}
