package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HandTest {

    @Test
    void initHandWithTwoCards() {
        // Init deck and hand
        Deck deck = new Deck();
        // Check hand take 2 cards
        assert (new Hand(deck)).size() == 2;
        // Check 2 cards was taken from deck
        assert deck.getRemainingCardsCount() == 50;
    }

    @Test
    void initTwoHandsWithTwoCards() {
        // Init deck and hand
        Deck deck = new Deck();
        // Check hand take 2 cards
        assert (new Hand(deck)).size() == 2;
        // Check another hand take 2 cards
        assert (new Hand(deck)).size() == 2;
        // Check 4 cards was taken from deck
        assert deck.getRemainingCardsCount() == 48;
    }

    @Test
    void cardsLimit() {
        // Init deck and hand
        Deck deck = new Deck();
        Hand hand = new Hand(deck); // Hand take 2 cards

        // Take card 52-2=50 times
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(hand::takeCard);
        }

        // Check deck, that hand using, is empty
        assertThrows(IndexOutOfBoundsException.class, hand::takeCard);
        // Check deck is empty
        assert deck.getRemainingCardsCount() == 0;
        assertThrows(IndexOutOfBoundsException.class, deck::takeCard);
    }

    @Test
    void takeCard() {
        // Init deck and hand
        Deck deck = new Deck();
        Hand hand = new Hand(deck); // Hand take 2 cards

        // Take all remaining cards
        while (deck.getRemainingCardsCount() > 0) {
            hand.takeCard();
            // Check that count of cards increases
            assert hand.size() == 52 - deck.getRemainingCardsCount();
        }
    }

    @Test
    void uniqueCards() {
        // Init deck and hand
        Deck deck = new Deck();
        Hand hand = new Hand(deck); // Hand take 2 cards

        // Take all remaining cards
        while (deck.getRemainingCardsCount() > 0) {
            hand.takeCard();

            // Check that cards are unique
            for (int j = 0; j < hand.size()-1; j++) {
                assert hand.get(j) != hand.getLast();
            }
        }
    }

    @Test
    void handCycle() {
        // Init deck and hand
        Deck deck = new Deck();
        Hand hand = new Hand(deck); // Hand take 2 cards

        // Repeat test 2 times
        for (int k = 0; k < 2; k++) {
            // Check hand have 2 cards
            assert hand.size() == 2;

            // Take card 52-2=50 times
            for (int i = 0; i < 50; i++) {
                hand.takeCard();
            }

            // Check deck is empty
            assert deck.getRemainingCardsCount() == 0;

            // Restore deck and hand
            deck.restore();
            hand.reinit();
        }
    }

    @Test
    void twoHandsCycle() {
        // Init deck and hands
        Deck deck = new Deck();
        // Hands take 4 cards
        Hand hand1 = new Hand(deck);
        Hand hand2 = new Hand(deck);

        // Repeat test 2 times
        for (int k = 0; k < 2; k++) {
            // Check each hand have 2 cards
            assert hand1.size() == 2;
            assert hand2.size() == 2;

            // Take card from deck ((52-4)/2)=24 times with two hands
            for (int i = 0; i < 24; i++) {
                hand1.takeCard();
                hand2.takeCard();
            }
            // Check deck is empty
            assert deck.getRemainingCardsCount() == 0;

            // Restore deck and hands
            deck.restore();
            hand1.reinit();
            hand2.reinit();
        }
    }

    @Test
    void getScore() {
        // Init deck and hand
        Deck deck = new Deck();
        Hand hand = new Hand(deck); // Hand take 2 cards

        for (GameCard gameCard : hand) {
            gameCard.hidden = false;
        }

        int handScore = hand.getScore();

        // Count with myself
        int myScore = 0;
        for (GameCard gameCard : hand) {
            gameCard.restore();
            gameCard.hidden = false;
            gameCard.initValue(myScore);
            myScore += gameCard.getValue();
        }

        // Test method
        assert handScore == myScore;
    }
}