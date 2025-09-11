package ru.nsu.vmarkidonov;

public class GameCard {
    private final CardValues cardValue;
    private final CardSuits cardSuit;
    private int value = 0;
    public boolean hidden;

    GameCard(CardValues cardValue, CardSuits cardSuit) {
        this.cardValue = cardValue;
        this.cardSuit = cardSuit;
        hidden = true;
    }

    public void initValue(int currentScore) {
        if (value > 0) {
            throw new IllegalStateException("Card value already initialized");
        }

        if (currentScore + cardValue.primaryValue > 21) {
            value = cardValue.primaryValue;
        } else {
            value = cardValue.alternativeValue;
        }
    }

    public int getValue() {
        return hidden ? 0 : this.value;
    }

    public void restore() {
        value = 0;
        hidden = true;
    }

    @Override
    public String toString() {
        if (hidden) {
            return "<hidden card>";
        }

        return String.format("%s %s (%d)", cardValue, cardSuit, value);
    }
}
