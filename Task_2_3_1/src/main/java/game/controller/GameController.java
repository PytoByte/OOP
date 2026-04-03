package game.controller;

import game.model.GameModel;
import game.view.GameView;
import game.model.Collider;
import game.model.Point;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.util.ArrayList;

public class GameController {
    private final ArrayList<Controller> controllers = new ArrayList<>();
    private final GameModel model;
    private final GameView view;
    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(200), e -> tick())
    );
    private final SceneController sceneController;

    public GameController(GameModel model, GameView view, SceneController sceneController) {
        this.model = model;
        this.view = view;
        this.sceneController = sceneController;
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    private void checkCollisions() {
        for (int i = 0; i < controllers.size() - 1; i++) {
            for (int j = i+1; j < controllers.size(); j++) {
                Controller controller1 = controllers.get(i);
                Controller controller2 = controllers.get(j);
                if (!(controller1 instanceof ColliderControl colliderControl1 &&
                        controller2 instanceof ColliderControl colliderControl2)) {
                    continue;
                }

                Object model1 = controller1.getModel();
                Object model2 = controller2.getModel();
                if (!(model1 instanceof Collider collider1 &&
                        model2 instanceof Collider collider2)) {
                    continue;
                }

                for (Point p1 : collider1.getCollider()) {
                    for (Point p2 : collider2.getCollider()) {
                        if (p1.equals(p2)) {
                            colliderControl1.collide(collider2, p1);
                            colliderControl2.collide(collider1, p2);
                        }
                    }
                }
            }
        }
    }

    public void restart() {
        model.setScore(0);
        for (Controller controller : controllers) {
            controller.restart();
        }
        timeline.play();
    }

    private void tick() {
        for (Controller controller : controllers) {
            controller.update();
        }
        checkCollisions();
        sceneController.updateScore(model.getScore());

        if (model.getGameOver()) {
            stop();
            sceneController.showGameOver(false);
            return;
        }

        view.render();

        if (model.getScore() >= model.getScoreToWin()) {
            stop();
            sceneController.showGameOver(true);
        }
    }

    public void start(Scene scene) {
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}
