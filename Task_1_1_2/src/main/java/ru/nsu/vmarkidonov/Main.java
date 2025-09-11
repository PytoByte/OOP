package ru.nsu.vmarkidonov;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();

        Hand playerHand = new Hand(deck);
        Hand dealerHand = new Hand(deck);

        int roundCount = 1;
        int playerWins = 0, dealerWins = 0;

        System.out.println("Welcome to Blackjack!");

        while (true) {
            System.out.printf("\nRound %d\n", roundCount);
            System.out.printf("player: %d | dealer %d\n", playerWins, dealerWins);

            System.out.println("The dealer has dealt the cards");

            printGameState(playerHand, dealerHand);

            System.out.println("\nYour turn\n---");
            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.println("Enter \"1\" to take a card, and \"0\" to stop...");
                int action = in.nextInt();

                if (action == 0) {
                    break;
                }

                GameCard newCard = playerHand.takeCard();

                System.out.printf("New card is: %s\n", newCard);

                printGameState(playerHand, dealerHand);

                if (playerHand.getScore() > 21) {
                    System.out.println("Dealer won!");
                    dealerWins++;
                    break;
                }
            }

            if (playerHand.getScore() > 21) {
                roundCount++;
                remakeGame(deck, playerHand, dealerHand);
                continue;
            }

            System.out.println("\nDealer turn\n---");
            for (GameCard gameCard : dealerHand) {
                if (gameCard.hidden) {
                    gameCard.hidden = false;
                    System.out.printf("Dealer show the card: %s\n", gameCard);
                    printGameState(playerHand, dealerHand);
                    in.next();
                }
            }

            while (dealerHand.getScore() < 17) {
                GameCard newCard = dealerHand.takeCard();
                dealerHand.add(newCard);
                System.out.printf("Dealer took new card: %s\n", newCard);
                printGameState(playerHand, dealerHand);
                in.next();
            }

            if (playerHand.getScore() > dealerHand.getScore() || dealerHand.getScore() > 21) {
                System.out.println("You won!");
                playerWins++;
            } else {
                System.out.println("Dealer won!");
                dealerWins++;
            }

            remakeGame(deck, playerHand, dealerHand);
            roundCount++;
        }
    }

    private static void printGameState(Hand playerHand, Hand dealerHand) {
        System.out.printf("\tYour cards: %s\n", playerHand);
        System.out.printf("\tDealer cards: %s\n", dealerHand);
    }

    private static void remakeGame(Deck deck, Hand playerHand, Hand dealerHand) {
        deck.restore();
        playerHand.remake();
        dealerHand.remake();
    }
}