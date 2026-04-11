package game.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import game.model.Direction;
import game.model.GameWorld;
import game.model.Snake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeControllerTest {
    private Snake snake;
    private SnakeController snakeController;

    @BeforeEach
    void setUp() {
        GameWorld world = new GameWorld(10, 10, 100);
        snake = new Snake(5, 5, 3, Direction.RIGHT, world);
        snakeController = new SnakeController(snake);
    }

    @Test
    void testRestartSetsStartDirection() {
        snakeController.restart();
        snakeController.tick();
        assertEquals(Direction.RIGHT, snake.getStartDirection());
    }
}
