package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameWorldTest {
    private GameWorld model;
    private final int width = 30;
    private final int height = 20;
    private final int winScore = 10;

    @BeforeEach
    void setUp() {
        model = new GameWorld(width, height, winScore);
    }

    @Test
    void getWidth() {
        assertEquals(width, model.getWidth());
    }

    @Test
    void getHeight() {
        assertEquals(height, model.getHeight());
    }

    @Test
    void getScore() {
        assertEquals(0, model.getScore());
    }

    @Test
    void increaseScore() {
        model.increaseScore(5);
        assertEquals(5, model.getScore());
        model.increaseScore(2);
        assertEquals(7, model.getScore());
    }

    @Test
    void setScore() {
        model.setScore(100);
        assertEquals(100, model.getScore());
    }

    @Test
    void getScoreToWin() {
        assertEquals(winScore, model.getScoreToWin());
    }

    @Test
    void getGameOver() {
        assertFalse(model.getGameOver());
        model.setGameOver(true);
        assertTrue(model.getGameOver());
    }

    @Test
    void getGameWin() {
        assertFalse(model.getGameWin());
        model.setGameWin(true);
        assertTrue(model.getGameWin());
    }

    @Test
    void setGameOver() {
        model.setGameOver(true);
        assertTrue(model.getGameOver());
        model.setGameOver(false);
        assertFalse(model.getGameOver());
    }

    @Test
    void setGameWin() {
        model.setGameWin(true);
        assertTrue(model.getGameWin());
    }
}