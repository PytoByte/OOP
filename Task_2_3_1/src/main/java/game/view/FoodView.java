package game.view;

import game.model.Food;
import game.model.GameWorld;
import game.model.Point;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Класс, отвечающий за визуальное отображение еды на игровом поле.
 * Реализует интерфейс {@link View}, предоставляя данные о координатах
 * и цвете каждой единицы еды для последующей отрисовки.
 */
public class FoodView implements View {
    GameWorld gameWorld;
    Food food;
    Color foodColor = new Color(1, 0, 0, 1);

    /**
     * Создает объект отображения еды.
     *
     * @param gameWorld модель игры, содержащая общие параметры поля.
     * @param food объект еды, данные которого необходимо визуализировать.
     */
    public FoodView(GameWorld gameWorld, Food food) {
        this.gameWorld = gameWorld;
        this.food = food;
    }

    /**
     * Формирует и возвращает итерируемый набор пар из координат и цвета.
     * Проходит по всем точкам еды и связывает их с заданным цветом (по умолчанию красным).
     *
     * @return {@link Iterable} набор объектов {@link Pair},
     *     содержащих {@link Point} и {@link Color}.
     */
    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        for (Point p : food.getPoints()) {
            view.add(new Pair<>(p, foodColor));
        }
        return view;
    }
}
