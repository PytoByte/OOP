import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

class GraphIncidenceMatrixTest {
    static Graph newGraph() {
        return new GraphIncidenceMatrix();
    }

    @Test
    void generalTests(@TempDir Path tempDir) {
        GeneralGraphTest.runTests(tempDir, GraphIncidenceMatrixTest::newGraph);
    }
}