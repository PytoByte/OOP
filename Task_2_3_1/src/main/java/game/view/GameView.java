package game.view;

import game.model.GameModel;
import game.model.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.LinkedList;

public class GameView {
    private final LinkedList<View> views = new LinkedList<>();
    private final GraphicsContext gc;
    private final GameModel gameModel;

    public GameView(GameModel gameModel, GraphicsContext gc) {
        this.gameModel = gameModel;
        this.gc = gc;
    }

    public void addView(View view) {
        views.add(view);
    }

    public void render() {
        // Фон
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameModel.getWidth() * gameModel.getTileSize(), gameModel.getHeight() * gameModel.getTileSize());

        for (View view : views) {
            for (Pair<Point, Color> pixel : view.getView()) {
                Point p = pixel.getKey();
                if (p.getX() < 0 || p.getY() < 0) {
                    continue;
                }

                gc.setFill(pixel.getValue());
                gc.fillRect(p.getX() * gameModel.getTileSize(), p.getY() * gameModel.getTileSize(), gameModel.getTileSize() - 1, gameModel.getTileSize() - 1);
            }
        }

        // Сообщения
//        if (model.isGameOver()) {
//            gc.setFill(Color.WHITE);
//            gc.setFont(new Font(40));
//            gc.fillText("GAME OVER", (legacy..getWidth() * TILE_SIZE) / 4.0,
//                    (legacy.HEIGHT * TILE_SIZE) / 2.0);
//        } else if (model.isGameWin()) {
//            gc.setFill(Color.YELLOW);
//            gc.setFont(new Font(40));
//            gc.fillText("YOU WIN!", (legacy..getWidth() * TILE_SIZE) / 3.0,
//                    (legacy.HEIGHT * TILE_SIZE) / 2.0);
//        }
    }
}