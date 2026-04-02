package game.view;

import game.model.GameModel;
import game.model.Point;
import game.model.Walls;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.LinkedList;

public class WallsView implements View {
    GameModel gameModel;
    Walls walls;
    Color wallColor = new Color(0.5, 0.5, 0.5, 1);

    public WallsView(GameModel gameModel, Walls walls) {
        this.gameModel = gameModel;
        this.walls = walls;
    }

    @Override
    public Iterable<Pair<Point, Color>> getView() {
        LinkedList<Pair<Point, Color>> view = new LinkedList<>();
        for (Point p : walls.getPoints()) {
            view.add(new Pair<>(p, wallColor));
        }
        return view;
    }
}
