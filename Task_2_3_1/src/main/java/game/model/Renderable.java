package game.model;

import java.util.List;
import javafx.util.Pair;

/**
 * Интерфейс для объектов, которые имеют визуальное представление.
 *
 * @param <T> Тип точек объекта (enum вариантов)
 */
public interface Renderable<T> {
    /**
     * Возвращает объект для отрисовки.
     *
     * @return список пар: точка и тип
     */
    List<Pair<Point, T>> getRenderData();
}
