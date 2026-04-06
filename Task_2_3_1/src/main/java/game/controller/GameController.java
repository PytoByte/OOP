package game.controller;

import game.model.GameWorld;
import game.view.GameView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;

public class GameController {
    private final GameWorld world;
    private final GameView view;
    private final SceneController sceneController;
    private final List<Controller> controllers = new ArrayList<>();
    private final GameTimer timer;

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

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    public void start(Scene scene) {
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timer.play();
    }

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

    public void restart() {
        for (Controller controller : controllers) {
            controller.restart();
        }
        world.restart();
        timer.play();
    }

    public void stop() {
        timer.stop();
    }
}