package com.example.fiftypoker;

import com.example.fiftypoker.view.GameStage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal para iniciar el proyecto "FiftyPoker".
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameStage.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}
