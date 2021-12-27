package org.but.feec.csfd.controller.title;

import javafx.fxml.FXML;
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
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    private TextField createdTextField;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        nicknameTextField.setEditable(false);
        emailTextField.setEditable(false);
        cityTextField.setEditable(false);
        streetTextField.setEditable(false);
        houseNumberTextField.setEditable(false);
        createdTextField.setEditable(false);

        loadTitlesData();

        logger.info("TitlesDetailViewController initialized");
    }

    private void loadTitlesData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof TitleDetailView) {
            TitleDetailView titleBasicView = (TitleDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(titleBasicView.getId()));
            firstNameTextField.setText(titleBasicView.getGivenName());
            lastNameTextField.setText(titleBasicView.getFamilyName());
            nicknameTextField.setText(titleBasicView.getNickname());
            emailTextField.setText(titleBasicView.getEmail());
            cityTextField.setText(titleBasicView.getCity());
            streetTextField.setText(titleBasicView.getStreet());
            houseNumberTextField.setText(titleBasicView.gethouseNumber());
            createdTextField.setText(titleBasicView.getCreated());
        }
    }

}
