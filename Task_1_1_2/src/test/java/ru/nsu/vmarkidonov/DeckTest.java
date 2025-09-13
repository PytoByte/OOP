package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {

    @Test
    void deckCycle() {
        Deck deck = new Deck();

        for (int k = 0; k < 2; k++) {
            GameCard[] gameCards = new GameCard[52];
            for (int i = 0; i < 52; i++) {
                gameCards[i] = deck.takeCard();
                for (int j = 0; j < i; j++) {
                    assert gameCards[i] != gameCards[j];
                }
            }
            assertThrows(IndexOutOfBoundsException.class, deck::takeCard);
            deck.restore();
        }
    }
}