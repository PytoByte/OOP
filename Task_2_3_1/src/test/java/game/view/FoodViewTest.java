package game.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.model.Food;
import game.model.GameWorld;
import game.model.Point;
import java.util.Iterator;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodViewTest {
    private Food food;
    private View foodView;

    @BeforeEach
    void setUp() {
        GameWorld world = new GameWorld(10, 10, 100);
        food = new Food(1, world);
        foodView = new FoodView(food);
    }

    @Test
    void testGetViewColor() {
        Iterable<Pair<Point, Color>> viewData = foodView.getView();
        Iterator<Pair<Point, Color>> iterator = viewData.iterator();

        assertTrue(iterator.hasNext());
        Pair<Point, Color> pair = iterator.next();
        assertEquals(new Color(1, 0, 0, 1), pair.getValue());
    }

    @Test
    void testGetViewSize() {
        int count = 0;
        for (Pair<Point, Color> pair : foodView.getView()) {
            count++;
        }
        assertEquals(1, count);
    }
}