package game.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import game.model.GameWorld;
import game.model.Point;

import java.util.Collections;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameViewTest {
    private GameView gameView;

    @BeforeEach
    void setUp() {
        GameWorld world = new GameWorld(10, 10, 100);
        Canvas canvas = new Canvas(400, 400);
        gameView = new GameView(world, canvas);
    }

    @Test
    void testRenderDoesNotThrow() {
        assertDoesNotThrow(() -> gameView.render());
    }

    @Test
    void testAddViewAndRender() {
        View mockView = () -> Collections.singletonList(
                new Pair<>(new Point(0, 0), Color.RED)
        );
        gameView.addView(mockView);
        assertDoesNotThrow(() -> gameView.render());
    }

    @Test
    void testRenderWithInvalidPoints() {
        View mockView = () -> List.of(
                new Pair<>(new Point(-1, -1), Color.BLUE),
                new Pair<>(new Point(11, 11), Color.YELLOW)
        );
        gameView.addView(mockView);
        assertDoesNotThrow(() -> gameView.render());
    }
}
