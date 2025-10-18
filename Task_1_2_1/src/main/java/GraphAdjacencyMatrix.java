import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph with adjacency matrix structure.
 */
public class GraphAdjacencyMatrix extends AbstractGraph {
    HashMap<String, HashMap<String, Boolean>> matrix = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getNodes() {
        return matrix.keySet().toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(String name) {
        matrix.put(name, new HashMap<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(String name1, String name2) {
        matrix.get(name1).put(name2, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(String name) {
        matrix.remove(name);
        for (String node : matrix.keySet()) {
            matrix.get(node).remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(String name1, String name2) {
        matrix.get(name1).remove(name2);
        matrix.get(name2).remove(name1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours getNeighbours(String name) {
        LinkedList<String> neighboursIn = new LinkedList<>();
        for (String node : getNodes()) {
            if (matrix.get(node).containsKey(name)) {
                neighboursIn.add(node);
            }
        }
        return new NodeNeighbours(
                neighboursIn.toArray(String[]::new),
                matrix.get(name).keySet().toArray(String[]::new)
        );
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

        int maxNodeName = 0;
        for (String node : allNodes) {
            maxNodeName = Math.max(node.length(), maxNodeName);
        }

        sb.append(" ".repeat(maxNodeName + 1));
        for (String to : allNodes) {
            sb.append(String.format("%s ", to));
        }
        sb.append("\n");

        for (String from : allNodes) {
            sb.append(String.format("%s", from));
            sb.append(" ".repeat(maxNodeName - from.length() + 1));
            for (String to : allNodes) {
                boolean hasEdge = matrix.get(from).containsKey(to);
                int toLen = to.length() / 2;
                sb.append(" ".repeat(toLen));
                sb.append(String.format("%s", hasEdge ? "1" : "0"));
                sb.append(" ".repeat(to.length() % 2 == 0 ? toLen : toLen + 1));
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

        GraphAdjacencyMatrix that = (GraphAdjacencyMatrix) o;
        return matrix.equals(that.matrix);
    }

    @Override
    public int hashCode() {
        return matrix.hashCode();
    }
}

