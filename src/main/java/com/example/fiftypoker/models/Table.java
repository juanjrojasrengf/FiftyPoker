package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the table of "Cincuentazo" game.
 */
public class Table {
    private Card activeCard; // Última carta jugada en la mesa
    private int currentSum; // Suma actual en la mesa
    private List<Card> playedCards; // Cartas jugadas en la mesa

    /**
     * Constructs a Table with an empty list of played cards and a current sum of 0.
     */
    public Table() {
        this.playedCards = new ArrayList<>();
        this.currentSum = 0;
    }

    /**
     * Plays a card on the table and updates the sum.
     * If the card is an Ace, its value is adjusted to either 1 or 10 based on the current sum.
     * Throws an IllegalStateException if the new sum exceeds 50.
     *
     * @param card the card to play
     */
    public void playCard(Card card) {
        int cardValue = card.getValue();
        if ("ACE".equals(card.getRank())) {
            cardValue = (currentSum + 10 > 50) ? 1 : 10;
        }
        int newSum = currentSum + cardValue;
        if (newSum > 50) {
            throw new IllegalStateException("La suma de la mesa no puede exceder 50.");
        }
        activeCard = card;
        playedCards.add(card);
        currentSum = newSum;
    }

    /**
     * Resets the table, keeping only the last played card.
     * The current sum is updated to the value of the last played card.
     */
    public void resetTable() {
        if (!playedCards.isEmpty()) {
            Card lastCard = playedCards.remove(playedCards.size() - 1);
            playedCards.clear();
            playedCards.add(lastCard);
            currentSum = lastCard.getValue();
        } else {
            currentSum = 0;
        }
    }

    /**
     * Collects all played cards except the last one.
     * Resets the table and returns the list of collected cards.
     *
     * @return a list of collected cards
     */
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
