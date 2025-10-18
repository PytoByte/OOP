import Graphs.*;

public class Main {
    public static void main(String[] args) {
        Graph g = new GraphAdjacencyList();

        String[] nodes = {"lex", "parse", "optimize", "build", "link", "codegen"};
        for (String node : nodes) {
            g.addNode(node);
        }
        // Добавляем рёбра согласно зависимостям
        g.addEdge("lex", "parse");
        g.addEdge("lex", "optimize");
        g.addEdge("parse", "build");
        g.addEdge("optimize", "build");
        g.addEdge("build", "link");
        g.addEdge("codegen", "link");

        System.out.println(g);
        g.toFile("g.txt");
        Graph g1 = new GraphIncidenceMatrix();
        g1.fromFile("g.txt");
        System.out.println(g1);
        Graph g2 = new GraphAdjacencyMatrix();
        g2.fromFile("g.txt");
        System.out.println(g2);
    }
}
