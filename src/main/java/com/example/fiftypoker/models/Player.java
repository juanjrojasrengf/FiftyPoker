package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un jugador en el juego "Cincuentazo".
 */
public class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        } else {
            throw new IllegalStateException("Un jugador no puede tener más de 4 cartas en su mano.");
        }
    }

    public Card playCard(int index) {
        if (index < 0 || index >= hand.size()) {
            throw new IndexOutOfBoundsException("Índice inválido para jugar una carta.");
        }
        return hand.remove(index);
    }

    @Override
    public String toString() {
        return name + " tiene " + hand;
    }
}
