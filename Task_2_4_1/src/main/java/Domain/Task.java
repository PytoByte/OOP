package Domain;

import java.util.List;

public record Task(String id, String title, float basePoints, List<Checkpoint> checkpoints) {
}
