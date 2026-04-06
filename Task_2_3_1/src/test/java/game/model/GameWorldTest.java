package game.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameWorldTest {
    private static class MockEntity implements Updatable, Collider, Restartable {
        Point position;
        boolean updated = false;
        boolean restarted = false;
        boolean collided = false;
        Collider lastHit;

        MockEntity(Point p) {
            this.position = p;
        }

        @Override
        public void update() {
            updated = true;
        }

        @Override
        public void restart() {
            restarted = true;
        }

        @Override
        public List<Point> getCollider() {
            return Collections.singletonList(position);
        }

        @Override
        public void onCollision(Collider other, Point p) {
            collided = true;
            lastHit = other;
        }
    }

    private GameWorld world;

    @BeforeEach
    void setUp() {
        world = new GameWorld(10, 10, 5);
    }

    @Test
    void testConstructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GameWorld(2, 2, 5));
    }

    @Test
    void testIncreaseScore() {
        world.increaseScore(3);
        assertEquals(3, world.getScore());
    }

    @Test
    void testGameWin() {
        world.increaseScore(5);
        world.tick();
        assertTrue(world.isGameWin());
    }

    @Test
    void testSetGameOver() {
        world.setGameOver(true);
        assertTrue(world.isGameOver());
        world.setGameOver(false);
        assertFalse(world.isGameOver());
    }

    @Test
    void testRestart() {
        MockEntity entity = new MockEntity(new Point(0, 0));
        world.addModel(entity);

        world.increaseScore(3);
        world.setGameOver(true);
        world.restart();

        assertEquals(0, world.getScore());
        assertFalse(world.isGameOver());
        assertFalse(world.isGameWin());
        assertTrue(entity.restarted);
    }

    @Test
    void testTickStopOnGameOver() {
        world.setGameOver(true);
        world.increaseScore(10);
        world.tick();
        assertFalse(world.isGameWin());
    }

    @Test
    void testTickUpdatesModels() {
        MockEntity entity = new MockEntity(new Point(0, 0));
        world.addModel(entity);
        world.tick();
        assertTrue(entity.updated);
    }

    @Test
    void testCollision() {
        MockEntity e1 = new MockEntity(new Point(1, 1));
        MockEntity e2 = new MockEntity(new Point(1, 1));

        world.addModel(e1);
        world.addModel(e2);
        world.tick();

        assertTrue(e1.collided);
        assertTrue(e2.collided);
        assertEquals(e2, e1.lastHit);
    }

    @Test
    void testGetAllCollidersPoints() {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(1, 1);
        world.addModel(new MockEntity(p1));
        world.addModel(new MockEntity(p2));

        List<Point> points = world.getAllCollidersPoints();
        assertEquals(2, points.size());
        assertTrue(
                (points.get(0).equals(p1) && points.get(1).equals(p2))
                        || (points.get(1).equals(p1) && points.get(0).equals(p2))
        );
    }

    @Test
    void testAddModelWithNonFunctionalObject() {
        world.addModel(new Object());
        assertDoesNotThrow(world::tick);
        assertDoesNotThrow(world::restart);
        assertDoesNotThrow(world::getAllCollidersPoints);
    }

    @Test
    void testNoIntersectionForDifferentPoints() {
        MockEntity e1 = new MockEntity(new Point(1, 1));
        MockEntity e2 = new MockEntity(new Point(2, 2));

        world.addModel(e1);
        world.addModel(e2);
        world.tick();

        assertFalse(e1.collided);
        assertFalse(e2.collided);
    }
}