import java.util.*;

/**
 * Graph with adjacency list structure.
 */
public class GraphAdjacencyList extends AbstractGraph {
    private final HashMap<String, LinkedList<String>> lists = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getNodes() {
        return lists.keySet().toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(String name) {
        lists.put(name, new LinkedList<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(String name1, String name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).add(name2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(String name) {
        lists.remove(name);
        for (String node : lists.keySet()) {
            lists.get(node).remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(String name1, String name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).remove(name2);
            lists.get(name2).remove(name1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours getNeighbours(String name) {
        if (lists.containsKey(name)) {
            LinkedList<String> neighboursOut = new LinkedList<>(lists.get(name));
            LinkedList<String> neighboursIn = new LinkedList<>();
            for (String node : getNodes()) {
                if (lists.get(node).contains(name) && !neighboursOut.contains(node)) {
                    neighboursIn.add(node);
                }
            }
            return new NodeNeighbours(
                    neighboursIn.toArray(String[]::new),
                    neighboursOut.toArray(String[]::new)
            );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String[] allNodes = getNodes();
        if (allNodes.length == 0) {
            sb.append("(no nodes)\n");
            return sb.toString();
        }

        int maxNodeNameWidth = 0;
        for (String node : allNodes) {
            maxNodeNameWidth = Math.max(maxNodeNameWidth, node.length());
        }

        for (String node : allNodes) {
            sb.append(node);
            sb.append(" ".repeat(maxNodeNameWidth - node.length()));
            sb.append(" -> [");

            LinkedList<String> neighbours = lists.get(node);
            if (!neighbours.isEmpty()) {
                sb.append(String.join(", ", neighbours));
            }

            sb.append("]\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphAdjacencyList that = (GraphAdjacencyList) o;

        return lists.equals(that.lists);
    }

    @Override
    public int hashCode() {
        return lists.hashCode();
    }
}

