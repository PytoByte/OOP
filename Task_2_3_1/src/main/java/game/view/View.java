package game.view;

import game.model.Point;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public interface View {
    Iterable<Pair<Point, Color>> getView();
}
