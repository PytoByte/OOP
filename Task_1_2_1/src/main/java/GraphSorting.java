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
    public static <NodeType extends Serializable> List<NodeType> topologicalSort(Graph<NodeType> graph) {
        if (graph == null) {
            return new ArrayList<>();
        }

        Map<NodeType, Integer> inDegree = new HashMap<>();
        Map<NodeType, List<NodeType>> adjList = new HashMap<>();
        Queue<NodeType> zeroInDegreeQueue = new LinkedList<>();
        List<NodeType> result = new ArrayList<>();

        List<NodeType> nodes = graph.getNodes();
        for (NodeType node : nodes) {
            inDegree.put(node, 0);
            adjList.put(node, new ArrayList<>());
        }

        for (NodeType node : nodes) {
            NodeNeighbours<NodeType> neighbours = graph.getNeighbours(node);
            List<NodeType> out = neighbours.out();
            for (NodeType to : out) {
                adjList.get(node).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        for (NodeType node : nodes) {
            if (inDegree.get(node) == 0) {
                zeroInDegreeQueue.offer(node);
            }
        }

        while (!zeroInDegreeQueue.isEmpty()) {
            NodeType current = zeroInDegreeQueue.poll();
            result.add(current);

            for (NodeType neighbor : adjList.get(current)) {
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
