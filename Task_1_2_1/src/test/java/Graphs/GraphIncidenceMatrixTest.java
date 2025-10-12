package Graphs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphIncidenceMatrixTest {
    @Test
    void addNode() {

    }

    @Test
    void addEdge() {

    }

    @Test
    void removeNode() {

    }

    @Test
    void removeEdge() {

    }

    @Test
    void getNeighbours() {
        Graph graph = new GraphIncidenceMatrix();
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addNode("d");

        graph.addEdge("a", "b");
        graph.addEdge("c", "a");
        graph.addEdge("a", "d");

        String[] expected = {"b", "c", "d"};
        String[] actual = graph.getNeighbours("a");
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            boolean found = false;
            for (int j = 0; j < actual.length; j++) {
                if (expected[i].equals(actual[j])) {
                    expected[i] = "match_e";
                    actual[j] = "match_a";
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void fromFile() {

    }
}