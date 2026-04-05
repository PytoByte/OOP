package game.model;

import java.util.List;
import javafx.util.Pair;

/**
 * Интерфейс для объектов, которые имеют визуальное представление.
 * Позволяет получить список координат и цветов для отрисовки.
 */
public interface Renderable {
    /**
     * Возвращает список пар: точка на поле и её цвет.
     */
    List<Pair<Point, String>> getRenderData();
}