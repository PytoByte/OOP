package game.view;

import game.model.GameModel;
import game.model.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.util.LinkedList;

/**
 * Главный класс отрисовки игрового мира.
 * Рассчитывает масштаб игровых ячеек относительно размеров холста, рисует фоновую
 * сетку ("шахматку") и последовательно отображает все зарегистрированные виды (views).
 */
public class GameView {
    private final LinkedList<View> views = new LinkedList<>();
    private final Canvas canvas;
    private final GameModel gameModel;

    /**
     * Создает объект отрисовки, привязанный к конкретной модели и холсту JavaFX.
     * @param gameModel модель игры для получения логических размеров поля.
     * @param canvas графический холст, на котором будет происходить отрисовка.
     */
    public GameView(GameModel gameModel, Canvas canvas) {
        this.gameModel = gameModel;
        this.canvas = canvas;
    }

    /**
     * Добавляет визуальный слой (например, вид змейки или еды) в список отрисовки.
     * @param view объект, реализующий интерфейс {@link View}.
     */
    public void addView(View view) {
        views.add(view);
    }

    /**
     * Основной метод рендеринга кадра.
     * Выполняет следующие действия:
     * 1. Рассчитывает оптимальный размер ячейки (tileSize).
     * 2. Вычисляет отступы (offset) для центрирования поля на холсте.
     * 3. Очищает холст и рисует шахматный фон.
     * 4. Отрисовывает все объекты из зарегистрированных видов, игнорируя точки вне поля.
     */
    public void render() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // Определение размера плитки по минимальной стороне, чтобы поле вписалось в холст
        double tileSize = Math.min(
                canvasWidth / gameModel.getWidth(),
                canvasHeight / gameModel.getHeight()
        );

        double actualFieldWidth = gameModel.getWidth() * tileSize;
        double actualFieldHeight = gameModel.getHeight() * tileSize;

        // Расчет смещения для центрирования игрового поля
        double offsetX = (canvasWidth - actualFieldWidth) / 2;
        double offsetY = (canvasHeight - actualFieldHeight) / 2;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Заполнение общего фона
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        Color color1 = Color.DARKGREEN;
        Color color2 = Color.GREEN;

        // Отрисовка шахматной сетки игрового поля
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

        // Отрисовка всех слоев объектов (Змейка, Еда, Стены)
        for (View view : views) {
            for (Pair<Point, Color> pixel : view.getView()) {
                Point p = pixel.getKey();

                // Пропуск точек с некорректными координатами (например, -1, -1 при росте хвоста)
                if (p.getX() < 0 || p.getY() < 0 ||
                        p.getX() >= gameModel.getWidth() || p.getY() >= gameModel.getHeight()) {
                    continue;
                }

                gc.setFill(pixel.getValue());

                double x = offsetX + p.getX() * tileSize;
                double y = offsetY + p.getY() * tileSize;

                // Отрисовка ячейки с небольшим зазором (tileSize - 1) для визуального разделения
                gc.fillRect(x, y, tileSize - 1, tileSize - 1);
            }
        }
    }
}
