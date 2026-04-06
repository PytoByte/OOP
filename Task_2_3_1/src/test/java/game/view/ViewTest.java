package game.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import game.model.Point;
import java.util.Collections;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

class ViewTest {

    @Test
    void testViewInterfaceConstants() {
        assertNotNull(View.errorColor);
        assertEquals(Color.BLACK, View.errorColor);
    }

    @Test
    void testViewImplementation() {
        View mockView = () -> Collections.singletonList(
                new Pair<>(new Point(1, 1), Color.RED)
        );

        assertNotNull(mockView.getView());
        Pair<Point, Color> firstElement = mockView.getView().iterator().next();

        assertEquals(new Point(1, 1), firstElement.getKey());
        assertEquals(Color.RED, firstElement.getValue());
    }
}