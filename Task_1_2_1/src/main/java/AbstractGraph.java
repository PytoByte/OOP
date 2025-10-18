import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.readAllLines;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Abstract graph class which contains
 * serialization/deserialization to/from files realizations.
 */
public abstract class AbstractGraph implements Graph {
    /**
     * {@inheritDoc}
     */
    public void toFile(String filepath) {
        try (BufferedWriter writer = newBufferedWriter(Paths.get(filepath))) {
            String[] nodes = getNodes();

            writer.write(nodes.length + "\n");
            for (String node : nodes) {
                writer.write(node + "\n");
            }

            for (String node : nodes) {
                NodeNeighbours neighbours = getNeighbours(node);
                for (String target : neighbours.out()) {
                    writer.write(node + " " + target + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing graph to file: " + filepath, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void fromFile(String filepath) {
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
                String[] parts = lines.get(i).split("\\s+", 2);
                if (parts.length == 2) {
                    addEdge(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading graph from file: " + filepath, e);
        }
    }
}