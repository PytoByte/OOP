import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Graph with adjacency list structure.
 */
public class GraphAdjacencyList<T extends Serializable> implements Graph<T> {
    private final HashMap<T, LinkedList<T>> lists = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getNodes() {
        return new ArrayList<>(lists.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(T name) {
        lists.put(name, new LinkedList<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(T name1, T name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).add(name2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(T name) {
        lists.remove(name);
        for (T node : lists.keySet()) {
            lists.get(node).remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(T name1, T name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).remove(name2);
            lists.get(name2).remove(name1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours<T> getNeighbours(T name) {
        if (lists.containsKey(name)) {
            LinkedList<T> neighboursOut = new LinkedList<>(lists.get(name));
            LinkedList<T> neighboursIn = new LinkedList<>();
            for (T node : getNodes()) {
                if (lists.get(node).contains(name) && !neighboursOut.contains(node)) {
                    neighboursIn.add(node);
                }
            }

            return new NodeNeighbours<>(
                    neighboursIn,
                    neighboursOut
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

        List<T> allNodes = getNodes();
        if (allNodes.isEmpty()) {
            sb.append("(no nodes)\n");
            return sb.toString();
        }

        int maxNodeNameWidth = 0;
        for (T node : allNodes) {
            maxNodeNameWidth = Math.max(maxNodeNameWidth, node.toString().length());
        }

        for (T node : allNodes) {
            sb.append(node);
            sb.append(" ".repeat(maxNodeNameWidth - node.toString().length()));
            sb.append(" -> [");

            LinkedList<T> neighbours = lists.get(node);
            if (!neighbours.isEmpty()) {
                sb.append(
                        String.join(", ", neighbours.stream().map(Object::toString).toList())
                );
            }
            sb.append("]\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        GraphAdjacencyList<T> that = (GraphAdjacencyList<T>) o;

        return lists.equals(that.lists);
    }

    @Override
    public int hashCode() {
        return lists.hashCode();
    }
}

