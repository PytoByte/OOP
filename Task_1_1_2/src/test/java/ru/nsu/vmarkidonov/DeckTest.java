package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {

    @Test
    void cardsLimit() {
        // Init deck
        Deck deck = new Deck();

        // Take card from deck 52 times
        for (int i = 0; i < 52; i++) {
            assertDoesNotThrow(deck::takeCard);
        }

        // Check deck is empty
        assert deck.getRemainingCardsCount() == 0;
        assertThrows(IndexOutOfBoundsException.class, deck::takeCard);
    }

    @Test
    void uniqueCards() {
        // Init deck
        Deck deck = new Deck();

        // Take card from deck 52 times
        GameCard[] gameCards = new GameCard[52];
        for (int i = 0; i < 52; i++) {
            gameCards[i] = deck.takeCard();

            // Check that cards are unique
            for (int j = 0; j < i; j++) {
                assert gameCards[i] != gameCards[j];
            }
        }

        // Check deck is empty
        assert deck.getRemainingCardsCount() == 0;
    }

    @Test
    void deckCycle() {
        // Init deck
        Deck deck = new Deck();

        // Repeat test 2 times
        for (int k = 0; k < 2; k++) {
            // Take card from deck 52 times
            for (int i = 0; i < 52; i++) {
                deck.takeCard();
            }

            // Check deck is empty
            assert deck.getRemainingCardsCount() == 0;

            // Restore deck
            deck.restore();
        }
    }

    @Test
    void memorySafe() {
        // Init deck
        Deck deck = new Deck();

        // Take card from deck 52 times
        GameCard[] gameCards = new GameCard[52];
        for (int i = 0; i < 52; i++) {
            gameCards[i] = deck.takeCard();
        }

        // Check deck is empty
        assert deck.getRemainingCardsCount() == 0;

        // Restore deck
        deck.restore();

        // Take card from deck 52 times again
        for (int i = 0; i < 52; i++) {
            GameCard takenCard = deck.takeCard();

            // Check takenCard pointer appeared in prev cycle
            int foundCard = 0;
            for (GameCard gameCard : gameCards) {
                if (takenCard == gameCard) {
                    foundCard = 1;
                    break;
                }
            }
            assert foundCard == 1;
        }
    }

    @Test
    void restoreCardState() {
        // Init deck
        Deck deck = new Deck();

        // Take card and change it's state
        GameCard gameCard = deck.takeCard();
        gameCard.hidden = false;
        gameCard.initValue(0);

        // Restore deck
        deck.restore();

        // Check card's state restored
        assert gameCard.hidden;
        gameCard.hidden = false;
        assert gameCard.getValue() == 0;
    }
}