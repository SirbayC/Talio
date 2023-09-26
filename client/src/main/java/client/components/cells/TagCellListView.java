package client.components.cells;

import client.components.MyCardTag;
import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import commons.entities.Tag;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.Getter;

public class TagCellListView extends ListCell<Tag> {

    @Getter
    private final MyCardTag myCardTag;

    /**
     * Tag Cell
     *
     * @param observableBoard observable board
     * @param tags            ref
     */
    public TagCellListView(ObservableBoard observableBoard, ListView<Tag> tags) {
        setStyle("-fx-padding: 10 0 0 0; -fx-background-color: none;");
        myCardTag = new MyCardTag(observableBoard, tags);

        Runnable unregister = observableBoard.listenToTags(((value, changeType) -> {
            if(changeType == ChangeType.DELETE)
                return;

            if(myCardTag.getTag() == null)
                return;

            if(!myCardTag.getTag().equals(value))
                return;

            myCardTag.setTag(value);
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

        myCardTag.setTag(tag);
        setGraphic(myCardTag);
    }


}
