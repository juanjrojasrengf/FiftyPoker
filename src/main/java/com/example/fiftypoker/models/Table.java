package com.example.fiftypoker.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la mesa del juego "Cincuentazo".
 */
public class Table {
    private Card activeCard; // Última carta jugada en la mesa
    private int currentSum; // Suma actual en la mesa
    private List<Card> playedCards; // Cartas jugadas en la mesa

    public Table() {
        this.playedCards = new ArrayList<>();
        this.currentSum = 0;
    }

    /**
     * Juega una carta en la mesa y actualiza la suma.
     * @param card Carta a jugar.
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
     * Reinicia la mesa conservando solo la última carta jugada.
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
     * Recolecta todas las cartas jugadas excepto la última.
     * @return Lista de cartas recolectadas.
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
