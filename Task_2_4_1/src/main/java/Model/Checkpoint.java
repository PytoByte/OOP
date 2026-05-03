package Model;

import java.time.LocalDate;

/**
 * Checkpoint model.
 *
 * @param name the name of the checkpoint
 * @param date the deadline date
 * @param rewardPoints the points awarded for completing the checkpoint
 */
public record Checkpoint(String name, LocalDate date, float rewardPoints) {

}
