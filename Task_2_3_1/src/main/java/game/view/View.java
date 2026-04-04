package game.view;

import game.model.Point;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Интерфейс, определяющий контракт для визуального представления игровых объектов.
 * Позволяет любому объекту (змейке, еде, стенам) предоставлять данные
 * для отрисовки в унифицированном формате.
 */
public interface View {
    /**
     * Возвращает наборы данных для отрисовки объекта.
     * Каждая пара содержит координаты точки на поле и соответствующий ей цвет.
     *
     * @return итерируемый набор объектов {@link Pair}, связывающих {@link Point} и {@link Color}.
     */
    Iterable<Pair<Point, Color>> getView();
}
