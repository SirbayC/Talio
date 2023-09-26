package client.components.dialog;

import client.utils.CustomizationUtils;
import commons.dto.TagDto;
import commons.entities.Tag;
import commons.mappers.TagMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TagDialog extends AbstractDialog<TagDto> {

    private final TagDto dto;

    @FXML
    protected TextField field;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text emptyInputError;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> fontPicker;

    private final CustomizationUtils customizationUtils;

    /**
     * Get tag dialog with title and dto
     *
     * @param title title
     * @param dto   tag dto
     */
    public TagDialog(String title, TagDto dto) {
        super(title);
        this.dto = dto;
        customizationUtils = new CustomizationUtils();
    }

    /**
     * {@inheritDoc}
     *
     * @return path to text dialog
     */
    @Override
    public String getFxmlPath() {
        return "/client/components/dialogs/TagDialog.fxml";
    }

    /**
     * {@inheritDoc}
     *
     * @return always true
     */
    @Override
    public boolean verifyResult() {
        if(!field.getText().isBlank())
            return true;

        emptyInputError.setVisible(true);
        PauseTransition showMark = new PauseTransition(Duration.seconds(5));
        showMark.setOnFinished(event1 -> emptyInputError.setVisible(false));
        showMark.play();
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return tag dto with the changes to the ta
     */
    @Override
    public TagDto gatherResult() {
        return new TagDto().setName(field.getText())
            .setColour(colorPicker.getValue().toString())
            .setFont(fontPicker.getValue())
            .diff(dto);
    }

    /**
     * Show edit card dialog
     */
    @Override
    public void show() {
        super.show();
        fontPicker.setItems(FXCollections.observableList(customizationUtils.getFonts()));
        fontPicker.setValue("Arial");
        if(dto == null)
            return;

        field.setText(dto.getName());
        colorPicker.setValue(Color.valueOf(dto.getColour()));
        if(dto.getFont() != null) {
            fontPicker.setValue(dto.getFont());
        }
    }

    /**
     * Sets the hover effects of the button
     */
    public void setHoverEffects() {
        List.of(cancelButton, confirmButton).forEach(this::setHoverEffects);
    }

    /**
     * Show tag dialog with provided information
     *
     * @param title title of dialog
     * @param tag   tag instance
     * @return future
     */
    public static CompletableFuture<TagDto> show(String title, Tag tag) {
        TagDialog dialog = new TagDialog(title, TagMapper.INSTANCE.toDto(tag));
        dialog.show();
        return dialog.getFuture();
    }

}
