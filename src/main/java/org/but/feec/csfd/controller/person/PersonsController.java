package org.but.feec.csfd.controller.person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.but.feec.csfd.App;
import org.but.feec.csfd.api.person.PersonBasicView;
import org.but.feec.csfd.api.person.PersonDetailView;
import org.but.feec.csfd.data.PersonRepository;
import org.but.feec.csfd.exception.ExceptionHandler;
import org.but.feec.csfd.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class PersonsController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsController.class);

    @FXML
    public Button addPersonButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button findButton;
    @FXML
    private TableColumn<PersonBasicView, Long> personsId;
    @FXML
    private TableColumn<PersonBasicView, String> personsGivenName;
    @FXML
    private TableColumn<PersonBasicView, String> personsFamilyName;
    @FXML
    private TableColumn<PersonBasicView, String> personsBirthday;
    @FXML
    private TableColumn<PersonBasicView, String> personsCity;
    @FXML
    private TableView<PersonBasicView> systemPersonsTableView;
//    @FXML
//    public MenuItem exitMenuItem;

    private PersonService personService;
    private PersonRepository personRepository;

    public PersonsController() {
    }

    @FXML
    private void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);
//        GlyphsDude.setIcon(exitMenuItem, FontAwesomeIcon.CLOSE, "1em");

        personsId.setCellValueFactory(new PropertyValueFactory<PersonBasicView, Long>("id"));
        personsGivenName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("givenName"));
        personsFamilyName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("familyName"));
        personsBirthday.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("birthday"));
        personsCity.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("city"));


        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);

        systemPersonsTableView.getSortOrder().add(personsId);

        initializeTableViewSelection();
        loadIcons();

        logger.info("PersonsController initialized");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit person");
        MenuItem delete = new MenuItem("Delete person");
        MenuItem detailedView = new MenuItem("Detailed person view");
        edit.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(personView);
                stage.setTitle("BDS CSFD Edit Person");

                PersonsEditController controller = new PersonsEditController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.getIcons().add(new Image(App.class.getResourceAsStream("logo/pp-h.png")));

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();

                PersonsDeleteController controller = new PersonsDeleteController();
                controller.initialize(personView.getId());
                fxmlLoader.setController(controller);

                ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
                systemPersonsTableView.setItems(observablePersonsList);
                systemPersonsTableView.refresh();
                systemPersonsTableView.sort();

            } catch (Exception ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonsDetailView.fxml"));
                Stage stage = new Stage();

                Long personId = personView.getId();
                PersonDetailView personDetailView = personService.getPersonDetailView(personId);

                stage.setUserData(personDetailView);
                stage.setTitle("BDS CSFD Persons Detailed View");

                PersonsDetailViewController controller = new PersonsDetailViewController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.getIcons().add(new Image(App.class.getResourceAsStream("logo/pp-m.png")));

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });


        ContextMenu menu = new ContextMenu();
        menu.getItems().add(edit);
        menu.getItems().add(delete);
        menu.getItems().addAll(detailedView);
        systemPersonsTableView.setContextMenu(menu);
    }

    private ObservableList<PersonBasicView> initializePersonsData() {
        List<PersonBasicView> persons = personService.getPersonsBasicView();
        return FXCollections.observableArrayList(persons);
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

    public void handleAddPersonButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/PersonsCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS CSFD Create Person");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("logo/plus.png")));
            stage.setScene(scene);

            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);
        systemPersonsTableView.refresh();
        systemPersonsTableView.sort();
    }

    public void handleChoiceBox(ActionEvent actionEvent) throws IOException{


    }

    public void handleFindButton(ActionEvent actionEvent) throws IOException{


    }

    public void handleUsersButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Users.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void handleDummyButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/DummyTable.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}
