package context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private static final String STUDENT_NICK = "tester-nick";

    @BeforeEach
    void setUp() {
        builder = new CheckAssignmentBuilder();

        Student student = new Student("tester", STUDENT_NICK);
        Group group = new Group(GROUP_NAME, Map.of(STUDENT_NICK, student));
        builder.addGroup(group);

        Task task = new Task(TASK_ID, "OOP Lab", 5.0f, List.of());
        builder.addTask(task);
    }

    @Test
    void testTaskWithSpecificStudent() {
        CheckContext context = new CheckContext(builder, GROUP_NAME, STUDENT_NICK);

        context.task(TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(1, results.size());
        assertEquals(STUDENT_NICK, results.get(0).student().nick());
        assertEquals(TASK_ID, results.get(0).task().id());
    }

    @Test
    void testTaskForWholeGroup() {
        CheckContext context = new CheckContext(builder, GROUP_NAME);

        context.task(TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(1, results.size());
        assertEquals(GROUP_NAME, results.get(0).group().name());
    }

    @Test
    void testTaskThrowsExceptionOnInvalidData() {
        CheckContext context = new CheckContext(builder, "UNKNOWN_GROUP");

        assertThrows(NullPointerException.class, () -> {
            context.task(TASK_ID);
        });
    }
}