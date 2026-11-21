package graph;

import java.util.List;

/** A graph that search algorithms can run on, with node type N. */
public interface SearchGraph<N> {

    /** A one-way connection to a neighbor, with the cost of moving along it. */
    record Edge<N>(N to, double cost) {}

    N start();

    N goal();

    /** Every node reachable in one move from node, with each move's cost. */
    List<Edge<N>> neighbors(N node);

    /**
     * Estimated cost from node to the goal. It must never overestimate,
     * or A* can return a path that isn't the shortest.
     */
    double heuristic(N node);
}