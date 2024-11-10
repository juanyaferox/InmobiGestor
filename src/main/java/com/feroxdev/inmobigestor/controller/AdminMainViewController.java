package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.model.Users;
import com.feroxdev.inmobigestor.navigation.LoginView;
import com.feroxdev.inmobigestor.service.UserServiceImpl;
import com.feroxdev.inmobigestor.service.UserSessionService;
import com.feroxdev.inmobigestor.validation.ModifyUserValidation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Slf4j
@Controller
public class AdminMainViewController {

    @Autowired
    LoginView loginView;
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ModifyUserValidation userValidation;

    @FXML
    Button adminLogout;
    @FXML
    Button btnUserModify;
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
    VBox optionModifyuser;
    @FXML
    Button btnConfirmChanges;
    @FXML
    Label lblFail;
    @FXML
    Label lblSucess;

    //variable global del usuario actual
    Users user;

    /**
     * A ejecutar al acceder a la vista asociada al controlador
     */
    public void initialize(){
        user = userSessionService.getLoggedInUser();
    }

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

    /**
     * Muestra en pantalla los datos del usuario en text boxs modificables
     */
    @FXML
    private void handleUserModify() {
        //Se obtiene el usuario al momento de modificar

        changeVisibility(optionModifyuser);
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
    private void handleChangeUserInfo() {
        user.setUser(textUser.getText());
        user.setPassword(textPassword.getText());
        user.setLastname1(text1Surname.getText());
        user.setLastname2(text2Surname.getText());
        user.setDni(textID.getText());
        user.setName(textName.getText());
        user.setEmail(textEmail.getText());
        //VALIDACIONES
        if (userValidation.validationUser(user)) {
            log.warn("CAMPOS A ENVIAR;------" + user.toString());
            userService.changeInfoUser(user);
            handleUserModify();
            Notifications.create()
                    .title("Éxito")
                    .text("Se han realizado los cambios correctamente")
                    .showWarning();
        }
    }


    /**
     * Cambia la visilidad de las distintas vbox, permitiendo el cambio entre opciones
     *
     * @param vbox: Opción que se quiere mostrar
     */
    private void changeVisibility(VBox vbox) {

        optionModifyuser.setVisible(false);

        vbox.setVisible(true);
    }


}