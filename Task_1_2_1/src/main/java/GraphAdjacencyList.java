import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph with adjacency list structure.
 */
public class GraphAdjacencyList<NodeType extends Serializable> implements Graph<NodeType> {
    private final HashMap<NodeType, LinkedList<NodeType>> lists = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeType[] getNodes() {
        //noinspection unchecked
        return (NodeType[]) lists.keySet().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(NodeType name) {
        lists.put(name, new LinkedList<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(NodeType name1, NodeType name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).add(name2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(NodeType name) {
        lists.remove(name);
        for (NodeType node : lists.keySet()) {
            lists.get(node).remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(NodeType name1, NodeType name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).remove(name2);
            lists.get(name2).remove(name1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours<NodeType> getNeighbours(NodeType name) {
        if (lists.containsKey(name)) {
            LinkedList<NodeType> neighboursOut = new LinkedList<>(lists.get(name));
            LinkedList<NodeType> neighboursIn = new LinkedList<>();
            for (NodeType node : getNodes()) {
                if (lists.get(node).contains(name) && !neighboursOut.contains(node)) {
                    neighboursIn.add(node);
                }
            }
            //noinspection unchecked
            return new NodeNeighbours<>(
                    (NodeType[])neighboursIn.toArray(),
                    (NodeType[])neighboursOut.toArray()
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

        NodeType[] allNodes = getNodes();
        if (allNodes.length == 0) {
            sb.append("(no nodes)\n");
            return sb.toString();
        }

        int maxNodeNameWidth = 0;
        for (NodeType node : allNodes) {
            maxNodeNameWidth = Math.max(maxNodeNameWidth, node.toString().length());
        }

        for (NodeType node : allNodes) {
            sb.append(node);
            sb.append(" ".repeat(maxNodeNameWidth - node.toString().length()));
            sb.append(" -> [");

            LinkedList<NodeType> neighbours = lists.get(node);
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
        GraphAdjacencyList<NodeType> that = (GraphAdjacencyList<NodeType>) o;

        return lists.equals(that.lists);
    }

    @Override
    public int hashCode() {
        return lists.hashCode();
    }
}

