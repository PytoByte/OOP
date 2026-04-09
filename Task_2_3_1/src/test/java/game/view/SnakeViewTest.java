package game.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.model.ConstPoint;
import game.model.Direction;
import game.model.GameWorld;
import game.model.Pair;
import game.model.Point;
import game.model.Snake;
import java.util.Iterator;
import javafx.scene.paint.Color;
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
        Iterable<Pair<ConstPoint, Color>> viewData = snakeView.getView();
        Iterator<Pair<ConstPoint, Color>> iterator = viewData.iterator();

        assertTrue(iterator.hasNext());
        Pair<ConstPoint, Color> head = iterator.next();
        assertEquals(Color.AQUAMARINE, head.value());

        assertTrue(iterator.hasNext());
        Pair<ConstPoint, Color> body = iterator.next();
        assertEquals(Color.MEDIUMAQUAMARINE, body.value());
    }

    @Test
    void testGetViewPoints() {
        Iterable<Pair<ConstPoint, Color>> viewData = snakeView.getView();
        Iterator<Pair<ConstPoint, Color>> iterator = viewData.iterator();

        ConstPoint head = iterator.next().key();
        assertEquals(new Point(6, 5), head);
        ConstPoint body = iterator.next().key();
        assertEquals(new Point(5, 5), body);
    }

    @Test
    void testGetViewSize() {
        int count = 0;
        for (Pair<ConstPoint, Color> pair : snakeView.getView()) {
            count++;
        }
        assertEquals(2, count);
    }
}