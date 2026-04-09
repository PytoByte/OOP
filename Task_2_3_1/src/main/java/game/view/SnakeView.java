package game.view;

import game.model.ConstPoint;
import game.model.Pair;
import game.model.Renderable;
import game.model.Snake;
import game.model.SnakePart;
import java.util.LinkedList;
import javafx.scene.paint.Color;

/**
 * Визуальное отображение змейки.
 */
public class SnakeView implements View {
    Renderable<SnakePart> snake;
    Color headColor = Color.AQUAMARINE;
    Color bodyColor = Color.MEDIUMAQUAMARINE;

    /**
     * Базовый конструктор класса.
     *
     * @param snake модель змейки.
     */
    public SnakeView(Snake snake) {
        this.snake = snake;
    }

    @Override
    public Iterable<Pair<ConstPoint, Color>> getView() {
        LinkedList<Pair<ConstPoint, Color>> view = new LinkedList<>();

        for (Pair<ConstPoint, SnakePart> pointRender : snake.getRenderData()) {
            Color color;
            switch (pointRender.value()) {
                case HEAD -> color = headColor;
                case BODY -> color = bodyColor;
                default -> {
                    System.err.printf("Unexpected snake part %s\n", pointRender.value());
                    color = errorColor;
                }
            }

            view.add(new Pair<>(pointRender.key(), color));
        }

        return view;
    }
}