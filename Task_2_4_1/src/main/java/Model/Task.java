package Model;

import java.util.List;

/**
 * Task model.
 *
 * @param id unique task id
 * @param title task title
 * @param basePoints points awarded for completing the task, without checkpoint rewards
 * @param checkpoints list of checkpoints with deadlines
 */
public record Task(String id, String title, float basePoints, List<Checkpoint> checkpoints) {
}
