package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la mesa del juego "Cincuentazo".
 */
public class Table {
    private Card activeCard; // Última carta jugada en la mesa
    private int currentSum; // Suma actual en la mesa
    private List<Card> playedCards; // Cartas jugadas

    public Table() {
        this.playedCards = new ArrayList<>();
        this.currentSum = 0;
    }

    public void playCard(Card card) {
        if (currentSum + card.getValue() > 50) {
            throw new IllegalStateException("La suma de la mesa no puede exceder 50.");
        }
        activeCard = card;
        playedCards.add(card);
        currentSum += card.getValue();
    }

    public void resetTable() {
        if (playedCards.size() > 1) {
            Card lastCard = playedCards.remove(playedCards.size() - 1); // Mantener la última carta
            playedCards.clear();
            playedCards.add(lastCard);
        }
        currentSum = activeCard != null ? activeCard.getValue() : 0;
    }

    public List<Card> collectPlayedCards() {
        List<Card> collectedCards = new ArrayList<>(playedCards);
        resetTable();
        return collectedCards;
    }

    public int getCurrentSum() {
        return currentSum;
    }

    public Card getActiveCard() {
        return activeCard;
    }
}
