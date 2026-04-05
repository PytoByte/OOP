package game.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

/**
 * Модель еды.
 */
public class Food implements Renderable<FoodType>, Collider, Restartable {
    private final ArrayList<Point> points;
    private final GameWorld gameWorld;
    private final Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    private int maxCount;

    /**
     * Базовый конструктор класса.
     *
     * @param maxCount максимальное количество еды на поле
     * @param gameWorld мир игры
     */
    public Food(int maxCount, GameWorld gameWorld) {
        this.maxCount = maxCount;
        this.gameWorld = gameWorld;
        this.points = new ArrayList<>(maxCount);
        restart();
    }

    @Override
    public void restart() {
        points.clear();
        spawnFood();
    }

    /**
     * Генерация еды.
     */
    private void spawnFood() {
        List<Point> redZone;
        int spawnCount = maxCount - points.size();
        if (spawnCount == 0) {
            return;
        }

        redZone = gameWorld.getAllCollidersPoints();

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
    public void onCollision(Collider other, Point p) {
        points.remove(p);
        spawnFood();
        if (other instanceof Snake snake) {
            if (snake.getHead().equals(p)) {
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
