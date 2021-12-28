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
    private TextField newTitleName;

    @FXML
    private TextField newTitleType;

    @FXML
    private TextField newTitleCountry;

    @FXML
    private TextField newTitleYear;

    @FXML
    private TextField newTitleLenght;

    @FXML
    private TextField newTitleDescription;

    private TitleService titleService;
    private TitleRepository titleRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        titleRepository = new TitleRepository();
        titleService = new TitleService(titleRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newTitleName, Validator.createEmptyValidator("The name must not be empty."));
        validation.registerValidator(newTitleType, Validator.createEmptyValidator("The type must not be empty."));
        validation.registerValidator(newTitleCountry, Validator.createEmptyValidator("The country must not be empty."));
        validation.registerValidator(newTitleYear, Validator.createEmptyValidator("The year must not be empty."));
        validation.registerValidator(newTitleLenght, Validator.createEmptyValidator("The lenght must not be empty."));
        validation.registerValidator(newTitleDescription, Validator.createEmptyValidator("The description must not be empty."));

        newTitleCreate.disableProperty().bind(validation.invalidProperty());

        logger.info("TitleCreateController initialized");
    }

    @FXML
    void handleCreateNewTitle(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        String name = newTitleName.getText();
        String type = newTitleType.getText();
        String country = newTitleCountry.getText();
        String year = newTitleYear.getText();
        String lenght = newTitleLenght.getText();
        String description = newTitleDescription.getText();

        TitleCreateView titleCreateView = new TitleCreateView();
        titleCreateView.setName(name);
        titleCreateView.setType(type);
        titleCreateView.setCountry(country);
        titleCreateView.setYear(year);
        titleCreateView.setLenght(lenght);
        titleCreateView.setDescription(description);

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
