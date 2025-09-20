package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void nextRound() {
        GameState gameState = new GameState();
        assertEquals(1, gameState.getRoundCount());
        gameState.nextRound();
        assertEquals(2, gameState.getRoundCount());
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