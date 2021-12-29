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
import javafx.util.Duration;
import org.but.feec.csfd.api.user.UserCreateView;
import org.but.feec.csfd.data.UserRepository;
import org.but.feec.csfd.service.UserService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserCreateController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.user.UserCreateController.class);

    @FXML
    public Button newUserCreateUser;

    @FXML
    private TextField newUserFirstName;

    @FXML
    private TextField newUserSurname;

    @FXML
    private TextField newUserNick;

    @FXML
    private TextField newUserPassword;

    @FXML
    private TextField newUserEmail;

    @FXML
    private TextField newUserAddress;

    private UserService userService;
    private UserRepository userRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newUserFirstName, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(newUserSurname, Validator.createEmptyValidator("The surname must not be empty."));
        validation.registerValidator(newUserNick, Validator.createEmptyValidator("The nickname must not be empty."));
        validation.registerValidator(newUserPassword, Validator.createEmptyValidator("The password must not be empty."));
        validation.registerValidator(newUserEmail, Validator.createEmptyValidator("The email must not be empty."));

        newUserCreateUser.disableProperty().bind(validation.invalidProperty());

        logger.info("UserCreateController initialized");
    }

    @FXML
    void handleCreateNewUser(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        String firstName = newUserFirstName.getText();
        String surname = newUserSurname.getText();
        String nick = newUserNick.getText();
        String password = newUserPassword.getText();
        String email = newUserEmail.getText();
        Long address = Long.valueOf(newUserAddress.getText());

        UserCreateView userCreateView = new UserCreateView();
        userCreateView.setFirstName(firstName);
        userCreateView.setSurname(surname);
        userCreateView.setNickname(nick);
        userCreateView.setPwd(password.toCharArray());
        userCreateView.setEmail(email);
        userCreateView.setAddress(address);

        userService.createUser(userCreateView);

        userCreatedConfirmationDialog();
    }

    private void userCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User Created Confirmation");
        alert.setHeaderText("Your user was successfully created.");

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
