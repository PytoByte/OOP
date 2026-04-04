package game.model;

import java.util.LinkedList;
import java.util.List;

public class Walls implements GameObject, Collider {
    private final List<Point> points = new LinkedList<>();

    public void add(Point p) {
        points.add(p);
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public List<Point> getCollider() {
        return new LinkedList<>(points);
    }
}
