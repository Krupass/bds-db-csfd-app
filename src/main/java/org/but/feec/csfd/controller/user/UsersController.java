package org.but.feec.csfd.controller.user;

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
import org.but.feec.csfd.api.user.UserBasicView;
import org.but.feec.csfd.api.user.UserDetailView;
import org.but.feec.csfd.controller.user.UsersDeleteController;
import org.but.feec.csfd.controller.user.UsersDetailViewController;
import org.but.feec.csfd.controller.user.UsersEditController;
import org.but.feec.csfd.data.UserRepository;
import org.but.feec.csfd.exception.ExceptionHandler;
import org.but.feec.csfd.service.UserService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(org.but.feec.csfd.controller.user.UsersController.class);

    @FXML
    public Button addUserButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button findButton;
    @FXML
    private TableColumn<UserBasicView, Long> usersId;
    @FXML
    private TableColumn<UserBasicView, String> usersCity;
    @FXML
    private TableColumn<UserBasicView, String> usersEmail;
    @FXML
    private TableColumn<UserBasicView, String> usersFamilyName;
    @FXML
    private TableColumn<UserBasicView, String> usersGivenName;
    @FXML
    private TableColumn<UserBasicView, String> usersNickname;
    @FXML
    private TableView<UserBasicView> systemUsersTableView;
    @FXML
    private TextField FindTextField;
    @FXML
    private ChoiceBox<String> choiceBox;

    private String[] columns = {"id","given name","family name","nickname","email","city"};
    private UserService userService;
    private UserRepository userRepository;
    private ValidationSupport validation;

    public UsersController() {
    }

    @FXML
    private void initialize() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
        validation = new ValidationSupport();

        usersId.setCellValueFactory(new PropertyValueFactory<UserBasicView, Long>("id"));
        usersCity.setCellValueFactory(new PropertyValueFactory<UserBasicView, String>("city"));
        usersEmail.setCellValueFactory(new PropertyValueFactory<UserBasicView, String>("email"));
        usersFamilyName.setCellValueFactory(new PropertyValueFactory<UserBasicView, String>("familyName"));
        usersGivenName.setCellValueFactory(new PropertyValueFactory<UserBasicView, String>("givenName"));
        usersNickname.setCellValueFactory(new PropertyValueFactory<UserBasicView, String>("nickname"));

        validation.registerValidator(FindTextField, Validator.createEmptyValidator("The value must not be empty."));

        findButton.disableProperty().bind(validation.invalidProperty());


        ObservableList<UserBasicView> observableUsersList = initializeUsersData();
        systemUsersTableView.setItems(observableUsersList);

        systemUsersTableView.getSortOrder().add(usersId);

        initializeTableViewSelection();
        initializeChoiceBox();
        loadIcons();

        logger.info("UsersController initialized");
    }

    private void initializeChoiceBox(){
        choiceBox.getItems().addAll(columns);
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit user");
        MenuItem delete = new MenuItem("Delete user");
        MenuItem detailedView = new MenuItem("Detailed user view");
        edit.setOnAction((ActionEvent event) -> {
            UserBasicView userView = systemUsersTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/UserEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(userView);
                stage.setTitle("BDS CSFD Edit User");

                UsersEditController controller = new UsersEditController();
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
            UserBasicView userView = systemUsersTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();

                UsersDeleteController controller = new UsersDeleteController();
                controller.initialize(userView.getId());
                fxmlLoader.setController(controller);

                ObservableList<UserBasicView> observableUsersList = initializeUsersData();
                systemUsersTableView.setItems(observableUsersList);
                systemUsersTableView.refresh();
                systemUsersTableView.sort();

            } catch (Exception ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            UserBasicView userView = systemUsersTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/UsersDetailView.fxml"));
                Stage stage = new Stage();

                Long userId = userView.getId();
                UserDetailView userDetailView = userService.getUserDetailView(userId);

                stage.setUserData(userDetailView);
                stage.setTitle("BDS CSFD Users Detailed View");

                UsersDetailViewController controller = new UsersDetailViewController();
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
        systemUsersTableView.setContextMenu(menu);
    }

    private ObservableList<UserBasicView> initializeUsersData() {
        List<UserBasicView> users = userService.getUsersBasicView();
        return FXCollections.observableArrayList(users);
    }

    private ObservableList<UserBasicView> initializeUsersFindData(String find, String choice) {
        List<UserBasicView> users = userService.getUsersFindView(find, choice);
        return FXCollections.observableArrayList(users);
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

    public void handleAddUserButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/UsersCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS CSFD Create User");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("logo/plus.png")));
            stage.setScene(scene);

            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<UserBasicView> observableUsersList = initializeUsersData();
        systemUsersTableView.setItems(observableUsersList);
        systemUsersTableView.refresh();
        systemUsersTableView.sort();
    }

    public void handleFindButton(ActionEvent actionEvent) throws IOException{
        String find = FindTextField.getText();

        UserBasicView userBasicView = new UserBasicView();
        userBasicView.setFind(find);
        userBasicView.setChoice(choiceBox.getValue());

        String choice = userBasicView.getChoice();
        String value = userBasicView.getFind();


        ObservableList<UserBasicView> observableUsersList = initializeUsersFindData(value, choice);
        systemUsersTableView.setItems(observableUsersList);
        systemUsersTableView.refresh();
        systemUsersTableView.sort();
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
