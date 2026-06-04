package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class BotTest {
    private TestGameWorld world;
    private Bot bot;

    private static class TestGameWorld extends GameWorld {
        private final List<ConstPoint> food = new ArrayList<>();
        private final List<ConstPoint> colliders = new ArrayList<>();

        public TestGameWorld(int width, int height) {
            super(width, height, 100);
        }

        public void addStaticFood(int x, int y) {
            food.add(new Point(x, y));
        }

        public void addStaticCollider(int x, int y) {
            colliders.add(new Point(x, y));
        }

        @Override
        public List<ConstPoint> getFoodPoints() {
            return food.isEmpty() ? super.getFoodPoints() : food;
        }

        @Override
        public List<ConstPoint> getAllCollidersPoints() {
            List<ConstPoint> res = new ArrayList<>(super.getAllCollidersPoints());
            res.addAll(colliders);
            return res;
        }
    }

    @BeforeEach
    void setUp() {
        world = new TestGameWorld(10, 10);
        bot = new Bot(5, 5, 3, Direction.RIGHT, world);
        world.addModel(bot);
    }

    @Test
    void testGetHead() {
        ConstPoint head = bot.getHead();
        assertEquals(5, head.getX());
        assertEquals(5, head.getY());
    }

    @Test
    void testUpdateMovesHead() {
        world.tick();
        ConstPoint head = bot.getHead();
        assertEquals(6, head.getX());
        assertEquals(5, head.getY());
    }

    @Test
    void testUpdateTeleportsAcrossWidth() {
        Snake edgeSnake = new Snake(9, 5, 1, Direction.RIGHT, world);
        world.addModel(edgeSnake);
        world.tick();
        assertEquals(0, edgeSnake.getHead().getX());
    }

    @Test
    void testUpdateTeleportsAcrossHeight() {
        Snake edgeSnake = new Snake(5, 0, 1, Direction.UP, world);
        world.addModel(edgeSnake);
        world.tick();
        assertEquals(9, edgeSnake.getHead().getY());
    }

    @Test
    void testSetDirectionValid() {
        bot.setDirection(Direction.UP);
        world.tick();
        assertEquals(4, bot.getHead().getY());
    }

    @Test
    void testSetDirectionInvalid() {
        bot.setDirection(Direction.LEFT);
        world.tick();
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
        world.tick();
        bot.setDirection(Direction.UP);
        world.restart();
        assertEquals(5, bot.getHead().getX());
        assertEquals(5, bot.getHead().getY());
        assertEquals(3, bot.getSize());
    }

    @Test
    void testGetRenderData() {
        assertEquals(1, bot.getRenderData().size());
        assertEquals(SnakePart.HEAD, bot.getRenderData().get(0).value());
        world.tick();
        assertEquals(2, bot.getRenderData().size());
        assertEquals(SnakePart.HEAD, bot.getRenderData().get(0).value());
        assertEquals(SnakePart.BODY, bot.getRenderData().get(1).value());
        world.tick();
        assertEquals(3, bot.getRenderData().size());
        assertEquals(SnakePart.HEAD, bot.getRenderData().get(0).value());
        assertEquals(SnakePart.BODY, bot.getRenderData().get(1).value());
        assertEquals(SnakePart.BODY, bot.getRenderData().get(2).value());
    }

    @Test
    void testSelfCollision() {
        Snake longSnake = new Snake(2, 2, 5, Direction.RIGHT, world);
        world.addModel(longSnake);
        world.tick();
        longSnake.setDirection(Direction.UP);
        world.tick();
        longSnake.setDirection(Direction.LEFT);
        world.tick();
        longSnake.setDirection(Direction.DOWN);
        world.tick();
        assertTrue(world.isGameOver());
    }

    @Test
    void testBotDiesOnSnakeCollision() {
        Snake enemy = new Snake(6, 5, 1, Direction.UP, world);
        world.addModel(enemy);
        bot.onCollision(enemy, bot.getHead());
        assertFalse(bot.isAlive());
        assertEquals(0, bot.getCollider().size());
    }

    @Test
    void testBotChoosesDirectionTowardsFood() {
        world.addStaticFood(7, 5);
        world.tick();
        assertEquals(6, bot.getHead().getX());
        assertEquals(5, bot.getHead().getY());
    }

    @Test
    void testBotAvoidsObstacles() {
        world.addStaticCollider(6, 5);
        world.tick();
        ConstPoint head = bot.getHead();
        assertTrue(
                (head.getX() == 5 && head.getY() == 4)
                        || (head.getX() == 5 && head.getY() == 6)
        );
    }

    @Test
    void testBotRespawnsAfterRestart() {
        Snake enemy = new Snake(6, 5, 1, Direction.UP, world);
        world.addModel(enemy);
        bot.onCollision(enemy, bot.getHead());
        world.restart();
        assertTrue(bot.isAlive());
        assertEquals(3, bot.getSize());
    }
}
