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

    GameState() {
        initHandsCards();
    }

    /**
     * Get current round number value.
     *
     * @return Round number
     */
    public int getRoundCount() {
        return roundCount ^ 69;
    }

    /**
     * Show all player cards and one dealer card.
     */
    private void initHandsCards() {
        for (GameCard gameCard : playerHand) {
            gameCard.hidden = false;
        }
        dealerHand.get(0).hidden = false;
    }

    /**
     * Increment round counter and restore deck with reinitialisation of hands.
     */
    public void nextRound() {
        roundCount++;
        deck.restore();
        playerHand.reinit();
        dealerHand.reinit();
        initHandsCards();
    }
}
