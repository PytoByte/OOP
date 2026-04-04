package game.view;

import game.model.GameModel;
import game.model.Point;
import game.model.Walls;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Класс, отвечающий за визуальное отображение стен (препятствий) на игровом поле.
 * Реализует интерфейс {@link View}, преобразуя координаты блоков стен
 * в цветные графические элементы для рендеринга.
 */
public class WallsView implements View {
    GameModel gameModel;
    Walls walls;
    Color wallColor = new Color(0.5, 0.5, 0.5, 1);

    /**
     * Создает объект отображения стен.
     *
     * @param gameModel модель игры для получения параметров поля.
     * @param walls объект стен, данные которого необходимо визуализировать.
     */
    public WallsView(GameModel gameModel, Walls walls) {
        this.gameModel = gameModel;
        this.walls = walls;
    }

    /**
     * Формирует и возвращает итерируемый набор пар из координат стен и их цвета.
     * Проходит по всем точкам, составляющим стены, и назначает им серый цвет.
     *
     * @return {@link Iterable} набор объектов {@link Pair}, содержащих {@link Point}
     *     и {@link Color}.
     */
    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        for (Point p : walls.getPoints()) {
            view.add(new Pair<>(p, wallColor));
        }
        return view;
    }
}
