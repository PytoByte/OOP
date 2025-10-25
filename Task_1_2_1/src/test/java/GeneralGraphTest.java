import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays; // Добавлен импорт для Arrays.asList и Arrays.stream

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public interface GeneralGraphTest {
    <NodeType extends Serializable> Graph<NodeType> newGraph();

    @Test
    default void addAndGetNodes() {
        Graph<String> graph = newGraph();
        assertEquals(0, graph.getNodes().size());
        
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("1"), graph.getNodes());
        graph.addNode("2");
        
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("1", "2"), graph.getNodes());
    }

    @Test
    default void addEdgeIn() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("1"), graph.getNeighbours("2").in());
    }

    @Test
    default void addEdgeOut() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addEdge("1", "2");
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("2"), graph.getNeighbours("1").out());
    }

    @Test
    default void removeNode() {
        Graph<String> graph = newGraph();
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.removeNode("1");
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("2", "3"), graph.getNodes());
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
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("3"), graph.getNeighbours("1").out());
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
        
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("1", "3"), graph.getNodes());
        
        TestsUtils.assertListsEqualIgnoreOrder(Arrays.asList("3"), graph.getNeighbours("1").out());
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

        List<String> expectedIn = Arrays.asList("c");
        List<String> expectedOut = Arrays.asList("b", "d");

        List<String> actualIn = graph.getNeighbours("a").in();
        List<String> actualOut = graph.getNeighbours("a").out();

        TestsUtils.assertListsEqualIgnoreOrder(expectedIn, actualIn);
        TestsUtils.assertListsEqualIgnoreOrder(expectedOut, actualOut);
    }

    @Test
    default void toFile(@TempDir Path tempDir) {
        Path testFilePath = tempDir.resolve("test.graph");

        Graph<String> graph = newGraph();
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
        TestsUtils.assertListsEqualIgnoreOrder(
                Arrays.asList("A", "B", "C"),
                content.subList(1, 4)
        );

        TestsUtils.assertListsEqualIgnoreOrder(
                Arrays.asList("A B", "B C"),
                content.subList(4, 6)
        );
    }

    @Test
    default void fromFile(@TempDir Path tempDir) {
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

    @Test
    default void notEqualHashCodeByEdgeTest() {
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

        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }

    @Test
    default void notEqualHashCodeByNodeTest() {
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

        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }
}