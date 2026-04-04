package game;

import game.controller.FoodController;
import game.controller.GameController;
import game.controller.SceneController;
import game.controller.SnakeController;
import game.model.Direction;
import game.model.Food;
import game.model.GameModel;
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

public class SnakeGame extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));

        Parent root = loader.load();

        SceneController sceneController = loader.getController();

        GameModel gameModel = new GameModel(8, 8, 10);

        StackPane holder = sceneController.getCanvasHolder();
        Canvas canvas = sceneController.getCanvas();

        GameView gameView = new GameView(gameModel, canvas);
        GameController gameController = new GameController(gameModel, gameView, sceneController);
        sceneController.setOnRestart(gameController::restart);

        holder.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            gameView.render();
        });

        holder.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            gameView.render();
        });

        Snake snake = new Snake(
                gameModel.getWidth() / 2 - 1,
                gameModel.getHeight() / 2 - 1,
                2,
                Direction.RIGHT
        );
        SnakeController snakeController = new SnakeController(gameModel, snake);
        SnakeView snakeView = new SnakeView(gameModel, snake);
        gameController.addController(snakeController);
        gameView.addView(snakeView);

        Food food = new Food(3);
        FoodController foodController = new FoodController(gameModel, food);
        FoodView foodView = new FoodView(gameModel, food);
        gameController.addController(foodController);
        gameView.addView(foodView);

//        Walls walls = new Walls();
//        WallsController wallsController = new WallsController(gameModel, walls);
//        WallsView wallsView = new WallsView(gameModel, walls);
//        gameController.addController(wallsController);
//        gameView.addView(wallsView);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        // Запускаем игру
        gameController.start(scene);
    }
}