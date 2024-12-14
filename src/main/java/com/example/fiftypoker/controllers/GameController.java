package com.example.fiftypoker.controllers;

import com.example.fiftypoker.models.*;
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

        // Verificar si el juego ha terminado
        checkGameOver();

        // Pasar al siguiente turno
        if (!gameOver) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

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

    private void recycleCardsFromTable() {
        List<Card> recycledCards = table.collectPlayedCards();
        System.out.println("Reutilizando cartas de la mesa: " + recycledCards.size() + " cartas.");
        deck.addCardsToDeck(recycledCards);
    }

    public boolean isCurrentPlayerHuman() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex).getName().equals("Humano");
        } return false;
    }

    private void eliminatePlayer(Player player) {
        System.out.println(player.getName() + " ha sido eliminado.");
        players.remove(player);
        deck.addCardsToDeck(player.getHand());

        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        checkGameOver();
    }

    private void checkGameOver() {
        if (players.size() == 1) {
            gameOver = true;
            System.out.println("¡El ganador es " + players.get(0).getName() + "!");
            currentPlayerIndex = 0;
        }
    }

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
