package Graphs;

import java.util.*;

public class GraphUtils {
    public static boolean equal(Graph g1, Graph g2) {
        if (g1 == g2) return true;
        if (g1 == null || g2 == null) return false;

        // 1. Сравниваем множества узлов
        Set<String> nodes1 = new HashSet<>(Arrays.asList(g1.getNodes()));
        Set<String> nodes2 = new HashSet<>(Arrays.asList(g2.getNodes()));
        if (!nodes1.equals(nodes2)) {
            return false;
        }

        // 2. Для каждого узла сравниваем исходящие рёбра (out)
        for (String node : nodes1) {
            NodeNeighbours n1 = g1.getNeighbours(node);
            NodeNeighbours n2 = g2.getNeighbours(node);

            if (n1 == null || n2 == null) return false;

            Set<String> out1 = new HashSet<>(Arrays.asList(n1.out()));
            Set<String> out2 = new HashSet<>(Arrays.asList(n2.out()));

            if (!out1.equals(out2)) {
                return false;
            }
        }

        return true;
    }
}