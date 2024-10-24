package com.feroxdev.inmobigestor;

import com.feroxdev.inmobigestor.navigation.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
public class InmobiGestorApp extends Application {

    private ConfigurableApplicationContext applicationContext;
    private LoginView loginView;

    @Override
    public void init() throws Exception{
        applicationContext = springBootApplicationContext();
        loginView = applicationContext.getBean(LoginView.class);
        //Obtiene el bean de loginView desde el contexto de Spring
        //Sucede porque la MainClass extienede desde Appliaction de JavaFX
        //la cual no es gestionada por Spring
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        loginView.showLogin(primaryStage);
    }
    @Override
    public void stop() throws Exception {
        // Cierra el contexto de Spring al detener la aplicación
        applicationContext.close();
    }

    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(InmobiGestorApp.class);
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        return builder.run(args);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
