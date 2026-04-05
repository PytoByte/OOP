package game.view;

import game.model.Point;
import game.model.Renderable;
import game.model.Snake;
import game.model.SnakePart;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.util.Pair;

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
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();

        for (Pair<Point, SnakePart> pointRender : snake.getRenderData()) {
            Color color;
            switch (pointRender.getValue()) {
                case SnakePart.HEAD -> color = headColor;
                case SnakePart.BODY -> color = bodyColor;
                default -> {
                    System.err.printf("Unexpected snake part %s\n", pointRender.getValue());
                    color = errorColor;
                }
            }

            view.add(new Pair<>(pointRender.getKey(), color));
        }

        return view;
    }
}