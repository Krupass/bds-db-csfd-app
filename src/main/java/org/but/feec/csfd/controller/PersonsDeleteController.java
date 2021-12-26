package org.but.feec.csfd.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.but.feec.csfd.api.PersonDeleteView;
import org.but.feec.csfd.data.PersonRepository;
import org.but.feec.csfd.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PersonsDeleteController {
    private static final Logger logger = LoggerFactory.getLogger(PersonsDeleteController.class);
    private PersonService personService;
    private PersonRepository personRepository;

    public void initialize(Long id) {

        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);

        PersonDeleteView personDeleteView = new PersonDeleteView();
        personDeleteView.setId(id);

        personService.deletePerson(personDeleteView);

        personDeletedConfirmationDialog();
        logger.info("PersonsDeleteController initialized");
    }


    private void personDeletedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User Deleted Confirmation");
        alert.setHeaderText("Your user was successfully deleted.");

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
