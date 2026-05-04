package dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import model.CheckAssignment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import services.CheckAssignmentBuilder;

class ParserTest {
    @TempDir
    Path tempDir;

    @Test
    void testParseValidFile() throws IOException {
        Path scriptPath = tempDir.resolve("config.groovy");
        String content = "task('T1') { title = 'Test' }\n"
                + "group('G1') { student('Ivan', 'vanya') }\n"
                + "check('G1') { task('T1') }";
        Files.writeString(scriptPath, content);

        List<CheckAssignment> results = Parser.parse(scriptPath.toFile());

        assertEquals(1, results.size());
        assertEquals("Ivan", results.get(0).student().name());
    }

    @Test
    void testParseWithCustomBuilder() throws IOException {
        Path scriptPath = tempDir.resolve("empty.groovy");
        Files.writeString(scriptPath, "// empty");

        CheckAssignmentBuilder customBuilder = new CheckAssignmentBuilder();
        List<CheckAssignment> results = Parser.parse(scriptPath.toFile(), customBuilder);

        assertEquals(customBuilder.getCheckAssignments(), results);
    }

    @Test
    void testParseFileNotFound() {
        File missingFile = new File(tempDir.toFile(), "missing.groovy");

        assertThrows(RuntimeException.class, () -> {
            Parser.parse(missingFile);
        });
    }

    @Test
    void testParseInvalidSyntax() throws IOException {
        Path badScript = tempDir.resolve("bad.groovy");
        Files.writeString(badScript, "task('T1') {");

        assertThrows(RuntimeException.class, () -> {
            Parser.parse(badScript.toFile());
        });
    }
}