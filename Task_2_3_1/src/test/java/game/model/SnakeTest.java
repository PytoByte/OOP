package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeTest {
    private Snake snake;
    private final int startX = 10;
    private final int startY = 10;
    private final int startSize = 3;

    @BeforeEach
    void setUp() {
        snake = new Snake(startX, startY, startSize, Direction.RIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(startX, snake.getStartX());
        assertEquals(startY, snake.getStartY());
        assertEquals(startSize, snake.getStartSize());
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void increaseBody() {
        Point p = new Point(11, 10);
        snake.increaseBody(p);

        assertEquals(1, snake.getPoints().size());
        assertEquals(p, snake.getHead());
    }

    @Test
    void setDirection_ValidTurn() {
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());

        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.LEFT, snake.getDirection());
    }

    @Test
    void setDirection_InvalidTurn() {
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getDirection());

        snake.setDirection(Direction.DOWN);
        snake.setDirection(Direction.UP);
        assertEquals(Direction.DOWN, snake.getDirection());
    }

    @Test
    void getCollider_ReturnsCopy() {
        snake.increaseBody(new Point(1, 1));
        List<Point> collider = snake.getCollider();

        assertNotSame(snake.getPoints(), collider);
        assertEquals(snake.getPoints().size(), collider.size());
    }
}