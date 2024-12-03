package com.feroxdev.inmobigestor.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class LoginView {

    @Autowired
    private ApplicationContext applicationContext;

    public void showLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        loader.setControllerFactory(applicationContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
}
