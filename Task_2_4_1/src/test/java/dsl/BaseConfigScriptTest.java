package dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.time.LocalDate;
import java.util.List;
import model.CheckAssignment;
import model.Group;
import model.Task;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.CheckAssignmentBuilder;

class BaseConfigScriptTest {

    private CheckAssignmentBuilder builder;
    private GroovyShell shell;

    @BeforeEach
    void setUp() {
        builder = new CheckAssignmentBuilder();

        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(BaseConfigScript.class.getName());

        Binding binding = new Binding();
        binding.setVariable("builder", builder);
        shell = new GroovyShell(binding, config);
    }

    @Test
    void testFullDslFlow() {
        String dsl = "task('T1') {\n"
                + "    title = 'task'\n"
                + "    checkpoint('Deadline', '04-05-2026', 1.0f)\n"
                + "}\n"
                + "group('24213') { student('tester', 'tester-nick') }\n"
                + "check('24213') { task('T1') }";

        shell.evaluate(dsl);

        List<CheckAssignment> assignments = builder.getCheckAssignments();

        assertEquals(1, assignments.size());

        CheckAssignment assignment = assignments.get(0);
        assertEquals("tester", assignment.student().name());
        assertEquals("task", assignment.task().title());
        assertEquals(
                LocalDate.of(2026, 5, 4),
                assignment.task().checkpoints().get(0).date()
        );
        assertTrue(assignment.group().students().containsKey("tester"));
        assertTrue(assignment.task().id().equals("T1"));
    }

    @Test
    void testDuplicateStudentError() {
        String dsl = "group('ErrorGroup') {\n"
                + "    student('SameName', 'nick1')\n"
                + "    student('SameName', 'nick2')\n"
                + "}";

        try {
            shell.evaluate(dsl);
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Duplicate name \"SameName\""));
        }
    }

    @Test
    void testCheckContextLogic() {
        builder.addTask(new Task("Task_1", "Title", 1.0f, List.of()));
        builder.addGroup(new Group("G1", java.util.Map.of(
                "Student1", new model.Student("Student1", "nick")
        )));

        String dsl = "check('G1', 'Student1') { task('Task_1') }";

        shell.evaluate(dsl);

        assertEquals(1, builder.getCheckAssignments().size());
    }
}