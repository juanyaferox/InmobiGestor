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
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
public class AdminMainViewController {

    @Autowired
    LoginView loginView;
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserServiceImpl userService;

    @FXML
    Button adminLogout;
    @FXML
    Button btnUserModify;
    @FXML
    TextField TextUser;
    @FXML
    TextField TextPassword;
    @FXML
    TextField Text1Surname;
    @FXML
    TextField Text2Surname;
    @FXML
    TextField TextID;
    @FXML
    TextField TextName;
    @FXML
    TextField TextEmail;
    @FXML
    VBox optionModifyuser;
    @FXML
    Button btnConfirmChanges;
    @FXML
    Label lblFail;
    @FXML
    Label lblSucess;


    Users user = null;//variable global del usuario actual

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

    /**
     * Recibe los datos del usuario y los modifica en la bbdd
     */
    @FXML
    private void handleChangeUserInfo() {
        user.setUser(TextUser.getText());
        user.setPassword(TextPassword.getText());
        user.setLastname1(Text1Surname.getText());
        user.setLastname2(Text2Surname.getText());
        user.setDni(TextID.getText());
        user.setName(TextName.getText());
        user.setEmail(TextEmail.getText());
        //VALIDACIONES
        if (validationUser(user)) {
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
     * Cambia la visilidad de las distintas vbox, permitiendo el cambio entre opciones de manera orgánica
     *
     * @param vbox: Opción que se quiere mostrar
     */
    private void changeVisibility(VBox vbox) {

        optionModifyuser.setVisible(false);

        vbox.setVisible(true);
    }

    private void validationNotification(String text, int num) {
        Notifications.create()
                .title("Campo " + text + " inválido")
                .text("Asegurese de que el campo " + text + " es correcto")
                .showError();
    }

    private void validationNotification(String text) {
        validationNotification(text, 0);
    }

    /**
     * @param user: Usuario a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    private boolean validationUser(Users user) {
        boolean isValid = true;

        if (user.getUser().length() > 50 || user.getUser().isEmpty()) {
            validationNotification("Usuario");
            isValid = false;
        }
        if (user.getPassword().length() > 255 || user.getPassword().isEmpty()) {
            validationNotification("Contraseña");
            isValid = false;
        }
        if (user.getEmail().length() > 255) {
            validationNotification("Correo");
            isValid = false;
        }
        if (user.getName().length() > 50 || user.getName().isEmpty()) {
            validationNotification("Nombre");
            isValid = false;
        }
        if (user.getLastname1().length() > 50 || user.getLastname1().isEmpty()) {
            validationNotification("Primer apellido");
            isValid = false;
        }
        if (user.getLastname2().length() > 50) {
            validationNotification("Segundo apellido");
            isValid = false;
        }
        if (user.getDni().length() > 9 || user.getDni().isEmpty()) {
            validationNotification("DNI");
            isValid = false;
        }
        return isValid;
    }


    //función para calcular el dni (no se usar para evitar molestias)
    private static boolean isDNIValid(String dni) {
        if (dni == null || !dni.matches("\\d{8}[A-Z]")) {
            return false;
        }
        int numeroDNI = Integer.parseInt(dni.substring(0, 8));
        char letraDNI = dni.charAt(8);
        String letras_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letraCorrecta = letras_DNI.charAt(numeroDNI % 23);

        return letraDNI == letraCorrecta;
    }
}