package ru.nsu.vmarkidonov;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameState gameState = new GameState();
        Scanner in = new Scanner(System.in);

        gameState.printIntro();
        while (true) {
            for (GameCard gameCard : gameState.playerHand) {
                gameCard.hidden = false;
            }
            gameState.dealerHand.get(0).hidden = false;

            gameState.printRoundStart();

            System.out.println("\nPlayer turn\n---");
            while (true) {
                gameState.printState();

                if (gameState.playerHand.getScore() == 21) {
                    break;
                }

                if (gameState.playerHand.getScore() > 21) {
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

            if (gameState.playerHand.getScore() > 21) {
                System.out.println("Bust, Dealer won!");
                gameState.dealerWins++;
                gameState.nextRound();
                continue;
            } else if (gameState.playerHand.getScore() == 21) {
                System.out.println("Blackjack, You won!");
                gameState.playerWins++;
                gameState.nextRound();
                continue;
            }

            System.out.println("\nDealer turn\n---");
            for (GameCard gameCard : gameState.dealerHand) {
                if (gameCard.hidden) {
                    gameCard.hidden = false;
                    System.out.printf("Dealer show the card: %s\n", gameCard);
                    gameState.printState();
                    System.out.print('\n');
                }
            }

            while (gameState.dealerHand.getScore() < 17) {
                GameCard newCard = gameState.dealerHand.takeCard();
                newCard.hidden = false;
                System.out.printf("Dealer took new card: %s\n", newCard);
                gameState.printState();
                System.out.print('\n');
            }

            if (gameState.dealerHand.getScore() > 21) {
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
}