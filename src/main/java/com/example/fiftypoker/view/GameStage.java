package com.example.fiftypoker.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the start stage for the "Fifty Poker" application.
 * This class extends the Stage class and initializes the game view.
 */
public class GameStage extends Stage {

    /**
     * Constructor for the GameStage class.
     * Loads the FXML file for the start view and sets up the stage.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    public GameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fiftypoker/game-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 700);
        setTitle("FiftyPoker - Juego de Cartas");
        getIcons().add(new Image(String.valueOf(
                getClass().getResource("/com/example/fiftypoker/favicon.png"))
        ));
        setResizable(false);
        setScene(scene);
        show();

    }

    /**
     * Holds the singleton instance of GameStage.
     */
    private static class GameStageHolder {
        private static GameStage INSTANCE;
    }

    /**
     * Returns the singleton instance of GameStage.
     * If the instance does not exist, it creates one.
     *
     * @return the singleton GameStage instance.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static GameStage getInstance() throws IOException {
        GameStageHolder.INSTANCE =
                GameStageHolder.INSTANCE != null ? GameStageHolder.INSTANCE : new GameStage();
        return GameStageHolder.INSTANCE;
    }

    /**
     * Closes the current instance of GameStage.
     */
    public static void deletedInstance() {
        GameStageHolder.INSTANCE.close();
    }
}
