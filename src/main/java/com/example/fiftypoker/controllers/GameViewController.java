package com.example.fiftypoker.controllers;

import com.example.fiftypoker.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;

/**
 * Controlador para manejar la vista del juego "FiftyPoker".
 */
public class GameViewController {

    @FXML
    private Label tableSumLabel;

    @FXML
    private ImageView activeCardImage;

    @FXML
    private HBox playerCardsBox;

    @FXML
    private VBox machinePlayersBox;

    @FXML
    private Button playCardButton;

    @FXML
    private Button endTurnButton;

    private GameController gameController;

    public void initialize() {
        // Inicializar el controlador del juego con 2 jugadores máquina
        gameController = new GameController(2);
        updateView();
    }

    @FXML
    private void onPlayCardButtonClicked() {
        try {
            gameController.playTurn(0);
            updateView();
        } catch (IllegalStateException e) {
            System.out.println("No se puede jugar esta carta: " + e.getMessage());
        }
    }

    @FXML
    private void onEndTurnButtonClicked() {
        try {
            gameController.playMachineTurn();
            updateView();
        } catch (IllegalStateException e) {
            System.out.println("Error al terminar el turno: " + e.getMessage());
        }
    }

    private void updateView() {
        // Actualizar la suma actual en la mesa
        tableSumLabel.setText("Suma actual: " + gameController.getTable().getCurrentSum());

        // Actualizar la carta activa en la mesa
        Card activeCard = gameController.getTable().getActiveCard();
        if (activeCard != null) {
            setImageToImageView(activeCardImage, activeCard);
        }

        // Actualizar las cartas del jugador humano
        playerCardsBox.getChildren().clear();
        Player humanPlayer = gameController.getPlayers().get(0);
        for (Card card : humanPlayer.getHand()) {
            ImageView cardImage = new ImageView();
            setImageToImageView(cardImage, card);
            cardImage.setFitWidth(60);
            cardImage.setFitHeight(90);
            playerCardsBox.getChildren().add(cardImage);
        }
    }

    private void setImageToImageView(ImageView imageView, Card card) {
        try {
            String resourcePath = "/com/example/fiftypoker/" + card.getRank().toLowerCase() + "_of_" + card.getSuit() + ".png";
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream != null) {
                imageView.setImage(new Image(inputStream));
            } else {
                System.out.println("Recurso no encontrado: " + resourcePath);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
}
