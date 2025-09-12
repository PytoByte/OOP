package ru.nsu.vmarkidonov;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Scanner in = new Scanner(System.in);

        game.printIntro();
        while (true) {
            for (GameCard gameCard : game.playerHand) {
                gameCard.hidden = false;
            }
            game.dealerHand.getFirst().hidden = false;

            game.printRoundStart();

            System.out.println("\nPlayer turn\n---");
            while (true) {
                game.printState();

                if (game.playerHand.getScore() == 21) {
                    break;
                }

                if (game.playerHand.getScore() > 21) {
                    break;
                }

                System.out.println("Enter \"1\" to take a card, and \"0\" to stop...");
                int action = in.nextInt();

                if (action == 0) {
                    break;
                }

                GameCard newCard = game.playerHand.takeCard();
                newCard.hidden = false;
                System.out.printf("New card is: %s\n\n", newCard);
            }

            if (game.playerHand.getScore() > 21) {
                System.out.println("Bust, Dealer won!");
                game.dealerWins++;
                game.nextRound();
                continue;
            } else if (game.playerHand.getScore() == 21) {
                System.out.println("Blackjack, You won!");
                game.playerWins++;
                game.nextRound();
                continue;
            }

            System.out.println("\nDealer turn\n---");
            for (GameCard gameCard : game.dealerHand) {
                if (gameCard.hidden) {
                    gameCard.hidden = false;
                    System.out.printf("Dealer show the card: %s\n", gameCard);
                    game.printState();
                    System.out.print('\n');
                }
            }

            while (game.dealerHand.getScore() < 17) {
                GameCard newCard = game.dealerHand.takeCard();
                newCard.hidden = false;
                System.out.printf("Dealer took new card: %s\n", newCard);
                game.printState();
                System.out.print('\n');
            }

            if (game.dealerHand.getScore() > 21) {
                System.out.println("Bust, You won!");
                game.playerWins++;
            } else if (game.playerHand.getScore() > game.dealerHand.getScore()) {
                System.out.println("You won!");
                game.playerWins++;
            } else {
                System.out.println("Dealer won!");
                game.dealerWins++;
            }
            game.nextRound();
        }
    }

}