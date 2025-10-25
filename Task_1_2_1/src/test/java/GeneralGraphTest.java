import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exceptions.GraphException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * General tests for each graph.
 */
public interface GeneralGraphTest {
    <T extends Serializable> Graph<T> newGraph();

    @Test
    default void notOnlyStringNode() {
        Graph<int[]> graph = newGraph();
        assertTrue(graph.getNodes().isEmpty());

        int[] el1 = new int[]{1, 2, 3};
        List<int[]> expected = new ArrayList<>(List.of(el1));
        graph.addNode(el1);
        TestsUtils.assertListsEqualIgnoreOrder(expected, graph.getNodes());

        int[] el2 = new int[]{4, 5};
        expected.add(el2);
        graph.addNode(el2);
        TestsUtils.assertListsEqualIgnoreOrder(expected, graph.getNodes());
    }

    @Test
    default void addAndGetNodes() {
        Graph<String> graph = newGraph();
        assertTrue(graph.getNodes().isEmpty());
        graph.addNode("1");
        TestsUtils.assertListsEqualIgnoreOrder(List.of("1"), graph.getNodes());
        graph.addNode("2");
        TestsUtils.assertListsEqualIgnoreOrder(List.of("1", "2"), graph.getNodes());
    }

    @Test
    default void addEdgeIn() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        
        TestsUtils.assertListsEqualIgnoreOrder(List.of("1"), graph.getNeighbours("2").in());
    }

    @Test
    default void addEdgeOut() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        TestsUtils.assertListsEqualIgnoreOrder(List.of("2"), graph.getNeighbours("1").out());
    }

    @Test
    default void removeNode() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.removeNode("1");
        TestsUtils.assertListsEqualIgnoreOrder(List.of("2", "3"), graph.getNodes());
    }

    @Test
    default void removeEdge() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.removeEdge("1", "2");
        TestsUtils.assertListsEqualIgnoreOrder(List.of("3"), graph.getNeighbours("1").out());
    }

    @Test
    default void removeNodeWithEdge() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.removeNode("2");
        
        TestsUtils.assertListsEqualIgnoreOrder(List.of("1", "3"), graph.getNodes());
        
        TestsUtils.assertListsEqualIgnoreOrder(List.of("3"), graph.getNeighbours("1").out());
    }

    @Test
    default void getNeighbours() {
        Graph<String> graph = newGraph();
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addNode("d");

        graph.addEdge("a", "b");
        graph.addEdge("c", "a");
        graph.addEdge("a", "d");

        List<String> expectedIn = List.of("c");
        List<String> expectedOut = List.of("b", "d");

        List<String> actualIn = graph.getNeighbours("a").in();
        List<String> actualOut = graph.getNeighbours("a").out();

        TestsUtils.assertListsEqualIgnoreOrder(expectedIn, actualIn);
        TestsUtils.assertListsEqualIgnoreOrder(expectedOut, actualOut);
    }

    @Test
    default void serializationTest(@TempDir Path tempDir) {
        Path testFilePath = tempDir.resolve("test.graph");

        Graph<String> graphWriter = newGraph();
        graphWriter.addNode("A");
        graphWriter.addNode("B");
        graphWriter.addNode("C");
        graphWriter.addEdge("A", "B");
        graphWriter.addEdge("B", "C");
        graphWriter.toFile(testFilePath.toString());

        Graph<String> graphReader = newGraph();
        graphReader.fromFile(testFilePath.toString());
        assertEquals(graphWriter, graphReader);
    }

    @Test
    default void serializationBadPathTest() {
        Graph<String> graphWriter = newGraph();
        graphWriter.addNode("A");
        graphWriter.addNode("B");
        graphWriter.addNode("C");
        graphWriter.addEdge("A", "B");
        graphWriter.addEdge("B", "C");
        assertThrows(
                GraphException.class,
                () -> graphWriter.toFile("/where/is/this/path/huh/bruh.idk")
        );
    }

    @Test
    default void deserializationBadPathTest() {
        Graph<String> graphReader = newGraph();
        assertThrows(GraphException.class, () -> graphReader.fromFile("bruh.idk"));
    }

    @Test
    default void deserializationBadCountTest(@TempDir Path tempDir) {
        Path testFilePath = tempDir.resolve("test.graph");

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(testFilePath))) {
            oos.writeInt(-5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graph<String> graphReader = newGraph();
        assertThrows(GraphException.class, () -> graphReader.fromFile(testFilePath.toString()));
    }

    @Test
    default void deserializationNoEnoughObjectsTest(@TempDir Path tempDir) {
        Path testFilePath = tempDir.resolve("test.graph");

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(testFilePath))) {
            oos.writeInt(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graph<String> graphReader = newGraph();
        assertThrows(GraphException.class, () -> graphReader.fromFile(testFilePath.toString()));
    }

    @Test
    default void equalsTest() {
        Graph<String> graph1 = newGraph();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("C", "A");

        Graph<String> graph2 = newGraph();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertEquals(graph1, graph2);
    }

    @Test
    default void notEqualsByEdgeTest() {
        Graph<String> graph1 = newGraph();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("A", "C");

        Graph<String> graph2 = newGraph();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertNotEquals(graph1, graph2);
    }

    @Test
    default void notEqualsByNodeTest() {
        Graph<String> graph1 = newGraph();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("D");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "D");
        graph1.addEdge("D", "A");

        Graph<String> graph2 = newGraph();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertNotEquals(graph1, graph2);
    }

    @Test
    default void equalHashCodeTest() {
        Graph<String> graph1 = newGraph();
        graph1.addNode("A");
        graph1.addNode("B");
        graph1.addNode("C");
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");
        graph1.addEdge("C", "A");

        Graph<String> graph2 = newGraph();
        graph2.addNode("A");
        graph2.addNode("B");
        graph2.addNode("C");
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");
        graph2.addEdge("C", "A");

        assertEquals(graph1.hashCode(), graph2.hashCode());
    }
}