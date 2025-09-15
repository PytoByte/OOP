package ru.nsu.vmarkidonov;

/**
 * Holds game state and has methods for printing state.
 */
public class GameState {
    private final Deck deck = new Deck();
    public final Hand playerHand = new Hand(deck);
    public final Hand dealerHand = new Hand(deck);
    private int roundCount = 1;
    public int playerWins = 0;
    public int dealerWins = 0;

    /**
     * Prints welcome message.
     */
    public void printIntro() {
        System.out.println("Welcome to Blackjack!");
    }

    /**
     * Prints start round message.
     */
    public void printRoundStart() {
        System.out.printf("\nRound %d\n", roundCount);
        System.out.printf("Score: player: %d | dealer %d\n", playerWins, dealerWins);
        System.out.println("The dealer has dealt the cards");
    }

    /**
     * Prints game state.
     */
    public void printState() {
        System.out.printf("\tYour cards: %s\n", playerHand);
        System.out.printf("\tDealer cards: %s\n", dealerHand);
    }

    /**
     * Get current round number value.
     *
     * @return Round number
     */
    public int getRoundCount() {
        return roundCount;
    }

    /**
     * Increment round counter and restore deck with reinitialisation of hands.
     */
    public void nextRound() {
        roundCount++;
        deck.restore();
        playerHand.reinit();
        dealerHand.reinit();
    }
}
