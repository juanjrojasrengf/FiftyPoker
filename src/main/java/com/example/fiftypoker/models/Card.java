package com.example.fiftypoker.models;

import com.example.fiftypoker.controllers.GameController;

/**
 * Representa una carta en el juego "Cincuentazo".
 */

public class Card {
    private String suit; // Palo: tréboles, diamantes, corazones, picas
    private String rank; // Valor: 2-10, J, Q, K, A
    private int value; // Valor numérico según las reglas

    /**
     * Constructs a Card with the specified suit and rank. The value of the card
     * is calculated based on its rank.
     *
     * @param suit the suit of the card (e.g., "HEARTS", "DIAMONDS", "CLUBS", "SPADES")
     * @param rank the rank of the card (e.g., "ACE", "2", "3", ..., "10", "JACK", "QUEEN", "KING")
     */
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = calculateValue(rank);
    }

    /**
     * Calculates the value of the card based on its rank. The value is determined
     * as follows:
     * - "ACE": 10 (default value, can be adjusted during gameplay)
     * - "JACK", "QUEEN", "KING": -10
     * - "9": 0
     * - "2" to "8" and "10": their numeric value
     *
     * @param rank the rank of the card
     * @return the value of the card
     */
    private int calculateValue(String rank) {
        switch (rank) {
            case "ACE":
                return 10; // El valor 10 es el valor por defecto del As, al ser jugado su valor se acoplará automaticamente
            case "JACK":
            case "QUEEN":
            case "KING":
                return -10;
            case "9":
                return 0;
            default:
                return Integer.parseInt(rank);
        }
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
