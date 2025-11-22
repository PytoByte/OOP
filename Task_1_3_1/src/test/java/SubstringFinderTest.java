import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for SubstringFinder.
 */
public class SubstringFinderTest {

    @Test
    void testEmptySubstring(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello".getBytes());
        assertTrue(SubstringFinder.find(testFile.toString(), "").isEmpty());
    }

    @Test
    void testNotFound(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello world".getBytes());
        assertTrue(SubstringFinder.find(testFile.toString(), "xyz").isEmpty());
    }

    @Test
    void testSingleMatch(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello world".getBytes());
        assertEquals(List.of(6L), SubstringFinder.find(testFile.toString(), "world"));
    }

    @Test
    void testMultipleMatches(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "ababa".getBytes());
        assertEquals(List.of(0L, 2L), SubstringFinder.find(testFile.toString(), "aba"));
    }

    @Test
    void testCrossChunkMatch(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String content = "x".repeat(8192) + "hello";
        Files.write(testFile, content.getBytes());
        assertEquals(List.of(8192L), SubstringFinder.find(testFile.toString(), "hello"));
    }

    @Test
    void testBetweenChunks(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String content = "x".repeat(8190) + "hello";
        Files.write(testFile, content.getBytes());
        assertEquals(List.of(8190L), SubstringFinder.find(testFile.toString(), "hello"));
    }

    @Test
    void testLargeFile(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("large_test.txt");

        String largeContent = ("x".repeat(1024 * 1024));
        for (int i = 0; i < 1024 * 34; i++) {
            Files.write(testFile, largeContent.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        }
        Files.write(testFile, "target".getBytes(), StandardOpenOption.APPEND);

        List<Long> result = SubstringFinder.find(testFile.toString(), "target");
        assertEquals(List.of(36507222016L), result);
    }

    @Test
    void testOverlapping(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("overlapping_test.txt");
        String largeContent = "a".repeat(7);
        Files.write(testFile, largeContent.getBytes());

        List<Long> result = SubstringFinder.find(testFile.toString(), "aaa");
        assertEquals(List.of(0L, 1L, 2L, 3L, 4L), result); // ← пробелы после запятых
    }

    @Test
    void testSingleMatchNotAscii(@TempDir Path tempDir) throws IOException { // ← ASCII → Ascii
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "Привет мир".getBytes());
        assertEquals(List.of(7L), SubstringFinder.find(testFile.toString(), "мир"));
    }
}