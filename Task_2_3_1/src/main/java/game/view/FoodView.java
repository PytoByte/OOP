package game.view;

import game.model.FoodType;
import game.model.Point;
import game.model.Renderable;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Визуальное отображение еды.
 */
public class FoodView implements View {
    Renderable<FoodType> food;
    Color defaultColor = new Color(1, 0, 0, 1);

    /**
     * Базовый конструктор класса.
     *
     * @param food модель еды.
     */
    public FoodView(Renderable<FoodType> food) {
        this.food = food;
    }

    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        for (Pair<Point, FoodType> pointRender : food.getRenderData()) {
            Color color;
            switch (pointRender.getValue()) {
                case FoodType.DEFAULT -> color = defaultColor;
                default -> {
                    System.err.printf("Unexpected food type %s\n", pointRender.getValue());
                    color = errorColor;
                }
            }
            view.add(new Pair<>(pointRender.getKey(), color));
        }
        return view;
    }
}
