package com.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Nodo<T> implements Comparable<Nodo<T>> {

    private final T name;
    private Integer distance = Integer.MAX_VALUE;
    private List<Nodo<T>> shortestPath = new LinkedList<>();
    private Map<Nodo<T>, Integer> adjacentNodes = new HashMap<>();

    public void addAdjacentNode(Nodo<T> node, int weight) {
        adjacentNodes.put(node, weight);
    }

    @Override
    public int compareTo(Nodo node) {
        return Integer.compare(this.distance, node.getDistance());
    }

}