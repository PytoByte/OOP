package game;

import game.controller.ColliderControl;
import game.controller.Controller;
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
    private final GameView view;
    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(200), e -> tick())
    );

    public GameController(GameView view) {
        this.view = view;
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
                if (!(controller1 instanceof ColliderControl &&
                        controller2 instanceof ColliderControl)) {
                    continue;
                }

                Object model1 = controller1.getModel();
                Object model2 = controller2.getModel();
                if (!(model1 instanceof Collider &&
                        model2 instanceof Collider)) {
                    continue;
                }

                for (Point p1 : ((Collider) model1).getCollider()) {
                    for (Point p2 : ((Collider) model2).getCollider()) {
                        if (p1 == p2) {
                            ((ColliderControl) controller1).collide(model2);
                            ((ColliderControl) controller2).collide(model1);
                        }
                    }
                }
            }
        }
    }

    private void tick() {
        for (Controller controller : controllers) {
            controller.update();
        }
        checkCollisions();
        view.render();
    }

    public void start(Scene scene) {
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timeline.play();
    }
}
