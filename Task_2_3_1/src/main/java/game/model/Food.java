package game.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Food implements GameObject, Collider {
    private final int maxCount;
    private final List<Point> points = new ArrayList<>();

    public Food(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxCount() {
        return maxCount;
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
