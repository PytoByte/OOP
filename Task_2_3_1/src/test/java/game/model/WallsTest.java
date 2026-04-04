package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class WallsTest {
    private Walls walls;

    @BeforeEach
    void setUp() {
        walls = new Walls();
    }

    @Test
    void add() {
        Point p = new Point(10, 20);
        walls.add(p);

        List<Point> points = walls.getPoints();
        assertEquals(1, points.size());
        assertEquals(p, points.get(0));
    }

    @Test
    void getPoints() {
        walls.add(new Point(1, 1));
        walls.add(new Point(2, 2));

        List<Point> points = walls.getPoints();
        assertNotNull(points);
        assertEquals(2, points.size());
        assertEquals(new Point(1, 1), points.get(0));
    }

    @Test
    void getCollider() {
        Point p1 = new Point(5, 5);
        walls.add(p1);

        List<Point> colliderPoints = walls.getCollider();

        assertEquals(1, colliderPoints.size());
        assertEquals(p1, colliderPoints.get(0));

        colliderPoints.add(new Point(0, 0));
        assertNotEquals(walls.getPoints().size(), colliderPoints.size(),
                "Изменение списка коллайдера не должно менять внутреннее состояние объекта Walls");
    }
}