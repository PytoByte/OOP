package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void dealerTurn() {
        GameState gameState = new GameState();
        Main.dealerTurn(gameState);
        assertTrue(gameState.dealerHand.getScore() > 17);
    }
}