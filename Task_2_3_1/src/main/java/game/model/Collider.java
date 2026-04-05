package game.model;

import java.util.List;

/**
 * Интерфейс модели игрового объекта, имеющего коллизию.
 */
public interface Collider {
    /**
     * Возвращает хитбокс объекта.
     *
     * @return список точек, представляющих хитбокс объекта
     */
    List<Point> getCollider();

    /**
     * Обработчик коллизии.
     *
     * @param other Модель игрового объекта с которым произошла коллизия
     * @param p Точка в которой произошла коллизия
     */
    void onCollision(Collider other, Point p);
}