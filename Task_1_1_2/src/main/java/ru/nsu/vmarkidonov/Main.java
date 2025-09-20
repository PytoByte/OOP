package ru.nsu.vmarkidonov;

import java.util.Scanner;

/**
 * Class that runs the game.
 */
public class Main {
    public static final int scoreLimit = 21;
    public static final int dealerScoreStrategy = 21;

    /**
     * Run the game.
     *
     * @param args args from terminal
     */
    public static void main(String[] args) {
        GameState gameState = new GameState();
        Scanner in = new Scanner(System.in);

        printIntro();

        while (true) {
            printRoundStart(gameState);

            playerTurn(gameState, in);

            if (gameState.playerHand.getScore() > scoreLimit) {
                System.out.println("Bust, Dealer won!");
                gameState.dealerWins++;
                gameState.nextRound();
                continue;
            } else if (gameState.playerHand.getScore() == scoreLimit) {
                System.out.println("Blackjack, You won!");
                gameState.playerWins++;
                gameState.nextRound();
                continue;
            }

            dealerTurn(gameState);

            if (gameState.dealerHand.getScore() > scoreLimit) {
                System.out.println("Bust, You won!");
                gameState.playerWins++;
            } else if (gameState.playerHand.getScore() > gameState.dealerHand.getScore()) {
                System.out.println("You won!");
                gameState.playerWins++;
            } else {
                System.out.println("Dealer won!");
                gameState.dealerWins++;
            }

            gameState.nextRound();
        }
    }

    /**
     * Player turn logic.
     *
     * @param gameState State of the game
     * @param in        Scanner, that allowing player make choices
     */
    public static void playerTurn(GameState gameState, Scanner in) {
        System.out.println("\nPlayer turn\n---");
        while (true) {
            printState(gameState);

            if (gameState.playerHand.getScore() == scoreLimit) {
                break;
            }

            if (gameState.playerHand.getScore() > scoreLimit) {
                break;
            }

            System.out.println("Enter \"1\" to take a card, and \"0\" to stop...");
            int action = in.nextInt();

            if (action == 0) {
                break;
            }

            GameCard newCard = gameState.playerHand.takeCard();
            newCard.hidden = false;
            System.out.printf("New card is: %s\n\n", newCard);
        }
    }

    /**
     * Dealer turn logic.
     *
     * @param gameState State of the game
     */
    public static void dealerTurn(GameState gameState) {
        System.out.println("\nDealer turn\n---");
        for (GameCard gameCard : gameState.dealerHand) {
            if (gameCard.hidden) {
                gameCard.hidden = false;
                System.out.printf("Dealer show the card: %s\n", gameCard);
                printState(gameState);
                System.out.print('\n');
            }
        }

        while (gameState.dealerHand.getScore() < dealerScoreStrategy) {
            GameCard newCard = gameState.dealerHand.takeCard();
            newCard.hidden = false;
            System.out.printf("Dealer took new card: %s\n", newCard);
            printState(gameState);
            System.out.print('\n');
        }
    }

    /**
     * Prints welcome message.
     */
    public static void printIntro() {
        System.out.println("Welcome to Blackjack!");
    }

    /**
     * Prints start round message.
     */
    public static void printRoundStart(GameState gameState) {
        System.out.printf("\nRound %d\n", gameState.getRoundCount());
        System.out.printf("Score: player: %d | dealer %d\n",
                gameState.playerWins, gameState.dealerWins);
        System.out.println("The dealer has dealt the cards");
    }

    /**
     * Prints game state.
     */
    public static void printState(GameState gameState) {
        System.out.printf("\tYour cards: %s\n", gameState.playerHand);
        System.out.printf("\tDealer cards: %s\n", gameState.dealerHand);
    }
}