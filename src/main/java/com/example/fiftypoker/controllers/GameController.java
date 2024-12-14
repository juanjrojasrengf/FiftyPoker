package com.example.fiftypoker.controllers;

import com.example.fiftypoker.models.*;
import com.example.fiftypoker.view.GameStage;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controlador principal para gestionar la lógica del juego "Cincuentazo".
 */
public class GameController {
    private List<Player> players;
    private Deck deck;
    private Table table;
    private int currentPlayerIndex;
    private boolean gameOver;

    /**
     * Constructs a GameController with the specified number of machine players.
     * Initializes the players, deck, table, and deals initial cards.
     * The first card is placed on the table to start the game.
     *
     * @param numberOfMachinePlayers the number of machine players to be included in the game
     */
    public GameController(int numberOfMachinePlayers) {
        this.players = new ArrayList<>();
        this.deck = new Deck();
        this.table = new Table();
        this.currentPlayerIndex = 0;
        this.gameOver = false;

        // Crear el jugador humano
        players.add(new Player("Humano"));

        // Crear jugadores máquina
        for (int i = 1; i <= numberOfMachinePlayers; i++) {
            players.add(new Player("Máquina " + i));
        }

        // Repartir cartas iniciales
        dealInitialCards();

        // Colocar la primera carta en la mesa
        table.playCard(deck.drawCard());
    }

    /**
     * Deals the initial set of cards to each player. Each player receives 4 cards.
     * If the deck runs out of cards, it recycles cards from the table.
     */
    private void dealInitialCards() {
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                if (deck.getRemainingCards() == 0) {
                    recycleCardsFromTable();
                }
                player.addCardToHand(deck.drawCard());
            }
        }
    }

    /**
     * Plays the turn for the current player using the card at the specified index.
     * If the game is over, it throws an IllegalStateException. It validates and plays
     * the selected card, draws a new card if possible, and checks if the player can continue.
     * If the player cannot continue, they are eliminated. The turn then passes to the next player.
     *
     * @param cardIndex the index of the card to be played
     */
    public void playTurn(int cardIndex) {
        if (gameOver) {
            throw new IllegalStateException("El juego ha terminado.");
        }

        Player currentPlayer = players.get(currentPlayerIndex);

        // Validar y jugar la carta seleccionada
        try {
            Card playedCard = currentPlayer.playCard(cardIndex);
            table.playCard(playedCard);
            System.out.println(currentPlayer.getName() + " jugó " + playedCard);

            // Tomar una nueva carta del mazo si es posible
            if (deck.getRemainingCards() == 0) {
                recycleCardsFromTable();
            }
            if (deck.getRemainingCards() > 0) {
                currentPlayer.addCardToHand(deck.drawCard());
            }

            // Verificar si el jugador puede continuar jugando
            if (currentPlayer.getHand().isEmpty()) {
                eliminatePlayer(currentPlayer);
            }
        } catch (Exception e) {
            System.out.println("Error al jugar: " + e.getMessage());
            eliminatePlayer(currentPlayer);
        }

        // Pasar al siguiente turno
        if (!gameOver) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /**
     * Allows the current player to take a card from the deck. If the game is over,
     * it throws an IllegalStateException. If the deck is empty, it recycles cards from the table.
     * If there are still cards in the deck, the current player draws a card.
     */
    public void takeCardFromDeck() {
        if (gameOver) {
            throw new IllegalStateException("El juego ha terminado.");
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        if (deck.getRemainingCards() == 0) {
            recycleCardsFromTable();
        }
        if (deck.getRemainingCards() > 0) {
            currentPlayer.addCardToHand(deck.drawCard());
        } else {
            System.out.println("No hay más cartas en el mazo.");
        }
    }

    /**
     * Recycles cards from the table back into the deck. This method collects all played cards
     * from the table and adds them back to the deck.
     */
    private void recycleCardsFromTable() {
        List<Card> recycledCards = table.collectPlayedCards();
        System.out.println("Reutilizando cartas de la mesa: " + recycledCards.size() + " cartas.");
        deck.addCardsToDeck(recycledCards);
    }

    /**
     * Checks if the current player is human. Returns true if the current player is human,
     * otherwise returns false.
     *
     * @return true if the current player is human, false otherwise
     */
    public boolean isCurrentPlayerHuman() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex).getName().equals("Humano");
        }
        return false;
    }

    /**
     * Eliminates the specified player from the game. This method removes the player from the
     * players list, adds their hand to the deck, and checks if the game is over.
     *
     * @param player the player to be eliminated
     */
    public void eliminatePlayer(Player player) {
        System.out.println(player.getName() + " ha sido eliminado.");
        players.remove(player);
        deck.addCardsToDeck(player.getHand());

        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        checkGameOver();
    }

    /**
     * Checks if the game is over. If only one player remains, the game is marked as over,
     * and a win or lose alert is displayed.
     */
    private void checkGameOver() {
        if (players.size() == 1) {
            gameOver = true;
            System.out.println("¡El ganador es " + players.get(0).getName() + "!");
            currentPlayerIndex = 0;
            winOrLoseAlert();
        }
    }

    /**
     * Plays the turn for the current machine player. This method simulates a delay for the machine's
     * response time, attempts to play a valid card, and eliminates the machine player if no valid card
     * can be played.
     */
    public void playMachineTurn() {
        if (gameOver) {
            return; // No hacer nada si el juego ha terminado
        }
        Player machinePlayer = players.get(currentPlayerIndex);
        try {
            Thread.sleep(new Random().nextInt(2000) + 2000); // Simula tiempo de respuesta
            boolean cardPlayed = false;
            for (int i = 0; i < machinePlayer.getHand().size(); i++) {
                Card card = machinePlayer.getHand().get(i);
                if (table.getCurrentSum() + card.getValue() <= 50) {
                    playTurn(i);
                    cardPlayed = true;
                    break;
                }
            }
            if (!cardPlayed) {
                eliminatePlayer(machinePlayer);
            }
        } catch (InterruptedException e) {
            System.out.println("Error en el turno de la máquina: " + e.getMessage());
        }
    }

    /**
     * Displays an alert indicating the winner of the game. This method shows an informational
     * alert with the winner's name and resets the game stage for a new game.
     */
    private void winOrLoseAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("¡El ganador es " + players.get(0).getName() + "!");

        alert.showAndWait();
        try {
            GameStage.deletedInstance();
            GameStage newGame = GameStage.getInstance();
            newGame.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Table getTable() {
        return table;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
