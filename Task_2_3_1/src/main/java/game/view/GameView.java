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

        // 1. Неигровое пространство (сплошной темный цвет)
        gc.setFill(Color.GREEN); // Глубокий серый/черный
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // 2. Рисуем шахматное игровое поле
        Color color1 = Color.DARKGREEN; // Темная клетка
        Color color2 = Color.GREEN; // Светлая клетка

        for (int x = 0; x < gameModel.getWidth(); x++) {
            for (int y = 0; y < gameModel.getHeight(); y++) {
                // Математика шахмат: если сумма координат четная — один цвет, иначе другой
                if ((x + y) % 2 == 0) {
                    gc.setFill(color1);
                } else {
                    gc.setFill(color2);
                }

                // Рисуем клетку с учетом смещения (offset)
                gc.fillRect(offsetX + x * tileSize, offsetY + y * tileSize, tileSize, tileSize);
            }
        }

        // 4. Отрисовка объектов
        for (View view : views) {
            for (Pair<Point, Color> pixel : view.getView()) {
                Point p = pixel.getKey();

                // Проверка границ (на всякий случай)
                if (p.getX() < 0 || p.getY() < 0 ||
                        p.getX() >= gameModel.getWidth() || p.getY() >= gameModel.getHeight()) {
                    continue;
                }

                gc.setFill(pixel.getValue());

                // Вычисляем финальные координаты с учетом смещения
                double x = offsetX + p.getX() * tileSize;
                double y = offsetY + p.getY() * tileSize;

                // Рисуем тайл (tileSize - 1 создает эффект сетки)
                gc.fillRect(x, y, tileSize - 1, tileSize - 1);
            }
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