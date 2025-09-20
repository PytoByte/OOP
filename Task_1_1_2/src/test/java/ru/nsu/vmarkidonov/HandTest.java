package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class HandTest {

    @Test
    void initHandWithTwoCards() {
        Deck deck = new Deck();
        assertEquals(2, (new Hand(deck)).size());
        assertEquals(50, deck.getRemainingCardsCount());
    }

    @Test
    void initTwoHandsWithTwoCards() {
        Deck deck = new Deck();
        assertEquals(2, (new Hand(deck)).size());
        assertEquals(2, (new Hand(deck)).size());
        assertEquals(48, deck.getRemainingCardsCount());
    }

    @Test
    void reinit() {
        Deck deck = new Deck();
        Hand hand = new Hand(deck);

        hand.takeCard();
        for (GameCard gameCard : hand) {
            gameCard.hidden = false;
        }
        assertEquals(3, hand.size());
        hand.reinit();
        assertEquals(2, hand.size());
    }

    @Test
    void cardsLimit() {
        Deck deck = new Deck();
        // Hand take 2 cards
        Hand hand = new Hand(deck);

        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(hand::takeCard);
        }
        assertEquals(0, deck.getRemainingCardsCount());
        assertThrows(IndexOutOfBoundsException.class, hand::takeCard);
        assertThrows(IndexOutOfBoundsException.class, deck::takeCard);
    }

    @Test
    void takeCard() {
        Deck deck = new Deck();
        Hand hand = new Hand(deck);

        while (deck.getRemainingCardsCount() > 0) {
            hand.takeCard();
            assertEquals(hand.size(), 52 - deck.getRemainingCardsCount());
        }
    }

    @Test
    void uniqueCards() {
        Deck deck = new Deck();
        Hand hand = new Hand(deck);

        while (deck.getRemainingCardsCount() > 0) {
            hand.takeCard();

            for (int j = 0; j < hand.size() - 1; j++) {
                assertNotEquals(hand.get(j), hand.get(hand.size() - 1));
            }
        }
    }

    @Test
    void handCycle() {
        Deck deck = new Deck();
        // Hand take 2 cards
        Hand hand = new Hand(deck);

        for (int k = 0; k < 2; k++) {
            assertEquals(2, hand.size());

            for (int i = 0; i < 50; i++) {
                hand.takeCard();
            }

            assertEquals(0, deck.getRemainingCardsCount());

            deck.restore();
            hand.reinit();
        }
    }

    @Test
    void twoHandsCycle() {
        Deck deck = new Deck();
        // Hands take 4 cards
        Hand hand1 = new Hand(deck);
        Hand hand2 = new Hand(deck);

        for (int k = 0; k < 2; k++) {
            assertEquals(2, hand1.size());
            assertEquals(2, hand2.size());

            // If two hands take cards, then each take ((52-4)/2)=24
            for (int i = 0; i < 24; i++) {
                hand1.takeCard();
                hand2.takeCard();
            }

            assertEquals(0, deck.getRemainingCardsCount());

            deck.restore();
            hand1.reinit();
            hand2.reinit();
        }
    }

    @Test
    void getScoreWithoutAlternativeValues() {
        ArrayList<GameCard> gameCards = new ArrayList<>();
        gameCards.add(new GameCard(CardValues.FOUR, CardSuits.CLUBS));
        gameCards.add(new GameCard(CardValues.FIVE, CardSuits.CLUBS));

        for (GameCard gameCard : gameCards) {
            gameCard.hidden = false;
        }

        Deck deck = new Deck();
        Hand hand = new Hand(deck);
        hand.clear();
        hand.addAll(gameCards);
        hand.reinitCardValues();

        int handScore = hand.getScore();

        // Count with myself
        int myScore = 0;
        for (GameCard gameCard : gameCards) {
            gameCard.restore();
            gameCard.hidden = false;
            gameCard.initValue(myScore);
            myScore += gameCard.getValue();
        }

        assertEquals(myScore, handScore);
    }

    @Test
    void getScoreWithAlternativeValuesWithoutBust() {
        ArrayList<GameCard> gameCards = new ArrayList<>();
        gameCards.add(new GameCard(CardValues.FOUR, CardSuits.CLUBS));
        gameCards.add(new GameCard(CardValues.ACE, CardSuits.CLUBS));

        for (GameCard gameCard : gameCards) {
            gameCard.hidden = false;
        }

        Deck deck = new Deck();
        Hand hand = new Hand(deck);
        hand.clear();
        hand.addAll(gameCards);
        hand.reinitCardValues();

        int handScore = hand.getScore();

        // Count with myself
        int myScore = 0;
        for (GameCard gameCard : gameCards) {
            gameCard.restore();
            gameCard.hidden = false;
            gameCard.initValue(myScore);
            myScore += gameCard.getValue();
        }

        assertEquals(myScore, handScore);
    }

    @Test
    void getScoreWithAlternativeValuesWithBust() {
        ArrayList<GameCard> gameCards = new ArrayList<>();
        gameCards.add(new GameCard(CardValues.JACK, CardSuits.CLUBS));
        gameCards.add(new GameCard(CardValues.KING, CardSuits.CLUBS));
        gameCards.add(new GameCard(CardValues.ACE, CardSuits.CLUBS));

        for (GameCard gameCard : gameCards) {
            gameCard.hidden = false;
        }

        Deck deck = new Deck();
        Hand hand = new Hand(deck);
        hand.clear();
        hand.addAll(gameCards);
        hand.reinitCardValues();

        int handScore = hand.getScore();

        ArrayList<GameCard> softCards = new ArrayList<>();

        // Count with myself
        int myScore = 0;
        for (GameCard gameCard : gameCards) {
            gameCard.restore();
            gameCard.hidden = false;

            if (gameCard.cardValue.primaryValue != gameCard.cardValue.alternativeValue) {
                softCards.add(gameCard);
                continue;
            }

            gameCard.initValue(myScore);
            myScore += gameCard.getValue();
        }

        for (GameCard softCard : softCards) {
            softCard.initValue(myScore);
            myScore += softCard.getValue();
        }

        assertEquals(myScore, handScore);
    }
}