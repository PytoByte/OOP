package game.view;

import game.model.GameModel;
import game.model.Food;
import game.model.Point;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.util.LinkedList;

public class FoodView implements View {
    GameModel gameModel;
    Food food;
    Color foodColor = new Color(1, 0, 0, 1);

    public FoodView(GameModel gameModel, Food food) {
        this.gameModel = gameModel;
        this.food = food;
    }

    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        for (Point p : food.getPoints()) {
            view.add(new Pair<>(p, foodColor));
        }
        return view;
    }
}
