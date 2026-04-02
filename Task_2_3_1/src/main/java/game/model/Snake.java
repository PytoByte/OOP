package game.model;

import java.util.LinkedList;
import java.util.List;

public class Snake implements GameObject, Collider {
    private final List<Point> points = new LinkedList<>();
    Direction direction;

    public Snake(int x, int y, int startSize, Direction startDirection) {
        this.direction = startDirection;
        for (int i = 0; i < startSize; i++) {
            points.add(new Point(x, y));
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction dir) {
        if (dir == Direction.UP && direction != Direction.DOWN) {
            direction = dir;
        }

        if (dir == Direction.DOWN && direction != Direction.UP) {
            direction = dir;
        }

        if (dir == Direction.LEFT && direction != Direction.RIGHT) {
            direction = dir;
        }

        if (dir == Direction.RIGHT && direction != Direction.LEFT) {
            direction = dir;
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public List<Point> getCollider() {
        return new LinkedList<>(points);
    }
}
