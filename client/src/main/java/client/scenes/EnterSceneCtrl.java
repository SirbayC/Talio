package client.scenes;

import client.utils.ServerExists;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.Getter;

public class EnterSceneCtrl implements ButtonCtrl {

    @Getter
    @FXML
    private Label error;

    @FXML
    private TextField serverHost;

    @FXML
    private Button confirmButton;

    @Getter
    @FXML
    private Button connectButton;

    @Getter
    @FXML
    private PasswordField password;

    @Getter
    @FXML
    private Label enterPassword;

    @Getter
    @FXML
    private ImageView passwordEye;

    @Getter
    @FXML
    private TextField passwordVisible;

    private final MainCtrl mainCtrl;

    /**
     * Constructor fot the EnterSceneController using the main controller provided
     *
     * @param mainCtrl the main controller provided on initialization
     */
    @Inject
    public EnterSceneCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sets the hover effects on the buttons
     */
    public void setHoverEffects() {
        List.of(confirmButton, connectButton).forEach(this::setHoverEffects);
    }

    /**
     * Enters the specified server when the "ok" button is clicked
     */
    public void ok() {
        AtomicReference<String> server = new AtomicReference<>(serverHost.getText());
        if(server.get().endsWith("admin")) {
            passwordChecker();
        } else {
            error.setText("Checking address...");
            error.setVisible(true);
            ServerUtils.checkServerExists(server.get()).thenAccept(exists -> {
                if(exists == ServerExists.VALID || exists == ServerExists.ADMIN) {
                    error.setText("Connecting to server...");
                    mainCtrl.enter(server.get(), exists == ServerExists.ADMIN);
                    return;
                }
                // Error message
                String msg = switch(exists) {
                    case EMPTY_ADDRESS -> "Please fill in the field";
                    case INVALID_ADDRESS -> "Address must be a valid URL starting with \"http://\" or \"https://\"";
                    case REQUEST_EXCEPTION -> "Unable to reach server";
                    default -> "Server at \"" + server.get() + "\" was of invalid type";
                };
                error.setText(msg);
            }).exceptionally(e -> {
                error.setText("Unexpected exception occurred");
                return null;
            });
        }
    }

    /**
     * Checks whether the password given by the user is correct
     */
    public void passwordChecker() {
        AtomicReference<String> pw = new AtomicReference<>();
        enterPassword.setVisible(true);
        password.setVisible(true);
        connectButton.setVisible(true);
        passwordEye.setVisible(true);
        connectButton.setOnAction(event -> {
            if(password.isVisible()) {
                pw.set(password.getText());
                passwordVisible.setText(password.getText());
            } else {
                pw.set(passwordVisible.getText());
                password.setText(passwordVisible.getText());
            }

            String server = serverHost.getText() + "?password=" + pw.get();
            error.setText("Checking address...");
            error.setVisible(true);
            ServerUtils.checkServerExists(server).thenAccept(exists -> {
                if(exists == ServerExists.VALID || exists == ServerExists.ADMIN) {
                    error.setText("Connecting to server...");
                    mainCtrl.enter(server, exists == ServerExists.ADMIN);
                    return;
                }
                // Error message
                String msg = switch(exists) {
                    case EMPTY_ADDRESS -> "Please fill in the field";
                    case INVALID_ADDRESS -> "Address must be a valid URL starting with \"http://\" or \"https://\"";
                    case REQUEST_EXCEPTION -> "Unable to reach server";
                    case INVALID_SERVER -> "The server you entered is not valid";
                    case INVALID_PASSWORD -> "Password is wrong";
                    default -> "Server at \"" + server + "\" was of invalid type";
                };
                error.setText(msg);
                passwordChecker();
            }).exceptionally(e -> {
                error.setText("Unexpected exception occurred");
                passwordChecker();
                return null;
            });
        });
    }

    /**
     * Shows/Hides password
     */
    public void showHidePassword() {
        if(password.isVisible()) {
            password.setVisible(false);
            passwordVisible.setText(password.getText());
            passwordVisible.setVisible(true);
        } else {
            passwordVisible.setVisible(false);
            password.setText(passwordVisible.getText());
            password.setVisible(true);
        }
    }

}
