package game.view;

import game.model.ConstPoint;
import game.model.FoodType;
import game.model.Pair;
import game.model.Renderable;
import java.util.LinkedList;
import javafx.scene.paint.Color;

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
    public Iterable<Pair<ConstPoint, Color>> getView() {
        LinkedList<Pair<ConstPoint, Color>> view = new LinkedList<>();
        for (Pair<ConstPoint, FoodType> pointRender : food.getRenderData()) {
            Color color;
            switch (pointRender.value()) {
                case DEFAULT -> color = defaultColor;
                default -> {
                    System.err.printf("Unexpected food type %s\n", pointRender.value());
                    color = errorColor;
                }
            }
            view.add(new Pair<>(pointRender.key(), color));
        }
        return view;
    }
}
