package Context;

import Model.Checkpoint;
import Model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Context for "task" function in gradle.
 */
public class TaskContext {
    public final String id;
    public String title = "Unnamed";
    public float basePoints = 1;
    List<Checkpoint> checkpoints = new LinkedList<>();

    /**
     * Default constructor.
     *
     * @param id unique task id
     */
    public TaskContext(String id) {
        this.id = id;
    }

    /**
     * Adds a task checkpoint with a specified deadline.
     *
     * @param name the name of the checkpoint
     * @param date the deadline in dd-MM-yyyy format
     * @param rewardPoints the points awarded for completing the checkpoint
     */
    public void checkpoint(String name, String date, float rewardPoints) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        checkpoints.add(new Checkpoint(name, LocalDate.parse(date, formatter), rewardPoints));
    }

    /**
     * Produce task model.
     *
     * @return task model
     */
    public Task produce() {
        return new Task(id, title, basePoints, List.copyOf(checkpoints));
    }
}
