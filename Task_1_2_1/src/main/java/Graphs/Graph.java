package Graphs;

public interface Graph {
    void addNode(String name);

    void addEdge(String name1, String name2);

    void removeNode(String name);

    void removeEdge(String name1, String name2);

    String[] getNeighbours(String name);

    Graph fromFile(String filepath);
}
