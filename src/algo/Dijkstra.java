package algo;

import java.awt.Point;

/**
 * Dijkstra's algorithm: A* with a heuristic of zero, so cells are expanded
 * purely by distance from the start, with no pull toward the goal.
 */
public class Dijkstra extends AStar {
    @Override
    public String name() {
        return "Dijkstra";
    }

    @Override
    protected int heuristic(Point p) {
        return 0;
    }
}
