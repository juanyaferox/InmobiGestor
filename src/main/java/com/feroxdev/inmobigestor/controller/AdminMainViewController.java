package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.navigation.LoginView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class AdminMainViewController {

    @FXML
    private Button adminLogout;

    // Método manejador del evento de logout
    @FXML
    private void handleLogout() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) adminLogout.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}