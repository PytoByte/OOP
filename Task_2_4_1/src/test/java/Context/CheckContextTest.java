package Context;

import Domain.Group;
import Domain.Student;
import Domain.Task;
import Domain.CheckAssignment;
import Services.CheckAssignmentBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CheckContextTest {

    private CheckAssignmentBuilder builder;
    private final String GROUP_NAME = "test-group";
    private final String TASK_ID = "lab1";
    private final String STUDENT_NICK = "tester-nick";

    @BeforeEach
    void setUp() {
        builder = new CheckAssignmentBuilder();

        // Регистрируем студента и группу
        Student student = new Student("tester", STUDENT_NICK);
        Group group = new Group(GROUP_NAME, Map.of(STUDENT_NICK, student));
        builder.addGroup(group);

        // Регистрируем задачу
        Task task = new Task(TASK_ID, "OOP Lab", 5.0f, List.of());
        builder.addTask(task);
    }

    @Test
    void testTaskWithSpecificStudent() {
        // Создаем контекст для конкретного студента
        CheckContext context = new CheckContext(builder, GROUP_NAME, STUDENT_NICK);

        context.task(TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        assertEquals(1, results.size());
        assertEquals(STUDENT_NICK, results.get(0).student().nick());
        assertEquals(TASK_ID, results.get(0).task().id());
    }

    @Test
    void testTaskForWholeGroup() {
        // Создаем контекст для всей группы
        CheckContext context = new CheckContext(builder, GROUP_NAME);

        context.task(TASK_ID);

        List<CheckAssignment> results = builder.getCheckAssignments();

        // В нашей тестовой группе 1 студент, проверяем наличие записи
        assertEquals(1, results.size());
        assertEquals(GROUP_NAME, results.get(0).group().name());
    }

    @Test
    void testTaskThrowsExceptionOnInvalidData() {
        // Контекст с несуществующей группой
        CheckContext context = new CheckContext(builder, "UNKNOWN_GROUP");

        // Проверяем, что пробрасывается исключение из билдера
        assertThrows(NullPointerException.class, () -> {
            context.task(TASK_ID);
        });
    }
}