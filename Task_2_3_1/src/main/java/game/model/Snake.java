package game.model;

import java.util.LinkedList;
import java.util.List;

public class Snake implements GameObject, Collider {
    private final List<Point> points = new LinkedList<>();
    private int startX;
    private int startY;

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getStartSize() {
        return startSize;
    }

    public void setStartSize(int startSize) {
        this.startSize = startSize;
    }

    private int startSize;
    Direction direction;

    public Snake(int startX, int startY, int startSize, Direction startDirection) {
        this.direction = startDirection;
        this.startSize = startSize;
        this.startX = startX;
        this.startY = startY;
    }

    public Point getHead() {
        return points.get(0);
    }

    public void increaseBody(Point p) {
        points.add(p);
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
