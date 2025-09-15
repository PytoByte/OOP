package ru.nsu.vmarkidonov;

/**
 * Representation of card.
 */
public class GameCard {
    private final CardValues cardValue;
    private final CardSuits cardSuit;
    private int value = 0;
    public boolean hidden = true;

    GameCard(CardValues cardValue, CardSuits cardSuit) {
        this.cardValue = cardValue;
        this.cardSuit = cardSuit;
    }

    /**
     * Initialize card value depends on hand score.
     *
     * @param currentScore Current score of hand
     */
    public void initValue(int currentScore) {
        if (value > 0) {
            throw new IllegalStateException("Card value already initialized");
        }

        if (currentScore + cardValue.primaryValue < 21) {
            value = cardValue.primaryValue;
        } else {
            value = cardValue.alternativeValue;
        }
    }

    /**
     * Get card value.
     *
     * @return Card value or 0 if hidden
     */
    public int getValue() {
        return hidden ? 0 : this.value;
    }

    /**
     * Sets card value to uninitialized and hide the card.
     */
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
