import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class GraphSortingTest {
    private static Stream<Supplier<Graph<String>>> graphImplementations() {
        return Stream.of(
                GraphAdjacencyList::new,
                GraphAdjacencyMatrix::new,
                GraphIncidenceMatrix::new
        );
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_linearDependencies(Supplier<Graph<String>> graphSupplier) {
        // A -> B -> C
        Graph<String> graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(3, result.size());
        assertTrue(result.indexOf("A") < result.indexOf("B"));
        assertTrue(result.indexOf("B") < result.indexOf("C"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_multipleBranches(Supplier<Graph<String>> graphSupplier) {
        //   A
        //  / \
        // B   C
        //  \ /
        //   D
        Graph<String> graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(4, result.size());

        assertTrue(result.indexOf("A") < result.indexOf("B"));
        assertTrue(result.indexOf("A") < result.indexOf("C"));
        assertTrue(result.indexOf("B") < result.indexOf("D"));
        assertTrue(result.indexOf("C") < result.indexOf("D"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_emptyGraph(Supplier<Graph<String>> graphSupplier) {
        Graph<String> graph = graphSupplier.get();
        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_singleNode(Supplier<Graph<String>> graphSupplier) {
        Graph<String> graph = graphSupplier.get();
        graph.addNode("A");

        List<String> result = GraphSorting.topologicalSort(graph);
        TestsUtils.assertListsEqualIgnoreOrder(List.of("A"), result);
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_disconnectedComponents(Supplier<Graph<String>> graphSupplier) {
        // A -> B    C -> D
        Graph<String> graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");

        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(4, result.size());

        assertTrue(result.indexOf("A") < result.indexOf("B"));
        assertTrue(result.indexOf("C") < result.indexOf("D"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_sortTree(Supplier<Graph<String>> graphSupplier) {
        //     A
        //    / \
        //   B   C
        //  / \ / \
        // D   E   F
        Graph<String> graph = graphSupplier.get();
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

        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(6, result.size());

        assertTrue(result.indexOf("A") < result.indexOf("B"));
        assertTrue(result.indexOf("A") < result.indexOf("C"));
        assertTrue(result.indexOf("B") < result.indexOf("D"));
        assertTrue(result.indexOf("B") < result.indexOf("E"));
        assertTrue(result.indexOf("C") < result.indexOf("E"));
        assertTrue(result.indexOf("C") < result.indexOf("F"));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_cycleDetection(Supplier<Graph<String>> graphSupplier) {
        // A -> B -> C -> A (loop)
        Graph<String> graph = graphSupplier.get();
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
    void topologicalSort_selfLoop(Supplier<Graph<String>> graphSupplier) {
        // A -> A (loop)
        Graph<String> graph = graphSupplier.get();
        graph.addNode("A");
        graph.addEdge("A", "A");

        assertThrows(IllegalArgumentException.class, () -> {
            GraphSorting.topologicalSort(graph);
        });
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_multipleValidOrders(Supplier<Graph<String>> graphSupplier) {
        // A -> C, B -> C (несколько валидных порядков)
        Graph<String> graph = graphSupplier.get();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");

        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(3, result.size());

        assertTrue(result.indexOf("A") < result.indexOf("C"));
        assertTrue(result.indexOf("B") < result.indexOf("C"));

        assertTrue(result.containsAll(List.of("A", "B", "C")));
    }

    @ParameterizedTest
    @MethodSource("graphImplementations")
    void topologicalSort_allNodesInResult(Supplier<Graph<String>> graphSupplier) {
        Graph<String> graph = graphSupplier.get();
        graph.addNode("X");
        graph.addNode("Y");
        graph.addNode("Z");
        graph.addEdge("X", "Y");
        graph.addEdge("Y", "Z");

        List<String> result = GraphSorting.topologicalSort(graph);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of("X", "Y", "Z")));
    }
}