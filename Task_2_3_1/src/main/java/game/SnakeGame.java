package game;

import game.model.Snake;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SnakeGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameModel gameModel = new GameModel();

        Canvas canvas = new Canvas(gameModel.WIDTH * 30, gameModel.HEIGHT * 30);
        Scene scene = new Scene(new StackPane(canvas));
        GameView gameView = new GameView(gameModel, canvas.getGraphicsContext2D());

        GameController gameController = new GameController(gameView);

        primaryStage.setTitle("SnakeGame");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        gameController.start(scene);
    }
}