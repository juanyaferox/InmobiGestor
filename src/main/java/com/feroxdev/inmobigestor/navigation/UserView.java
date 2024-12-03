package com.feroxdev.inmobigestor.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserView {

    @Autowired
    private ApplicationContext applicationContext;

    public void showUserView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_MainView.fxml"));
        loader.setControllerFactory(applicationContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("User View");
        stage.setScene(scene);
        stage.show();
    }
}