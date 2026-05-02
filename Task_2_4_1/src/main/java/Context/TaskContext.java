package Context;

import Domain.Checkpoint;
import Domain.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskContext {
    private final Task task;

    public String title;
    public int maxPoints;

    public TaskContext(Task task) {
        this.task = task;
    }

    public void checkpoint(String name, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        task.addCheckpoint(new Checkpoint(name, LocalDate.parse(date, formatter)));
    }
}
