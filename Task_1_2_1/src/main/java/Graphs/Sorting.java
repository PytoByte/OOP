package Graphs;

import java.util.*;

public class Sorting {
    public static String[] topologicalSort(Graph graph) {
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();
        Queue<String> zeroInDegreeQueue = new LinkedList<>();
        List<String> result = new ArrayList<>();

        // Инициализация
        String[] nodes = graph.getNodes();
        for (String node : nodes) {
            inDegree.put(node, 0);
            adjList.put(node, new ArrayList<>());
        }

        // Построение списка смежности и подсчёт входящих степеней
        for (String node : nodes) {
            NodeNeighbours neighbours = graph.getNeighbours(node);
            String[] out = neighbours.out();
            for (String to : out) {
                adjList.get(node).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Начинаем с узлов, у которых входящая степень = 0
        for (String node : nodes) {
            if (inDegree.get(node) == 0) {
                zeroInDegreeQueue.offer(node);
            }
        }

        // Основной цикл алгоритма Кана
        while (!zeroInDegreeQueue.isEmpty()) {
            String current = zeroInDegreeQueue.poll();
            result.add(current);

            for (String neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    zeroInDegreeQueue.offer(neighbor);
                }
            }
        }

        // Проверка на цикл
        if (result.size() != nodes.length) {
            throw new IllegalArgumentException("Graph has a cycle, topological sort is not possible.");
        }

        return result.toArray(new String[0]);
    }
}
