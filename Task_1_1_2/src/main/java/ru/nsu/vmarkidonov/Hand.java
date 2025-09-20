package ru.nsu.vmarkidonov;

import java.util.ArrayList;

/**
 * List of the player or dealer cards.
 */
public class Hand extends ArrayList<GameCard> {
    private Deck deck;

    Hand(Deck deck) {
        this.deck = deck;
        takeTwoCards();
    }

    /**
     * Clear hand and take 2 cards.
     */
    public void reinit() {
        this.clear();
        takeTwoCards();
    }

    /**
     * Take two cards from deck.
     */
    private void takeTwoCards() {
        this.add(deck.takeCard());
        this.add(deck.takeCard());
        reinitCardValues();
    }

    /**
     * Take one card from deck and add it to hand.
     *
     * @return Card taken from deck
     */
    public GameCard takeCard() {
        GameCard takenCard = deck.takeCard();
        this.add(takenCard);
        reinitCardValues();
        return takenCard;
    }

    /**
     * Reinit hand's cards values.
     */
    public void reinitCardValues() {
        ArrayList<GameCard> softCards = new ArrayList<>();
        int score = 0;
        for (GameCard gameCard : this) {
            if (gameCard.cardValue.primaryValue != gameCard.cardValue.alternativeValue) {
                softCards.add(gameCard);
                continue;
            }

            gameCard.initValue(score);
            score += gameCard.getValue();
        }

        for (GameCard softCard : softCards) {
            softCard.initValue(score);
            score += softCard.getValue();
        }
    }

    /**
     * Get sum of all cards values.
     *
     * @return Sum of all cards values or 0 if it has hidden cards
     */
    public int getScore() {
        int score = 0;
        for (GameCard gameCard : this) {
            if (gameCard.hidden) {
                return 0;
            }
            score += gameCard.getValue();
        }
        return score;
    }

    @Override
    public String toString() {
        return String.format("%s => %d", super.toString(), getScore());
    }
}
