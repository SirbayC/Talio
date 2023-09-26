package client.components;

import client.components.dialog.ConfirmDialog;
import client.components.dialog.TagDialog;
import client.observable.ObservableBoard;
import client.scenes.ButtonCtrl;
import client.utils.CustomizationUtils;
import commons.entities.Tag;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

public class MyTag extends VBox implements ButtonCtrl {

    @FXML
    private Text title;

    @FXML
    private Button removeTagButton;

    @FXML
    private Button editTagButton;

    @FXML
    private VBox cardBox;

    private final ObservableBoard observableBoard;

    private final ListView<Tag> tags;

    private final CustomizationUtils customizationUtils;

    @Getter
    private Tag tag;


    /**
     * Constructor for MyTag
     *
     * @param observableBoard the observable board of the tag
     * @param tags            the tags contained
     */
    public MyTag(ObservableBoard observableBoard, ListView<Tag> tags) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/components/Tag.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        customizationUtils = new CustomizationUtils();

        try {
            fxmlLoader.load();
            setHoverEffects();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.observableBoard = observableBoard;
        this.tags = tags;
    }


    /**
     * Sets the hover effects of the buttons
     */
    public void setHoverEffects() {
        List.of(removeTagButton, editTagButton)
            .forEach(this::setHoverEffects);
    }

    /**
     * Displays context buttons when mouse enters card
     */
    @SuppressWarnings("unused")
    public void cardBoxOnMouseEnter() {
        removeTagButton.setVisible(true);
        editTagButton.setVisible(true);
    }

    /**
     * Hides context buttons when mouse leaves card
     */
    @SuppressWarnings("unused")
    public void cardBoxOnMouseExit() {
        removeTagButton.setVisible(false);
        editTagButton.setVisible(false);
    }

    /**
     * Sets the card's fields
     *
     * @param tag card instance
     */
    public void setTag(Tag tag) {
        this.tag = tag;
        title.setText(tag.getName());
        cardBox.setStyle("-fx-background-radius: 20px; -fx-background-color: #" + tag.getColour().substring(2) + ";");
        if(tag.getColour() != null) {
            title.setFill(customizationUtils.getContrastingColor("#" + tag.getColour().substring(2)));
            removeTagButton.setStyle(removeTagButton.getStyle() +
                "-fx-background-color: " +
                (customizationUtils.getContrastingColor("#" + tag.getColour().substring(2)) == Color.WHITE
                    ? "white" : "black") + " ;");
            editTagButton.setStyle(editTagButton.getStyle() +
                "-fx-background-color: " +
                (customizationUtils.getContrastingColor("#" + tag.getColour().substring(2)) == Color.WHITE
                    ? "white" : "black") + " ;");
        }
        if(tag.getFont() != null) {
            title.setStyle(title.getStyle() +
                "-fx-font-family:" + tag.getFont() + " ;");
        }
    }

    /**
     * Shows the remove tag dialog and removes the tag
     */
    public void remove() {
        ConfirmDialog.show(
            "Delete tag",
            "Are you sure you want to remove the tag with title: " + tag.getName() + " ?"
        ).thenAccept(v -> observableBoard.send(
            "/boards/{boardId}/tags/delete",
            tag.getId()
        )).thenAccept(str -> {
            tags.getItems().remove(tag);
        });
    }

    /**
     * Shows the edit tag dialog
     */
    @SuppressWarnings("unused")
    public void edit() {
        TagDialog.show(
            "Edit Tag",
            tag
        ).thenAccept(dto -> observableBoard.send(
            "/boards/{boardId}/tags",
            dto
        ));
    }

}
