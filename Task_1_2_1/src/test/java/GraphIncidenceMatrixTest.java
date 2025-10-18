import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GraphIncidenceMatrixTest {
    static Graph newGraph() {
        return new GraphIncidenceMatrix();
    }

    @Test
    void generalTests(@TempDir Path tempDir) {
        GeneralGraphTest.runTests(tempDir, GraphIncidenceMatrixTest::newGraph);
    }

    @Test
    void testToString_emptyGraph() {
        GraphIncidenceMatrix graph = new GraphIncidenceMatrix();
        String result = graph.toString();
        assertEquals("(no nodes)\n", result);
    }

    @Test
    void testToString_singleNode() {
        GraphIncidenceMatrix graph = new GraphIncidenceMatrix();
        graph.addNode("A");

        String result = graph.toString();
        String expected = "  \n" + "A \n";

        assertEquals(expected, result);
    }

    @Test
    void testToString_withEdges() {
        GraphIncidenceMatrix graph = new GraphIncidenceMatrix();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        String result = graph.toString();

        assertTrue(result.contains("E0"));
        assertTrue(result.contains("E1"));
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("C"));
        assertTrue(result.contains("-1") || result.contains("1") || result.contains("0"));
    }
}