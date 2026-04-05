package game.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Модель игрового мира.
 */
public class GameWorld {
    private final int width;
    private final int height;
    private final int scoreToWin;

    private int score = 0;
    private boolean gameOver = false;
    private boolean gameWin = false;

    private final List<Restartable> restartable = new LinkedList<>();
    private final List<Updatable> updatable = new LinkedList<>();
    private final List<Collider> colliders = new LinkedList<>();

    /**
     * Базовый конструктор класа.
     *
     * @param width ширина игрового поля
     * @param height высота игрового поля
     * @param scoreToWin счёт для победы
     */
    public GameWorld(int width, int height, int scoreToWin) {
        this.width = width;
        this.height = height;

        if (scoreToWin > width * height) {
            throw new IllegalArgumentException("Too high scoreToWin. Limit is width*height");
        }
        this.scoreToWin = scoreToWin;
    }

    /**
     * Добавить игровую модель в мир.
     *
     * @param model новая игровая модель
     */
    public void addModel(Object model) {
        if (model instanceof Updatable) {
            updatable.add((Updatable) model);
        }

        if (model instanceof Collider) {
            colliders.add((Collider) model);
        }

        if (model instanceof Restartable) {
            restartable.add((Restartable) model);
        }
    }

    /**
     * Шаг игровой симуляции.
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

    /**
     * Проверка коллизий игровых объектов и вызов обработчиков коллизии.
     */
    private void checkCollisions() {
        for (int i = 0; i < colliders.size() - 1; i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider c1 = colliders.get(i);
                Collider c2 = colliders.get(j);

                Point intersectionPoint = intersection(c1, c2);
                if (intersectionPoint != null) {
                    c1.onCollision(c2, intersectionPoint);
                    c2.onCollision(c1, intersectionPoint);
                }
            }
        }
    }

    /**
     * Проверка коллизии двух конкретных игровых объектов.
     *
     * @param c1 первый объект
     * @param c2 второй объект
     * @return true если есть пересечение, иначе false
     */
    private Point intersection(Collider c1, Collider c2) {
        for (Point p1 : c1.getCollider()) {
            for (Point p2 : c2.getCollider()) {
                if (p1.equals(p2)) {
                    return new Point(p1.coordX, p2.coordY);
                }
            }
        }
        return null;
    }

    /**
     * Перезапуск игры.
     */
    public void restart() {
        score = 0;
        gameOver = false;
        gameWin = false;
        for (Restartable obj : restartable) {
            obj.restart();
        }
    }

    /**
     * Собирает координаты всех существующих коллайдеров в мире.
     *
     * @return список точек, занятых другими сущностями.
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

    /**
     * Увеличить счёт.
     *
     * @param inc на сколько увеличить счёт
     */
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
