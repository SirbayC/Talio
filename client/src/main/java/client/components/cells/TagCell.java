package client.components.cells;

import client.components.MyTag;
import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import commons.entities.Tag;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.Getter;

public class TagCell extends ListCell<Tag> {

    @Getter
    private final MyTag myTag;

    /**
     * Tag Cell
     *
     * @param observableBoard observable board
     * @param tags            ref
     */
    public TagCell(ObservableBoard observableBoard, ListView<Tag> tags) {
        setStyle("-fx-padding: 10 0 0 0; -fx-background-color: none;");
        myTag = new MyTag(observableBoard, tags);

        Runnable unregister = observableBoard.listenToTags(((value, changeType) -> {
            if(changeType == ChangeType.DELETE)
                return;

            if(myTag.getTag() == null)
                return;

            if(!myTag.getTag().equals(value))
                return;

            myTag.setTag(value);
        }));

        itemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                return;
            Platform.runLater(unregister);
        }));
    }

    @Override
    protected void updateItem(Tag tag, boolean empty) {
        super.updateItem(tag, empty);
        if(empty || tag == null) {
            setGraphic(null);
            return;
        }

        myTag.setTag(tag);
        setGraphic(myTag);
    }


}
