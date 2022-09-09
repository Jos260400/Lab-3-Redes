package com.example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class Dijkstra<T> {

    public void calculateShortestPath(Nodo<T> source) {
        source.setDistance(0);
        Set<Nodo<T>> settledNodos = new HashSet<>();
        Queue<Nodo<T>> unsettledNodos = new PriorityQueue<>(Collections.singleton(source));
        while (!unsettledNodos.isEmpty()) {
            Nodo<T> currentNodo = unsettledNodos.poll();
            currentNodo.getAdjacentNodes()
                    .entrySet().stream()
                    .filter(entry -> !settledNodos.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentNodo);
                        unsettledNodos.add(entry.getKey());
                    });
            settledNodos.add(currentNodo);
        }
    }

    private void evaluateDistanceAndPath(Nodo<T> adjacentNodo, Integer edgeWeight, Nodo<T> sourceNodo) {
        Integer newDistance = sourceNodo.getDistance() + edgeWeight;
        if (newDistance < adjacentNodo.getDistance()) {
            adjacentNodo.setDistance(newDistance);
            adjacentNodo.setShortestPath(Stream.concat(sourceNodo.getShortestPath().stream(), Stream.of(sourceNodo)).toList());
        }
    }

    public void printPaths(List<Nodo<T>> nodes) {
        nodes.forEach(node -> {
            String path = node.getShortestPath().stream()
                    .map(Nodo::getName).map(Objects::toString)
                    .collect(Collectors.joining(" -> "));
            System.out.println((path.isBlank()
                    ? "%s : %s".formatted(node.getName(), node.getDistance())
                    : "%s -> %s : %s".formatted(path, node.getName(), node.getDistance()))
            );
        });
    }

}