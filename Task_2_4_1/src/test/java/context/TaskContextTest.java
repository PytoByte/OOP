package context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import model.Checkpoint;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskContextTest {
    private TaskContext context;
    private static final String TASK_ID = "lab1";

    @BeforeEach
    void setUp() {
        context = new TaskContext(TASK_ID);
    }

    @Test
    void testTaskProductionWithDefaultValues() {
        Task task = context.produce();

        assertEquals(TASK_ID, task.id());
        assertEquals("Unnamed", task.title());
        assertEquals(1.0f, task.basePoints());
        assertTrue(task.checkpoints().isEmpty());
    }

    @Test
    void testTaskProductionWithCustomValues() {
        context.title = "Memory Management";
        context.basePoints = 10.0f;
        context.checkpoint("Soft Deadline", "15-05-2026", 2.0f);

        Task task = context.produce();

        assertEquals("Memory Management", task.title());
        assertEquals(10.0f, task.basePoints());
        assertEquals(1, task.checkpoints().size());

        Checkpoint cp = task.checkpoints().get(0);
        assertEquals("Soft Deadline", cp.name());
        assertEquals(LocalDate.of(2026, 5, 15), cp.date());
        assertEquals(2.0f, cp.rewardPoints());
    }

    @Test
    void testInvalidDateFormatThrowsException() {
        assertThrows(DateTimeParseException.class, () -> {
            context.checkpoint("Fail", "2026-05-15", 1.0f);
        });
    }

    @Test
    void testCheckpointsAreImmutableInProducedTask() {
        context.checkpoint("CP1", "01-01-2026", 1.0f);
        Task task = context.produce();

        assertThrows(UnsupportedOperationException.class, () -> {
            task.checkpoints().clear();
        });
    }

    @Test
    void testMultipleCheckpointsPreserveOrder() {
        context.checkpoint("First", "01-01-2026", 1.0f);
        context.checkpoint("Second", "02-01-2026", 2.0f);

        Task task = context.produce();

        assertEquals(2, task.checkpoints().size());
        assertEquals("First", task.checkpoints().get(0).name());
        assertEquals("Second", task.checkpoints().get(1).name());
    }
}