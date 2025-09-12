package ru.nsu.vmarkidonov;

public enum CardSuits {
    SPADES("Spades"),
    DIAMONDS("Diamonds"),
    HEARTS("Hearts"),
    CLUBS("Clubs");

    private final String name;

    CardSuits(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
