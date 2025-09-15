package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void nextRound() {
        GameState gameState = new GameState();
        assert gameState.getRoundCount() == 1;
        gameState.nextRound();
        assert gameState.getRoundCount() == 2;
    }
}