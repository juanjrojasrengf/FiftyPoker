package com.example.fiftypoker.controllers;

import com.example.fiftypoker.models.Card;
import com.example.fiftypoker.models.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private Label turnLabel;

    private ScheduledExecutorService scheduler;

    @FXML
    private void initialize() {
        scheduler = Executors.newScheduledThreadPool(1); // Inicializar el scheduler aquí
        configureGame(0);
        machineCountComboBox.getItems().clear();
        machineCountComboBox.getItems().addAll("1", "2", "3");
        machineCountComboBox.setOnAction(this::onSelectNumberOfMachines);
        setBackgroundAndTableImages();
        handleHelpButton();
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
                Platform.runLater(this::updateView);
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
                Platform.runLater(this::updateView);
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
        scheduler.scheduleAtFixedRate(() -> {
            if (gameController.isGameOver()) {
                scheduler.shutdown(); // Detener el scheduler cuando el juego haya terminado
                return;
            }

            // Determina quién tiene el turno
            if (gameController.isCurrentPlayerHuman()) {
                Platform.runLater(() -> setTurnLabel("Tu turno"));
            } else {
                int currentMachinePlayerIndex = gameController.getCurrentPlayerIndex();
                Platform.runLater(() -> setTurnLabel("Turno de la Máquina " + (currentMachinePlayerIndex)));
            }

            if (!gameController.isCurrentPlayerHuman()) {
                try {
                    gameController.playMachineTurn();
                    Platform.runLater(this::updateView);
                } catch (Exception e) {
                    System.out.println("Error en el sistema de turnos: " + e.getMessage());
                }
            }
        }, 0, 4, TimeUnit.SECONDS);
    }


    public void configureGame(int numberOfMachinePlayers) {
        gameController = new GameController(numberOfMachinePlayers);
        Platform.runLater(this::updateView);
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

    @FXML
    private void handleHelpButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reglas de Fifty Poker");
        alert.setHeaderText("Reglas de Fifty Poker");
        alert.setContentText(
                "El objetivo es ser el último jugador en el juego evitando que la suma de las cartas en la mesa supere 50. Cada jugador (tú y las máquinas) recibe 4 cartas al azar. Una carta se coloca boca arriba en la mesa, y su valor inicial será la suma de la mesa. Las cartas restantes forman el mazo.\n\n" +
                        "Reglas de las cartas:\n" +
                        "- 2-8 y 10: Suman su número a la suma de la mesa.\n" +
                        "- 9: No modifica la suma.\n" +
                        "- J, Q, K: Restan 10.\n" +
                        "- A (As): Puede sumar 1 o 10, según lo que más te convenga.\n\n" +
                        "Turnos:\n" +
                        "El juego se juega por turnos. En tu turno:\n" +
                        "- Mira las cartas en tu mano y elige una que puedas jugar sin que la suma supere 50.\n" +
                        "- Coloca la carta elegida en la mesa. La suma de la mesa se actualiza según el valor de la carta.\n" +
                        "- Toma una nueva carta del mazo para tener siempre 4 cartas en la mano.\n" +
                        "- Si no puedes jugar ninguna carta porque todas exceden 50, quedas eliminado.\n\n" +
                        "Turno de la máquina:\n" +
                        "Las máquinas también jugarán en su turno siguiendo las mismas reglas.\n\n" +
                        "Eliminación:\n" +
                        "Si un jugador (tú o una máquina) no puede jugar ninguna carta sin exceder 50, queda eliminado y sus cartas se agregan al final del mazo.\n\n" +
                        "Fin del Juego:\n" +
                        "El juego termina cuando solo queda un jugador. ¡Ese jugador será el ganador!"
        );
        alert.getDialogPane().setMinWidth(600);
        alert.getDialogPane().setMinHeight(300);
        alert.showAndWait();
        howToPlayAlert();
    }

    private void howToPlayAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¿Como jugar Fifty Poker?");
        alert.setHeaderText("¿Como jugar Fifty Poker?");
        alert.setContentText(
                "¡Para poder jugar Fifty Poker debes revisar primero las reglas del juego!\n\n" +
                        "Luego de leer las reglas, puedes eligir el Número(#) de bots contra los cuales quieres jugar\n" +
                        "Al escoger la cantidad de bots contra los cuales quieres jugar, ya podras visualizar la mesa con la suma total de las cartas en juego\n" +
                        "Para lanzar una carta deberás dar un doble click sobre esta.\n" +
                        "¡Es hora de ganar!"
        );
        alert.showAndWait();
    }

    public void setTurnLabel(String turn){
        turnLabel.setText(turn);
    }
}
