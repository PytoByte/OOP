package game.view;

import game.model.ConstPoint;
import game.model.Pair;
import game.model.Point;
import javafx.scene.paint.Color;

/**
 * Интерфейс для визуального представления игровых объектов.
 */
public interface View {
    Color errorColor = new Color(0, 0, 0, 1);

    /**
     * Формирует и возвращает итерируемый набор пар из координат и цвета.
     *
     * @return Набор пар {@link Point} и {@link Color}.
     */
    Iterable<Pair<ConstPoint, Color>> getView();
}
