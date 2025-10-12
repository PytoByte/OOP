package Graphs;

import java.util.HashMap;

public class GraphAdjacencyMatrix implements Graph {
    HashMap<String, HashMap<String, Boolean>> matrix = new HashMap<>();

    @Override
    public void addNode(String name) {
        matrix.put()
    }

    @Override
    public void addEdge(String name1, String name2) {

    }

    @Override
    public void removeNode(String name) {

    }

    @Override
    public void removeEdge(String name1, String name2) {

    }

    @Override
    public String[] getNeighbours(String name) {
        return new String[0];
    }

    @Override
    public Graph fromFile(String filepath) {
        return null;
    }
}

