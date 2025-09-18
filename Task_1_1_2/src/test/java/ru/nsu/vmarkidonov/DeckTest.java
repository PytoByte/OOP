package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckTest {

    @Test
    void cardsLimit() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            assertDoesNotThrow(deck::takeCard);
        }

        assertEquals(0, deck.getRemainingCardsCount());
        assertThrows(IndexOutOfBoundsException.class, deck::takeCard);
    }

    @Test
    void uniqueCards() {
        Deck deck = new Deck();

        // Take card from deck 52 times
        GameCard[] gameCards = new GameCard[52];
        for (int i = 0; i < 52; i++) {
            gameCards[i] = deck.takeCard();

            for (int j = 0; j < i; j++) {
                assertNotEquals(gameCards[i], gameCards[j]);
            }
        }

        assertEquals(0, deck.getRemainingCardsCount());
    }

    @Test
    void deckCycle() {
        Deck deck = new Deck();

        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 52; i++) {
                deck.takeCard();
            }

            assertEquals(0, deck.getRemainingCardsCount());

            deck.restore();
        }
    }

    @Test
    void reusingSameCardObjects() {
        Deck deck = new Deck();

        GameCard[] gameCards = new GameCard[52];
        for (int i = 0; i < 52; i++) {
            gameCards[i] = deck.takeCard();
        }

        assertEquals(0, deck.getRemainingCardsCount());

        deck.restore();

        for (int i = 0; i < 52; i++) {
            GameCard takenCard = deck.takeCard();

            int foundCard = 0;
            for (GameCard gameCard : gameCards) {
                if (takenCard == gameCard) {
                    foundCard = 1;
                    break;
                }
            }

            assertEquals(1, foundCard);
        }
    }

    @Test
    void restoreCardState() {
        Deck deck = new Deck();

        GameCard gameCard = deck.takeCard();
        gameCard.hidden = false;
        gameCard.initValue(0);

        deck.restore();

        assertTrue(gameCard.hidden);
        gameCard.hidden = false;
        assertEquals(0, gameCard.getValue());
    }
}