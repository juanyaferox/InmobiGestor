package com.feroxdev.inmobigestor.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminView {

    public void showAdminView(Stage stage) throws IOException {
        // Cargar el FXML y establecer la escena para la vista de administrador
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin_MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Admin View");
        stage.setScene(scene);
    }
}