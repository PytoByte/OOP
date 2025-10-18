package Graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class GraphIncidenceMatrixTest {
    public static void assertArraysEqualIgnoreOrder(String[] expected, String[] actual) {
        if (expected == null || actual == null) {
            return;
        }
        assertEquals(expected.length, actual.length);

        HashMap<String, Integer> freq = new HashMap<>();
        for (String s : actual) {
            freq.put(s, freq.getOrDefault(s, 0) + 1);
        }
        for (String s : expected) {
            Integer count = freq.get(s);
            if (count == null) {
                System.out.printf("Expected more \"%s\" elements", s);
                assertNotNull(count);
            }
            if (count - 1 <= 0) {
                freq.remove(s);
            }
        }

        if (!freq.isEmpty()) {
            System.out.println("Not all elements presented in actual array");
            assertTrue(freq.isEmpty());
        }
    }

    @Test
    void addAndGetNodes() {
        Graph graph = new GraphIncidenceMatrix();
        assertEquals(0, graph.getNodes().length);
        graph.addNode("1");
        assertArraysEqualIgnoreOrder(new String[]{"1"}, graph.getNodes());
        graph.addNode("2");
        assertArraysEqualIgnoreOrder(new String[]{"1", "2"}, graph.getNodes());
    }

    @Test
    void addEdgeIn() {
        Graph graph = new GraphIncidenceMatrix();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"1"}, graph.getNeighbours("2").in());
    }

    @Test
    void addEdgeOut() {
        Graph graph = new GraphIncidenceMatrix();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"2"}, graph.getNeighbours("1").out());
    }

    @Test
    void removeNode() {
        Graph graph = new GraphIncidenceMatrix();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.removeNode("1");
        assertArraysEqualIgnoreOrder(new String[]{"2", "3"}, graph.getNodes());
    }

    @Test
    void removeEdge() {
        Graph graph = new GraphIncidenceMatrix();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.removeEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"3"}, graph.getNeighbours("1").out());
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

        String[] expectedIn = {"c"};
        String[] expectedOut = {"b", "d"};
        String[] actualIn = graph.getNeighbours("a").in();
        String[] actualOut = graph.getNeighbours("a").out();

        assertArraysEqualIgnoreOrder(expectedIn, actualIn);
        assertArraysEqualIgnoreOrder(expectedOut, actualOut);
    }

    @Test
    void fromFile() {

    }
}