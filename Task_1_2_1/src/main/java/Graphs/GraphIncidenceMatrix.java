package Graphs;

import java.util.HashMap;
import java.util.LinkedList;

public class GraphIncidenceMatrix implements Graph {
    LinkedList<String> nodes = new LinkedList<>();
    LinkedList<HashMap<String, Integer>> matrix = new LinkedList<>();

    @Override
    public String[] getNodes() {
        return nodes.toArray(String[]::new);
    }

    @Override
    public void addNode(String name) {
        if (!nodes.contains(name)) {
            nodes.add(name);
        }
    }

    @Override
    public void addEdge(String name1, String name2) {
        if (nodes.contains(name1) && nodes.contains(name2)) {
            HashMap<String, Integer> edge = new HashMap<>();
            edge.put(name1, -1);
            edge.put(name2, 1);
            matrix.add(edge);
        }
    }

    @Override
    public void removeNode(String name) {
        if (nodes.remove(name)) {
            matrix.removeIf(edge -> edge.containsKey(name));
        }
    }

    @Override
    public void removeEdge(String name1, String name2) {
        matrix.removeIf(edge -> edge.containsKey(name1) && edge.containsKey(name2));
    }

    @Override
    public String[] getNeighbours(String name) {
        LinkedList<String> neighbours = new LinkedList<>();
        for (HashMap<String, Integer> edge : matrix) {
            if (edge.containsKey(name)) {
                for (String node : edge.keySet()) {
                    if (!node.equals(name)) {
                        neighbours.add(node);
                        break;
                    }
                }
            }
        }
        return neighbours.toArray(String[]::new);
    }

    @Override
    public Graph fromFile(String filepath) {
        return null;
    }
}
