package client.components.dialog;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TextDialog extends AbstractDialog<String> {

    private final String labelText;

    @FXML
    private Label label;

    @FXML
    protected TextField field;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text emptyInputError;

    /**
     * Get text dialog with title
     *
     * @param title     dialog title
     * @param labelText text of label
     */
    private TextDialog(String title, String labelText) {
        super(title);
        this.labelText = labelText;
    }

    /**
     * {@inheritDoc}
     *
     * @return path to text dialog
     */
    @Override
    public String getFxmlPath() {
        return "/client/components/dialogs/TextDialog.fxml";
    }

    /**
     * {@inheritDoc}
     *
     * @return whether text field is non-empty
     */
    @Override
    public boolean verifyResult() {
        if(!field.getText().isBlank())
            return true;

        emptyInputError.setVisible(true);
        PauseTransition showMark = new PauseTransition(Duration.seconds(5));
        showMark.setOnFinished(event2 -> emptyInputError.setVisible(false));
        showMark.play();
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return value of text field
     */
    @Override
    public String gatherResult() {
        return field.getText();
    }

    /**
     * Show text dialog
     *
     * @param value current value
     */
    public void show(String value) {
        super.show();
        label.setText(labelText);
        field.setText(value);
    }

    /**
     * Sets the hover effects of the buttons
     */
    public void setHoverEffects() {
        List.of(confirmButton, cancelButton).forEach(this::setHoverEffects);
    }

    /**
     * Show text dialog with provided information
     *
     * @param title title of dialog
     * @param label text of label
     * @param value current value
     * @return future
     */
    public static CompletableFuture<String> show(String title, String label, String value) {
        TextDialog dialog = new TextDialog(title, label);
        dialog.show(value);
        return dialog.getFuture();
    }

}