package game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void testEquals() {
        Point p1 = new Point(5, 10);
        Point p2 = new Point(5, 10);
        Point p3 = new Point(10, 5);
        Point p4 = new Point(5, 11);
        Point p5 = new Point(6, 10);

        assertEquals(p1, p1);
        assertEquals(p1, p2);

        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
        assertNotEquals(p1, p5);
        assertNotEquals(null, p1);
        assertNotEquals("not a point", p1);
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
    void testGetAndSetCoordX() {
        Point p = new Point(8, 15);
        assertEquals(8, p.getCoordX());
        p.setCoordX(42);
        assertEquals(42, p.getCoordX());
    }

    @Test
    void testGetAndSetCoordY() {
        Point p = new Point(8, 15);
        assertEquals(15, p.getCoordY());
        p.setCoordY(99);
        assertEquals(99, p.getCoordY());
    }
}