package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.model.User;
import com.feroxdev.inmobigestor.navigation.AdminView;

import com.feroxdev.inmobigestor.navigation.UserView;
import com.feroxdev.inmobigestor.service.UserServiceImpl;
import com.feroxdev.inmobigestor.service.UserSessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class LoginController  {

    @Autowired
    AdminView adminView;

    @Autowired
    UserView userView;

    @Autowired
    UserServiceImpl userService;
    @Autowired
    UserSessionService userSessionService;

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
        User user = isValidCredentials(username, password);
        if (user != null) {
            Notifications.create()
                    .title("Login Successful")
                    .text("Welcome, " + username)
                    .showInformation();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            if (user.getIdUser()==0){
                adminView.showAdminView(stage);
            } else {
                userView.showUserView(stage);
            }

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

    private User isValidCredentials(String username, String password) {
    User user = userService.GetUserByUsername(username);
    if (user != null && user.getPassword().equals(password)) {
        userSessionService.setLoggedInUser(user);
        return user;
    }
    return null;
}

}
