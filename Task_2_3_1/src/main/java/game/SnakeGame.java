package game;

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
 * Главный класс приложения, отвечающий за инициализацию и запуск игры.
 */
public class SnakeGame extends Application {

    /**
     * Инициализация и запуск игры.
     *
     * @param primaryStage основное окно приложения.
     * @throws Exception если возникли ошибки при загрузке FXML-файла.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        SceneController sceneController = loader.getController();
        Canvas canvas = sceneController.getCanvas();
        GameWorld gameWorld = new GameWorld(8, 8, 10);
        GameView gameView = new GameView(gameWorld, canvas);
        GameController gameController = new GameController(gameWorld, gameView, sceneController);

        sceneController.setOnRestart(gameController::restart);

        StackPane holder = sceneController.getCanvasHolder();
        holder.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            gameView.render();
        });
        holder.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            gameView.render();
        });

        Snake snake = new Snake(
                gameWorld.getWidth() / 2 - 1,
                gameWorld.getHeight() / 2 - 1,
                2,
                Direction.RIGHT,
                gameWorld
        );
        SnakeController snakeController = new SnakeController(snake);
        SnakeView snakeView = new SnakeView(snake);
        gameWorld.addModel(snake);
        gameController.addController(snakeController);
        gameView.addView(snakeView);

        Food food = new Food(3, gameWorld);
        FoodView foodView = new FoodView(food);
        gameWorld.addModel(food);
        gameView.addView(foodView);

        primaryStage.show();
        gameController.start(scene);
    }
}
