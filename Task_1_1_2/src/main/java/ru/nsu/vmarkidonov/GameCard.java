package ru.nsu.vmarkidonov;

import java.util.Random;

public class GameCard {
    private final int nameIndex;
    private final int suitIndex;
    boolean hidden = false;

    public static GameCard GameCardRandom() {
        Random random = new Random();

        return new GameCard(
                random.nextInt(CardNames.getNameCount()),
                random.nextInt(CardNames.getSuitCount())
        );
    }

    public static GameCard GameCardRandom(boolean hide) {
        Random random = new Random();

        GameCard newCard = new GameCard(
                random.nextInt(CardNames.getNameCount()),
                random.nextInt(CardNames.getSuitCount())
        );

        newCard.hidden = true;

        return newCard;
    }

    GameCard(int nameIndex, int suitIndex) {
        this.nameIndex = nameIndex;
        this.suitIndex = suitIndex;
    }

    public int getScore() {
        return Math.min(nameIndex + 2, 10);
    }

    @Override
    public String toString() {
        if (hidden) {
            return "<hidden card>";
        }

        return String.format("%s %s (%d)", CardNames.getName(nameIndex), CardNames.getSuit(suitIndex), getScore());
    }
}
