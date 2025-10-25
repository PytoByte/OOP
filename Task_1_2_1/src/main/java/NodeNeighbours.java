import java.util.List;

/**
 * Supporting class for returning node neighbours.
 *
 * @param in array of incoming neighbours
 * @param out array of outgoing neighbors
 */
public record NodeNeighbours<NodeType>(
        List<NodeType> in,
        List<NodeType> out
) {}
