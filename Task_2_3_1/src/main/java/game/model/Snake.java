package game.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс, представляющий змейку как управляемый игровой объект.
 * Реализует логику движения, хранения сегментов тела и смены направления
 * с проверкой на допустимость поворота.
 */
public class Snake implements GameObject, Collider {
    private final List<Point> points = new LinkedList<>();
    private int startX;
    private int startY;
    private int startSize;
    Direction direction;

    /**
     * Создает новую змейку с начальными параметрами позиции, размера и направления.
     *
     * @param startX начальная координата X головы.
     * @param startY начальная координата Y головы.
     * @param startSize начальное количество сегментов.
     * @param startDirection исходное направление движения.
     */
    public Snake(int startX, int startY, int startSize, Direction startDirection) {
        this.direction = startDirection;
        this.startSize = startSize;
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Возвращает текущую позицию головы змейки.
     *
     * @return {@link Point} головы (первый элемент в списке сегментов).
     */
    public Point getHead() {
        return points.get(0);
    }

    /**
     * Добавляет новый сегмент к телу змейки (рост змейки).
     *
     * @param p точка, в которой появляется новый сегмент.
     */
    public void increaseBody(Point p) {
        points.add(p);
    }

    /**
     * Устанавливает новое направление движения змейки.
     * Поворот игнорируется, если он является противоположным текущему направлению
     * (например, нельзя повернуть вверх, если змейка движется вниз).
     *
     * @param dir желаемое направление {@link Direction}.
     */
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

    @Override
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Возвращает список всех сегментов змейки для проверки столкновений с собой
     * или другими объектами.
     *
     * @return копия списка {@link Point} всех частей тела змейки.
     */
    @Override
    public List<Point> getCollider() {
        return new LinkedList<>(points);
    }

    // Геттеры параметров инициализации оставлены без комментариев
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartSize() {
        return startSize;
    }

    public Direction getDirection() {
        return direction;
    }
}