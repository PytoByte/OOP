package game;

import game.controller.FoodController;
import game.controller.SnakeController;
import game.model.Direction;
import game.model.Food;
import game.model.Snake;
import game.view.FoodView;
import game.view.SnakeView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SnakeGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameModel gameModel = new GameModel(20, 20, 20*20);

        Canvas canvas = new Canvas(
                gameModel.getWidth() * gameModel.getTileSize(),
                gameModel.getHeight() * gameModel.getTileSize());
        Scene scene = new Scene(new StackPane(canvas));
        GameView gameView = new GameView(gameModel, canvas.getGraphicsContext2D());

        GameController gameController = new GameController(gameModel, gameView);

        primaryStage.setTitle("SnakeGame");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Snake snake = new Snake(5, 5, 1, Direction.RIGHT);
        SnakeController snakeController = new SnakeController(gameModel, snake);
        SnakeView snakeView = new SnakeView(gameModel, snake);
        gameController.addController(snakeController);
        gameView.addView(snakeView);

        Food food = new Food(3);
        FoodController foodController = new FoodController(gameModel, food);
        FoodView foodView = new FoodView(gameModel, food);
        gameController.addController(foodController);
        gameView.addView(foodView);

        gameController.start(scene);
    }
}