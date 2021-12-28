package org.but.feec.csfd.controller.title;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.api.title.TitleEditView;
import org.but.feec.csfd.data.TitleRepository;
import org.but.feec.csfd.service.TitleService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TitlesEditController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.title.TitlesEditController.class);

    @FXML
    public Button editTitleButton;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField lenghtTextField;
    @FXML
    private TextField countryTextField;

    private TitleService titleService;
    private TitleRepository titleRepository;
    private ValidationSupport validation;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        titleRepository = new TitleRepository();
        titleService = new TitleService(titleRepository);

        validation = new ValidationSupport();
        validation.registerValidator(idTextField, Validator.createEmptyValidator("The id must not be empty."));
        idTextField.setEditable(false);
        validation.registerValidator(titleTextField, Validator.createEmptyValidator("The title must not be empty."));
        validation.registerValidator(typeTextField, Validator.createEmptyValidator("The type must not be empty."));
        validation.registerValidator(yearTextField, Validator.createEmptyValidator("The year must not be empty."));
        validation.registerValidator(lenghtTextField, Validator.createEmptyValidator("The lenght must not be empty."));
        validation.registerValidator(countryTextField, Validator.createEmptyValidator("The country must not be empty."));

        editTitleButton.disableProperty().bind(validation.invalidProperty());

        loadTitlesData();

        logger.info("TitlesEditController initialized");
    }

    /**
     * Load passed data from Titles controller. Check this tutorial explaining how to pass the data between controllers: https://dev.to/devtony101/javafx-3-ways-of-passing-information-between-scenes-1bm8
     */
    private void loadTitlesData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof TitleBasicView) {
            TitleBasicView titleBasicView = (TitleBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(titleBasicView.getId()));
            titleTextField.setText(titleBasicView.getTitle());
            typeTextField.setText(titleBasicView.getType());
            yearTextField.setText(titleBasicView.getYear());
            lenghtTextField.setText(titleBasicView.getLenght());
            countryTextField.setText(titleBasicView.getCountry());
        }
    }

    @FXML
    public void handleEditTitleButton(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        Long id = Long.valueOf(idTextField.getText());
        String title = titleTextField.getText();
        String type = typeTextField.getText();
        String year = yearTextField.getText();
        String lenght = lenghtTextField.getText();
        String country = countryTextField.getText();

        TitleEditView titleEditView = new TitleEditView();
        titleEditView.setId(id);
        titleEditView.setTitle(title);
        titleEditView.setType(type);
        titleEditView.setYear(year);
        titleEditView.setLenght(lenght);
        titleEditView.setCountry(country);

        titleService.editTitle(titleEditView);

        titleEditedConfirmationDialog();
    }

    private void titleEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Title Edited Confirmation");
        alert.setHeaderText("Your title was successfully edited.");

        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.setResult(ButtonType.CANCEL);
                alert.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
        Optional<ButtonType> result = alert.showAndWait();
    }

}
