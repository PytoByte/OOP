import exceptions.GraphException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.readAllLines;

/**
 * Interface representing a graph with basic operations
 * and serialization/deserialization to/from files.
 */
public interface Graph<NodeType> {

    /**
     * Returns an array of all nodes in the graph.
     *
     * @return array of node names, or empty array if graph has no nodes
     */
    NodeType[] getNodes();

    /**
     * Adds a new node to the graph.
     *
     * @param name the name of the node to add
     * @throws IllegalArgumentException if node name is null or empty
     */
    void addNode(NodeType name);

    /**
     * Adds an edge between two existing nodes.
     * For directed graphs, the edge goes from name1 to name2.
     * For undirected graphs, creates a bidirectional connection.
     *
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     * @throws IllegalArgumentException if either node doesn't exist
     */
    void addEdge(NodeType name1, NodeType name2);

    /**
     * Removes a node and all edges connected to it from the graph.
     *
     * @param name the name of the node to remove
     */
    void removeNode(NodeType name);

    /**
     * Removes an edge between two nodes.
     *
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     */
    void removeEdge(NodeType name1, NodeType name2);

    /**
     * Returns the neighbors of a specified node.
     * For directed graphs, separates incoming and outgoing neighbors.
     *
     * @param name the name of the node to get neighbors for
     * @return NodeNeighbours object containing incoming and outgoing neighbors,
     *         or null if the node doesn't exist
     */
    NodeNeighbours<NodeType> getNeighbours(NodeType name);

    /**
     * Serializes the graph and writes it to a file.
     *
     * @param filepath the path to the output file
     * @throws RuntimeException if an I/O error occurs during writing
     */
    default void toFile(String filepath) {
        try (BufferedWriter writer = newBufferedWriter(Paths.get(filepath))) {
            NodeType[] nodes = getNodes();

            writer.write(nodes.length + "\n");
            for (NodeType node : nodes) {
                writer.write(node + "\n");
            }

            for (NodeType node : nodes) {
                NodeNeighbours<NodeType> neighbours = getNeighbours(node);
                for (NodeType target : neighbours.out()) {
                    writer.write(node + " " + target.toString() + "\n");
                }
            }
        } catch (IOException e) {
            throw new GraphException(String.format(
                    "Error reading graph from file: %s\nCausing by: %s",
                    filepath, e
            ));
        }
    }

    /**
     * Reads a graph from a file and initializes the current instance.
     *
     * @param filepath the path to the input file
     * @throws RuntimeException if an I/O error occurs during reading
     * @throws IllegalArgumentException if the file format is invalid
     */
    default void fromFile(String filepath) {
        try {
            List<String> lines = readAllLines(Paths.get(filepath));
            if (lines.isEmpty()) {
                return;
            }

            int nodeCount = Integer.parseInt(lines.get(0));
            for (int i = 1; i <= nodeCount && i < lines.size(); i++) {
                addNode(lines.get(i).trim());
            }

            for (int i = nodeCount + 1; i < lines.size(); i++) {
                NodeType[] parts = lines.get(i).split("\\s+", 2);
                if (parts.length == 2) {
                    addEdge(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new GraphException(String.format(
                    "Error reading graph from file: %s\nCausing by: %s",
                    filepath, e
            ));
        }
    }
}
