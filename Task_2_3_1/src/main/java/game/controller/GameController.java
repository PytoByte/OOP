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

public class GameController {
    private final GameWorld world;
    private final GameView view;
    private final SceneController sceneController;

    // Снова храним список абстрактных контроллеров!
    private final List<Controller> controllers = new ArrayList<>();

    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(200), e -> {
                tick();
            })
    );

    public GameController(GameWorld world, GameView view, SceneController sceneController) {
        this.world = world;
        this.view = view;
        this.sceneController = sceneController;
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    public void start(Scene scene) {
        // Пробегаемся по всем контроллерам и вешаем события на сцену
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timeline.play();
    }

    private void tick() {
        // 1. Сначала обновляем все вспомогательные контроллеры (применяем буферы ввода)
        for (Controller controller : controllers) {
            controller.update();
        }

        // 2. Затем делаем тик мира (движение, коллизии)
        world.tick();

        // 3. Обновляем UI и проверяем статусы
        sceneController.updateScore(world.getScore());

        if (world.isGameOver()) {
            stop();
            sceneController.showGameOver(false);
        } else if (world.isGameWin()) {
            stop();
            sceneController.showGameOver(true);
        }

        view.render();
    }

    public void restart() {
        world.restart();
        for (Controller controller : controllers) {
            controller.restart();
        }
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}