import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

        String largeContent = "x".repeat(100 * 1024) + "target";
        Files.write(testFile, largeContent.getBytes());

        List<Long> result = SubstringFinder.find(testFile.toString(), "target");
        assertEquals(List.of(102400L), result);
    }
}