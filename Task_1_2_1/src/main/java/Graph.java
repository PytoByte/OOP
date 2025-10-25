import exceptions.GraphException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.readAllLines;

/**
 * Interface representing a graph with basic operations
 * and serialization/deserialization to/from files.
 */
public interface Graph<NodeType extends Serializable> {

    /**
     * Returns an array of all nodes in the graph.
     *
     * @return list of nodes, or empty list if graph has no nodes
     */
    List<NodeType> getNodes();

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
     * or null if the node doesn't exist
     */
    NodeNeighbours<NodeType> getNeighbours(NodeType name);

    /**
     * Serializes the graph and writes it to a file.
     *
     * @param filepath the path to the output file
     * @throws RuntimeException if an I/O error occurs during writing
     */
    default void toFile(String filepath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filepath)))) {
            List<NodeType> nodes = getNodes();
            oos.writeInt(nodes.size()); // Записываем количество узлов
            // Записываем узлы
            for (NodeType node : nodes) {
                oos.writeObject(node);
            }

            // Записываем рёбра
            for (NodeType node : nodes) {
                for (NodeType target : getNeighbours(node).out()) {
                    oos.writeObject(node);
                    oos.writeObject(target);
                }
            }
            oos.flush();
        } catch (IOException e) {
            throw new GraphException(
                    String.format("Error writing graph to file: %s. Because %s", filepath, e)
            );
        }
    }

    /**
     * Reads a graph from a file and initializes the current instance.
     *
     * @param filepath the path to the input file
     * @throws RuntimeException         if an I/O error occurs during reading
     * @throws IllegalArgumentException if the file format is invalid
     */
    default void fromFile(String filepath) {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filepath)))) {
            int nodeCount = ois.readInt(); // Читаем количество узлов
            if (nodeCount < 0) {
                throw new GraphException("Invalid file format: negative node count.");
            }

            // Читаем и добавляем узлы
            for (int i = 0; i < nodeCount; i++) {
                @SuppressWarnings("unchecked")
                NodeType node = (NodeType) ois.readObject();
                addNode(node);
            }
            try {
                //noinspection InfiniteLoopStatement
                while (true) { // Бесконечный цикл, который завершится исключением EOF
                    @SuppressWarnings("unchecked") NodeType source = (NodeType) ois.readObject();
                    @SuppressWarnings("unchecked") NodeType target = (NodeType) ois.readObject();
                    addEdge(source, target);
                }
            } catch (EOFException e) {
                // Ожидаемое завершение цикла, когда закончились объекты
            }

        } catch (IOException e) {
            throw new GraphException(String.format("Error reading graph from file: %s. Because: %s", filepath, e));
        } catch (ClassNotFoundException e) {
            throw new GraphException(String.format("Class not found during deserialization while reading file: %s. Because: %s", filepath, e));
        }
    }
}
