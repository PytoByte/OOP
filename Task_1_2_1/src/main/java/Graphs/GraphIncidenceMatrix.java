package Graphs;

import java.util.HashMap;

public class GraphIncidenceMatrix implements Graph {
    HashMap<String, List<String, Integer>> matrix = new HashMap<>();

    @Override
    public void addNode(String name) {
        matrix.put(name, new HashMap<>());
    }

    @Override
    public void addEdge(String name1, String name2) {
        matrix.get(name1).put(name2, 1);
        matrix.get(name2).put(name1, -1);
    }

    @Override
    public void removeNode(String name) {
        HashMap<String, Integer> value = matrix.remove(name);
        for (String key : value.keySet()) {
            matrix.get(key).remove(name);
        }
    }

    @Override
    public void removeEdge(String name1, String name2) {
        matrix.get(name1).remove(name2);
        matrix.get(name2).remove(name1);
    }

    @Override
    public String[] getNeighbours(String name) {
        return matrix.get(name).keySet().toArray(String[]::new);
    }

    @Override
    public Graph fromFile(String filepath) {
        return null;
    }
}
