package org.but.feec.csfd.controller.title;

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
import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.api.title.TitleCreateView;
import org.but.feec.csfd.api.title.TitleDetailView;
import org.but.feec.csfd.controller.title.TitlesDeleteController;
import org.but.feec.csfd.controller.title.TitlesDetailViewController;
import org.but.feec.csfd.controller.title.TitlesEditController;
import org.but.feec.csfd.data.TitleRepository;
import org.but.feec.csfd.exception.ExceptionHandler;
import org.but.feec.csfd.service.TitleService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class TitlesController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.title.TitlesController.class);

    @FXML
    public Button addTitleButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button findButton;
    @FXML
    private TableColumn<TitleBasicView, Long> titlesId;
    @FXML
    private TableColumn<TitleBasicView, String> titlesName;
    @FXML
    private TableColumn<TitleBasicView, String> titlesType;
    @FXML
    private TableColumn<TitleBasicView, String> titlesYear;
    @FXML
    private TableColumn<TitleBasicView, String> titlesLenght;
    @FXML
    private TableColumn<TitleBasicView, String> titlesCountry;
    @FXML
    private TableView<TitleBasicView> systemTitlesTableView;
    @FXML
    private TextField FindTextField;
    @FXML
    private ChoiceBox<String> choiceBox;

    private String[] columns = {"id", "title", "type", "year", "lenght", "country"};
    private TitleService titleService;
    private TitleRepository titleRepository;
    private ValidationSupport validation;

    public TitlesController() {
    }

    @FXML
    private void initialize() {
        titleRepository = new TitleRepository();
        titleService = new TitleService(titleRepository);
        validation = new ValidationSupport();

        titlesId.setCellValueFactory(new PropertyValueFactory<TitleBasicView, Long>("id"));
        titlesName.setCellValueFactory(new PropertyValueFactory<TitleBasicView, String>("title"));
        titlesType.setCellValueFactory(new PropertyValueFactory<TitleBasicView, String>("type"));
        titlesYear.setCellValueFactory(new PropertyValueFactory<TitleBasicView, String>("year"));
        titlesLenght.setCellValueFactory(new PropertyValueFactory<TitleBasicView, String>("lenght"));
        titlesCountry.setCellValueFactory(new PropertyValueFactory<TitleBasicView, String>("country"));

        validation.registerValidator(FindTextField, Validator.createEmptyValidator("The value must not be empty."));

        findButton.disableProperty().bind(validation.invalidProperty());


        ObservableList<TitleBasicView> observableTitlesList = initializeTitlesData();
        systemTitlesTableView.setItems(observableTitlesList);

        systemTitlesTableView.getSortOrder().add(titlesId);

        initializeTableViewSelection();
        initializeChoiceBox();
        loadIcons();

        logger.info("TitlesController initialized");
    }

    private void initializeChoiceBox(){
        choiceBox.getItems().addAll(columns);
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit title");
        MenuItem delete = new MenuItem("Delete title");
        MenuItem detailedView = new MenuItem("Detailed title view");
        edit.setOnAction((ActionEvent event) -> {
            TitleBasicView titleView = systemTitlesTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/TitleEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(titleView);
                stage.setTitle("BDS CSFD Edit Title");

                TitlesEditController controller = new TitlesEditController();
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
            TitleBasicView titleView = systemTitlesTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();

                TitlesDeleteController controller = new TitlesDeleteController();
                controller.initialize(titleView.getId());
                fxmlLoader.setController(controller);

                ObservableList<TitleBasicView> observableTitlesList = initializeTitlesData();
                systemTitlesTableView.setItems(observableTitlesList);
                systemTitlesTableView.refresh();
                systemTitlesTableView.sort();

            } catch (Exception ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            TitleBasicView titleView = systemTitlesTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/TitlesDetailView.fxml"));
                Stage stage = new Stage();

                Long titleId = titleView.getId();
                TitleDetailView titleDetailView = titleService.getTitleDetailView(titleId);

                stage.setUserData(titleDetailView);
                stage.setTitle("BDS CSFD Titles Detailed View");

                TitlesDetailViewController controller = new TitlesDetailViewController();
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
        systemTitlesTableView.setContextMenu(menu);
    }

    private ObservableList<TitleBasicView> initializeTitlesData() {
        List<TitleBasicView> titles = titleService.getTitlesBasicView();
        return FXCollections.observableArrayList(titles);
    }

    private ObservableList<TitleBasicView> initializeTitlesFindData(String find, String choice) {
        List<TitleBasicView> titles = titleService.getTitlesFindView(find, choice);
        return FXCollections.observableArrayList(titles);
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

    public void handleAddTitleButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/TitlesCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS CSFD Create Title");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("logo/plus.png")));
            stage.setScene(scene);

            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<TitleBasicView> observableTitlesList = initializeTitlesData();
        systemTitlesTableView.setItems(observableTitlesList);
        systemTitlesTableView.refresh();
        systemTitlesTableView.sort();
    }

    public void handleFindButton(ActionEvent actionEvent) throws IOException{
        String find = FindTextField.getText();

        TitleBasicView titleBasicView = new TitleBasicView();
        titleBasicView.setFind(find);
        titleBasicView.setChoice(choiceBox.getValue());

        String choice = titleBasicView.getChoice();
        String value = titleBasicView.getFind();


        ObservableList<TitleBasicView> observableTitlesList = initializeTitlesFindData(value, choice);
        systemTitlesTableView.setItems(observableTitlesList);
        systemTitlesTableView.refresh();
        systemTitlesTableView.sort();

    }

    public void handleDummyButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/DummyTable.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void handleUsersButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Users.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}
