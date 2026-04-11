package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeTest {
    private GameWorld world;
    private Snake snake;

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 100);
        snake = new Snake(5, 5, 3, Direction.RIGHT, world);
    }

    @Test
    void testGetHead() {
        ConstPoint head = snake.getHead();
        assertEquals(5, head.getX());
        assertEquals(5, head.getY());
    }

    @Test
    void testUpdateMovesHead() {
        snake.update();
        ConstPoint head = snake.getHead();
        assertEquals(6, head.getX());
        assertEquals(5, head.getY());
    }

    @Test
    void testUpdateTeleportsAcrossWidth() {
        Snake edgeSnake = new Snake(9, 5, 1, Direction.RIGHT, world);
        edgeSnake.update();
        assertEquals(0, edgeSnake.getHead().getX());
    }

    @Test
    void testUpdateTeleportsAcrossHeight() {
        Snake edgeSnake = new Snake(5, 0, 1, Direction.UP, world);
        edgeSnake.update();
        assertEquals(9, edgeSnake.getHead().getY());
    }

    @Test
    void testSetDirectionValid() {
        snake.setDirection(Direction.UP);
        snake.update();
        assertEquals(4, snake.getHead().getY());
    }

    @Test
    void testSetDirectionInvalid() {
        snake.setDirection(Direction.LEFT);
        snake.update();
        assertEquals(6, snake.getHead().getX());
    }

    @Test
    void testOnCollisionWithFood() {
        FoodManager foodManager = new FoodManager(1, world);
        ConstPoint headPos = snake.getHead();
        snake.onCollision(foodManager, new Point(headPos.getX(), headPos.getY()));
        assertEquals(4, snake.getSize());
    }

    @Test
    void testRestart() {
        snake.update();
        snake.setDirection(Direction.UP);
        snake.restart();
        assertEquals(5, snake.getHead().getX());
        assertEquals(5, snake.getHead().getY());
        assertEquals(3, snake.getSize());
        assertEquals(1, snake.getCollider().size());
    }

    @Test
    void testGetRenderData() {
        assertEquals(1, snake.getRenderData().size());
        assertEquals(SnakePart.HEAD, snake.getRenderData().get(0).value());
        snake.update();
        assertEquals(2, snake.getRenderData().size());
        assertEquals(SnakePart.HEAD, snake.getRenderData().get(0).value());
        assertEquals(SnakePart.BODY, snake.getRenderData().get(1).value());
        snake.update();
        assertEquals(3, snake.getRenderData().size());
        assertEquals(SnakePart.HEAD, snake.getRenderData().get(0).value());
        assertEquals(SnakePart.BODY, snake.getRenderData().get(1).value());
        assertEquals(SnakePart.BODY, snake.getRenderData().get(2).value());
    }

    @Test
    void testSelfCollision() {
        Snake longSnake = new Snake(5, 5, 5, Direction.RIGHT, world);
        longSnake.update();
        longSnake.setDirection(Direction.UP);
        longSnake.update();
        longSnake.setDirection(Direction.LEFT);
        longSnake.update();
        longSnake.setDirection(Direction.DOWN);
        longSnake.update();
        assertTrue(world.isGameOver());
    }
}