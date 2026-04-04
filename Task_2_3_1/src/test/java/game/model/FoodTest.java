package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodTest {
    private Food food;
    private final int initialMaxCount = 5;

    @BeforeEach
    void setUp() {
        food = new Food(initialMaxCount);
    }

    @Test
    void setMaxCount() {
        food.setMaxCount(10);
        assertEquals(10, food.getMaxCount());
    }

    @Test
    void getMaxCount() {
        assertEquals(initialMaxCount, food.getMaxCount());
    }

    @Test
    void getCount() {
        assertEquals(0, food.getCount());
        food.addFood(new Point(1, 1));
        assertEquals(1, food.getCount());
    }

    @Test
    void addFood_shouldNotExceedMaxCount() {
        for (int i = 0; i < initialMaxCount; i++) {
            food.addFood(new Point(i, i));
        }
        assertEquals(initialMaxCount, food.getCount());

        food.addFood(new Point(10, 10));
        assertEquals(initialMaxCount, food.getCount());
    }

    @Test
    void removeFood() {
        Point p = new Point(5, 5);
        food.addFood(p);
        assertEquals(1, food.getCount());

        food.removeFood(p);
        assertEquals(0, food.getCount());
    }

    @Test
    void getPoints() {
        Point p1 = new Point(1, 1);
        food.addFood(p1);
        List<Point> points = food.getPoints();

        assertTrue(points.contains(p1));
        assertEquals(1, points.size());
    }

    @Test
    void getCollider() {
        Point p1 = new Point(2, 2);
        food.addFood(p1);
        List<Point> collider = food.getCollider();

        assertNotNull(collider);
        assertEquals(1, collider.size());
        assertEquals(p1, collider.get(0));

        assertNotSame(food.getPoints(), food.getCollider());
    }
}