package game.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.model.GameWorld;
import game.view.GameView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {
    private GameWorld world;
    private GameView view;
    private SceneController sceneController;
    private GameController gameController;
    private MockController mockController;

    @BeforeAll
    static void initJavafx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            System.err.println("Ignoring illegal javafx startup");
        }
    }

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 10);
        view = new GameView(world, new Canvas());
        sceneController = new SceneController();
        gameController = new GameController(world, view, sceneController);
        mockController = new MockController();
        gameController.addController(mockController);
    }

    @Test
    void testAddController() {
        gameController.restart();
        assertTrue(mockController.restarted);
    }

    @Test
    void testRestart() {
        world.increaseScore(5);
        world.setGameOver(true);
        gameController.restart();

        assertEquals(0, world.getScore());
        assertFalse(world.isGameOver());
        assertTrue(mockController.restarted);
    }

    @Test
    void testStop() {
        gameController.stop();
    }

    private static class MockController implements Controller {
        boolean restarted = false;
        boolean ticked = false;
        boolean setup = false;

        @Override
        public void tick() {
            ticked = true;
        }

        @Override
        public void restart() {
            restarted = true;
        }

        @Override
        public void setupEvents(Scene scene) {
            setup = true;
        }
    }
}