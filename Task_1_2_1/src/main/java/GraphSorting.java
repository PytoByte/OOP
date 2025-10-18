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
     * @return array of nodes, sorted by topological sort.
     */
    public static String[] topologicalSort(Graph graph) {
        if (graph == null) {
            return new String[]{};
        }

        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();
        Queue<String> zeroInDegreeQueue = new LinkedList<>();
        List<String> result = new ArrayList<>();

        String[] nodes = graph.getNodes();
        for (String node : nodes) {
            inDegree.put(node, 0);
            adjList.put(node, new ArrayList<>());
        }

        for (String node : nodes) {
            NodeNeighbours neighbours = graph.getNeighbours(node);
            String[] out = neighbours.out();
            for (String to : out) {
                adjList.get(node).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        for (String node : nodes) {
            if (inDegree.get(node) == 0) {
                zeroInDegreeQueue.offer(node);
            }
        }

        while (!zeroInDegreeQueue.isEmpty()) {
            String current = zeroInDegreeQueue.poll();
            result.add(current);

            for (String neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    zeroInDegreeQueue.offer(neighbor);
                }
            }
        }

        if (result.size() != nodes.length) {
            throw new IllegalArgumentException("Graph has a cycle, topological sort is not possible.");
        }

        return result.toArray(new String[0]);
    }
}
