package Graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GraphAdjacencyList implements Graph {
    private final HashMap<String, LinkedList<String>> lists = new HashMap<>();

    @Override
    public String[] getNodes() {
        return lists.keySet().toArray(String[]::new);
    }

    @Override
    public void addNode(String name) {
        lists.put(name, new LinkedList<>());
    }

    @Override
    public void addEdge(String name1, String name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).add(name2);
        }
    }

    @Override
    public void removeNode(String name) {
        LinkedList<String> adj = lists.remove(name);
        if (adj != null) {
            for (String node : adj) {
                lists.get(node).remove(name);
            }
        }
    }

    @Override
    public void removeEdge(String name1, String name2) {
        if (lists.containsKey(name1) && lists.containsKey(name2)) {
            lists.get(name1).remove(name2);
            lists.get(name2).remove(name1);
        }
    }

    @Override
    public String[] getNeighbours(String name) {
        if (lists.containsKey(name)) {
            ArrayList<String> neighbours = new ArrayList<>(lists.get(name));
            for (String node : lists.keySet()) {
                if (lists.get(node).contains(name) && !neighbours.contains(node)) {
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

