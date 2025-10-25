import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Class that contains sorting algorithms for graphs.
 */
public class GraphSorting {
    /**
     * Topological sort.
     *
     * @param graph any realization of Graph interface.
     * @return list of nodes, sorted by topological sort.
     */
    public static <T extends Serializable> List<T> topologicalSort(Graph<T> graph) {
        if (graph == null) {
            return new ArrayList<>();
        }

        Map<T, Integer> inDegree = new HashMap<>();
        Map<T, List<T>> adjList = new HashMap<>();
        Queue<T> zeroInDegreeQueue = new LinkedList<>();
        List<T> result = new ArrayList<>();

        List<T> nodes = graph.getNodes();
        for (T node : nodes) {
            inDegree.put(node, 0);
            adjList.put(node, new ArrayList<>());
        }

        for (T node : nodes) {
            NodeNeighbours<T> neighbours = graph.getNeighbours(node);
            List<T> out = neighbours.out();
            for (T to : out) {
                adjList.get(node).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        for (T node : nodes) {
            if (inDegree.get(node) == 0) {
                zeroInDegreeQueue.offer(node);
            }
        }

        while (!zeroInDegreeQueue.isEmpty()) {
            T current = zeroInDegreeQueue.poll();
            result.add(current);

            for (T neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    zeroInDegreeQueue.offer(neighbor);
                }
            }
        }

        if (result.size() != nodes.size()) {
            throw new IllegalArgumentException("Graph has a cycle.");
        }

        return result;
    }
}
