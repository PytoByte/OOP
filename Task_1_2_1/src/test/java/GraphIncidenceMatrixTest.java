import java.nio.file.Path;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Test;

class GraphIncidenceMatrixTest {
    static Graph newGraph() {
        return new GraphIncidenceMatrix();
    }

    @Test
    void generalTests(@TempDir Path tempDir) {
        GeneralGraphTest.runTests(tempDir, GraphIncidenceMatrixTest::newGraph);
    }
}