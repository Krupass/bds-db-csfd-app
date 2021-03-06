package org.but.feec.csfd.controller.title;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.csfd.api.title.TitleDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitlesDetailViewController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.title.TitlesDetailViewController.class);

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private TextField genreTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField lenghtTextField;

    @FXML
    private TextArea descriptionTextField;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTextField.setEditable(false);
        nameTextField.setEditable(false);
        typeTextField.setEditable(false);
        genreTextField.setEditable(false);
        countryTextField.setEditable(false);
        yearTextField.setEditable(false);
        lenghtTextField.setEditable(false);
        descriptionTextField.setEditable(false);

        loadTitlesData();

        logger.info("TitlesDetailViewController initialized");
    }

    private void loadTitlesData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof TitleDetailView) {
            TitleDetailView personBasicView = (TitleDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(personBasicView.getId()));
            nameTextField.setText(personBasicView.getName());
            typeTextField.setText(personBasicView.getType());
            genreTextField.setText(personBasicView.getGenre());
            countryTextField.setText(personBasicView.getCountry());
            yearTextField.setText(personBasicView.getYear());
            lenghtTextField.setText(personBasicView.getLenght());
            descriptionTextField.setText(personBasicView.getDescription());
        }
    }

}
