package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of the "Cincuentazo" game.
 */

public class Deck {
    private List<Card> cards;

    /**
     * Constructs a Deck and initializes it with a full set of cards.
     * The deck is then shuffled.
     */
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    /**
     * Initializes the deck with a full set of 52 cards.
     * Each card is created with a suit and rank.
     */
    private void initializeDeck() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "JACK", "QUEEN", "KING", "ACE"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Shuffles the deck using the Collections.shuffle method.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws a card from the deck. If the deck is empty, it throws an IllegalStateException.
     *
     * @return the drawn card
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("El mazo está vacío. No se pueden robar más cartas.");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Adds a list of new cards to the deck and shuffles the deck.
     *
     * @param newCards the list of new cards to add to the deck
     */
    public void addCardsToDeck(List<Card> newCards) {
        cards.addAll(newCards);
        shuffle();
    }

    public int getRemainingCards() {
        return cards.size();
    }
}
