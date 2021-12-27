package org.but.feec.csfd.controller.user;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.but.feec.csfd.api.user.UserDeleteView;
import org.but.feec.csfd.data.UserRepository;
import org.but.feec.csfd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UsersDeleteController {
    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.user.UsersDeleteController.class);
    private UserService userService;
    private UserRepository userRepository;

    public void initialize(Long id) {

        userRepository = new UserRepository();
        userService = new UserService(userRepository);

        UserDeleteView userDeleteView = new UserDeleteView();
        userDeleteView.setId(id);

        userService.deleteUser(userDeleteView);

        userDeletedConfirmationDialog();
        logger.info("UsersDeleteController initialized");
    }


    private void userDeletedConfirmationDialog() {
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
