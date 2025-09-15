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
        for (int i = 0; i < 2; i++) {
            takeCard();
        }
    }

    /**
     * Take one card from deck and add it to hand.
     *
     * @return Card taken from deck
     */
    public GameCard takeCard() {
        GameCard takenCard = deck.takeCard();
        takenCard.initValue(getScore());
        this.add(takenCard);

        return takenCard;
    }

    /**
     * Get sum of all cards values.
     *
     * @return Sum of all cards values or 0 if it has hidden cards
     */
    public int getScore() {
        int score = 0;
        for (GameCard gameCard : this) {
            if (gameCard.getValue() == 0) {
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
