package game.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.model.Direction;
import game.model.GameWorld;
import game.model.Point;
import game.model.Snake;
import java.util.Iterator;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeViewTest {
    private View snakeView;

    @BeforeEach
    void setUp() {
        GameWorld world = new GameWorld(10, 10, 100);
        Snake snake = new Snake(5, 5, 2, Direction.RIGHT, world);
        snake.update();
        snakeView = new SnakeView(snake);
    }

    @Test
    void testGetViewColors() {
        Iterable<Pair<Point, Color>> viewData = snakeView.getView();
        Iterator<Pair<Point, Color>> iterator = viewData.iterator();

        assertTrue(iterator.hasNext());
        Pair<Point, Color> head = iterator.next();
        assertEquals(Color.AQUAMARINE, head.getValue());

        assertTrue(iterator.hasNext());
        Pair<Point, Color> body = iterator.next();
        assertEquals(Color.MEDIUMAQUAMARINE, body.getValue());
    }

    @Test
    void testGetViewPoints() {
        Iterable<Pair<Point, Color>> viewData = snakeView.getView();
        Iterator<Pair<Point, Color>> iterator = viewData.iterator();

        Point head = iterator.next().getKey();
        assertEquals(new Point(6, 5), head);
        Point body = iterator.next().getKey();
        assertEquals(new Point(5, 5), body);
    }

    @Test
    void testGetViewSize() {
        int count = 0;
        for (Pair<Point, Color> pair : snakeView.getView()) {
            count++;
        }
        assertEquals(2, count);
    }
}