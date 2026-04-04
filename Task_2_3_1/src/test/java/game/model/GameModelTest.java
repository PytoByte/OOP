package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {
    private GameModel model;
    private final int WIDTH = 30;
    private final int HEIGHT = 20;
    private final int WIN_SCORE = 10;

    @BeforeEach
    void setUp() {
        model = new GameModel(WIDTH, HEIGHT, WIN_SCORE);
    }

    @Test
    void getWidth() {
        assertEquals(WIDTH, model.getWidth());
    }

    @Test
    void getHeight() {
        assertEquals(HEIGHT, model.getHeight());
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
        assertEquals(WIN_SCORE, model.getScoreToWin());
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