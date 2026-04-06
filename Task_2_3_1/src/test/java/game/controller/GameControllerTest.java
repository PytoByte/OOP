package game.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.model.GameWorld;
import game.view.GameView;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {
    private GameWorld world;
    private GameController gameController;
    private MockTimer mockTimer;
    private MockController mockSubController;

    private static class MockTimer implements GameTimer {
        boolean running = false;
        Runnable tickAction;

        @Override
        public void play() {
            running = true;
        }

        @Override
        public void stop() {
            running = false;
        }

        @Override
        public void setOnTick(Runnable action) {
            this.tickAction = action;
        }
    }

    private static class MockSceneController extends SceneController {
        boolean showGameOverCalled = false;
        boolean winParam = false;

        @Override
        public void updateScore(int score) {
        }

        @Override
        public void showGameOver(boolean win) {
            showGameOverCalled = true;
            winParam = win;
        }
    }

    private static class MockGameView extends GameView {
        public MockGameView(GameWorld world) {
            super(world, new Canvas());
        }

        @Override
        public void render() {
        }
    }

    private static class MockController implements Controller {
        boolean ticked = false;
        boolean restarted = false;
        boolean setupEventsCalled = false;

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
            setupEventsCalled = true;
        }
    }

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 10);
        mockTimer = new MockTimer();
        mockSubController = new MockController();

        gameController = new GameController(
                world,
                new MockGameView(world),
                new MockSceneController(), mockTimer
        );
        gameController.addController(mockSubController);
    }

    @Test
    void testStartSetupAndPlay() {
        gameController.start(null);
        assertTrue(mockSubController.setupEventsCalled);
        assertTrue(mockTimer.running);
    }

    @Test
    void testTickProcessesGameOver() {
        world.setGameOver(true);

        if (mockTimer.tickAction != null) {
            mockTimer.tickAction.run();
        }

        assertFalse(mockTimer.running);
        assertTrue(mockSubController.ticked);
    }

    @Test
    void testRestartCallsSubControllers() {
        gameController.restart();
        assertTrue(mockSubController.restarted);
        assertTrue(mockTimer.running);
    }

    @Test
    void testTickProcessesWin() {
        world.increaseScore(10);

        if (mockTimer.tickAction != null) {
            mockTimer.tickAction.run();
        }

        assertTrue(world.isGameWin());
        assertFalse(mockTimer.running);
    }
}