import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GraphAdjacencyMatrixTest {
    static Graph newGraph() {
        return new GraphAdjacencyMatrix();
    }

    @Test
    void generalTests(@TempDir Path tempDir) {
        GeneralGraphTest.runTests(tempDir, GraphAdjacencyMatrixTest::newGraph);
    }
}