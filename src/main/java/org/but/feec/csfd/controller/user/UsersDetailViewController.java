package org.but.feec.csfd.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.csfd.api.user.UserDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsersDetailViewController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.user.UsersDetailViewController.class);

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

        loadUsersData();

        logger.info("UsersDetailViewController initialized");
    }

    private void loadUsersData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof UserDetailView) {
            UserDetailView userBasicView = (UserDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(userBasicView.getId()));
            firstNameTextField.setText(userBasicView.getGivenName());
            lastNameTextField.setText(userBasicView.getFamilyName());
            nicknameTextField.setText(userBasicView.getNickname());
            emailTextField.setText(userBasicView.getEmail());
            cityTextField.setText(userBasicView.getCity());
            streetTextField.setText(userBasicView.getStreet());
            houseNumberTextField.setText(userBasicView.gethouseNumber());
            createdTextField.setText(userBasicView.getCreated());
        }
    }

}
