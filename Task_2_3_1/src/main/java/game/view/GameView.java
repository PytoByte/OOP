package game.view;

import game.model.ConstPoint;
import game.model.GameWorld;
import game.model.Pair;
import java.util.LinkedList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Класс отрисовки игрового мира.
 */
public class GameView {
    private final LinkedList<View> views = new LinkedList<>();
    private final Canvas canvas;
    private final GameWorld gameWorld;

    /**
     * Базовый конструктор класса.
     *
     * @param gameWorld модель игры для получения логических размеров поля.
     * @param canvas холст, на котором будет происходить отрисовка.
     */
    public GameView(GameWorld gameWorld, Canvas canvas) {
        this.gameWorld = gameWorld;
        this.canvas = canvas;
    }

    /**
     * Добавляет визуальный слой в список отрисовки.
     *
     * @param view визуальное представление объекта.
     */
    public void addView(View view) {
        views.add(view);
    }

    /**
     * Отрисовка кадра.
     */
    public void render() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        double tileSize = Math.min(
                canvasWidth / gameWorld.getWidth(),
                canvasHeight / gameWorld.getHeight()
        );

        double actualFieldWidth = gameWorld.getWidth() * tileSize;
        double actualFieldHeight = gameWorld.getHeight() * tileSize;

        double offsetX = (canvasWidth - actualFieldWidth) / 2;
        double offsetY = (canvasHeight - actualFieldHeight) / 2;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        Color color1 = Color.DARKGREEN;
        Color color2 = Color.GREEN;

        for (int x = 0; x < gameWorld.getWidth(); x++) {
            for (int y = 0; y < gameWorld.getHeight(); y++) {
                if ((x + y) % 2 == 0) {
                    gc.setFill(color1);
                } else {
                    gc.setFill(color2);
                }

                gc.fillRect(offsetX + x * tileSize,
                        offsetY + y * tileSize,
                        tileSize,
                        tileSize
                );
            }
        }

        for (View view : views) {
            for (Pair<ConstPoint, Color> pixel : view.getView()) {
                ConstPoint p = pixel.key();

                if (p.getX() < 0 || p.getY() < 0
                        || p.getX() >= gameWorld.getWidth()
                        || p.getY() >= gameWorld.getHeight()) {
                    continue;
                }

                gc.setFill(pixel.value());

                double x = offsetX + p.getX() * tileSize;
                double y = offsetY + p.getY() * tileSize;

                gc.fillRect(x, y, tileSize - 1, tileSize - 1);
            }
        }
    }
}
