package game.controller;

import game.model.Collider;
import game.model.GameModel;
import game.model.Point;
import game.view.GameView;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * Главный контроллер игры, координирующий работу всех подсистем.
 * Управляет игровым циклом (таймером), вызывает обновления дочерних контроллеров,
 * проверяет коллизии между объектами и отслеживает условия победы или поражения.
 */
public class GameController {
    private final ArrayList<Controller> controllers = new ArrayList<>();
    private final GameModel model;
    private final GameView view;
    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(200), e -> tick())
    );
    private final SceneController sceneController;

    /**
     * Создает основной контроллер игры и настраивает бесконечный цикл таймера.
     *
     * @param model общая модель данных игры.
     * @param view объект для отрисовки графики.
     * @param sceneController контроллер графического интерфейса пользователя.
     */
    public GameController(GameModel model, GameView view, SceneController sceneController) {
        this.model = model;
        this.view = view;
        this.sceneController = sceneController;
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Регистрирует новый дочерний контроллер (например, змейки или еды) в системе.
     *
     * @param controller объект, реализующий интерфейс {@link Controller}.
     */
    public void addController(Controller controller) {
        controllers.add(controller);
    }

    /**
     * Проверяет пересечение всех объектов, поддерживающих интерфейс {@link Collider}.
     * В случае обнаружения совпадения координат вызывает
     * метод collide у соответствующих контроллеров.
     *
     * @return true, если в текущем кадре было зафиксировано хотя бы одно столкновение.
     */
    private boolean checkCollisions() {
        boolean detectedCollisions = false;
        for (int i = 0; i < controllers.size() - 1; i++) {
            for (int j = i + 1; j < controllers.size(); j++) {
                Controller controller1 = controllers.get(i);
                Controller controller2 = controllers.get(j);

                if (!(controller1 instanceof ColliderControl colliderControl1
                        && controller2 instanceof ColliderControl colliderControl2)) {
                    continue;
                }

                Object model1 = controller1.getModel();
                Object model2 = controller2.getModel();

                if (!(model1 instanceof Collider collider1
                        && model2 instanceof Collider collider2)) {
                    continue;
                }

                for (Point p1 : collider1.getCollider()) {
                    for (Point p2 : collider2.getCollider()) {
                        if (p1.equals(p2)) {
                            colliderControl1.collide(collider2, p1);
                            colliderControl2.collide(collider1, p2);
                            detectedCollisions = true;
                        }
                    }
                }
            }
        }
        return detectedCollisions;
    }

    /**
     * Сбрасывает состояние игры и всех контроллеров к начальным значениям.
     * Обнуляет счет, флаги состояния и перезапускает игровой таймер.
     */
    public void restart() {
        model.setScore(0);
        model.setGameOver(false);
        model.setGameWin(false);
        for (Controller controller : controllers) {
            controller.restart();
        }
        timeline.play();
    }

    /**
     * Основной метод "тиков" таймера. Вызывается каждые 200мс.
     * Обновляет логику всех контроллеров, проверяет коллизии, обновляет UI
     * и инициирует отрисовку кадра.
     */
    private void tick() {
        for (Controller controller : controllers) {
            controller.update();
        }

        if (model.getGameOver()) {
            stop();
            sceneController.showGameOver(false);
            return;
        }

        while (checkCollisions()) {
            sceneController.updateScore(model.getScore());

            if (model.getGameOver()) {
                stop();
                sceneController.showGameOver(false);
                return;
            }

            if (model.getScore() >= model.getScoreToWin() || model.getGameWin()) {
                stop();
                sceneController.showGameOver(true);
                view.render();
                return;
            }
        }

        view.render();

        if (model.getScore() >= model.getScoreToWin() || model.getGameWin()) {
            stop();
            sceneController.showGameOver(true);
        }
    }

    /**
     * Запускает игровой процесс.
     * Регистрирует события ввода и запускает таймер.
     *
     * @param scene сцена JavaFX для регистрации слушателей событий.
     */
    public void start(Scene scene) {
        for (Controller controller : controllers) {
            controller.setupEvents(scene);
        }
        timeline.play();
    }

    /**
     * Останавливает игровой таймер, фактически ставя игру на паузу.
     */
    public void stop() {
        timeline.stop();
    }
}
