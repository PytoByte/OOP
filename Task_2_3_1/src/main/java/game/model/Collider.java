package game.model;

import java.util.List;

public interface Collider {
    /**
     * Возвращает список точек, представляющих хитбокс объекта.
     */
    List<Point> getCollider();

    /**
     * Вызывается миром, когда зафиксировано столкновение с другим объектом.
     * Объект сам решает, как реагировать (змейка умрёт, еда переродится).
     */
    void onCollision(Collider other);
}