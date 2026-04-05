package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Главная модель игрового мира.
 * Хранит состояние, список игровых объектов и содержит ВСЕ правила игры.
 */
public class GameWorld {
    private final int width;
    private final int height;
    private final int scoreToWin;

    private int score = 0;
    private boolean gameOver = false;
    private boolean gameWin = false;

    private final List<Updatable> updatable = new ArrayList<>();
    private final List<Collider> colliders = new ArrayList<>();

    public GameWorld(int width, int height, int scoreToWin) {
        this.width = width;
        this.height = height;
        this.scoreToWin = scoreToWin;
    }

    public void addModel(Object model) {
        if (model instanceof Updatable) {
            updatable.add((Updatable) model);
        }

        if (model instanceof Collider) {
            colliders.add((Collider) model);
        }
    }

    /**
     * Главный шаг игровой симуляции.
     * Вызывается контроллером на каждый тик таймера.
     */
    public void tick() {
        if (gameOver || gameWin) {
            return;
        }

        for (Updatable obj : updatable) {
            obj.update();
        }

        checkCollisions();

        if (score >= scoreToWin) {
            gameWin = true;
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < colliders.size() - 1; i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider c1 = colliders.get(i);
                Collider c2 = colliders.get(j);

                if (hasIntersection(c1, c2)) {
                    c1.onCollision(c2);
                    c2.onCollision(c1);
                }
            }
        }
    }

    private boolean hasIntersection(Collider c1, Collider c2) {
        for (Point p1 : c1.getCollider()) {
            for (Point p2 : c2.getCollider()) {
                if (p1.equals(p2)) return true;
            }
        }
        return false;
    }

    public void restart() {
        score = 0;
        gameOver = false;
        gameWin = false;
        for (Updatable obj : updatable) {
            obj.restart();
        }
    }

    /**
     * Собирает координаты всех существующих коллайдеров в мире.
     * Используется для определения "красной зоны" при генерации новых объектов.
     *
     * @return список объектов {@link Point}, занятых другими сущностями.
     */
    public List<Point> getAllCollidersPoints() {
        List<Point> allPoints = new ArrayList<>();

        for (Collider collider : colliders) {
            List<Point> colliderPoints = collider.getCollider();
            if (colliderPoints != null) {
                allPoints.addAll(colliderPoints);
            }
        }

        return allPoints;
    }

    public void increaseScore(int inc) {
        this.score += inc;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWin() {
        return gameWin;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
