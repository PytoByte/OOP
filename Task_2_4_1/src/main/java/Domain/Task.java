package Domain;

import java.util.LinkedList;
import java.util.List;

public class Task {
    private final String id;
    private String title = "Unnamed";
    private int maxPoints = 0;
    private final List<Checkpoint> checkpoints = new LinkedList<>();

    public Task(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }

    public List<Checkpoint> getCheckpoints() {
        return new LinkedList<>(checkpoints);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", maxPoints=" + maxPoints +
                ", checkpoints=" + checkpoints +
                '}';
    }
}
