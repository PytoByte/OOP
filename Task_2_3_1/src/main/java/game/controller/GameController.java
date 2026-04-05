package game.controller;

import game.model.GameWorld;
import game.view.GameView;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * Основной контроллер игры, управляющий другими контроллерами, моделью игры и её сценой.
 */
public class GameController {
    private final GameWorld world;
    private final GameView view;
    private final SceneController sceneController;
    private final List<Controller> controllers = new ArrayList<>();

    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(200), e -> {
                tick();
            })
    );

    /**
     * Базовый конструктор класса.
     *
     * @param world модель игрового мира
     * @param view отрисовка игрового мира
     * @param sceneController сцена игры
     */
    public GameController(GameWorld world, GameView view, SceneController sceneController) {
        this.world = world;
        this.view = view;
        this.sceneController = sceneController;
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Добавить контроллер.
     *
     * @param controller новый контроллер
     */
    public void addController(Controller controller) {
        controllers.add(controller);
    }

    /**
     * Запуск игры.
     *
     * @param scene сцена, которая будет реагировать на события поставленные игровыми контроллерами.
     */
    public void start(Scene scene) {
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timeline.play();
    }

    /**
     * Воспроизведение такта игры.
     */
    private void tick() {
        for (Controller controller : controllers) {
            controller.tick();
        }

        world.tick();

        sceneController.updateScore(world.getScore());

        if (world.isGameOver()) {
            stop();
            sceneController.showGameOver(false);
            return;
        } else if (world.isGameWin()) {
            stop();
            sceneController.showGameOver(true);
        }

        view.render();
    }

    /**
     * Перезапуск игры.
     */
    public void restart() {
        for (Controller controller : controllers) {
            controller.restart();
        }
        world.restart();
        timeline.play();
    }

    /**
     * Остановка игры.
     */
    public void stop() {
        timeline.stop();
    }
}