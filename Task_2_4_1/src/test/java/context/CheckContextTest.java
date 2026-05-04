package context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import model.CheckAssignment;
import model.Group;
import model.Student;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.CheckAssignmentBuilder;

class CheckContextTest {

    private CheckAssignmentBuilder builder;
    private static final String GROUP_NAME = "test-group";
    private static final String TASK_ID = "lab1";
    private static final String STUDENT_NICK1 = "tester-nick1";
    private static final String STUDENT_NICK2 = "tester-nick2";

    @BeforeEach
    void setUp() {
        builder = new CheckAssignmentBuilder();

        Student student1 = new Student("tester", STUDENT_NICK1);
        Student student2 = new Student("tester", STUDENT_NICK2);
        Group group = new Group(GROUP_NAME, Map.of(
                STUDENT_NICK1, student1,
                STUDENT_NICK2, student2
        ));
        builder.addGroup(group);

        Task task = new Task(TASK_ID, "OOP Lab", 5.0f, List.of());
        builder.addTask(task);
    }

    @Test
    void testTaskWithSpecificStudent() {
        CheckContext context = new CheckContext(builder, GROUP_NAME, STUDENT_NICK1);

        context.task(TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(1, results.size());
        assertEquals(GROUP_NAME, results.get(0).group().name());
        assertEquals(STUDENT_NICK1, results.get(0).student().nick());
        assertEquals(TASK_ID, results.get(0).task().id());
    }

    @Test
    void testTaskForWholeGroup() {
        CheckContext context = new CheckContext(builder, GROUP_NAME);

        context.task(TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(2, results.size());
        for (int i = 0; i < 2; i++) {
            assertEquals(GROUP_NAME, results.get(i).group().name());
            assertEquals(TASK_ID, results.get(i).task().id());
        }
        assertTrue(results.get(0).student().nick().equals(STUDENT_NICK1)
                && results.get(1).student().nick().equals(STUDENT_NICK2)
                || results.get(0).student().nick().equals(STUDENT_NICK2)
                && results.get(1).student().nick().equals(STUDENT_NICK1)
        );
    }

    @Test
    void testTaskThrowsExceptionOnInvalidData() {
        CheckContext context = new CheckContext(builder, "UNKNOWN_GROUP");

        assertThrows(NullPointerException.class, () -> {
            context.task(TASK_ID);
        });
    }
}