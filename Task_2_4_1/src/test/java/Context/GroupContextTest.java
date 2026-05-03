package Context;

import Domain.Group;
import Domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GroupContextTest {

    private GroupContext context;
    private final String GROUP_NAME = "test-group";

    @BeforeEach
    void setUp() {
        context = new GroupContext(GROUP_NAME);
    }

    @Test
    void testStudentAdditionAndProduce() {
        context.student("Иван", "ivan-nick");
        context.student("Петр", "petr-nick");

        Group group = context.produce();

        assertEquals(GROUP_NAME, group.name());
        assertEquals(2, group.students().size());

        Student s1 = group.students().get("Иван");
        assertNotNull(s1);
        assertEquals("ivan-nick", s1.nick());
    }

    @Test
    void testDuplicateNameThrowsException() {
        context.student("Иван", "ivan-nick");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            context.student("Иван", "another-nick");
        });

        assertTrue(ex.getMessage().contains("Duplicate name \"Иван\""));
    }

    @Test
    void testProduceReturnsImmutableMap() {
        context.student("Иван", "ivan-nick");
        Group group = context.produce();
        Map<String, Student> studentsMap = group.students();

        // Проверяем, что Map.copyOf действительно возвращает неизменяемую коллекцию
        assertThrows(UnsupportedOperationException.class, () -> {
            studentsMap.put("Петр", new Student("Петр", "petr-nick"));
        });
    }

    @Test
    void testProduceWithEmptyGroup() {
        Group group = context.produce();

        assertEquals(GROUP_NAME, group.name());
        assertTrue(group.students().isEmpty());
    }
}