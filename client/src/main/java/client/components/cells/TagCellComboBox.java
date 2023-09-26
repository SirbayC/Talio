package client.components.cells;

import client.utils.CustomizationUtils;
import commons.entities.Tag;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class TagCellComboBox extends ListCell<Tag> {

    private final Text text = new Text();

    private final Circle circle = new Circle();

    @Override
    protected void updateItem(Tag item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null || empty) {
            setGraphic(null);
            return;
        }
        setText(null);
        text.setText(item.getName());

        circle.setRadius(9);

        if(item.getColour() != null) {
            circle.setFill(Color.web(item.getColour()));
        } else {
            circle.setFill(Color.web(new CustomizationUtils().getDefaultBoardInnerColor()));
        }
        HBox hbox = new HBox();

        hbox.getChildren().addAll(circle, text);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(5);
        setGraphic(hbox);
    }

}
