package algo;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import model.Grid;

/**
 * A search algorithm that runs ONE step at a time, so the UI can animate it.
 * BFS, Dijkstra and A* will all implement this, so the UI never needs to know
 * which one it is running.
 */
public interface PathFinder {
    /** Display name, e.g. "BFS". */
    String name();

    /** Resets all state and prepares to search the given grid from start to goal. */
    void init(Grid grid);

    /**
     * Expands ONE cell. Returns true if the search is finished
     * (goal found, or nothing left to explore), false otherwise.
     */
    boolean step();

    /** True once the search has finished. */
    boolean isDone();

    /** True if the goal was reached. */
    boolean foundPath();

    /** Cells already expanded (drawn in light blue). */
    Set<Point> visited();

    /** Cells discovered but not yet expanded (drawn in green). */
    Collection<Point> frontier();

    /** Start-to-goal path once found, otherwise an empty list (drawn in yellow). */
    List<Point> path();

    /** Number of cells expanded so far. This is the stat we compare across algorithms. */
    int nodesExpanded();
}
