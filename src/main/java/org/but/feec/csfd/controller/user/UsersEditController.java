package org.but.feec.csfd.controller.user;

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
import org.but.feec.csfd.api.user.UserBasicView;
import org.but.feec.csfd.api.user.UserEditView;
import org.but.feec.csfd.data.UserRepository;
import org.but.feec.csfd.service.UserService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UsersEditController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.user.UsersEditController.class);

    @FXML
    public Button editUserButton;
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

    private UserService userService;
    private UserRepository userRepository;
    private ValidationSupport validation;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);

        validation = new ValidationSupport();
        validation.registerValidator(idTextField, Validator.createEmptyValidator("The id must not be empty."));
        idTextField.setEditable(false);
        validation.registerValidator(emailTextField, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(firstNameTextField, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(lastNameTextField, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(nicknameTextField, Validator.createEmptyValidator("The nickname must not be empty."));

        editUserButton.disableProperty().bind(validation.invalidProperty());

        loadUsersData();

        logger.info("UsersEditController initialized");
    }

    /**
     * Load passed data from Userns controller. Check this tutorial explaining how to pass the data between controllers: https://dev.to/devtony101/javafx-3-ways-of-passing-information-between-scenes-1bm8
     */
    private void loadUsersData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof UserBasicView) {
            UserBasicView userBasicView = (UserBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(userBasicView.getId()));
            firstNameTextField.setText(userBasicView.getGivenName());
            lastNameTextField.setText(userBasicView.getFamilyName());
            nicknameTextField.setText(userBasicView.getNickname());
            emailTextField.setText(userBasicView.getEmail());
            addressTextField.setText(String.valueOf(userBasicView.getAddress()));
        }
    }

    @FXML
    public void handleEditUserButton(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        Long id = Long.valueOf(idTextField.getText());
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String nickname = nicknameTextField.getText();
        String email = emailTextField.getText();
        Long address = null;
        if((addressTextField.getText().compareTo(Long.toString(Long.MIN_VALUE)) >= 0) && (addressTextField.getText().compareTo(Long.toString(Long.MAX_VALUE)) <= 0)){
            address = Long.valueOf(addressTextField.getText());
        }

        UserEditView userEditView = new UserEditView();
        userEditView.setId(id);
        userEditView.setFirstName(firstName);
        userEditView.setSurname(lastName);
        userEditView.setNickname(nickname);
        userEditView.setEmail(email);
        userEditView.setAddress(address);

        userService.editUser(userEditView);

        userEditedConfirmationDialog();
    }

    private void userEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User Edited Confirmation");
        alert.setHeaderText("Your user was successfully edited.");

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
