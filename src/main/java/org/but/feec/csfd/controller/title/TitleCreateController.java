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
import javafx.util.Duration;
import org.but.feec.csfd.api.title.TitleCreateView;
import org.but.feec.csfd.data.TitleRepository;
import org.but.feec.csfd.service.TitleService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TitleCreateController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.title.TitleCreateController.class);

    @FXML
    public Button newTitleCreate;

    @FXML
    private TextField newTitleFirstName;

    @FXML
    private TextField newTitleLastName;

    @FXML
    private TextField newTitleNickname;

    @FXML
    private TextField newTitlePwd;

    @FXML
    private TextField newTitleEmail;

    @FXML
    private TextField newTitleAddress;

    private TitleService titleService;
    private TitleRepository titleRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        titleRepository = new TitleRepository();
        titleService = new TitleService(titleRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newTitleEmail, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(newTitleFirstName, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(newTitleLastName, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(newTitleNickname, Validator.createEmptyValidator("The nickname must not be empty."));
        validation.registerValidator(newTitlePwd, Validator.createEmptyValidator("The password must not be empty."));

        newTitleCreate.disableProperty().bind(validation.invalidProperty());

        logger.info("TitleCreateController initialized");
    }

    @FXML
    void handleCreateNewTitle(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        String firstName = newTitleFirstName.getText();
        String lastName = newTitleLastName.getText();
        String nickname = newTitleNickname.getText();
        String password = newTitlePwd.getText();
        String email = newTitleEmail.getText();
        String address = newTitleAddress.getText();

        TitleCreateView titleCreateView = new TitleCreateView();
        titleCreateView.setFirstName(firstName);
        titleCreateView.setSurname(lastName);
        titleCreateView.setNickname(nickname);
        titleCreateView.setPwd(password.toCharArray());
        titleCreateView.setEmail(email);
        titleCreateView.setAddress(address);

        titleService.createTitle(titleCreateView);

        titleCreatedConfirmationDialog();
    }

    private void titleCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Title Created Confirmation");
        alert.setHeaderText("Your title was successfully created.");

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
