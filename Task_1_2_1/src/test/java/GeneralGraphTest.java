import static utils.TestsUtils.assertArraysEqualIgnoreOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

/**
 * General test for Graph interface realizations.
 */
public class GeneralGraphTest {

    /**
     * Run all tests.
     *
     * @param tempDir temporary directory for file-based tests
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void runTests(Path tempDir, Supplier<Graph> newGraph) {
        addAndGetNodes(newGraph);
        addEdgeIn(newGraph);
        addEdgeOut(newGraph);
        removeNode(newGraph);
        removeEdge(newGraph);
        removeNodeWithEdge(newGraph);
        getNeighbours(newGraph);
        toFile(tempDir, newGraph);
        fromFile(tempDir, newGraph);
        equalsTest(newGraph);
        notEqualsByEdgeTest(newGraph);
        notEqualsByNodeTest(newGraph);
        equalHashCodeTest(newGraph);
        notEqualHashCodeByEdgeTest(newGraph);
        notEqualHashCodeByNodeTest(newGraph);
    }

    /**
     * Test adding and getting nodes.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void addAndGetNodes(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
        assertEquals(0, graph.getNodes().length);
        graph.addNode("1");
        assertArraysEqualIgnoreOrder(new String[]{"1"}, graph.getNodes());
        graph.addNode("2");
        assertArraysEqualIgnoreOrder(new String[]{"1", "2"}, graph.getNodes());
    }

    /**
     * Test adding incoming edges.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void addEdgeIn(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"1"}, graph.getNeighbours("2").in());
    }

    /**
     * Test adding outgoing edges.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void addEdgeOut(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"2"}, graph.getNeighbours("1").out());
    }

    /**
     * Test node removal.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     * */
    public static void removeNode(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.removeNode("1");
        assertArraysEqualIgnoreOrder(new String[]{"2", "3"}, graph.getNodes());
    }

    /**
     * Test edge removal.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void removeEdge(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.removeEdge("1", "2");
        assertArraysEqualIgnoreOrder(new String[]{"3"}, graph.getNeighbours("1").out());
    }

    /**
     * Test node removal with edges.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void removeNodeWithEdge(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.removeNode("2");
        assertArraysEqualIgnoreOrder(new String[]{"1", "3"}, graph.getNodes());
        assertArraysEqualIgnoreOrder(new String[]{"3"}, graph.getNeighbours("1").out());
    }

    /**
     * Test getting neighbors.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void getNeighbours(Supplier<Graph> newGraph) {
        Graph graph = newGraph.get();
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

    /**
     * Test saving graph to file.
     *
     * @param tempDir temporary directory for file-based tests
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void toFile(Path tempDir, Supplier<Graph> newGraph) {
        Path testFilePath = tempDir.resolve("test.graph");

        Graph graph = newGraph.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        graph.toFile(testFilePath.toString());

        assertTrue(Files.exists(testFilePath));
        List<String> content;

        try {
            content = Files.readAllLines(testFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals("3", content.get(0));
        assertArraysEqualIgnoreOrder(
                new String[]{"A", "B", "C"},
                content.subList(1, 4).toArray(String[]::new)
        );
        assertArraysEqualIgnoreOrder(
                new String[]{"A B", "B C"},
                content.subList(4, 6).toArray(String[]::new)
        );
    }

    /**
     * Test loading graph from file.
     *
     * @param tempDir temporary directory for file-based tests
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void fromFile(Path tempDir, Supplier<Graph> newGraph) {
        Path testFilePath = tempDir.resolve("test.graph");

        Graph graphWriter = newGraph.get();
        graphWriter.addNode("A");
        graphWriter.addNode("B");
        graphWriter.addNode("C");
        graphWriter.addEdge("A", "B");
        graphWriter.addEdge("B", "C");
        graphWriter.toFile(testFilePath.toString());

        Graph graphReader = newGraph.get();
        graphReader.fromFile(testFilePath.toString());
        assertEquals(graphWriter, graphReader);
    }

    /**
     * Test graph equality.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void equalsTest(Supplier<Graph> newGraph) {
        Graph graph1 = newGraph.get();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("C", "A");

        Graph graph2 = newGraph.get();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertEquals(graph1, graph2);
    }

    /**
     * Test inequality by edges.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void notEqualsByEdgeTest(Supplier<Graph> newGraph) {
        Graph graph1 = newGraph.get();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("A", "C");

        Graph graph2 = newGraph.get();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertNotEquals(graph1, graph2);
    }

    /**
     * Test inequality by nodes.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void notEqualsByNodeTest(Supplier<Graph> newGraph) {
        Graph graph1 = newGraph.get();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("D");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "D");
        graph1.addEdge("D", "A");

        Graph graph2 = newGraph.get();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertNotEquals(graph1, graph2);
    }

    /**
     * Test equal hash codes.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void equalHashCodeTest(Supplier<Graph> newGraph) {
        Graph graph1 = newGraph.get();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("C", "A");

        Graph graph2 = newGraph.get();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertEquals(graph1.hashCode(), graph2.hashCode());
    }

    /**
     * Test different hash codes by edges.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void notEqualHashCodeByEdgeTest(Supplier<Graph> newGraph) {
        Graph graph1 = newGraph.get();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("A", "C");

        Graph graph2 = newGraph.get();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }

    /**
     * Test different hash codes by nodes.
     *
     * @param newGraph supplier function that creates an empty Graph instance
     */
    public static void notEqualHashCodeByNodeTest(Supplier<Graph> newGraph) {
        Graph graph1 = newGraph.get();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("D");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "D");
        graph1.addEdge("D", "A");

        Graph graph2 = newGraph.get();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }
}