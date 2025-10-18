import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GraphAdjacencyListTest {
    static Graph newGraph() {
        return new GraphAdjacencyList();
    }

    @Test
    void generalTests(@TempDir Path tempDir) {
        GeneralGraphTest.runTests(tempDir, GraphAdjacencyListTest::newGraph);
    }
}