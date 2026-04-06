package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodTest {
    private GameWorld world;
    private Food food;

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 100);
        food = new Food(3, world);
    }

    @Test
    void testGetColliderSize() {
        assertEquals(3, food.getCollider().size());
    }

    @Test
    void testOnCollisionRemovesAndRespawnsFood() {
        Point p = food.getCollider().get(0);
        food.onCollision(new MockCollider(), p);

        assertEquals(3, food.getCollider().size());
        assertEquals(0, world.getScore());
    }

    @Test
    void testOnCollisionWithSnakeIncreasesScore() {
        Snake snake = new Snake(5, 5, 1, Direction.RIGHT, world);
        Point p = new Point(5, 5);

        food.onCollision(snake, p);
        assertEquals(1, world.getScore());
    }

    @Test
    void testOnCollisionWithSnakeBodyDoesNotIncreaseScore() {
        Snake snake = new Snake(5, 5, 2, Direction.RIGHT, world);
        Point bodyPoint = new Point(4, 5);

        food.onCollision(snake, bodyPoint);
        assertEquals(0, world.getScore());
    }

    @Test
    void testRestart() {
        int initialSize = food.getCollider().size();
        food.restart();
        assertEquals(initialSize, food.getCollider().size());
    }

    @Test
    void testSpawnFoodWhenRandomPointIsOccupied() {
        GameWorld tinyWorld = new GameWorld(2, 1, 2);

        MockCollider obstacle = new MockCollider();
        obstacle.points = Collections.singletonList(new Point(0, 0));
        tinyWorld.addModel(obstacle);

        Food foodInTinyWorld = new Food(1, tinyWorld);

        assertEquals(1, foodInTinyWorld.getCollider().size());
        assertEquals(1, foodInTinyWorld.getCollider().get(0).coordX);
    }

    @Test
    void testSpawnWhenNoSpaceLeft() {
        GameWorld tinyWorld = new GameWorld(1, 1, 1);

        MockCollider obstacle = new MockCollider();
        obstacle.points = Collections.singletonList(new Point(0, 0));
        tinyWorld.addModel(obstacle);

        Food foodInFullWorld = new Food(1, tinyWorld);

        assertTrue(foodInFullWorld.getCollider().isEmpty());
    }

    @Test
    void testGetRenderData() {
        var data = food.getRenderData();
        assertFalse(data.isEmpty());
        assertEquals(FoodType.DEFAULT, data.get(0).getValue());
    }

    private static class MockCollider implements Collider {
        List<Point> points = Collections.emptyList();
        @Override public List<Point> getCollider() { return points; }
        @Override public void onCollision(Collider other, Point p) {}
    }
}