package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodManagerTest {
    private static class MockCollider implements Collider {
        List<ConstPoint> points = Collections.emptyList();

        @Override
        public List<ConstPoint> getCollider() {
            return points;
        }

        @Override
        public void onCollision(Collider other, ConstPoint p) {
        }
    }

    private GameWorld world;
    private FoodManager foodManager;

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 100);
        foodManager = new FoodManager(3, world);
    }

    @Test
    void testGetColliderSize() {
        assertEquals(3, foodManager.getCollider().size());
    }

    @Test
    void testOnCollisionRemovesAndRespawnsFoodManager() {
        ConstPoint p = foodManager.getCollider().get(0);
        foodManager.onCollision(new MockCollider(), p);

        assertEquals(3, foodManager.getCollider().size());
        assertEquals(0, world.getScore());
    }

    @Test
    void testOnCollisionWithSnakeIncreasesScore() {
        Snake snake = new Snake(5, 5, 1, Direction.RIGHT, world);
        Point p = new Point(5, 5);

        foodManager.onCollision(snake, p);
        assertEquals(1, world.getScore());
    }

    @Test
    void testOnCollisionWithSnakeBodyDoesNotIncreaseScore() {
        Snake snake = new Snake(-1, -1, 2, Direction.RIGHT, world);
        ConstPoint bodyPoint = foodManager.getCollider().get(0);

        foodManager.onCollision(snake, bodyPoint);
        assertEquals(0, world.getScore());
    }

    @Test
    void testRestart() {
        int initialSize = foodManager.getCollider().size();
        foodManager.restart();
        assertEquals(initialSize, foodManager.getCollider().size());
    }

    @Test
    void testSpawnFoodManagerWhenRandomPointIsOccupied() {
        GameWorld tinyWorld = new GameWorld(2, 1, 2);

        MockCollider obstacle = new MockCollider();
        obstacle.points = Collections.singletonList(new Point(0, 0));
        tinyWorld.addModel(obstacle);

        FoodManager foodManagerInTinyWorld = new FoodManager(1, tinyWorld);

        assertEquals(1, foodManagerInTinyWorld.getCollider().size());
        assertEquals(1, foodManagerInTinyWorld.getCollider().get(0).getX());
    }

    @Test
    void testSpawnWhenNoSpaceLeft() {
        GameWorld tinyWorld = new GameWorld(1, 1, 1);

        MockCollider obstacle = new MockCollider();
        obstacle.points = Collections.singletonList(new Point(0, 0));
        tinyWorld.addModel(obstacle);

        FoodManager foodManagerInFullWorld = new FoodManager(1, tinyWorld);

        assertTrue(foodManagerInFullWorld.getCollider().isEmpty());
    }

    @Test
    void testGetRenderData() {
        var data = foodManager.getRenderData();
        assertFalse(data.isEmpty());
        assertEquals(FoodType.DEFAULT, data.get(0).value());
    }
}