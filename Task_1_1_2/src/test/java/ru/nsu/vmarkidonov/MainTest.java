package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void dealerTurn() {
        GameState gameState = new GameState();
        Main.dealerTurn(gameState);
        assertTrue(gameState.dealerHand.getScore() > Main.dealerScoreStrategy);
    }
}