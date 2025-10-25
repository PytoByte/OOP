import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import org.junit.jupiter.api.Test;

class GraphAdjacencyMatrixTest implements GeneralGraphTest {
    @Override
    public <NodeType extends Serializable> Graph<NodeType> newGraph() {
        return new GraphAdjacencyMatrix<>();
    }

    @Test
    void testToString_emptyGraph() {
        Graph<String> graph = newGraph();
        String result = graph.toString();
        assertEquals("(no nodes)\n", result);
    }

    @Test
    void testToString_singleNode() {
        Graph<String> graph = newGraph();
        graph.addNode("A");

        String result = graph.toString();
        String expected = "  A \n" + "A 0 \n";

        assertEquals(expected, result);
    }

    @Test
    void testToString_withEdges() {
        Graph<String> graph = newGraph();
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