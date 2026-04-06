package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameWorldTest {
    private GameWorld world;

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 5);
    }

    @Test
    void testConstructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GameWorld(2, 2, 5));
    }

    @Test
    void testIncreaseScore() {
        world.increaseScore(3);
        assertEquals(3, world.getScore());
    }

    @Test
    void testGameWin() {
        world.increaseScore(5);
        world.tick();
        assertTrue(world.isGameWin());
    }

    @Test
    void testSetGameOver() {
        world.setGameOver(true);
        assertTrue(world.isGameOver());
    }

    @Test
    void testRestart() {
        world.increaseScore(3);
        world.setGameOver(true);
        world.restart();
        assertEquals(0, world.getScore());
        assertFalse(world.isGameOver());
        assertFalse(world.isGameWin());
    }

    @Test
    void testTickStopOnGameOver() {
        world.setGameOver(true);
        world.increaseScore(10);
        world.tick();
        assertFalse(world.isGameWin());
    }

    @Test
    void testGetDimensions() {
        assertEquals(10, world.getWidth());
        assertEquals(10, world.getHeight());
    }
}
