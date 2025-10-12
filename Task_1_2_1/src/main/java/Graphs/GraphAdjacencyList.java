package Graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GraphAdjacencyList implements Graph {
    private final HashMap<String, LinkedList<String>> matrix = new HashMap<>();

    @Override
    public void addNode(String name) {
        matrix.put(name, new LinkedList<>());
    }

    @Override
    public void addEdge(String name1, String name2) {
        if (matrix.containsKey(name1) && matrix.containsKey(name2)) {
            matrix.get(name1).add(name2);
        }
    }

    @Override
    public void removeNode(String name) {
        LinkedList<String> adj = matrix.remove(name);
        if (adj != null) {
            for (String node : adj) {
                matrix.get(node).remove(name);
            }
        }
    }

    @Override
    public void removeEdge(String name1, String name2) {
        if (matrix.containsKey(name1) && matrix.containsKey(name2)) {
            matrix.get(name1).remove(name2);
            matrix.get(name2).remove(name1);
        }
    }

    @Override
    public String[] getNeighbours(String name) {
        if (matrix.containsKey(name)) {
            ArrayList<String> neighbours = new ArrayList<>(matrix.get(name));
            for (String node : matrix.keySet()) {
                if (matrix.get(node).contains(name) && !neighbours.contains(node)) {
                    neighbours.add(node);
                }
            }
            return neighbours.toArray(String[]::new);
        }
        return null;
    }

    @Override
    public Graph fromFile(String filepath) {
        return null;
    }
}

