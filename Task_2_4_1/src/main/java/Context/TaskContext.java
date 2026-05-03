package Context;

import Domain.Checkpoint;
import Domain.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class TaskContext {
    public final String id;
    public String title = "Unnamed";
    public float basePoints = 1;
    List<Checkpoint> checkpoints = new LinkedList<>();

    public TaskContext(String id) {
        this.id = id;
    }

    public void checkpoint(String name, String date, float rewardPoints) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        checkpoints.add(new Checkpoint(name, LocalDate.parse(date, formatter), rewardPoints));
    }

    public Task produce() {
        return new Task(id, title, basePoints, List.copyOf(checkpoints));
    }
}
