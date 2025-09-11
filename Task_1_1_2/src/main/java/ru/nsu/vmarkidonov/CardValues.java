package ru.nsu.vmarkidonov;

public enum CardValues {
    TWO("two", 2),
    THREE("three", 3),
    FOUR("four", 4),
    FIVE("five", 5),
    SIX("six", 6),
    SEVEN("seven", 7),
    EIGHT("eight", 8),
    NINE("nine", 9),
    TEN("ten", 10),
    JACK("jack", 10),
    QUEEN("queen", 10),
    KING("king", 10),
    ACE("ace", 11, 1);

    private final String name;
    public final int primaryValue;
    public final int alternativeValue;

    CardValues(String name, int primaryValue, int alternativeValue) {
        this.name = name;
        this.primaryValue = primaryValue;
        this.alternativeValue = alternativeValue;
    }

    CardValues(String name, int primaryValue) {
        this.name = name;
        this.primaryValue = primaryValue;
        this.alternativeValue = primaryValue;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
