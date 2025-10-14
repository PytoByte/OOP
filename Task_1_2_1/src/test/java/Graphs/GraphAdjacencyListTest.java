package Graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class GraphAdjacencyListTest {
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
    void addEdge() {
        Graph graph = new GraphIncidenceMatrix();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"2"}, graph.getNeighbours("1"));
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
        assertArraysEqualIgnoreOrder(new String[]{"1"}, graph.getNeighbours("3"));
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