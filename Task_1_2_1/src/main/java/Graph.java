/**
 * Interface representing a graph with basic operations
 * and serialization/deserialization to/from files.
 */
public interface Graph {

    /**
     * Returns an array of all nodes in the graph.
     *
     * @return array of node names, or empty array if graph has no nodes
     */
    String[] getNodes();

    /**
     * Adds a new node to the graph.
     *
     * @param name the name of the node to add
     * @throws IllegalArgumentException if node name is null or empty
     */
    void addNode(String name);

    /**
     * Adds an edge between two existing nodes.
     * For directed graphs, the edge goes from name1 to name2.
     * For undirected graphs, creates a bidirectional connection.
     *
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     * @throws IllegalArgumentException if either node doesn't exist
     */
    void addEdge(String name1, String name2);

    /**
     * Removes a node and all edges connected to it from the graph.
     *
     * @param name the name of the node to remove
     */
    void removeNode(String name);

    /**
     * Removes an edge between two nodes.
     *
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     */
    void removeEdge(String name1, String name2);

    /**
     * Returns the neighbors of a specified node.
     * For directed graphs, separates incoming and outgoing neighbors.
     *
     * @param name the name of the node to get neighbors for
     * @return NodeNeighbours object containing incoming and outgoing neighbors,
     *         or null if the node doesn't exist
     */
    NodeNeighbours getNeighbours(String name);

    /**
     * Serializes the graph and writes it to a file.
     *
     * @param filepath the path to the output file
     * @throws RuntimeException if an I/O error occurs during writing
     */
    void toFile(String filepath);

    /**
     * Reads a graph from a file and initializes the current instance.
     *
     * @param filepath the path to the input file
     * @throws RuntimeException if an I/O error occurs during reading
     * @throws IllegalArgumentException if the file format is invalid
     */
    void fromFile(String filepath);
}
