package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Town;
import com.feroxdev.inmobigestor.model.User;
import com.feroxdev.inmobigestor.navigation.AdminView;
import com.feroxdev.inmobigestor.navigation.LoginView;
import com.feroxdev.inmobigestor.repository.TownRepository;
import com.feroxdev.inmobigestor.service.BranchServiceImpl;
import com.feroxdev.inmobigestor.service.UserServiceImpl;
import com.feroxdev.inmobigestor.service.UserSessionService;
import com.feroxdev.inmobigestor.validation.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class AdminMainViewController {

    @Autowired
    LoginView loginView;
    @Autowired
    AdminView adminView;

    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BranchServiceImpl branchService;
    @Autowired
    TownRepository townRepository;

    @Autowired
    Validation validation;

    @FXML
    Button adminLogout;
    @FXML
    Button btnUserModify;
    @FXML
    Button btnAddNewUser;
    @FXML
    TextField textUser;
    @FXML
    TextField textPassword;
    @FXML
    TextField text1Surname;
    @FXML
    TextField text2Surname;
    @FXML
    TextField textID;
    @FXML
    TextField textName;
    @FXML
    TextField textEmail;
    @FXML
    AnchorPane optionModifyUser;
    @FXML
    AnchorPane optionListUsers;
    @FXML
    Button btnConfirmChanges;
    @FXML
    GridPane gridPaneUserList;

    //variable global del usuario actual
    User user;

    /**
     * A ejecutar al acceder a la vista asociada al controlador
     */
    public void initialize(){
        if (userSessionService!=null)
            user = userSessionService.getLoggedInUser();
    }

    //region Logout
    /**
     * Obtiene el Stage actual y lo sustituye por la pantalla de Login
     *
     * @throws IOException:
     */
    @FXML
    private void handleLogout() throws IOException {
        Stage stage = (Stage) adminLogout.getScene().getWindow();
        loginView.showLogin(stage);
    }
//endregion

    //region Modificacion de datos proprios del usuario
    /**
     * Muestra en pantalla los datos del usuario en text boxs modificables
     */
    @FXML
    private void showUserModify() {
        //Se obtiene el usuario al momento de modificar
        changeVisibility(optionModifyUser);
        textUser.setEditable(false);
        int idUser = user.getIdUser();
        textUser.setText(userService.GetUserById(idUser).getUser());
        textPassword.setText(userService.GetUserById(idUser).getPassword());
        text1Surname.setText(userService.GetUserById(idUser).getLastname1());
        text2Surname.setText(userService.GetUserById(idUser).getLastname2());
        textID.setText(userService.GetUserById(idUser).getDni());
        textName.setText(userService.GetUserById(idUser).getName());
        textEmail.setText(userService.GetUserById(idUser).getEmail());
    }

    /**
     * Recibe los datos del usuario y los modifica en la bbdd
     */
    @FXML
    private void handleUserModify() {
        log.warn(user.toString());
        user.setUser(textUser.getText());
        user.setPassword(textPassword.getText());
        user.setLastname1(text1Surname.getText());
        user.setLastname2(text2Surname.getText());
        user.setDni(textID.getText());
        user.setName(textName.getText());
        user.setEmail(textEmail.getText());
        //VALIDACIONES
        if (validation.validationUser(user)) {
            log.warn("CAMPOS A ENVIAR;------" + user.toString());
            userService.changeInfoUser(user);
            showUserModify();
            Notifications.create()
                    .title("Éxito")
                    .text("Se han realizado los cambios correctamente")
                    .showWarning();
            reloadView();
            showUserModify();
        }
    }
//endregion

    //region GESTIÓN

    //MÉTODOS A CREAR
    //metodo para que al acceder a sucursales se muestre un listado con la información básica de las sucursales
    //método para crear sucursal <-crear ventana modal para editar
    //método para editar sucursal <-crear ventana modal para editar
    //método para borrar sucursal
    //método para crear usuario desde el listado <-crear ventana modal para editar
    //(practicamente es copiar usuarios cambiando 3 cosas)

    //region SUCURSALES

    //endregion

    //region USUARIOS (administradores)

    //region Listado de usuarios
    /**
     * Maneja la opción de listado de administradores (usuarios), mostrando todos junto a algunos datos y botones
     * para editar y borrar
     */
    @FXML
    private void showAllUsersList() {
        changeVisibility(optionListUsers);
        List<User> userList = userService.allUsersList();
        log.warn("LISTA DE USUARIOS;---------------------- "+ userList.toString());
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String fullName = user.getName()+" "+user.getLastname1()+" "+(user.getLastname2() != null ? user.getLastname2(): "");
            // Añadir las celdas correspondientes en cada columna de la fila actual
            gridPaneUserList.add(new Label(user.getUser()), 0, i + 1);
            gridPaneUserList.add(new Label(user.getEmail()), 1, i + 1);
            gridPaneUserList.add(new Label(fullName), 2, i + 1);
            var userBranch = user.getBranch();
            Integer idBranch = (userBranch != null) ? userBranch.getIdBranch() : null;
            gridPaneUserList.add(new Label(String.valueOf(idBranch)), 3, i + 1);

            // Crear un HBox para contener los botones
            HBox buttonBox = new HBox();
            Button btnDelete = new Button();
            FontIcon deleteIcon = new FontIcon();
            deleteIcon.setIconColor(Color.RED);
            deleteIcon.setIconLiteral("mdi2d-delete");
            deleteIcon.setIconSize(14);
            btnDelete.setGraphic(deleteIcon);
            btnDelete.setOnAction(e -> handleUserDelete(user));

            Button btnEdit = new Button();
            FontIcon editIcon = new FontIcon();
            editIcon.setIconColor(Color.LIGHTBLUE);
            editIcon.setIconLiteral("mdi2h-human-edit");
            editIcon.setIconSize(14);
            btnEdit.setGraphic(editIcon);
            btnEdit.setOnAction(e -> showModalUserEdit(user));//aquí tiene que abrirse

            HBox.setMargin(btnEdit, new Insets(10));
            HBox.setMargin(btnDelete, new Insets(10));

            buttonBox.getChildren().addAll(btnDelete, btnEdit);

            gridPaneUserList.add(buttonBox, 4, i + 1);
        }
    }
    //endregion

    //region Edición de usuarios del listado
    /**
     * Se trata de un "mini" controlador para manejar la ventana emergente para la edición de usuario
     * @param user: usuario a editar
     */
    @FXML
    private void showModalUserEdit(User user){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin_EditUser_ModalWindow.fxml"));
            log.warn("CAMPOS A ENVIAR;------" + user.toString());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Modificar Usuario");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);//Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow();//Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);


            TextField textUser = (TextField) root.lookup("#textUser");
            TextField textPassword = (TextField) root.lookup("#textPassword");
            TextField text1Surname = (TextField) root.lookup("#text1Surname");
            TextField text2Surname = (TextField) root.lookup("#text2Surname");
            TextField textID = (TextField) root.lookup("#textID");
            TextField textName = (TextField) root.lookup("#textName");
            TextField textEmail = (TextField) root.lookup("#textEmail");
            @SuppressWarnings ("unchecked")
            ComboBox<Town> boxTown = (ComboBox<Town>) root.lookup("#boxCity");
            Button btnConfirmEditUserModal = (Button) root.lookup("#btnConfirmEditUserModal");

            int idUser = user.getIdUser();
            var userToShow = userService.GetUserById(idUser);
            //List<Town> towns = townRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
            List<Town> towns = townRepository.findAllTownsWithBranches();
            var branchItems = FXCollections.observableArrayList(towns);
            log.warn("Ciudades---------"+towns.toString());

            textUser.setEditable(false);
            textUser.setText(userToShow.getUser());
            textPassword.setText(userToShow.getPassword());
            text1Surname.setText(userToShow.getLastname1());
            text2Surname.setText(userToShow.getLastname2());
            textID.setText(userToShow.getDni());
            textName.setText(userToShow.getName());
            textEmail.setText(userToShow.getEmail());
            boxTown.setValue(userToShow.getBranch() != null ? userToShow.getBranch().getTown() : new Town());
            boxTown.setItems(branchItems);
            //para mostrar en el desplegable los nombres de ciudades y que al seleccionar funcione correctamente
            boxTown.setConverter(new StringConverter<>() {
                @Override
                public String toString(Town town) {
                    return town != null ? town.getName() : "";
                }

                @Override
                public Town fromString(String string) {
                    return branchItems.stream()
                            .filter(town -> town.getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            TextField editor = boxTown.getEditor();
            editor.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    boxTown.setItems(branchItems); // Restaurar la lista completa si el texto está vacío
                } else {
                    String search = newValue.toLowerCase();
                    ObservableList<Town> filteredItems = branchItems.filtered(town ->
                            town.getName().toLowerCase().contains(search));
                    boxTown.setItems(filteredItems); // Mostrar solo los resultados que coinciden
                    boxTown.show(); // Mantener el menú desplegado mientras se escribe
                }
            });
            btnConfirmEditUserModal.setOnAction(e -> handleListUserEdit(user, root));

            stage.showAndWait();//Bloquea la interacción con la ventana principal hasta que cierre la emergente
            reloadView();//recargo la ventana
            showAllUsersList();//muestro la lista de usuarios
            //if (btnConfirmEditUserModal != null)

        } catch (IOException e){
            e.printStackTrace();
        }


    }

    /**
     * Edición de usuario desde la ventana emergente
     * @param user: usuario a ser editado
     * @param root: ventana emergente
     */
    @FXML
    private void handleListUserEdit(User user, Parent root){
        TextField textUser = (TextField) root.lookup("#textUser");
        TextField textPassword = (TextField) root.lookup("#textPassword");
        TextField text1Surname = (TextField) root.lookup("#text1Surname");
        TextField text2Surname = (TextField) root.lookup("#text2Surname");
        TextField textID = (TextField) root.lookup("#textID");
        TextField textName = (TextField) root.lookup("#textName");
        TextField textEmail = (TextField) root.lookup("#textEmail");
        @SuppressWarnings ("unchecked")
        ComboBox<Town> boxTown = (ComboBox<Town>) root.lookup("#boxCity");

        log.warn("User antes de cambios-----------------\n"+user.toString());
        user.setUser(textUser.getText());
        user.setPassword(textPassword.getText());
        user.setLastname1(text1Surname.getText());
        user.setLastname2(text2Surname.getText());
        user.setDni(textID.getText());
        user.setName(textName.getText());
        user.setEmail(textEmail.getText());

        Town selectedTown = boxTown.getValue();
        Branch branch = branchService.verifyIfCityInBranch(selectedTown);
        log.warn("Branch despues de cambios-----------------\n"+branch.toString());
        user.setBranch(branch);
        log.warn("User despues de cambios-----------------\n"+ user.toString());

        //VALIDACIONES
        if (validation.validationUser(user) && validation.validationBranch(branch)) {
            //Crear objeto sucursal basándose en la id
            log.warn("CAMPOS A ENVIAR;------\n" + user.toString()+"\n"+branch.toString());
            userService.changeInfoUser(user);
            showAllUsersList();
            //al modificar el texto se ve mal, pero es un bug de java fx, solo se corrige recargando toda la vista
            Notifications.create()
                    .title("Éxito")
                    .text("Se han realizado los cambios correctamente")
                    .showWarning();
        }
    }
    //endregion

    //region Borrado de usuarios del listado
    /**
     * Borra el usuario, si branch es null o id=0 no borra y lanza mensaje de error
     * @param user: usuario a ser borrado
     */
    @FXML
    private void handleUserDelete(User user){
        userService.deleteUser(user);//en teoría tiene que existir por cojones
        //si devuelve null notification de error, caso contrario de éxito
        //**hay que recargar la ventana
        reloadView();
        showAllUsersList();
    }
    //endregion

    //region Creación de usuarios del listado

    @FXML
    private void showModalUserAdd(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin_EditUser_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Añadir Usuario");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);//Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow();//Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);
            @SuppressWarnings ("unchecked")
            ComboBox<Town> boxTown = (ComboBox<Town>) root.lookup("#boxCity");
            Button btnConfirmEditUserModal = (Button) root.lookup("#btnConfirmEditUserModal");

            List<Town> towns = townRepository.findAllTownsWithBranches();
            var branchItems = FXCollections.observableArrayList(towns);
            log.warn("Ciudades---------"+towns.toString());
            boxTown.setItems(branchItems);
            //para mostrar en el desplegable los nombres de ciudades y que al seleccionar funcione correctamente
            boxTown.setConverter(new StringConverter<>() {
                @Override
                public String toString(Town town) {
                    return town != null ? town.getName() : "";
                }

                @Override
                public Town fromString(String string) {
                    return branchItems.stream()
                            .filter(town -> town.getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            TextField editor = boxTown.getEditor();
            editor.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    boxTown.setItems(branchItems); // Restaurar la lista completa si el texto está vacío
                } else {
                    String search = newValue.toLowerCase();
                    ObservableList<Town> filteredItems = branchItems.filtered(town ->
                            town.getName().toLowerCase().contains(search));
                    boxTown.setItems(filteredItems); // Mostrar solo los resultados que coinciden
                    boxTown.show(); // Mantener el menú desplegado mientras se escribe
                }
            });
            btnConfirmEditUserModal.setOnAction(e -> handleListUserAdd(root));

            stage.showAndWait(); // Bloquea la interacción con la ventana principal hasta que cierre la emergente
            reloadView(); // Recargo la ventana
            showAllUsersList(); // muestro la lista de usuarios
            //if (btnConfirmEditUserModal != null)

        } catch (IOException e){
            e.printStackTrace();
        }


    }

    @FXML
    private void handleListUserAdd(Parent root){
        TextField textUser = (TextField) root.lookup("#textUser");
        TextField textPassword = (TextField) root.lookup("#textPassword");
        TextField text1Surname = (TextField) root.lookup("#text1Surname");
        TextField text2Surname = (TextField) root.lookup("#text2Surname");
        TextField textID = (TextField) root.lookup("#textID");
        TextField textName = (TextField) root.lookup("#textName");
        TextField textEmail = (TextField) root.lookup("#textEmail");
        @SuppressWarnings ("unchecked")
        ComboBox<Town> boxTown = (ComboBox<Town>) root.lookup("#boxCity");

        User user = new User();
        log.warn("User antes de cambios-----------------\n"+user.toString());
        user.setUser(textUser.getText());
        user.setPassword(textPassword.getText());
        user.setLastname1(text1Surname.getText());
        user.setLastname2(text2Surname.getText());
        user.setDni(textID.getText());
        user.setName(textName.getText());
        user.setEmail(textEmail.getText());

        Town selectedTown = boxTown.getValue();
        Branch branch = branchService.verifyIfCityInBranch(selectedTown);
        log.warn("Branch despues de cambios-----------------\n"+branch.toString());

        log.warn("User despues de cambios-----------------\n"+ user.toString());

        //VALIDACIONES
        if (validation.validationUser(user) && validation.validationBranch(branch)) {
            //Crear objeto sucursal basándose en la id
            log.warn("CAMPOS A ENVIAR;------\n" + user.toString()+"\n"+branch.toString());
            var userSaved = userService.addUser(user);
            userSaved.setBranch(branch);
            userService.changeInfoUser(user);
            showAllUsersList();
            //al modificar el texto se ve mal, pero es un bug de java fx, solo se corrige recargando toda la vista
            Notifications.create()
                    .title("Éxito")
                    .text("Se han realizado los cambios correctamente")
                    .showWarning();
        }
    }

    //endregion

    //endregion

    //endregion

    //region Cambiar visibilidad
    /**
     * Cambia la visilidad de las distintas vbox, permitiendo el cambio entre opciones
     *
     * @param anchorPane: Opción que se quiere mostrar
     */
    private void changeVisibility(AnchorPane anchorPane) {

        optionModifyUser.setVisible(false);
        optionListUsers.setVisible(false);

        anchorPane.setVisible(true);
    }

    /**
     * Sobre carga del metodo para manejar hbox
     * @param hbox:
     */
    private void changeVisibility(HBox hbox) {

        optionModifyUser.setVisible(false);
        optionListUsers.setVisible(false);

        hbox.setVisible(true);
    }

    private void changeVisibility(VBox vb) {
        optionModifyUser.setVisible(false);
        optionListUsers.setVisible(false);

        vb.setVisible(true);
    }

    /**
     * Sobre carga del metodo para ocultar todo
     */
    private void changeVisibility() {
        optionModifyUser.setVisible(false);
        optionListUsers.setVisible(false);
    }
    //endregion

    @FXML
    private void reloadView() {
        try {
            // Cargar nuevamente el archivo FXML
            adminView.showAdminView((Stage) adminLogout.getScene().getWindow());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}