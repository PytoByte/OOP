import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Graph with incidence matrix structure.
 */
public class GraphIncidenceMatrix<T extends Serializable> implements Graph<T> {
    LinkedList<T> nodes = new LinkedList<>();
    LinkedList<HashMap<T, IncidenceMatrixValues>> matrix = new LinkedList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getNodes() {
        return nodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(T name) {
        if (!nodes.contains(name)) {
            nodes.add(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(T name1, T name2) {
        if (nodes.contains(name1) && nodes.contains(name2)) {
            HashMap<T, IncidenceMatrixValues> edge = new HashMap<>();
            if (name1.equals(name2)) {
                edge.put(name1, IncidenceMatrixValues.SELF_LOOP);
            } else {
                edge.put(name1, IncidenceMatrixValues.FROM);
                edge.put(name2, IncidenceMatrixValues.TO);
            }
            matrix.add(edge);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(T name) {
        if (nodes.remove(name)) {
            matrix.removeIf(edge -> edge.containsKey(name));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(T name1, T name2) {
        matrix.removeIf(edge -> edge.containsKey(name1) && edge.containsKey(name2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours<T> getNeighbours(T name) {
        LinkedList<T> neighboursIn = new LinkedList<>();
        LinkedList<T> neighboursOut = new LinkedList<>();
        for (HashMap<T, IncidenceMatrixValues> edge : matrix) {
            if (edge.containsKey(name)) {
                if (edge.get(name) == IncidenceMatrixValues.SELF_LOOP) {
                    neighboursIn.add(name);
                    neighboursOut.add(name);
                    continue;
                }
                for (T node : edge.keySet()) {
                    if (!node.equals(name)) {
                        if (edge.get(node) == IncidenceMatrixValues.TO) {
                            neighboursOut.add(node);
                        } else if (edge.get(node) == IncidenceMatrixValues.FROM) {
                            neighboursIn.add(node);
                        }
                        break;
                    }
                }
            }
        }
        return new NodeNeighbours<>(
                neighboursIn,
                neighboursOut
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<T> allNodes = getNodes();
        int edgeCount = matrix.size();

        if (allNodes.isEmpty()) {
            sb.append("(no nodes)\n");
            return sb.toString();
        }

        int maxNodeNameWidth = 0;
        for (T node : allNodes) {
            maxNodeNameWidth = Math.max(maxNodeNameWidth, node.toString().length());
        }

        String[] edgeLabels = new String[edgeCount];
        int maxEdgeLabelWidth = 0;
        for (int i = 0; i < edgeCount; i++) {
            edgeLabels[i] = "E" + i;
            maxEdgeLabelWidth = Math.max(maxEdgeLabelWidth, edgeLabels[i].length());
        }

        sb.append(" ".repeat(maxNodeNameWidth + 1));
        for (String label : edgeLabels) {
            sb.append(String.format("%" + maxEdgeLabelWidth + "s ", label));
        }
        sb.append("\n");

        for (T node : allNodes) {
            sb.append(node);
            sb.append(" ".repeat(maxNodeNameWidth - node.toString().length() + 1));

            for (int e = 0; e < edgeCount; e++) {
                HashMap<T, IncidenceMatrixValues> edge = matrix.get(e);
                IncidenceMatrixValues value = edge.get(node);
                String cell = (value != null) ? value.value : IncidenceMatrixValues.EMPTY.value;

                int padTotal = maxEdgeLabelWidth - cell.length();
                int padLeft = padTotal / 2;
                int padRight = padTotal - padLeft;

                sb.append(" ".repeat(padLeft))
                        .append(cell)
                        .append(" ".repeat(padRight))
                        .append(" ");
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
        GraphIncidenceMatrix<T> that = (GraphIncidenceMatrix<T>) o;

        if (!new HashSet<>(nodes).equals(new HashSet<>(that.nodes))) {
            return false;
        }

        return new HashSet<>(matrix).equals(new HashSet<>(that.matrix));
    }

    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<>(nodes), new HashSet<>(matrix));
    }
}
