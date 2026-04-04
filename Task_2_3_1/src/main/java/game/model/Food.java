package game.model;

import java.util.ArrayList;
import java.util.List;

public class Food implements GameObject, Collider {
    private int maxCount;
    private final ArrayList<Point> points;

    public Food(int maxCount) {
        this.maxCount = maxCount;
        this.points = new ArrayList<>(maxCount);
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCount() {
        return points.size();
    }

    public void addFood(Point p) {
        if (points.size() < maxCount) {
            points.add(p);
        }
    }

    public void removeFood(Point p) {
        points.remove(p);
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public List<Point> getCollider() {
        return new ArrayList<>(points);
    }
}
