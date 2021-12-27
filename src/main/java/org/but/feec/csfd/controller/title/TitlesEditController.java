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
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField addressTextField;

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
        validation.registerValidator(emailTextField, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(firstNameTextField, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(lastNameTextField, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(nicknameTextField, Validator.createEmptyValidator("The nickname must not be empty."));

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
            firstNameTextField.setText(titleBasicView.getGivenName());
            lastNameTextField.setText(titleBasicView.getFamilyName());
            nicknameTextField.setText(titleBasicView.getNickname());
            emailTextField.setText(titleBasicView.getEmail());
            addressTextField.setText(String.valueOf(titleBasicView.getAddress()));
        }
    }

    @FXML
    public void handleEditTitleButton(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        Long id = Long.valueOf(idTextField.getText());
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String nickname = nicknameTextField.getText();
        String email = emailTextField.getText();
        String address = addressTextField.getText();

        TitleEditView titleEditView = new TitleEditView();
        titleEditView.setId(id);
        titleEditView.setFirstName(firstName);
        titleEditView.setSurname(lastName);
        titleEditView.setNickname(nickname);
        titleEditView.setEmail(email);
        titleEditView.setAddress(address);

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
