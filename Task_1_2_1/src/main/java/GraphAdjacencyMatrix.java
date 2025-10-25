import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Graph with adjacency matrix structure.
 */
public class GraphAdjacencyMatrix<T extends Serializable> implements Graph<T> {
    HashMap<T, HashMap<T, Boolean>> matrix = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getNodes() {
        return matrix.keySet().stream().toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(T name) {
        matrix.put(name, new HashMap<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(T name1, T name2) {
        matrix.get(name1).put(name2, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(T name) {
        matrix.remove(name);
        for (T node : matrix.keySet()) {
            matrix.get(node).remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(T name1, T name2) {
        matrix.get(name1).remove(name2);
        matrix.get(name2).remove(name1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours<T> getNeighbours(T name) {
        LinkedList<T> neighboursIn = new LinkedList<>();
        for (T node : getNodes()) {
            if (matrix.get(node).containsKey(name)) {
                neighboursIn.add(node);
            }
        }
        return new NodeNeighbours<>(
                neighboursIn,
                matrix.get(name).keySet().stream().toList()
        );
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

        int maxNodeName = 0;
        for (T node : allNodes) {
            maxNodeName = Math.max(node.toString().length(), maxNodeName);
        }

        sb.append(" ".repeat(maxNodeName + 1));
        for (T to : allNodes) {
            sb.append(String.format("%s ", to));
        }
        sb.append("\n");

        for (T from : allNodes) {
            sb.append(String.format("%s", from));
            sb.append(" ".repeat(maxNodeName - from.toString().length() + 1));
            for (T to : allNodes) {
                boolean hasEdge = matrix.get(from).containsKey(to);
                int toLen = to.toString().length() / 2;
                sb.append(" ".repeat(toLen));
                sb.append(String.format("%s", hasEdge ? "1" : "0"));
                sb.append(" ".repeat(to.toString().length() % 2 == 0 ? toLen : toLen + 1));
            }
            sb.append("\n");
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
        GraphAdjacencyMatrix<T> that = (GraphAdjacencyMatrix<T>) o;
        return matrix.equals(that.matrix);
    }

    @Override
    public int hashCode() {
        return matrix.hashCode();
    }
}

