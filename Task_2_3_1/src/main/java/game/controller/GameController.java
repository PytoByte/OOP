package game.controller;

import game.model.GameWorld;
import game.view.GameView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;

/**
 * Основной контроллер игры, управляющий другими контроллерами, моделью игры и её сценой.
 */
public class GameController {
    private final GameWorld world;
    private final GameView view;
    private final SceneController sceneController;
    private final List<Controller> controllers = new ArrayList<>();
    private final GameTimer timer;

    /**
     * Базовый конструктор класса.
     *
     * @param world модель игрового мира.
     * @param view отрисовка игрового мира.
     * @param sceneController сцена игры.
     * @param timer абстракция таймера для управления игровыми тактами.
     */
    public GameController(
            GameWorld world,
            GameView view,
            SceneController sceneController,
            GameTimer timer
    ) {
        this.world = world;
        this.view = view;
        this.sceneController = sceneController;
        this.timer = timer;

        this.timer.setOnTick(this::tick);
    }

    /**
     * Добавить контроллер.
     *
     * @param controller новый контроллер.
     */
    public void addController(Controller controller) {
        controllers.add(controller);
    }

    /**
     * Запуск игры.
     *
     * @param scene сцена, которая будет реагировать на события, поставленные игровыми
     *     контроллерами.
     */
    public void start(Scene scene) {
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timer.play();
    }

    /**
     * Воспроизведение такта игры.
     * Обновляет состояние всех контроллеров, модели, проверяет условия конца игры и перерисовывает
     *     экран.
     */
    public void tick() {
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
     * Сбрасывает состояние контроллеров и модели, после чего запускает таймер.
     */
    public void restart() {
        for (Controller controller : controllers) {
            controller.restart();
        }
        world.restart();
        timer.play();
    }

    /**
     * Остановка игры.
     * Останавливает игровой таймер.
     */
    public void stop() {
        timer.stop();
    }
}