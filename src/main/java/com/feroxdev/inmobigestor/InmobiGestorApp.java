package com.feroxdev.inmobigestor;

import com.feroxdev.inmobigestor.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        // Cargar el FXML y establecer el controlador desde el contexto de Spring
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        loader.setControllerFactory(applicationContext::getBean);  // Inyecta las dependencias de Spring en el controlador
        Parent loginRoot = loader.load();

        Scene scene = new Scene(loginRoot);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

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
}
