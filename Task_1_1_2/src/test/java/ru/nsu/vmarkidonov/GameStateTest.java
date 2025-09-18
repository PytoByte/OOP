package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTest {

    @Test
    void nextRound() {
        GameState gameState = new GameState();
        assert gameState.getRoundCount() == 1;
        gameState.nextRound();
        assert gameState.getRoundCount() == 2;
    }

    @Test
    void correctInitialState() {
        GameState gameState = new GameState();
        for (GameCard gameCard : gameState.playerHand) {
            assertFalse(gameCard.hidden);
        }

        for (int i = 0; i < gameState.dealerHand.size(); i++) {
            if (i == 0) {
                assertFalse(gameState.dealerHand.get(i).hidden);
            } else {
                assertTrue(gameState.dealerHand.get(i).hidden);
            }
        }
    }
}