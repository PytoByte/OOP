package context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import model.Group;
import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupContextTest {

    private GroupContext context;
    private static final String GROUP_NAME = "test-group";

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