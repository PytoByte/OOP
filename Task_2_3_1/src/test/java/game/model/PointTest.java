package game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PointTest {

    @Test
    void testEquals() {
        Point p1 = new Point(5, 10);
        Point p2 = new Point(5, 10);
        Point p3 = new Point(10, 5);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    void testHashCode() {
        Point p1 = new Point(3, 7);
        Point p2 = new Point(3, 7);
        Point p3 = new Point(7, 3);

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    void testToString() {
        Point p = new Point(4, 2);
        assertEquals("(4, 2)", p.toString());
    }

    @Test
    void getCoordX() {
        Point p = new Point(8, 15);
        assertEquals(8, p.getCoordX());
    }

    @Test
    void setCoordX() {
        Point p = new Point(0, 0);
        p.setCoordX(42);
        assertEquals(42, p.getCoordX());
    }

    @Test
    void getCoordY() {
        Point p = new Point(8, 15);
        assertEquals(15, p.getCoordY());
    }

    @Test
    void setCoordY() {
        Point p = new Point(0, 0);
        p.setCoordY(99);
        assertEquals(99, p.getCoordY());
    }
}