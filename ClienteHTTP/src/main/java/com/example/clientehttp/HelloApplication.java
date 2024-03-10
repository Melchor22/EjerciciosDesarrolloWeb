package com.example.clientehttp;

import com.example.clientehttp.Controllers.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Views/hello-view.fxml"));
            Parent root = fxmlLoader.load();
            HelloController controlador = fxmlLoader.getController();
            controlador.iniciarVentana();
            Scene scene = new Scene(root);
            stage.setTitle("Cliente HTTP");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}