package client.components.dialog;

import client.scenes.ButtonCtrl;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractDialog<T> implements ButtonCtrl {

    @Getter
    private final CompletableFuture<T> future = new CompletableFuture<>();
    private final String title;

    /**
     * Get the path to the dialog resource
     *
     * @return path to fxml resource
     */
    public abstract String getFxmlPath();

    /**
     * Verify whether the dialog can be confirmed.
     * <p>
     * This should show errors in the dialog when there is an issue and return false.
     * </p>
     *
     * @return whether the result is valid
     */
    public abstract boolean verifyResult();

    /**
     * Get the result of the dialog
     *
     * @return result
     */
    public abstract T gatherResult();

    /**
     * Show the dialog
     */
    public void show() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFxmlPath()));
        fxmlLoader.setController(this);

        try {
            Parent parent = fxmlLoader.load();
            setHoverEffects();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Confirm the dialog
     *
     * @param event action event
     */
    @FXML
    public void confirm(ActionEvent event) {
        if(!verifyResult())
            return;

        T result = gatherResult();
        future.complete(result);
        close(event);
    }

    /**
     * Cancel the dialog
     *
     * @param event action event
     */
    @FXML
    public void cancel(ActionEvent event) {
        future.completeExceptionally(new IllegalStateException("dialog was cancelled"));
        close(event);
    }

    /**
     * Close the dialog
     *
     * @param event action event
     */
    protected void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
