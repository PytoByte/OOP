package game.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.model.ConstPoint;
import game.model.Food;
import game.model.GameWorld;
import game.model.Pair;
import java.util.Iterator;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodViewTest {
    private View foodView;

    @BeforeEach
    void setUp() {
        GameWorld world = new GameWorld(10, 10, 100);
        Food food = new Food(1, world);
        foodView = new FoodView(food);
    }

    @Test
    void testGetViewColor() {
        Iterable<Pair<ConstPoint, Color>> viewData = foodView.getView();
        Iterator<Pair<ConstPoint, Color>> iterator = viewData.iterator();

        assertTrue(iterator.hasNext());
        Pair<ConstPoint, Color> pair = iterator.next();
        assertEquals(new Color(1, 0, 0, 1), pair.value());
    }

    @Test
    void testGetViewSize() {
        int count = 0;
        for (Pair<ConstPoint, Color> pair : foodView.getView()) {
            count++;
        }
        assertEquals(1, count);
    }
}