import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Graph with incidence matrix structure.
 */
public class GraphIncidenceMatrix extends AbstractGraph {
    LinkedList<String> nodes = new LinkedList<>();
    LinkedList<HashMap<String, Integer>> matrix = new LinkedList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getNodes() {
        return nodes.toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNode(String name) {
        if (!nodes.contains(name)) {
            nodes.add(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(String name1, String name2) {
        if (nodes.contains(name1) && nodes.contains(name2)) {
            HashMap<String, Integer> edge = new HashMap<>();
            if (name1.equals(name2)) {
                edge.put(name1, 2);
            } else {
                edge.put(name1, -1);
                edge.put(name2, 1);
            }
            matrix.add(edge);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(String name) {
        if (nodes.remove(name)) {
            matrix.removeIf(edge -> edge.containsKey(name));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(String name1, String name2) {
        matrix.removeIf(edge -> edge.containsKey(name1) && edge.containsKey(name2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeNeighbours getNeighbours(String name) {
        LinkedList<String> neighboursIn = new LinkedList<>();
        LinkedList<String> neighboursOut = new LinkedList<>();
        for (HashMap<String, Integer> edge : matrix) {
            if (edge.containsKey(name)) {
                if (edge.get(name) == 2) {
                    neighboursIn.add(name);
                    neighboursOut.add(name);
                    continue;
                }
                for (String node : edge.keySet()) {
                    if (!node.equals(name)) {
                        if (edge.get(node) == 1) {
                            neighboursOut.add(node);
                        } else {
                            neighboursIn.add(node);
                        }
                        break;
                    }
                }
            }
        }
        return new NodeNeighbours(
                neighboursIn.toArray(String[]::new),
                neighboursOut.toArray(String[]::new)
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String[] allNodes = getNodes();
        int edgeCount = matrix.size();

        if (allNodes.length == 0) {
            sb.append("(no nodes)\n");
            return sb.toString();
        }

        int maxNodeNameWidth = 0;
        for (String node : allNodes) {
            maxNodeNameWidth = Math.max(maxNodeNameWidth, node.length());
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

        for (String node : allNodes) {
            sb.append(node);
            sb.append(" ".repeat(maxNodeNameWidth - node.length() + 1));

            for (int e = 0; e < edgeCount; e++) {
                HashMap<String, Integer> edge = matrix.get(e);
                Integer value = edge.get(node);
                String cell = (value != null) ? value.toString() : "0";

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphIncidenceMatrix that = (GraphIncidenceMatrix) o;

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
