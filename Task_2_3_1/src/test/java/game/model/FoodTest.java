package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void testOnCollisionRemovesFood() {
        Point p = food.getCollider().get(0);
        food.onCollision(null, p);
        assertEquals(3, food.getCollider().size());
        assertFalse(food.getCollider().contains(p));
    }

    @Test
    void testOnCollisionIncreasesScore() {
        Snake snake = new Snake(0, 0, 1, Direction.RIGHT, world);
        Point p = new Point(0, 0);
        food.onCollision(snake, p);
        assertEquals(1, world.getScore());
    }

    @Test
    void testRestart() {
        Point p = food.getCollider().get(0);
        food.onCollision(null, p);
        food.restart();
        assertEquals(3, food.getCollider().size());
    }

    @Test
    void testSpawnFoodFillsToMax() {
        food.onCollision(null, food.getCollider().get(0));
        food.onCollision(null, food.getCollider().get(0));
        assertEquals(3, food.getCollider().size());
    }

    @Test
    void testGetRenderData() {
        assertEquals(3, food.getRenderData().size());
        assertEquals(FoodType.DEFAULT, food.getRenderData().get(0).getValue());
    }

    @Test
    void testSpawnWhenWorldIsFull() {
        GameWorld smallWorld = new GameWorld(1, 1, 1);
        Food manyFood = new Food(2, smallWorld);
        assertEquals(1, manyFood.getCollider().size());
    }
}