package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo de cartas en el juego "Cincuentazo".
 */

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "JACK", "QUEEN", "KING", "ACE"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("El mazo está vacío. No se pueden robar más cartas.");
        }
        return cards.remove(cards.size() - 1);
    }

    public void addCardsToDeck(List<Card> newCards) {
        cards.addAll(newCards);
        shuffle();
    }

    public int getRemainingCards() {
        return cards.size();
    }
}
