package client.components.dialog;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class ConfirmDialog extends AbstractDialog<Void> {

    private final String labelText;

    @FXML
    private Text confirmLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    /**
     * Get text dialog with title
     *
     * @param title     dialog title
     * @param labelText text of label
     */
    private ConfirmDialog(String title, String labelText) {
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
        return "/client/components/dialogs/ConfirmDialog.fxml";
    }

    /**
     * {@inheritDoc}
     *
     * @return always true
     */
    @Override
    public boolean verifyResult() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return always null
     */
    @Override
    public Void gatherResult() {
        return null;
    }

    /**
     * Show confirm dialog
     */
    public void show() {
        super.show();
        confirmLabel.setText(labelText);
    }

    /**
     * Sets the hover effects of the buttons
     */
    public void setHoverEffects() {
        List.of(confirmButton, cancelButton).forEach(this::setHoverEffects);
    }

    /**
     * Show confirm dialog with provided information
     *
     * @param title title of dialog
     * @param label text of label
     * @return future
     */
    public static CompletableFuture<Void> show(String title, String label) {
        ConfirmDialog dialog = new ConfirmDialog(title, label);
        dialog.show();
        return dialog.getFuture();
    }

}