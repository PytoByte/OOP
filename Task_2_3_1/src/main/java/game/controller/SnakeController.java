package game.controller;

import game.model.Collider;
import game.model.Direction;
import game.model.Food;
import game.model.GameModel;
import game.model.Point;
import game.model.Snake;
import game.model.Walls;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Контроллер управления змейкой.
 * Реализует логику перемещения, обработки ввода с клавиатуры,
 * проверки столкновения с самим собой и реакции на игровые события (еда, стены).
 */
public class SnakeController implements Controller, ColliderControl {
    Snake snake;
    GameModel gameModel;
    Direction directionBuffer;

    /**
     * Создает контроллер змейки и инициализирует начальное состояние.
     *
     * @param gameModel общая модель игры для доступа к параметрам поля и состоянию игры.
     * @param snake объект змейки, которым управляет данный контроллер.
     */
    public SnakeController(GameModel gameModel, Snake snake) {
        this.snake = snake;
        this.gameModel = gameModel;
        this.directionBuffer = snake.getDirection();
        restart();
    }

    /**
     * Основной цикл обновления состояния змейки.
     * Рассчитывает новую позицию головы с учетом циклического переноса через границы поля,
     * перемещает все сегменты тела и проверяет столкновение головы с хвостом ("самоубийство").
     */
    public void update() {
        snake.setDirection(directionBuffer);

        Point head = snake.getHead();
        Point nextPos = new Point(head.getCoordX(), head.getCoordY());

        // Расчет координат следующего шага с учетом бесконечного поля (wrap-around)
        switch (snake.getDirection()) {
            case UP ->
                    nextPos.setCoordY(
                            (head.getCoordY() - 1 + gameModel.getHeight()) % gameModel.getHeight()
                    );
            case DOWN ->
                    nextPos.setCoordY((head.getCoordY() + 1) % gameModel.getHeight());
            case LEFT ->
                    nextPos.setCoordX(
                            (head.getCoordX() - 1 + gameModel.getWidth()) % gameModel.getWidth()
                    );
            case RIGHT ->
                    nextPos.setCoordX((head.getCoordX() + 1) % gameModel.getWidth());
            default ->
                System.err.printf("Unexpected snake direction %s\n", snake.getDirection());
        }

        // Логика перемещения сегментов тела (каждый сегмент встает на место предыдущего)
        for (Point body : snake.getPoints()) {
            int oldX = body.getCoordX();
            body.setCoordX(nextPos.getCoordX());

            int oldY = body.getCoordY();
            body.setCoordY((nextPos.getCoordY()));

            nextPos.setCoordX(oldX);
            nextPos.setCoordY(oldY);

            // Проверка на столкновение головы с телом
            if (body != head && body.equals(head)) {
                System.out.println("Suicide");
                gameModel.setGameOver(true);
            }
        }
    }

    /**
     * Сбрасывает состояние змейки к начальному.
     * Очищает список точек и создает змейку стартового размера в начальных координатах.
     */
    @Override
    public void restart() {
        snake.getPoints().clear();
        snake.increaseBody(new Point(snake.getStartX(), snake.getStartY()));
        for (int i = 0; i < snake.getStartSize() - 1; i++) {
            snake.increaseBody(new Point(-1, -1));
        }
    }

    @Override
    public Object getModel() {
        return snake;
    }

    /**
     * Настраивает обработку событий клавиатуры для изменения направления движения.
     *
     * @param scene объект {@link Scene}, к которому привязывается слушатель нажатий.
     */
    @Override
    public void setupEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            switch (code) {
                case UP -> directionBuffer = Direction.UP;
                case DOWN -> directionBuffer = Direction.DOWN;
                case LEFT -> directionBuffer = Direction.LEFT;
                case RIGHT -> directionBuffer = Direction.RIGHT;
                default -> System.err.println("Skip unused button");
            }
        });
    }

    /**
     * Обрабатывает столкновение змейки с другими объектами.
     * При столкновении с едой змейка растет. При столкновении со стеной — конец игры.
     *
     * @param model объект, с которым произошло столкновение.
     * @param p точка столкновения.
     */
    @Override
    public void collide(Collider model, Point p) {
        if (model instanceof Food) {
            Point head = snake.getHead();
            if (p.equals(head)) {
                snake.increaseBody(new Point(-1, -1));
            }
        } else if (model instanceof Walls) {
            gameModel.setGameOver(true);
        }
    }
}
