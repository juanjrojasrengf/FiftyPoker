package com.example.fiftypoker.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Juan");
    }

    @Test
    public void testAddCardToHand() {
        Card card1 = new Card("HEARTS", "2");
        Card card2 = new Card("DIAMONDS", "3");

        player.addCardToHand(card1);
        player.addCardToHand(card2);

        List<Card> hand = player.getHand();
        assertEquals(2, hand.size(), "El jugador debería tener 2 cartas en su mano.");
        assertTrue(hand.contains(card1), "La carta 2 de corazones debería estar en la mano.");
        assertTrue(hand.contains(card2), "La carta 3 de diamantes debería estar en la mano.");
    }

    @Test
    public void testAddMoreThanFourCards() {
        player.addCardToHand(new Card("HEARTS", "2"));
        player.addCardToHand(new Card("DIAMONDS", "3"));
        player.addCardToHand(new Card("CLUBS", "4"));
        player.addCardToHand(new Card("SPADES", "5"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            player.addCardToHand(new Card("HEARTS", "6"));
        });
        assertEquals("Un jugador no puede tener más de 4 cartas en su mano.", exception.getMessage());
    }

    @Test
    public void testPlayCard() {
        Card card1 = new Card("HEARTS", "2");
        player.addCardToHand(card1);

        Card playedCard = player.playCard(0);

        assertEquals(card1, playedCard, "La carta jugada debería ser la misma que la carta en la mano.");

        assertTrue(player.getHand().isEmpty(), "La mano del jugador debería estar vacía después de jugar la carta.");
    }

    @Test
    public void testPlayCardWithInvalidIndex() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            player.playCard(0);
        });
        assertEquals("Índice inválido para jugar una carta.", exception.getMessage());
    }

    @Test
    public void testToString() {
        Card card1 = new Card("HEARTS", "2");
        player.addCardToHand(card1);

        String expectedString = "Juan tiene [2 of HEARTS]";
        assertEquals(expectedString, player.toString(), "La cadena representativa del jugador debería coincidir.");
    }
}
