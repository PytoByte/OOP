package game;

import game.controller.FoodController;
import game.controller.GameController;
import game.controller.SceneController;
import game.controller.SnakeController;
import game.model.Direction;
import game.model.Food;
import game.model.GameWorld;
import game.model.Snake;
import game.view.FoodView;
import game.view.GameView;
import game.view.SnakeView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Главный класс приложения JavaFX, отвечающий за инициализацию и запуск игры.
 * Выполняет загрузку UI из FXML, создает основные компоненты игры (модели, контроллеры, виды)
 * и связывает их между собой.
 */
public class SnakeGame extends Application {

    /**
     * Точка входа в JavaFX приложение.
     * Настраивает сцену, холст с автоматическим изменением размера и инициализирует
     * игровые объекты (Змейку, Еду и закомментированные Стены).
     *
     * @param primaryStage основное окно приложения.
     * @throws Exception если возникли ошибки при загрузке FXML-файла.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загрузка интерфейса из файла ресурсов
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));

        // Инициализация контроллера сцены и базовой модели игры
        SceneController sceneController = loader.getController();
        GameWorld gameWorld = new GameWorld(8, 8, 10);

        StackPane holder = sceneController.getCanvasHolder();
        Canvas canvas = sceneController.getCanvas();

        // Создание главных управляющих компонентов
        GameView gameView = new GameView(gameWorld, canvas);
        GameController gameController = new GameController(gameWorld, gameView, sceneController);

        // Передача метода перезапуска в UI-контроллер
        sceneController.setOnRestart(gameController::restart);

        // Настройка слушателей для адаптивного изменения размера холста
        holder.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            gameView.render();
        });

        holder.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            gameView.render();
        });

        // Сборка модуля "Змейка"
        Snake snake = new Snake(
                gameWorld.getWidth() / 2 - 1,
                gameWorld.getHeight() / 2 - 1,
                2,
                Direction.RIGHT
        );
        SnakeController snakeController = new SnakeController(gameWorld, snake);
        SnakeView snakeView = new SnakeView(gameWorld, snake);

        gameController.addController(snakeController);
        gameView.addView(snakeView);

        // Сборка модуля "Еда"
        Food food = new Food(3);
        FoodController foodController = new FoodController(gameWorld, food);
        FoodView foodView = new FoodView(gameWorld, food);

        gameController.addController(foodController);
        gameView.addView(foodView);

        // Инициализация графического окна
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        // Запуск игрового цикла
        gameController.start(scene);
    }
}
