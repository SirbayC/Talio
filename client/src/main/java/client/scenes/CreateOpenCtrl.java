package client.scenes;

import client.components.cells.BoardCell;
import client.utils.CustomizationUtils;
import client.utils.RepeatingFuture;
import client.utils.TaskWrapper;
import com.google.inject.Inject;
import commons.dto.BoardDto;
import commons.entities.Board;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CreateOpenCtrl implements ButtonCtrl {

    @FXML
    private TextField newBoard;

    @FXML
    private ComboBox<Board> existingBoards;

    @FXML
    private Button create;

    @FXML
    private Button open;

    @FXML
    private Text emptyInputError;

    @FXML
    private Text adminMode;
    @FXML
    private Button back;
    @FXML
    private Label labelOpen;

    private final MainCtrl mainCtrl;
    private final AtomicReference<RepeatingFuture<?>> pollFuture;

    private final CustomizationUtils customizationUtils;

    /**
     * Constructor fot the CreateOpenCtrl using the main controller provided
     *
     * @param mainCtrl the main controller provided on initialization
     */
    @Inject
    public CreateOpenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.pollFuture = new AtomicReference<>();
        customizationUtils = new CustomizationUtils();
    }

    /**
     * Sets the hover effects for the buttons
     */
    public void setHoverEffects() {
        List.of(create, open, back).forEach(this::setHoverEffects);
    }

    /**
     * Open selected board if any
     */
    public void open() {
        Board selectedBoard = existingBoards.getValue();
        if(selectedBoard == null)
            return;

        mainCtrl.getServer()
            .getBoard(selectedBoard.getId())
            .thenAccept(board -> {
                cancelPoll();
                mainCtrl.showOverview(board);
            });
    }

    /**
     * Creates a new board
     */
    public void create() {
        if(newBoard.getText().isBlank()) {
            emptyInputError.setVisible(true);
            PauseTransition showMark = new PauseTransition(Duration.seconds(1));
            showMark.setOnFinished(event -> emptyInputError.setVisible(false));
            showMark.play();
            return;
        }

        String boardName = newBoard.getText();
        mainCtrl.getServer()
            .createBoard(new BoardDto().setName(boardName)
                .setInnerColor(customizationUtils.getDefaultBoardInnerColor())
                .setOuterColor(customizationUtils.getDefaultBoardOuterColor())
                .setCardListBorderColor(customizationUtils.getDefaultCardListBorderColor())
                .setCardListBackgroundColor(customizationUtils.getDefaultCardListColor())
                .setFont("Arial"))
            .thenAccept(board -> TaskWrapper.wrap(() ->
                mainCtrl.getClientBoardManager().write(board)
            ).thenAccept(completed -> {
                if(!completed)
                    return;

                cancelPoll();
                newBoard.setText("");
                mainCtrl.showOverview(board);
            }));
    }

    /**
     * Populates the dropdown with the existing values
     *
     * @return future
     */
    public CompletableFuture<Void> initializeBoardList() {
        adminMode.setVisible(mainCtrl.isAdmin());
        if(mainCtrl.isAdmin()) {
            labelOpen.setText("Choose a board");
        } else {
            labelOpen.setText("Choose one of the previously visited boards:");
        }

        existingBoards.setCellFactory(param -> new BoardCell());
        existingBoards.setButtonCell(new BoardCell());

        return mainCtrl.getServer()
            .getBoards()
            .thenApply(boards -> {
                if(!mainCtrl.isAdmin())
                    boards = mainCtrl.getClientBoardManager().getClientBoardList(boards);

                existingBoards.setItems(FXCollections.observableList(boards));
                startPoll();
                return null;
            });
    }

    /**
     * Start Long-Polling
     */
    public void startPoll() {
        RepeatingFuture<List<Board>> future = mainCtrl.getServer().longPoll();
        future.thenAccept(boards -> {
            if(!mainCtrl.isAdmin())
                boards = mainCtrl.getClientBoardManager().getClientBoardList(boards);

            existingBoards.setItems(FXCollections.observableList(boards));
        });

        Optional.ofNullable(pollFuture.getAndSet(future)).ifPresent(RepeatingFuture::cancel);
    }

    /**
     * Cancel polling
     */
    public void cancelPoll() {
        RepeatingFuture<?> future = pollFuture.getAndSet(null);
        if(future == null)
            return;

        future.cancel();
    }

    /**
     * Returns to the initial scene
     */
    public void back() {
        mainCtrl.showInitialScene();
    }

}
