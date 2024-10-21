package com.feroxdev.inmobigestor;

import com.feroxdev.inmobigestor.navigation.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
public class InmobiGestorApp extends Application {
    private ConfigurableApplicationContext applicationContext;

    //protected StageManager stageManager;
    @Override
    public void init() throws Exception{
        // Inicia el contexto de Spring, AppConfig debe estar bien configurado
        applicationContext = springBootApplicationContext();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        loader.setControllerFactory(applicationContext::getBean); // Usar el contexto de Spring para la creación de controladores
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
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
