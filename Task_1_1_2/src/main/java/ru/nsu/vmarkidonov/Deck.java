package ru.nsu.vmarkidonov;

import java.util.ArrayList;
import java.util.Random;

/**
 * Representation of the card deck
 */
public class Deck {
    private final ArrayList<GameCard> cards = new ArrayList<>();
    private final ArrayList<GameCard> takenCards = new ArrayList<>();

    Deck() {
        for (CardValues cardValue : CardValues.values()) {
            for (CardSuits cardSuit : CardSuits.values()) {
                cards.add(new GameCard(cardValue, cardSuit));
            }
        }
    }

    /**
     * Get count of remaining cards in deck
     * @return Count of remaining cards in deck
     */
    public int getRemainingCardsCount() {
        return cards.size();
    }

    /**
     * Take card from deck
     * @return Taken card
     */
    public GameCard takeCard() {
        if (cards.isEmpty()) {
            throw new IndexOutOfBoundsException("Deck is empty");
        }

        Random random = new Random();
        GameCard takenCard = cards.remove(random.nextInt(0, cards.size()));
        takenCards.add(takenCard);
        return takenCard;
    }

    /**
     * Return taken cards to deck
     */
    public void restore() {
        for (GameCard gameCard : takenCards) {
            gameCard.restore();
        }
        cards.addAll(takenCards);
        takenCards.clear();
    }
}
