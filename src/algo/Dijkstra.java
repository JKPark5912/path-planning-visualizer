package algo;

/**
 * Dijkstra's algorithm: A* with a heuristic of 0, so it expands nodes purely
 * in order of cost from the start.
 */
public class Dijkstra<N> extends AStar<N> {
    @Override
    public String name() {
        return "Dijkstra";
    }

    @Override
    protected double heuristic(N node) {
        return 0;
    }
}