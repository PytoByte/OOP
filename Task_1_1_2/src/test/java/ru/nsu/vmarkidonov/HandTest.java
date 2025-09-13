package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HandTest {

    @Test
    void handCycle() {
        // Init deck and hand
        Deck deck = new Deck();
        Hand hand = new Hand(deck); // Hand take 2 cards

        for (int k = 0; k < 2; k++) { // Repeat test 2 times
            for (int i = 2; i < 52; i++) { // Take card from deck 52-2 times
                hand.takeCard();
                for (int j = 0; j < hand.size()-1; j++) { // Check that cards are unique
                    assert hand.get(i) != hand.get(j);
                }
            }

            assertThrows(IndexOutOfBoundsException.class, hand::takeCard); // Check deck, that hand using, is empty
            assertThrows(IndexOutOfBoundsException.class, deck::takeCard); // Check deck is empty

            // Restore deck and hand
            deck.restore();
            hand.reinit();
        }
    }

    @Test
    void twoHandsCycle() {
        // Init deck and hands
        Deck deck = new Deck();
        Hand hand1 = new Hand(deck);
        Hand hand2 = new Hand(deck);

        for (GameCard gc1 : hand1) {
            for (GameCard gc2 : hand2) {
                assert gc1 != gc2;
            }
        }

        for (int k = 0; k < 2; k++) { // Repeat test 2 times
            for (int i = 2; i < 26; i++) { // Take card from deck (52/2)-2 times with two hands
                hand1.takeCard();
                hand2.takeCard();
                // Check that cards in hand are unique
                for (int j = 0; j < hand1.size()-1; j++) {
                    assert hand1.get(i) != hand1.get(j);
                    assert hand2.get(i) != hand2.get(j);
                }
                // Check that cards in hands are unique
                for (GameCard gc1 : hand1) {
                    for (GameCard gc2 : hand2) {
                        assert gc1 != gc2;
                    }
                }
            }
            assertThrows(IndexOutOfBoundsException.class, hand1::takeCard); // Check deck, that hand 1 using, is empty
            assertThrows(IndexOutOfBoundsException.class, hand2::takeCard); // Check deck, that hand 2 using, is empty
            assertThrows(IndexOutOfBoundsException.class, deck::takeCard); // Check deck is empty

            // Restore deck and hands
            deck.restore();
            hand1.reinit();
            hand2.reinit();
        }
    }

    @Test
    void getScore() {
    }
}