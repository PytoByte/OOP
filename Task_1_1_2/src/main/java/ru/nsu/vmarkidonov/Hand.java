package ru.nsu.vmarkidonov;

import java.util.ArrayList;

public class Hand extends ArrayList<GameCard> {
    private Deck deck;
    private int score = 0;

    Hand(Deck deck) {
        this.deck = deck;
        takeCards();
    }

    public void remake() {
        this.clear();
        takeCards();
    }

    private void takeCards() {
        for (int i = 0; i < 2; i++) {
            takeCard();
        }
    }

    public GameCard takeCard() {
        GameCard takenCard = deck.takeCard();
        takenCard.initValue(score);
        score += takenCard.getValue();
        this.add(takenCard);

        return takenCard;
    }

    public int getScore() {
        int score = 0;
        for (GameCard gameCard : this) {
            score += gameCard.getValue();
        }
        return score;
    }

    @Override
    public String toString() {
        return String.format("%s => %d", super.toString(), getScore());
    }
}
