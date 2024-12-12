package com.example.fiftypoker;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fiftypoker/GameView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("FiftyPoker - Juego de Cartas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
