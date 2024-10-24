package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.model.Users;
import com.feroxdev.inmobigestor.navigation.LoginView;
import com.feroxdev.inmobigestor.service.UserServiceImpl;
import com.feroxdev.inmobigestor.service.UserSessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class AdminMainViewController {

    @Autowired
    LoginView loginView;
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserServiceImpl userService;

    @FXML Button adminLogout;
    @FXML Button btnUserModify;
    @FXML TextField TextUser;
    @FXML TextField TextPassword;
    @FXML TextField Text1Surname;
    @FXML TextField Text2Surname;
    @FXML TextField TextID;
    @FXML TextField TextName;
    @FXML TextField TextEmail;
    @FXML VBox optionModifyuser;
    @FXML Button btnConfirmChanges;
    @FXML Label lblFail;
    @FXML Label lblSucess;


    Users user = null;

    /**
     * Obtiene el Stage actual y lo sustituye por la pantalla de Login
     * @throws IOException:
     */
    @FXML
    private void handleLogout() throws IOException {
        Stage stage = (Stage) adminLogout.getScene().getWindow();
        loginView.showLogin(stage);
    }

    /**
     * Muestra en pantalla los datos del usuario y permite la modificación de todos los campos
     */
    @FXML
    private void handleUserModify() {
        user = userSessionService.getLoggedInUser();//hacer que esto se haga al instante de entrar en la vista
        changeVisibility(optionModifyuser);
        int idUser = user.getIdUser();
        TextUser.setText(userService.GetUserById(idUser).getUser());
        TextPassword.setText(userService.GetUserById(idUser).getPassword());
        Text1Surname.setText(userService.GetUserById(idUser).getLastname1());
        Text2Surname.setText(userService.GetUserById(idUser).getLastname2());
        TextID.setText(userService.GetUserById(idUser).getDni());
        TextName.setText(userService.GetUserById(idUser).getName());
        TextEmail.setText(userService.GetUserById(idUser).getEmail());
    }

    @FXML
    private void handleChangeUserInfo(){
        user.setUser(TextUser.getText());
        user.setPassword(TextPassword.getText());
        user.setLastname1(Text1Surname.getText());
        user.setLastname2(Text2Surname.getText());
        user.setDni(TextID.getText());
        user.setName(TextName.getText());
        user.setEmail(TextEmail.getText());
        userService.changeInfoUser(user);
        handleUserModify();
        lblSucess.setVisible(true);
    }

    /**
     * Cambia la visilidad de las distintas vbox, permitiendo el cambio entre opciones de manera orgánica
     * @param vbox: Opción que se quiere mostrar
     */
    private void changeVisibility(VBox vbox){

        optionModifyuser.setVisible(false);

        vbox.setVisible(true);
    }
}