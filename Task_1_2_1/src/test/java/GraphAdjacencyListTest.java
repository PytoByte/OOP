import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testToString_emptyGraph() {
        GraphAdjacencyList graph = new GraphAdjacencyList();
        String result = graph.toString();
        assertEquals("(no nodes)\n", result);
    }

    @Test
    void testToString_singleNode() {
        GraphAdjacencyList graph = new GraphAdjacencyList();
        graph.addNode("A");
        String result = graph.toString();
        assertEquals("A -> []\n", result);
    }

    @Test
    void testToString_withEdges() {
        GraphAdjacencyList graph = new GraphAdjacencyList();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");

        String result = graph.toString();

        assertTrue(result.contains("A -> [B, C]") || result.contains("A -> [C, B]"));
        assertTrue(result.contains("B -> []"));
        assertTrue(result.contains("C -> []"));
    }
}