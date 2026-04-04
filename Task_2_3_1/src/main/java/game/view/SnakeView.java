package game.view;

import game.model.GameModel;
import game.model.Point;
import game.model.Snake;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.util.LinkedList;

/**
 * Класс, отвечающий за визуальное представление змейки.
 * Формирует список цветных точек, где первый сегмент (голова)
 * окрашивается в отличительный цвет для удобства ориентации игрока.
 */
public class SnakeView implements View {
    GameModel gameModel;
    Snake snake;
    Color headColor = Color.AQUAMARINE;
    Color bodyColor = Color.MEDIUMAQUAMARINE;

    /**
     * Создает объект отображения змейки.
     *
     * @param gameModel модель игры для доступа к общим параметрам.
     * @param snake объект змейки, чьи координаты будут визуализированы.
     */
    public SnakeView(GameModel gameModel, Snake snake) {
        this.snake = snake;
        this.gameModel = gameModel;
    }

    /**
     * Формирует итерируемый набор пар из координат сегментов змейки и их цветов.
     *
     * Использует флаг для переключения цвета после обработки первого элемента (головы).
     * @return {@link Iterable} набор объектов {@link Pair}, содержащих {@link Point}
     * и {@link Color}.
     */
    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        boolean head = true;

        for (Point p : snake.getPoints()) {
            // Если это первый элемент списка — красим в цвет головы, иначе в цвет тела
            view.add(new Pair<>(p, head ? headColor : bodyColor));
            head = false;
        }

        return view;
    }
}