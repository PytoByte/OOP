package game.controller;

import game.model.Point;

public class FoodController {
    private void spawnFood() {
        while (true) {
            Point p = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
            if (snake.stream().noneMatch(s -> s.equals(p)) &&
                    obstacles.stream().noneMatch(o -> o.equals(p)) &&
                    food.stream().noneMatch(f -> f.equals(p))) {
                food.add(p);
                break;
            }
        }
    }
}
