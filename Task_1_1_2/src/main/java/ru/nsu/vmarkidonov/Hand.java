package ru.nsu.vmarkidonov;

import java.util.ArrayList;

public class Hand extends ArrayList<GameCard> {

    Hand() {
        this.add(GameCard.GameCardRandom());
        this.add(GameCard.GameCardRandom());
    }

    Hand(boolean hide) {
        this.add(GameCard.GameCardRandom());
        this.add(GameCard.GameCardRandom(hide));
    }

    public int getScore() {
        int sum = 0;
        for (GameCard gameCard : this) {
            if (!gameCard.hidden) {
                sum += gameCard.getScore();
            }
        }
        return sum;
    }

    public void remake() {
        this.clear();
        this.add(GameCard.GameCardRandom());
        this.add(GameCard.GameCardRandom());
    }

    public void remake(boolean hide) {
        this.clear();
        this.add(GameCard.GameCardRandom());
        this.add(GameCard.GameCardRandom(hide));
    }

    @Override
    public String toString() {
        return String.format("%s => %d", super.toString(), getScore());
    }
}
