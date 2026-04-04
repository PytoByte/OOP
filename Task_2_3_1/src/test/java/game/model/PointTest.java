package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void getX() {
        Point p = new Point(10, 20);
        assertEquals(10, p.getX());
    }

    @Test
    void setX() {
        Point p = new Point(0, 0);
        p.setX(50);
        assertEquals(50, p.getX());
    }

    @Test
    void getY() {
        Point p = new Point(10, 20);
        assertEquals(20, p.getY());
    }

    @Test
    void setY() {
        Point p = new Point(0, 0);
        p.setY(100);
        assertEquals(100, p.getY());
    }

    @Test
    void testEquals() {
        Point p1 = new Point(5, 5);
        assertEquals(p1, p1);

        Point p2 = new Point(5, 5);
        assertEquals(p1, p2);
        assertEquals(p2, p1);

        Point p3 = new Point(10, 5);
        assertNotEquals(p1, p3);

        assertNotEquals(null, p1);

        String notPoint = "5, 5";
        assertNotEquals(p1, notPoint);
    }

    @Test
    void testToString() {
        Point p = new Point(7, -3);
        assertEquals("(7, -3)", p.toString());
    }
}