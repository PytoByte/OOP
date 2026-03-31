package Game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends Application {

    // --- Настройки игры (N x M) ---
    private static final int WIDTH = 20;  // M (колонки)
    private static final int HEIGHT = 15; // N (строки)
    private static final int TILE_SIZE = 30;
    private static final int T = 3;       // Количество еды на поле
    private static final int L = 10;      // Длина для победы

    private final LinkedList<Point> snake = new LinkedList<>();
    private final List<Point> food = new ArrayList<>();
    private final List<Point> obstacles = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private final Random random = new Random();

    @Override
    public void start(Stage primaryStage) {
        initGame();

        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Игровой цикл (обновление каждые 0.2 секунды)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            update();
            draw(gc);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(new StackPane(canvas));

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.UP && direction != Direction.DOWN) {
                direction = Direction.UP;
            }
            if (code == KeyCode.DOWN && direction != Direction.UP) {
                direction = Direction.DOWN;
            }
            if (code == KeyCode.LEFT && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            }
            if (code == KeyCode.RIGHT && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            }
        });

        primaryStage.setTitle("JavaFX Snake: Win at length " + L);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initGame() {
        // 1. Начало: змейка из 1 звена в центре
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));

        // 2. Добавляем искусственные препятствия (для примера)
        obstacles.clear();
        obstacles.add(new Point(5, 5));
        obstacles.add(new Point(5, 6));
        obstacles.add(new Point(14, 10));

        // 3. Создаем T элементов еды
        food.clear();
        for (int i = 0; i < T; i++) {
            spawnFood();
        }
    }

    private void spawnFood() {
        while (true) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            Point p = new Point(x, y);

            // Проверка, чтобы еда не попала на змейку, препятствия или другую еду
            if (snake.stream().noneMatch(s -> s.equals(p)) &&
                    obstacles.stream().noneMatch(o -> o.equals(p)) &&
                    food.stream().noneMatch(f -> f.equals(p))) {
                food.add(p);
                break;
            }
        }
    }

    private void update() {
        if (gameOver || gameWin) return;

        // Определяем новую голову
        Point head = snake.getFirst();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, head.y - 1);
            case DOWN -> new Point(head.x, head.y + 1);
            case LEFT -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };

        // Проверка столкновения с границами или препятствиями (условие 4)
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT ||
                obstacles.stream().anyMatch(o -> o.equals(newHead)) ||
                snake.stream().anyMatch(s -> s.equals(newHead))) {
            gameOver = true;
            return;
        }

        // Проверка на еду (условие 6)
        boolean ate = false;
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).equals(newHead)) {
                food.remove(i);
                spawnFood();
                ate = true;
                break;
            }
        }

        snake.addFirst(newHead); // Добавляем звено к голове (условие 3)

        if (!ate) {
            snake.removeLast(); // Удаляем хвост, если не ели
        }

        // Проверка победы (условие 7)
        if (snake.size() >= L) {
            gameWin = true;
        }
    }

    private void draw(GraphicsContext gc) {
        // Фон
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Препятствия
        gc.setFill(Color.GRAY);
        for (Point p : obstacles) {
            gc.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
        }

        // Еда
        gc.setFill(Color.RED);
        for (Point p : food) {
            gc.fillOval(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
        }

        // Змейка
        for (int i = 0; i < snake.size(); i++) {
            gc.setFill(i == 0 ? Color.LIME : Color.GREEN); // Голова светлее
            Point p = snake.get(i);
            gc.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
        }

        // Сообщения
        if (gameOver) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(40));
            gc.fillText("GAME OVER", (WIDTH * TILE_SIZE) / 4.0, (HEIGHT * TILE_SIZE) / 2.0);
        } else if (gameWin) {
            gc.setFill(Color.YELLOW);
            gc.setFont(new Font(40));
            gc.fillText("YOU WIN!", (WIDTH * TILE_SIZE) / 3.0, (HEIGHT * TILE_SIZE) / 2.0);
        }
    }

    private enum Direction { UP, DOWN, LEFT, RIGHT }

    public static void main(String[] args) {
        launch(args);
    }
}