package game.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class SceneController {
    @FXML private Label scoreLabel;
    @FXML private Label statusLabel;
    @FXML private StackPane canvasHolder;
    @FXML private Canvas gameCanvas;
    @FXML private Button restartButton;

    private Runnable restartHandler; // Колбэк для логики перезапуска

    public void setOnRestart(Runnable handler) {
        this.restartHandler = handler;
    }

    @FXML
    private void handleRestart() {
        statusLabel.setVisible(false);
        if (restartHandler != null) {
            restartHandler.run();
        }
    }

    public void showGameOver(boolean win) {
        statusLabel.setText(win ? "YOU WIN!" : "GAME OVER");
        statusLabel.setTextFill(win ? Color.LIME : Color.RED);
        statusLabel.setVisible(true);
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    // Геттеры для Canvas и Holder (как были раньше)
    public Canvas getCanvas() { return gameCanvas; }
    public StackPane getCanvasHolder() { return canvasHolder; }
}