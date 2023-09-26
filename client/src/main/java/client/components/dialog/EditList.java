package client.components.dialog;

import client.utils.CustomizationUtils;
import commons.dto.CardListDto;
import commons.entities.CardList;
import commons.mappers.CardListMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EditList extends AbstractDialog<CardListDto> {

    private final CardListDto dto;

    @FXML
    private TextField cardListTitle;

    @FXML
    private Button saveButton;

    @FXML
    private Text emptyInputError;

    @FXML
    private ColorPicker backgroundColorPicker;

    @FXML
    private ColorPicker borderColorPicker;

    private final CustomizationUtils customizationUtils;

    /**
     * Get edit card dialog with title and dto
     *
     * @param title title
     * @param dto   card list dto
     */
    public EditList(String title, CardListDto dto) {
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
        return "/client/components/dialogs/EditCardList.fxml";
    }

    /**
     * {@inheritDoc}
     *
     * @return always true
     */
    @Override
    public boolean verifyResult() {
        if(!cardListTitle.getText().isBlank())
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
     * @return card dto with the changes to the card
     */
    @Override
    public CardListDto gatherResult() {
        return new CardListDto().setTitle(cardListTitle.getText())
            .setBackgroundColor(customizationUtils.toString(backgroundColorPicker.getValue()))
            .setBackgroundColor(customizationUtils.toString(borderColorPicker.getValue()))
            .diff(dto);
    }

    /**
     * Show edit card dialog
     */
    @Override
    public void show() {
        super.show();
        if(dto == null)
            return;

        cardListTitle.setText(dto.getTitle());
        if(dto.getBackgroundColor() != null) {
            backgroundColorPicker.setValue(Color.web(dto.getBackgroundColor()));
        } else {
            backgroundColorPicker.setValue(Color.web(customizationUtils.getDefaultCardListColor()));
        }
        if(dto.getBorderColor() != null) {
            borderColorPicker.setValue(Color.web(dto.getBorderColor()));
        } else {
            borderColorPicker.setValue(Color.web(customizationUtils.getDefaultCardListBorderColor()));
        }
    }

    /**
     * Sets the hover effects of the button
     */
    public void setHoverEffects() {
        List.of(saveButton, backgroundColorPicker, borderColorPicker)
            .forEach(this::setHoverEffects);
    }

    /**
     * Show edit card dialog with provided information
     *
     * @param title    title of dialog
     * @param cardList cardList instance
     * @return future
     */
    public static CompletableFuture<CardListDto> show(String title, CardList cardList) {
        EditList dialog = new EditList(title, CardListMapper.INSTANCE.toDto(cardList));
        dialog.show();
        return dialog.getFuture();
    }

    /**
     * Resets the background color of the list to its default value
     */
    public void resetBackgroundColor() {
        backgroundColorPicker.setValue(Color.web(customizationUtils.getDefaultCardListColor()));
    }

    /**
     * Resets the border color of the list to its default value
     */
    public void resetBorderColor() {
        borderColorPicker.setValue(Color.web(customizationUtils.getDefaultCardListBorderColor()));
    }

}
