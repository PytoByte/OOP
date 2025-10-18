import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testToString_emptyGraph() {
        GraphAdjacencyMatrix graph = new GraphAdjacencyMatrix();
        String result = graph.toString();
        assertEquals("(no nodes)\n", result);
    }

    @Test
    void testToString_singleNode() {
        GraphAdjacencyMatrix graph = new GraphAdjacencyMatrix();
        graph.addNode("A");

        String result = graph.toString();
        String expected =
                "   A \n" +
                        "A  0 \n";

        assertEquals(expected, result);
    }

    @Test
    void testToString_withEdges() {
        GraphAdjacencyMatrix graph = new GraphAdjacencyMatrix();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");

        String result = graph.toString();

        assertTrue(result.contains("  A B C"));
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("C"));
        assertTrue(result.contains("1"));
    }
}