package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.model.Town;
import com.feroxdev.inmobigestor.model.User;
import com.feroxdev.inmobigestor.navigation.LoginView;
import com.feroxdev.inmobigestor.navigation.UserView;
import com.feroxdev.inmobigestor.service.UserService;
import com.feroxdev.inmobigestor.service.UserSessionService;
import com.feroxdev.inmobigestor.validation.Validation;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class UserMainViewController {
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserService userService;

    @Autowired
    LoginView loginView;
    @Autowired
    UserView userView;

    @Autowired
    Validation validation;

    @FXML
    Button adminLogout;
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
    AnchorPane optionListBranchs;

    //variable global del usuario actual
    User user;

    //region Logout

    /**
     * Obtiene el Stage actual y lo sustituye por la pantalla de Login
     *
     * @throws IOException :
     */
    @FXML
    private void handleLogout() throws IOException {
        Stage stage = (Stage) adminLogout.getScene().getWindow();
        loginView.showLogin(stage);
    }
//endregion

    //region MODIFICACION de datos proprios del usuario

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

    @FXML
    private void reloadView() {
        try {
            userView.showUserView((Stage) adminLogout.getScene().getWindow());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeVisibility(AnchorPane anchorPane) {

        optionModifyUser.setVisible(false);
        optionListUsers.setVisible(false);
        optionListBranchs.setVisible(false);

        anchorPane.setVisible(true);
    }

    private void configureTownComboBox(ComboBox<Town> boxTown, ObservableList<Town> branchItems) {
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

        // Configurar el filtrado del ComboBox mediante el editor de texto
        TextField editor = boxTown.getEditor();
        FilteredList<Town> filteredItems = new FilteredList<>(branchItems, p -> true);
        boxTown.setItems(filteredItems);

        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filteredItems.setPredicate(town -> true); // Muestra todos los elementos
            } else {
                String search = newValue.toLowerCase();
                filteredItems.setPredicate(town ->
                        town.getName().toLowerCase().contains(search)); // Filtro aplicado
            }
            boxTown.show(); // Mantiene el ComboBox desplegado mientras se escribe
        });
    }
}
