import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph with adjacency matrix structure.
 */
public class GraphAdjacencyMatrix<NodeType extends Serializable> implements Graph<NodeType> {
    HashMap<NodeType, HashMap<NodeType, Boolean>> matrix = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeType[] getNodes() {
        //noinspection unchecked
        return (NodeType[]) matrix.keySet().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(NodeType name) {
        matrix.put(name, new HashMap<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(NodeType name1, NodeType name2) {
        matrix.get(name1).put(name2, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(NodeType name) {
        matrix.remove(name);
        for (NodeType node : matrix.keySet()) {
            matrix.get(node).remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(NodeType name1, NodeType name2) {
        matrix.get(name1).remove(name2);
        matrix.get(name2).remove(name1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours<NodeType> getNeighbours(NodeType name) {
        LinkedList<NodeType> neighboursIn = new LinkedList<>();
        for (NodeType node : getNodes()) {
            if (matrix.get(node).containsKey(name)) {
                neighboursIn.add(node);
            }
        }
        //noinspection unchecked
        return new NodeNeighbours<>(
                (NodeType[])neighboursIn.toArray(),
                (NodeType[])matrix.get(name).keySet().toArray()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        NodeType[] allNodes = getNodes();
        if (allNodes.length == 0) {
            sb.append("(no nodes)\n");
            return sb.toString();
        }

        int maxNodeName = 0;
        for (NodeType node : allNodes) {
            maxNodeName = Math.max(node.toString().length(), maxNodeName);
        }

        sb.append(" ".repeat(maxNodeName + 1));
        for (NodeType to : allNodes) {
            sb.append(String.format("%s ", to));
        }
        sb.append("\n");

        for (NodeType from : allNodes) {
            sb.append(String.format("%s", from));
            sb.append(" ".repeat(maxNodeName - from.toString().length() + 1));
            for (NodeType to : allNodes) {
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
        GraphAdjacencyMatrix<NodeType> that = (GraphAdjacencyMatrix<NodeType>) o;
        return matrix.equals(that.matrix);
    }

    @Override
    public int hashCode() {
        return matrix.hashCode();
    }
}

