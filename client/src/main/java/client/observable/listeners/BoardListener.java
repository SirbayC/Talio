package client.observable.listeners;

import client.utils.CustomizationUtils;
import commons.entities.Board;
import commons.entities.CardList;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BoardListener implements Consumer<Board> {

    private final Text boardName;

    private final AnchorPane boardBackground;
    private final Button settingsButton;
    private final CustomizationUtils customizationUtils;
    private final ListView<CardList> lists;
    private final Button backButton;

    /**
     * @param board           that was changed
     * @param boardName       the text containing the name of the board
     * @param boardBackground the background of the board
     * @param settingsButton  the settings button appearing in the board overview
     * @param lists           the list view containing the card lists
     * @param backButton
     */
    public BoardListener(Board board,
        Text boardName,
        AnchorPane boardBackground,
        Button settingsButton,
        ListView<CardList> lists, Button backButton) {
        this.boardBackground = boardBackground;
        this.settingsButton = settingsButton;
        this.boardName = boardName;
        this.customizationUtils = new CustomizationUtils();
        this.lists = lists;
        this.backButton = backButton;
        if(board != null) {
            accept(board);
        }
    }

    /**
     * Handle change to board
     */
    @Override
    public void accept(Board board) {

        boardName.setText(board.getName());
        boardName.setWrappingWidth(board.getName().length() * 13);

        boardBackground.setStyle(boardBackground.getStyle() + "-fx-background-color: " + board.getOuterColor() + ";");
        Color contrastingColor = customizationUtils.getContrastingColor(board.getOuterColor());
        settingsButton.setStyle(settingsButton.getStyle() + "-fx-background-color: " +
            (contrastingColor == Color.WHITE ? "white" : "black") + ";");
        boardName.setFill(contrastingColor);

        lists.setStyle(lists.getStyle() + "-fx-background-color: " + board.getInnerColor() + ";");

        boardBackground.setStyle(boardBackground.getStyle() + "-fx-font-family:" + board.getFont() + " ;");
        boardName.setStyle(boardName.getStyle() + "-fx-font-family:" + board.getFont() + " ;");

        backButton.setStyle(backButton.getStyle() + "-fx-background-color: " +
            (contrastingColor == Color.WHITE ? "white" : "black") + ";");
    }
}
