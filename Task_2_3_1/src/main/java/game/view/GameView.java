package game.view;

import game.model.GameModel;
import game.model.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.LinkedList;

public class GameView {
    private final LinkedList<View> views = new LinkedList<>();
    private final Canvas canvas;
    private final GameModel gameModel;

    public GameView(GameModel gameModel, Canvas canvas) {
        this.gameModel = gameModel;
        this.canvas = canvas;
    }

    public void addView(View view) {
        views.add(view);
    }

    public void render() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        double tileSize = Math.min(
                canvasWidth / gameModel.getWidth(),
                canvasHeight / gameModel.getHeight()
        );

        double actualFieldWidth = gameModel.getWidth() * tileSize;
        double actualFieldHeight = gameModel.getHeight() * tileSize;

        double offsetX = (canvasWidth - actualFieldWidth) / 2;
        double offsetY = (canvasHeight - actualFieldHeight) / 2;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        Color color1 = Color.DARKGREEN;
        Color color2 = Color.GREEN;

        for (int x = 0; x < gameModel.getWidth(); x++) {
            for (int y = 0; y < gameModel.getHeight(); y++) {
                if ((x + y) % 2 == 0) {
                    gc.setFill(color1);
                } else {
                    gc.setFill(color2);
                }

                gc.fillRect(offsetX + x * tileSize, offsetY + y * tileSize, tileSize, tileSize);
            }
        }

        for (View view : views) {
            for (Pair<Point, Color> pixel : view.getView()) {
                Point p = pixel.getKey();

                if (p.getX() < 0 || p.getY() < 0 ||
                        p.getX() >= gameModel.getWidth() || p.getY() >= gameModel.getHeight()) {
                    continue;
                }

                gc.setFill(pixel.getValue());

                double x = offsetX + p.getX() * tileSize;
                double y = offsetY + p.getY() * tileSize;

                gc.fillRect(x, y, tileSize - 1, tileSize - 1);
            }
        }
    }
}