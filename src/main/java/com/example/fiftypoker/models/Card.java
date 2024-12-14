package com.example.fiftypoker.models;

import com.example.fiftypoker.controllers.GameController;

/**
 * Representa una carta en el juego "Cincuentazo".
 */

public class Card {
    private String suit; // Palo: tréboles, diamantes, corazones, picas
    private String rank; // Valor: 2-10, J, Q, K, A
    private int value; // Valor numérico según las reglas

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = calculateValue(rank);
    }

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
