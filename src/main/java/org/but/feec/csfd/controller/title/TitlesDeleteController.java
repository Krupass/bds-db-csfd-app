package org.but.feec.csfd.controller.title;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.but.feec.csfd.api.title.TitleDeleteView;
import org.but.feec.csfd.data.TitleRepository;
import org.but.feec.csfd.service.TitleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TitlesDeleteController {
    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.title.TitlesDeleteController.class);
    private TitleService titleService;
    private TitleRepository titleRepository;

    public void initialize(Long id) {

        titleRepository = new TitleRepository();
        titleService = new TitleService(titleRepository);

        TitleDeleteView titleDeleteView = new TitleDeleteView();
        titleDeleteView.setId(id);

        titleService.deleteTitle(titleDeleteView);

        titleDeletedConfirmationDialog();
        logger.info("TitlesDeleteController initialized");
    }


    private void titleDeletedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Title Deleted Confirmation");
        alert.setHeaderText("Your title was successfully deleted.");

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
