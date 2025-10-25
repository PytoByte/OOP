import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import org.junit.jupiter.api.Test;

class GraphAdjacencyListTest implements GeneralGraphTest {
    @Override
    public <T extends Serializable> Graph<T> newGraph() {
        return new GraphAdjacencyList<>();
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
        assertEquals("A -> []\n", result);
    }

    @Test
    void testToString_withEdges() {
        Graph<String> graph = newGraph();
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