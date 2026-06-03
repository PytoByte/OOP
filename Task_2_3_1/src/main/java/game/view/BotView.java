package game.view;

import game.model.Bot;
import game.model.ConstPoint;
import game.model.Pair;
import game.model.SnakePart;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.LinkedList;

public class BotView implements View {
    Bot bot;
    Color headColor = Color.ORANGE;
    Color bodyColor = Color.BROWN;

    /**
     * Базовый конструктор класса.
     *
     * @param bot модель бота.
     */
    public BotView(Bot bot) {
        this.bot = bot;
    }

    @Override
    public Iterable<Pair<ConstPoint, Color>> getView() {
        if (!bot.isAlive()) {
            return Collections.emptyList();
        }

        LinkedList<Pair<ConstPoint, Color>> view = new LinkedList<>();

        for (Pair<ConstPoint, SnakePart> pointRender : bot.getRenderData()) {
            Color color;
            switch (pointRender.value()) {
                case HEAD -> color = headColor;
                case BODY -> color = bodyColor;
                default -> {
                    System.err.printf("Unexpected snake part %s\n", pointRender.value());
                    color = errorColor;
                }
            }

            view.add(new Pair<>(pointRender.key(), color));
        }

        return view;
    }
}
