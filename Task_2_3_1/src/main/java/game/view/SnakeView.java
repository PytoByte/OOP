package game.view;

import game.model.GameModel;
import game.model.Point;
import game.model.Snake;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.LinkedList;

public class SnakeView implements View {
    GameModel gameModel;
    Snake snake;
    Color headColor = Color.AQUAMARINE;
    Color bodyColor = Color.MEDIUMAQUAMARINE;

    public SnakeView(GameModel gameModel, Snake snake) {
        this.snake = snake;
        this.gameModel = gameModel;
    }

    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        boolean head = true;
        for (Point p : snake.getPoints()) {
            view.add(new Pair<>(p, head ? headColor : bodyColor));
            head = false;
        }
        return view;
    }
}
