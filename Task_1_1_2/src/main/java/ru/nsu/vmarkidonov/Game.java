package ru.nsu.vmarkidonov;

public class Game {
    private final Deck deck = new Deck();
    public final Hand playerHand = new Hand(deck);
    public final Hand dealerHand = new Hand(deck);
    private int roundCount = 0;
    public int playerWins = 0;
    public int dealerWins = 0;

    public void printIntro() {
        System.out.println("Welcome to Blackjack!");
    }

    public void printRoundStart() {
        System.out.printf("\nRound %d\n", roundCount);
        System.out.printf("Score: player: %d | dealer %d\n", playerWins, dealerWins);
        System.out.println("The dealer has dealt the cards");
    }

    public void printState() {
        System.out.printf("\tYour cards: %s\n", playerHand);
        System.out.printf("\tDealer cards: %s\n", dealerHand);
    }

    public void nextRound() {
        roundCount++;
        deck.restore();
        playerHand.remake();
        dealerHand.remake();
    }
}
