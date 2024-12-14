package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the "Cincuentazo" game.
 */
public class Player {
    private String name;
    private List<Card> hand;

    /**
     * Constructs a Player with the specified name and an empty hand.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the hand of the player.
     *
     * @return the hand of the player
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand. If the hand already contains 4 cards,
     * it throws an IllegalStateException.
     *
     * @param card the card to add to the hand
     * @throws IllegalStateException if the hand already contains 4 cards
     */
    public void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        } else {
            throw new IllegalStateException("Un jugador no puede tener más de 4 cartas en su mano.");
        }
    }

    /**
     * Plays a card from the player's hand at the specified index.
     * If the index is out of bounds, it throws an IndexOutOfBoundsException.
     *
     * @param index the index of the card to play
     * @return the card that was played
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
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
