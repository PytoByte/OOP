package game.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс, представляющий препятствия (стены) в игровом мире.
 * Хранит координаты сегментов стен и предоставляет их для отрисовки и обработки столкновений.
 */
public class Walls implements GameObject, Collider {
    private final List<Point> points = new LinkedList<>();

    /**
     * Добавляет новую точку (сегмент) в состав стен.
     *
     * @param p объект {@link Point} с координатами сегмента.
     */
    public void add(Point p) {
        points.add(p);
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Создает копию списка точек стен для использования в системе коллизий.
     *
     * @return новый {@link LinkedList}, содержащий все точки стен.
     */
    @Override
    public List<Point> getCollider() {
        return new LinkedList<>(points);
    }
}
