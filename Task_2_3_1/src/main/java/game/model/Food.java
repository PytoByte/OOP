package game.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

public class Food implements Renderable<FoodType>, Collider, Updatable {
    private final ArrayList<Point> points;
    private final GameWorld gameWorld;
    private final Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    private int maxCount;

    public Food(int maxCount, GameWorld gameWorld) {
        this.maxCount = maxCount;
        this.gameWorld = gameWorld;
        this.points = new ArrayList<>(maxCount);
        restart();
    }

    /**
     * По логике нашей новой архитектуры, GameWorld вызывает update() у всех Updatable.
     * Здесь мы можем проверять, нужно ли доспавнить еду.
     */
    @Override
    public void update() {
        if (points.size() < maxCount) {
            spawnFood();
        }
    }

    @Override
    public void restart() {
        points.clear();
        spawnFood();
    }

    /**
     * Логика генерации еды переехала из контроллера в модель.
     */
    private void spawnFood() {
        List<Point> redZone = gameWorld.getAllCollidersPoints();

        int spawnCount = maxCount - points.size();
        for (int i = 0; i < spawnCount; i++) {
            boolean found = true;
            Point p = new Point(
                    random.nextInt(gameWorld.getWidth()),
                    random.nextInt(gameWorld.getHeight())
            );

            if (redZone.contains(p)) {
                found = false;
                for (int x = 0; x < gameWorld.getWidth() && !found; x++) {
                    for (int y = 0; y < gameWorld.getHeight(); y++) {
                        Point candidate = new Point(x, y);
                        if (!redZone.contains(candidate)) {
                            p = candidate;
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                maxCount = points.size();
                break;
            }

            points.add(p);
            redZone.add(p);
        }
    }

    @Override
    public void onCollision(Collider other) {
        if (other instanceof Snake) {
            Point head = ((Snake) other).getHead();

            if (points.contains(head)) {
                points.remove(head);
                gameWorld.increaseScore(1);
            }
        }
    }

    @Override
    public List<Point> getCollider() {
        return new ArrayList<>(points);
    }

    @Override
    public List<Pair<Point, FoodType>> getRenderData() {
        List<Pair<Point, FoodType>> renderData = new ArrayList<>();
        for (Point p : points) {
            renderData.add(new Pair<>(p, FoodType.DEFAULT));
        }
        return renderData;
    }
}
