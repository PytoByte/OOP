import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GraphSortingTest {
    private int indexOf(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private boolean containsAll(String[] array, String... values) {
        for (String value : values) {
            if (indexOf(array, value) == -1) {
                return false;
            }
        }
        return true;
    }

    private static Stream<Supplier<Graph>> graphImplementations() {
        return Stream.of(
                GraphAdjacencyList::new,
                GraphAdjacencyMatrix::new,
                GraphIncidenceMatrix::new
        );
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_linearDependencies(Supplier<Graph> graphSupplier) {
        // A -> B -> C
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        String[] result = GraphSorting.topologicalSort(graph);

        assertEquals(3, result.length);

        assertTrue(indexOf(result, "A") < indexOf(result, "B"));
        assertTrue(indexOf(result, "B") < indexOf(result, "C"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_multipleBranches(Supplier<Graph> graphSupplier) {
        //   A
        //  / \
        // B   C
        //  \ /
        //   D
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        String[] result = GraphSorting.topologicalSort(graph);

        assertEquals(4, result.length);

        assertTrue(indexOf(result, "A") < indexOf(result, "B"));
        assertTrue(indexOf(result, "A") < indexOf(result, "C"));
        assertTrue(indexOf(result, "B") < indexOf(result, "D"));
        assertTrue(indexOf(result, "C") < indexOf(result, "D"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_emptyGraph(Supplier<Graph> graphSupplier) {
        Graph graph = graphSupplier.get();
        String[] result = GraphSorting.topologicalSort(graph);

        assertEquals(0, result.length);
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_singleNode(Supplier<Graph> graphSupplier) {
        Graph graph = graphSupplier.get();
        graph.addNode("A");

        String[] result = GraphSorting.topologicalSort(graph);

        assertArrayEquals(new String[]{"A"}, result);
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_disconnectedComponents(Supplier<Graph> graphSupplier) {
        // A -> B    C -> D
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");

        String[] result = GraphSorting.topologicalSort(graph);

        assertEquals(4, result.length);

        assertTrue(indexOf(result, "A") < indexOf(result, "B"));
        assertTrue(indexOf(result, "C") < indexOf(result, "D"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_complexDAG(Supplier<Graph> graphSupplier) {
        //     A
        //    / \
        //   B   C
        //  / \ / \
        // D   E   F
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("B", "E");
        graph.addEdge("C", "E");
        graph.addEdge("C", "F");

        String[] result = GraphSorting.topologicalSort(graph);

        assertEquals(6, result.length);

        assertTrue(indexOf(result, "A") < indexOf(result, "B"));
        assertTrue(indexOf(result, "A") < indexOf(result, "C"));
        assertTrue(indexOf(result, "B") < indexOf(result, "D"));
        assertTrue(indexOf(result, "B") < indexOf(result, "E"));
        assertTrue(indexOf(result, "C") < indexOf(result, "E"));
        assertTrue(indexOf(result, "C") < indexOf(result, "F"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_cycleDetection(Supplier<Graph> graphSupplier) {
        // A -> B -> C -> A (loop)
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        assertThrows(IllegalArgumentException.class, () -> {
            GraphSorting.topologicalSort(graph);
        });
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_selfLoop(Supplier<Graph> graphSupplier) {
        // A -> A (loop)
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addEdge("A", "A");

        assertThrows(IllegalArgumentException.class, () -> {
            GraphSorting.topologicalSort(graph);
        });
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_multipleValidOrders(Supplier<Graph> graphSupplier) {
        // A -> C, B -> C (несколько валидных порядков)
        Graph graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");

        String[] result = GraphSorting.topologicalSort(graph);

        assertEquals(3, result.length);

        assertTrue(indexOf(result, "A") < indexOf(result, "C"));
        assertTrue(indexOf(result, "B") < indexOf(result, "C"));

        assertTrue(containsAll(result, "A", "B", "C"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_allNodesInResult(Supplier<Graph> graphSupplier) {
        Graph graph = graphSupplier.get();
        graph.addNode("X");
        graph.addNode("Y");
        graph.addNode("Z");
        graph.addEdge("X", "Y");
        graph.addEdge("Y", "Z");

        String[] result = GraphSorting.topologicalSort(graph);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertTrue(containsAll(result, "X", "Y", "Z"));
    }
}