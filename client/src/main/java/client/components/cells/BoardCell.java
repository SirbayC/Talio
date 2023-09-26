package client.components.cells;

import client.utils.CustomizationUtils;
import commons.entities.Board;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class BoardCell extends ListCell<Board> {

    private final Text text = new Text();

    private final Circle circle = new Circle();

    @Override
    protected void updateItem(Board item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null || empty) {
            setGraphic(null);
            return;
        }
        setText(null);
        text.setText(item.getName());

        circle.setRadius(9);

        if(item.getInnerColor() != null) {
            circle.setFill(Color.web(item.getInnerColor()));
        } else {
            circle.setFill(Color.web(new CustomizationUtils().getDefaultBoardInnerColor()));
        }

        if(item.getOuterColor() != null) {
            circle.setStroke(Color.web(item.getOuterColor()));
        } else {
            circle.setStroke(Color.web(new CustomizationUtils().getDefaultBoardOuterColor()));
        }

        circle.setStrokeWidth(5);

        HBox hbox = new HBox();

        hbox.getChildren().addAll(circle, text);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(5);
        setGraphic(hbox);
    }

}
