package org.but.feec.csfd.controller.person;

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
import org.but.feec.csfd.api.person.PersonCreateView;
import org.but.feec.csfd.data.PersonRepository;
import org.but.feec.csfd.service.PersonService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.ParseException;
import java.util.Optional;

public class PersonCreateController {

    private static final Logger logger = LoggerFactory.getLogger(PersonCreateController.class);

    @FXML
    public Button newPersonCreatePerson;

    @FXML
    private TextField newPersonFirstName;

    @FXML
    private TextField newPersonLastName;

    @FXML
    private TextField newPersonBirthday;

    @FXML
    private TextField newPersonAddress;

    private PersonService personService;
    private PersonRepository personRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newPersonFirstName, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(newPersonLastName, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(newPersonBirthday, Validator.createEmptyValidator("The birthday must not be empty."));
        validation.registerValidator(newPersonAddress, Validator.createEmptyValidator("The address must not be empty."));

        newPersonCreatePerson.disableProperty().bind(validation.invalidProperty());

        logger.info("PersonCreateController initialized");
    }

    @FXML
    void handleCreateNewPerson(ActionEvent event) throws ParseException {
        // can be written easier, its just for better explanation here on so many lines
        String firstName = newPersonFirstName.getText();
        String lastName = newPersonLastName.getText();
        java.sql.Date birthday = Date.valueOf(newPersonBirthday.getText());
        Long address = Long.valueOf(newPersonAddress.getText());

        PersonCreateView personCreateView = new PersonCreateView();
        personCreateView.setFirstName(firstName);
        personCreateView.setSurname(lastName);
        personCreateView.setBirthday(birthday);
        personCreateView.setAddress(address);

        personService.createPerson(personCreateView);

        personCreatedConfirmationDialog();
    }

    private void personCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Person Created Confirmation");
        alert.setHeaderText("Your person was successfully created.");

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
