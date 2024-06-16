package com.feroxdev.inmobigestor.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class LoginController  {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginLbl;

    @FXML
    private Button btnSingIn;

    @FXML
    private void initialize() {
        Font roboto = Font.loadFont(getClass().getResourceAsStream("/fonts/roboto/Roboto-Regular.ttf"), 30);
        loginLbl.setFont(roboto);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
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

    private boolean isValidCredentials(String username, String password) {
        // Here you can add your authentication logic, for simplicity we just check if both fields are non-empty
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }

    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Main Application");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
