package game.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Контроллер графического интерфейса (UI).
 * Управляет визуальными элементами сцены, такими как надписи счета,
 * сообщения о состоянии игры и холст для отрисовки.
 */
public class SceneController {
    @FXML private Label scoreLabel;
    @FXML private Label statusLabel;
    @FXML private StackPane canvasHolder;
    @FXML private Canvas gameCanvas;

    private Runnable restartHandler;

    /**
     * Устанавливает обработчик для события перезапуска игры.
     *
     * @param handler объект {@link Runnable}, который будет выполнен при нажатии кнопки рестарта.
     */
    public void setOnRestart(Runnable handler) {
        this.restartHandler = handler;
    }

    /**
     * Обрабатывает нажатие кнопки перезапуска в интерфейсе.
     * Скрывает сообщение о статусе и запускает зарегистрированный обработчик.
     */
    @FXML
    private void handleRestart() {
        statusLabel.setVisible(false);
        if (restartHandler != null) {
            restartHandler.run();
        }
    }

    /**
     * Отображает сообщение об окончании игры.
     *
     * @param win флаг, определяющий текст сообщения: "YOU WIN!" при true или "GAME OVER" при false.
     */
    public void showGameOver(boolean win) {
        statusLabel.setText(win ? "YOU WIN!" : "GAME OVER");
        statusLabel.setTextFill(win ? Color.LIME : Color.RED);
        statusLabel.setVisible(true);
    }

    /**
     * Обновляет текстовое поле со счетом игрока.
     *
     * @param score текущее количество набранных очков.
     */
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public Canvas getCanvas() {
        return gameCanvas;
    }

    public StackPane getCanvasHolder() {
        return canvasHolder;
    }
}
