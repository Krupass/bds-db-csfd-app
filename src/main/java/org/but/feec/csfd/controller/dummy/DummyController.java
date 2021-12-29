package org.but.feec.csfd.controller.dummy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.csfd.App;
import org.but.feec.csfd.api.dummy.DummyBasicView;
import org.but.feec.csfd.data.DummyRepository;
import org.but.feec.csfd.exception.ExceptionHandler;
import org.but.feec.csfd.service.DummyService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DummyController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.dummy.DummyController.class);

    @FXML
    public Button addStringButton;
    @FXML
    public Button refreshButton;
    @FXML
    private TextField stringTextField;
    @FXML
    private TableColumn<DummyBasicView, String> stringContent;
    @FXML
    private TableView<DummyBasicView> systemDummyTableView;

    private DummyService dummyService;
    private DummyRepository dummyRepository;

    @FXML
    private void initialize() {
        dummyRepository = new DummyRepository();
        dummyService = new DummyService(dummyRepository);

        stringContent.setCellValueFactory(new PropertyValueFactory<DummyBasicView, String>("string"));


        ObservableList<DummyBasicView> observableDummyList = initializeDummyData();
        systemDummyTableView.setItems(observableDummyList);

        systemDummyTableView.getSortOrder().add(stringContent);

        loadIcons();

        logger.info("DummyController initialized");
    }

    private ObservableList<DummyBasicView> initializeDummyData() {
        List<DummyBasicView> dummy = dummyService.getDummyBasicView();
        return FXCollections.observableArrayList(dummy);
    }

    private void loadIcons() {
        Image vutLogoImage = new Image(App.class.getResourceAsStream("logo/vut-logo-eng.png"));
        ImageView vutLogo = new ImageView(vutLogoImage);
        vutLogo.setFitWidth(150);
        vutLogo.setFitHeight(50);
    }

    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddStringButton(ActionEvent actionEvent) {
        try {

            ValidationSupport validation;
            validation = new ValidationSupport();
            validation.registerValidator(stringTextField, Validator.createEmptyValidator("The string not be empty."));

            addStringButton.disableProperty().bind(validation.invalidProperty());

            String string = stringTextField.getText();

            DummyBasicView dummyBasicView = new DummyBasicView();
            dummyBasicView.setString(string);

            dummyService.createString(dummyBasicView);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("String Created Confirmation");
            alert.setHeaderText("Your string was successfully created.");

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


        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<DummyBasicView> observableDummyList = initializeDummyData();
        systemDummyTableView.setItems(observableDummyList);
        systemDummyTableView.refresh();
        systemDummyTableView.sort();
    }

    public void handlePersonsButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Persons.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void handleTitlesButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Titles.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}
