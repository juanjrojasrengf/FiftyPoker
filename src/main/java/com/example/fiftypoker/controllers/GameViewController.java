package com.example.fiftypoker.controllers;

import com.example.fiftypoker.models.Card;
import com.example.fiftypoker.models.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import java.io.InputStream;

public class GameViewController {

    @FXML
    private Label tableSumLabel;

    @FXML
    private ImageView activeCardImage;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView tableImage;

    @FXML
    private ImageView deckBackImage;

    @FXML
    private HBox playerCardsBox;

    @FXML
    private VBox machineLeftBox;

    @FXML
    private HBox machineTopBox;

    @FXML
    private VBox machineRightBox;

    private GameController gameController;

    @FXML
    private ComboBox<String> machineCountComboBox;

    @FXML
    private void initialize() {
        // Configurar el controlador del juego con 3 jugadores máquina
        configureGame(0);
        machineCountComboBox.getItems().clear();
        machineCountComboBox.getItems().addAll("1", "2", "3");
        machineCountComboBox.setOnAction(this::onSelectNumberOfMachines);
        setBackgroundAndTableImages();
    }

    private void setBackgroundAndTableImages() {
        try {
            // Cargar el fondo
            InputStream backgroundStream = getClass().getResourceAsStream("/com/example/fiftypoker/background.png");
            if (backgroundStream != null) {
                backgroundImage.setImage(new Image(backgroundStream));
            }

            // Cargar el tablero (mesa)
            InputStream tableStream = getClass().getResourceAsStream("/com/example/fiftypoker/Table.png");
            if (tableStream != null) {
                tableImage.setImage(new Image(tableStream));
            }

            // Cargar la carta volteada (parte trasera de las cartas)
            InputStream cardBackStream = getClass().getResourceAsStream("/com/example/fiftypoker/cardback.png");
            if (cardBackStream != null) {
                deckBackImage.setImage(new Image(cardBackStream));
            }
        } catch (Exception e) {
            System.out.println("Error al cargar las imágenes: " + e.getMessage());
        }
    }

    @FXML
    private void onCardDoubleClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ImageView cardView = (ImageView) event.getSource();
            int index = playerCardsBox.getChildren().indexOf(cardView);
            if (gameController.isCurrentPlayerHuman()) {
                gameController.playTurn(index);
                updateView();
            } else {
                System.out.println("No es el turno del jugador humano.");
            }
        }
    }

    @FXML
    private void onDeckDoubleClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (gameController.isCurrentPlayerHuman()) {
                gameController.takeCardFromDeck();
                updateView();
            } else {
                System.out.println("No es el turno del jugador humano.");
            }
        }
    }

    private void updateView() {
        // Actualizar la suma actual en la mesa
        tableSumLabel.setText("Suma actual: " + gameController.getTable().getCurrentSum());

        // Actualizar la carta activa en la mesa
        Card activeCard = gameController.getTable().getActiveCard();
        if (activeCard != null) {
            activeCardImage.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/fiftypoker/" + activeCard.getRank().toLowerCase() + "_of_" + activeCard.getSuit() + ".png")));
        }

        // Actualizar las cartas del jugador humano
        playerCardsBox.getChildren().clear();
        Player humanPlayer = gameController.getPlayers().get(0);
        for (int i = 0; i < humanPlayer.getHand().size(); i++) {
            Card card = humanPlayer.getHand().get(i);
            ImageView cardImage = new ImageView(new Image(getClass().getResourceAsStream(
                    "/com/example/fiftypoker/" + card.getRank().toLowerCase() + "_of_" + card.getSuit() + ".png")));
            cardImage.setFitWidth(80);
            cardImage.setFitHeight(120);
            cardImage.setOnMouseClicked(this::onCardDoubleClicked);
            playerCardsBox.getChildren().add(cardImage);
        }

        // Actualizar las cartas de los jugadores máquina
        updateMachineBox(machineLeftBox, 1);
        updateMachineBox(machineTopBox, 2);
        updateMachineBox(machineRightBox, 3);
    }

    private void updateMachineBox(Pane machineBox, int playerIndex) {
        if (playerIndex < gameController.getPlayers().size()) {
            machineBox.getChildren().clear(); // Limpia las cartas existentes
            Player machinePlayer = gameController.getPlayers().get(playerIndex);

            // Añade las cartas del jugador máquina
            for (int j = 0; j < machinePlayer.getHand().size(); j++) {
                ImageView backCardImage = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fiftypoker/cardback.png")));
                backCardImage.setFitWidth(80);
                backCardImage.setFitHeight(120);
                machineBox.getChildren().add(backCardImage);
            }
        } else {
            machineBox.getChildren().clear(); // Si no hay jugador, deja vacío el contenedor
        }
    }

    private void configureTurnSystem() {
        new Thread(() -> {
            while (!gameController.isGameOver()) {
                if (!gameController.isCurrentPlayerHuman()) {
                    try {
                        gameController.playMachineTurn();
                        updateView();
                        Thread.sleep(1000); // Espera para que se vea el cambio de turno
                    } catch (InterruptedException e) {
                        System.out.println("Error en el sistema de turnos: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    public void configureGame(int numberOfMachinePlayers) {
        gameController = new GameController(numberOfMachinePlayers);
        updateView();
        configureTurnSystem();
    }
    @FXML
    private void onSelectNumberOfMachines(ActionEvent event) {
        String selectedValue = machineCountComboBox.getValue();
        if (selectedValue != null) {
            int numberOfMachines = Integer.parseInt(selectedValue);
            configureGame(numberOfMachines);
        }
    }
}
