package client.components;

import client.components.cells.TagCell;
import client.components.dialog.TagDialog;
import client.observable.ObservableBoard;
import client.observable.listeners.TagModifyListener;
import client.scenes.ButtonCtrl;
import commons.entities.Tag;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TagList implements ButtonCtrl {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private ListView<Tag> tags;

    private ObservableBoard observableBoard;

    private Stage stage;
    private Runnable unregister;

    /**
     * Sets the hover effects
     */
    @Override
    public void setHoverEffects() {
        List.of(addButton, backButton)
            .forEach(this::setHoverEffects);
    }

    /**
     * Show the dialog and wait for state change Sets the currently edited card to the specified card and the new title
     *
     * @param board the observable board
     * @param stage the stage
     */
    public void load(ObservableBoard board, Stage stage) {
        this.tags.setCellFactory(param -> new TagCell(observableBoard, this.tags));
        this.tags.setItems(FXCollections.observableArrayList(board.getTags().values()));

        this.stage = stage;
        this.observableBoard = board;
        // Listen to changes in tags

        unregister = board.listenToTags(new TagModifyListener(this.tags.getItems()));
    }

    /**
     * Shows the pop-up for the card details
     *
     * @param board the observable board
     */
    public static void show(ObservableBoard board) {
        FXMLLoader fxmlLoader = new FXMLLoader(TagList.class.getResource("/client/components/TagList.fxml"));

        try {
            Parent parent = fxmlLoader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            TagList dialog = fxmlLoader.getController();
            dialog.setHoverEffects();
            dialog.load(board, stage);

            stage.setTitle("Tag view");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Shows the add Tag dialog
     */
    public void add() {
        TagDialog.show(
            "Add Tag",
            null
        ).thenAccept(dto -> observableBoard.send(
            "/boards/{boardId}/tags",
            dto
        ));
    }

    /**
     * Closes the scene
     */
    public void back() {
        ForkJoinPool.commonPool().execute(unregister);
        stage.close();
    }

}
