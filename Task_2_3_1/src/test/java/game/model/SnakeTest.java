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
        Point head = snake.getHead();
        assertEquals(5, head.getCoordX());
        assertEquals(5, head.getCoordY());
    }

    @Test
    void testUpdateMovesHead() {
        snake.update();
        Point head = snake.getHead();
        assertEquals(6, head.getCoordX());
        assertEquals(5, head.getCoordY());
    }

    @Test
    void testUpdateTeleportsAcrossWidth() {
        Snake edgeSnake = new Snake(9, 5, 1, Direction.RIGHT, world);
        edgeSnake.update();
        assertEquals(0, edgeSnake.getHead().getCoordX());
    }

    @Test
    void testUpdateTeleportsAcrossHeight() {
        Snake edgeSnake = new Snake(5, 0, 1, Direction.UP, world);
        edgeSnake.update();
        assertEquals(9, edgeSnake.getHead().getCoordY());
    }

    @Test
    void testSetDirectionValid() {
        snake.setDirection(Direction.UP);
        snake.update();
        assertEquals(4, snake.getHead().getCoordY());
    }

    @Test
    void testSetDirectionInvalid() {
        snake.setDirection(Direction.LEFT);
        snake.update();
        assertEquals(6, snake.getHead().getCoordX());
    }

    @Test
    void testOnCollisionWithFood() {
        Food food = new Food(1, world);
        Point headPos = snake.getHead();
        snake.onCollision(food, new Point(headPos.getCoordX(), headPos.getCoordY()));
        assertEquals(4, snake.getCollider().size());
    }

    @Test
    void testRestart() {
        snake.update();
        snake.setDirection(Direction.UP);
        snake.restart();
        assertEquals(5, snake.getHead().getCoordX());
        assertEquals(5, snake.getHead().getCoordY());
        assertEquals(3, snake.getCollider().size());
        assertEquals(Direction.RIGHT, snake.getStartDirection());
    }

    @Test
    void testGetRenderData() {
        assertEquals(3, snake.getRenderData().size());
        assertEquals(SnakePart.HEAD, snake.getRenderData().get(0).getValue());
        assertEquals(SnakePart.BODY, snake.getRenderData().get(1).getValue());
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