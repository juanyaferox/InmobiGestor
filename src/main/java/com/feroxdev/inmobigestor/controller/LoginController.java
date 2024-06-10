package com.feroxdev.inmobigestor.controller;


import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

public class LoginController  {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Aquí puedes agregar la lógica para verificar el usuario y la contraseña.
        if ("admin".equals(username) && "admin".equals(password)) {
            Notifications.create()
                    .title("Login Successful")
                    .text("Welcome, " + username)
                    .showInformation();
        } else {
            Notifications.create()
                    .title("Login Failed")
                    .text("Invalid username or password")
                    .showError();
        }
    }
}
