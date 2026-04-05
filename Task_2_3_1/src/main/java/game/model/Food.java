package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за управление объектами еды на игровом поле.
 * Хранит текущие позиции еды и ограничивает их максимальное количество.
 */
public class Food implements Renderable, Collider {
    private int maxCount;
    private final ArrayList<Point> points;

    /**
     * Создает объект управления едой с ограничением по количеству.
     *
     * @param maxCount максимально допустимое количество еды на поле одновременно.
     */
    public Food(int maxCount) {
        this.maxCount = maxCount;
        this.points = new ArrayList<>(maxCount);
    }

    /**
     * Добавляет новую единицу еды в указанную точку, если лимит не превышен.
     *
     * @param p координаты новой еды.
     */
    public void addFood(Point p) {
        if (points.size() < maxCount) {
            points.add(p);
        }
    }

    /**
     * Удаляет еду из указанной точки (например, когда змейка её съедает).
     *
     * @param p координаты еды, которую нужно удалить.
     */
    public void removeFood(Point p) {
        points.remove(p);
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Создает копию списка точек еды для обработки столкновений.
     *
     * @return новый {@link ArrayList}, содержащий текущие координаты всей еды.
     */
    @Override
    public List<Point> getCollider() {
        return new ArrayList<>(points);
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
}
