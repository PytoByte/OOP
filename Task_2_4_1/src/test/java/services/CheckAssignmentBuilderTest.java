package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import model.Group;
import model.Student;
import model.Task;
import model.CheckAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckAssignmentBuilderTest {
    private CheckAssignmentBuilder builder;
    private final String GROUP_NAME = "test-group";
    private final String TASK_ID = "lab1";

    @BeforeEach
    void setUp() {
        builder = new CheckAssignmentBuilder();

        // Подготавливаем базовые данные
        Student s1 = new Student("Ivanov", "ivan-nick");
        Student s2 = new Student("Petrov", "petr-nick");

        Group group = new Group(GROUP_NAME, Map.of(
                "ivan-nick", s1,
                "petr-nick", s2
        ));

        Task task = new Task(TASK_ID, "Threading", 5.0f, List.of());

        builder.addGroup(group);
        builder.addTask(task);
    }

    @Test
    void testAddSingleStudentAssignment() {
        builder.buildCheckAssignment(GROUP_NAME, "ivan-nick", TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(1, results.size());
        assertEquals("Ivanov", results.get(0).student().name());
        assertEquals(TASK_ID, results.get(0).task().id());
    }

    @Test
    void testAddFullGroupAssignment() {
        builder.buildCheckAssignments(GROUP_NAME, TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(a -> a.student().nick().equals("ivan-nick")));
        assertTrue(results.stream().anyMatch(a -> a.student().nick().equals("petr-nick")));
    }

    @Test
    void testAddTaskThrowsExceptionWhenAlreadyExists() {
        Task duplicateTask = new Task(TASK_ID, "Different Title", 10.0f, List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            builder.addTask(duplicateTask);
        });

        assertEquals("Task already exist: " + TASK_ID, ex.getMessage());
    }

    @Test
    void testAddGroupThrowsExceptionWhenAlreadyExists() {
        Group duplicateGroup = new Group(GROUP_NAME, Map.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            builder.addGroup(duplicateGroup);
        });

        assertEquals("Group already exist: " + GROUP_NAME, ex.getMessage());
    }

    @Test
    void testAddDuplicateAssignmentDoesNotIncreaseSize() {
        builder.buildCheckAssignment(GROUP_NAME, "ivan-nick", TASK_ID);
        builder.buildCheckAssignment(GROUP_NAME, "ivan-nick", TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(1, results.size(), "Set should prevent duplicate assignments");
    }

    @Test
    void testThrowsExceptionWhenGroupNotFound() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            builder.buildCheckAssignments("NON_EXISTENT", TASK_ID);
        });
        assertTrue(ex.getMessage().contains("Group not found"));
    }

    @Test
    void testThrowsExceptionWhenStudentNotFound() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            builder.buildCheckAssignment(GROUP_NAME, "ghost-nick", TASK_ID);
        });
        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void testThrowsExceptionWhenTaskNotFound() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            builder.buildCheckAssignment(GROUP_NAME, "ivan-nick", "wrong_task");
        });
        assertTrue(ex.getMessage().contains("Task not found"));
    }

    @Test
    void testGetCheckAssignmentsReturnsImmutableList() {
        builder.buildCheckAssignment(GROUP_NAME, "ivan-nick", TASK_ID);
        List<CheckAssignment> list = builder.getCheckAssignments();

        assertThrows(UnsupportedOperationException.class, () -> {
            list.clear();
        });
    }
}