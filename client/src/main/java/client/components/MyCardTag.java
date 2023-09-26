package client.components;

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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

public class MyCardTag extends HBox implements ButtonCtrl {

    @FXML
    private Text title;

    @FXML
    private Button removeTagButton;

    @FXML
    private HBox cardBox;

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
    public MyCardTag(ObservableBoard observableBoard, ListView<Tag> tags) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/components/CardTag.fxml"));
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
        List.of(removeTagButton)
            .forEach(this::setHoverEffects);
    }

    /**
     * Displays context buttons when mouse enters card
     */
    @SuppressWarnings("unused")
    public void cardBoxOnMouseEnter() {
        removeTagButton.setVisible(true);
    }

    /**
     * Hides context buttons when mouse leaves card
     */
    @SuppressWarnings("unused")
    public void cardBoxOnMouseExit() {
        removeTagButton.setVisible(false);
    }

    /**
     * Sets the card's fields
     *
     * @param tag card instance
     */
    public void setTag(Tag tag) {
        this.tag = tag;
        title.setText(tag.getName());
        cardBox.setStyle("-fx-background-color: #" + tag.getColour().substring(2) + ";  -fx-border-style: solid; "
            + "-fx-border-width: 1; -fx-border-color: black;");
        if(tag.getColour() != null) {
            title.setFill(customizationUtils.getContrastingColor("#" + tag.getColour().substring(2)));
            removeTagButton.setStyle(removeTagButton.getStyle() +
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
        tags.getItems().remove(tag);
    }
}
