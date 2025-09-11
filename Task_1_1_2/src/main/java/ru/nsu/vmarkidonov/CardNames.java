package ru.nsu.vmarkidonov;

import java.util.Random;

public class CardNames {
    private static String[] names = {
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten",
            "Jack",
            "Queen",
            "King",
            "Ace"
    };

    private static String[] suit = {
            "Spades",     // Пики
            "Diamonds",   // Бубны
            "Hearts",     // Черви
            "Clubs"       // Трефы
    };

    public static String getName(int value) {
        return names[value];
    }

    public static int getNameCount() {
        return names.length;
    }

    public static String getSuit(int value) {
        return suit[value];
    }

    public static int getSuitCount() {
        return suit.length;
    }
}
