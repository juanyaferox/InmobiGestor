package com.feroxdev.inmobigestor.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
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
    private Label lblForgotPassword;

    @FXML
    private void initialize() {
        Font roboto = Font.loadFont(getClass().getResourceAsStream("/fonts/roboto/Roboto-Regular.ttf"), 30);
        loginLbl.setFont(roboto);
    }

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
            Notifications.create()
                    .title("Login Successful")
                    .text("Welcome, " + username)
                    .showInformation();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin_MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } else {
            Notifications.create()
                    .title("Login Failed")
                    .text("Invalid username or password")
                    .showError();
        }
    }

    @FXML
    private void handleMouseEntered() {
        lblForgotPassword.setStyle("-fx-text-fill: #D1EBBF;");
    }

    @FXML
    private void handleMouseExited() {
        lblForgotPassword.setStyle("-fx-text-fill: #AEE486;");
    }

    @FXML
    private void handleForgotPassword() {
        lblForgotPassword.setStyle("-fx-text-fill: #477624;");
    }

    private boolean isValidCredentials(String username, String password) {
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

    /*public void onLoginSuccess(Users user) {
        if (user.getRole().equals("ADMIN")) {
            sceneManager.switchToAdminView();
        } else if (user.getRole().equals("USER")) {
            sceneManager.switchToUserView();
        } else if (user.getRole().equals("MANAGER")) {
            sceneManager.switchToManagerView();
        }
    }*/

}
