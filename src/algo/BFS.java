package algo;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Grid;

/**
 * Breadth-first search on a grid. Cells are explored in rings of increasing
 * distance from the start, so the first time the goal is reached, the path
 * found is a shortest path (every move costs 1).
 *
 * <p>The search advances one cell per call to {@link #step()} so the UI can
 * animate it.
 */
public class BFS implements PathFinder {
    private Grid grid;
    /** Cells discovered but not yet expanded, in first-in, first-out order. */
    private final ArrayDeque<Point> queue = new ArrayDeque<>();
    /** Cells that have been removed from the queue and expanded. */
    private final Set<Point> visited = new HashSet<>();
    /**
     * Cells that have ever been added to the queue. Marking cells here when
     * they are queued (not when they are expanded) guarantees each cell is
     * queued at most once.
     */
    private final Set<Point> discovered = new HashSet<>();
    /** Maps each discovered cell to the cell it was first reached from. */
    private final Map<Point, Point> parent = new HashMap<>();
    private List<Point> path = new ArrayList<>();
    private boolean done;
    private boolean found;

    @Override
    public String name() {
        return "BFS";
    }

    /**
     * Clears any state left over from a previous run and seeds the queue with
     * the start cell, so the same BFS object can be reused across runs.
     */
    @Override
    public void init(Grid grid) {
        this.grid = grid;
        queue.clear();
        visited.clear();
        discovered.clear();
        parent.clear();
        path = new ArrayList<>();
        done = false;
        found = false;

        queue.add(grid.start());
        discovered.add(grid.start());
    }

    /**
     * Expands the next cell in the queue. If it is the goal, the search ends
     * and the path is built; otherwise each undiscovered neighbor is recorded
     * with its parent and added to the back of the queue.
     *
     * @return true if the search has finished (goal found or queue empty)
     */
    @Override
    public boolean step() {
        if (done) {
            return true;
        }

        if (queue.isEmpty()) {
            done = true;
            found = false;
            return true;
        }

        Point cur = queue.poll();
        visited.add(cur);

        if (cur.equals(grid.goal())) {
            done = true;
            found = true;
            buildPath();
            return true;
        }

        for (Point n : grid.neighbors(cur)) {
            if (!discovered.contains(n)) {
                discovered.add(n);
                parent.put(n, cur);
                queue.add(n);
            }
        }

        return false;
    }

    /**
     * Rebuilds the start-to-goal path by following parent links backward
     * from the goal. The start has no parent, so the walk stops there; the
     * list is then reversed so it runs from start to goal.
     */
    private void buildPath() {
        Point cur = grid.goal();

        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }

        Collections.reverse(path);
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean foundPath() {
        return found;
    }

    @Override
    public Set<Point> visited() {
        return visited;
    }

    @Override
    public Collection<Point> frontier() {
        return queue;
    }

    @Override
    public List<Point> path() {
        return path;
    }

    @Override
    public int nodesExpanded() {
        return visited.size();
    }
}
