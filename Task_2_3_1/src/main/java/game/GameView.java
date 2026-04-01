package game;

import game.model.Point;
import game.view.View;
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
        gc.fillRect(0, 0, gameModel.WIDTH * gameModel.TILE_SIZE, gameModel.HEIGHT * gameModel.TILE_SIZE);

        for (View view : views) {
            for (Pair<Point, Color> pixel : view.getView()) {
                Point p = pixel.getKey();
                gc.setFill(pixel.getValue());
                gc.fillRect(p.getX() * gameModel.TILE_SIZE, p.getY() * gameModel.TILE_SIZE, gameModel.TILE_SIZE - 1, gameModel.TILE_SIZE - 1);
            }
        }

        // Сообщения
//        if (model.isGameOver()) {
//            gc.setFill(Color.WHITE);
//            gc.setFont(new Font(40));
//            gc.fillText("GAME OVER", (legacy.WIDTH * TILE_SIZE) / 4.0,
//                    (legacy.HEIGHT * TILE_SIZE) / 2.0);
//        } else if (model.isGameWin()) {
//            gc.setFill(Color.YELLOW);
//            gc.setFont(new Font(40));
//            gc.fillText("YOU WIN!", (legacy.WIDTH * TILE_SIZE) / 3.0,
//                    (legacy.HEIGHT * TILE_SIZE) / 2.0);
//        }
    }
}