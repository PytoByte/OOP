package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BotTest {
    private GameWorld world;
    private Bot bot;

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 100);
        bot = new Bot(5, 5, 3, Direction.RIGHT, world);
    }

    @Test
    void testGetHead() {
        ConstPoint head = bot.getHead();
        assertEquals(5, head.getX());
        assertEquals(5, head.getY());
    }

    @Test
    void testUpdateMovesHead() {
        bot.update();
        ConstPoint head = bot.getHead();
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
        bot.setDirection(Direction.UP);
        bot.update();
        assertEquals(4, bot.getHead().getY());
    }

    @Test
    void testSetDirectionInvalid() {
        bot.setDirection(Direction.LEFT);
        bot.update();
        assertEquals(6, bot.getHead().getX());
    }

    @Test
    void testOnCollisionWithFood() {
        FoodManager foodManager = new FoodManager(1, world);
        ConstPoint headPos = bot.getHead();
        bot.onCollision(foodManager, new Point(headPos.getX(), headPos.getY()));
        assertEquals(4, bot.getSize());
    }

    @Test
    void testRestart() {
        bot.update();
        bot.setDirection(Direction.UP);
        bot.restart();
        assertEquals(5, bot.getHead().getX());
        assertEquals(5, bot.getHead().getY());
        assertEquals(3, bot.getSize());
        assertEquals(1, bot.getCollider().size());
    }

    @Test
    void testGetRenderData() {
        assertEquals(1, bot.getRenderData().size());
        assertEquals(SnakePart.HEAD, bot.getRenderData().get(0).value());
        bot.update();
        assertEquals(2, bot.getRenderData().size());
        assertEquals(SnakePart.HEAD, bot.getRenderData().get(0).value());
        assertEquals(SnakePart.BODY, bot.getRenderData().get(1).value());
        bot.update();
        assertEquals(3, bot.getRenderData().size());
        assertEquals(SnakePart.HEAD, bot.getRenderData().get(0).value());
        assertEquals(SnakePart.BODY, bot.getRenderData().get(1).value());
        assertEquals(SnakePart.BODY, bot.getRenderData().get(2).value());
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
