package game.model;

import java.util.ArrayList;
import java.util.List;

public class Food implements GameObject, Collider {
    private final List<Point> points = new ArrayList<>();

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public Iterable<Point> getCollider() {
        return points;
    }
}
