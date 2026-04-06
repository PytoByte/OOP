package game.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class JavafxTimer implements GameTimer {
    private final Timeline timeline;
    private final Duration duration;

    public JavafxTimer(Duration duration) {
        this.timeline = new Timeline();
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.duration = duration;
    }

    @Override
    public void setOnTick(Runnable action) {
        timeline.getKeyFrames().setAll(new KeyFrame(duration, e -> action.run()));
    }

    @Override
    public void play() {
        timeline.play();
    }

    @Override
    public void stop() {
        timeline.stop();
    }
}
