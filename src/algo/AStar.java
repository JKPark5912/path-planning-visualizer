package algo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import model.Grid;

/**
 * A* search: expands the cell with the lowest f = g + h, where g is the
 * steps taken so far and h estimates the steps remaining. With an
 * admissible heuristic (one that never overestimates), the path found is
 * a shortest path.
 */
public class AStar implements PathFinder {
    /** One queue entry: a cell, its f = g + h, and its g. */
    private record Entry(Point p, int f, int g) {}

    protected Grid grid;
    /** Cells waiting to be expanded, lowest f first; ties go to larger g. */
    private final PriorityQueue<Entry> open = new PriorityQueue<>(
        (a, b) -> a.f() != b.f()
            ? Integer.compare(a.f(), b.f())   // lower f first
            : Integer.compare(b.g(), a.g())); // tie: larger g (closer to goal) first
    private final Set<Point> visited = new HashSet<>();
    /** Best known number of steps from the start to each cell. */
    private final Map<Point, Integer> gScore = new HashMap<>();
    private final Map<Point, Point> parent = new HashMap<>();
    private List<Point> path = new ArrayList<>();
    private boolean done;
    private boolean found;

    @Override
    public String name() {
        return "A*";
    }

    /**
     * Manhattan distance from p to the goal: the fewest moves possible on a
     * 4-direction grid with no walls. It never overestimates, so A* still
     * finds a shortest path. Subclasses can override this (Dijkstra uses 0).
     */
    protected int heuristic(Point p) {
        Point g = grid.goal();
        return Math.abs(p.x - g.x) + Math.abs(p.y - g.y);
    }

    /**
     * Clears state from any previous run, then seeds the search with the
     * start cell at cost 0.
     */
    @Override
    public void init(Grid grid) {
        this.grid = grid;

        open.clear();
        visited.clear();
        gScore.clear();
        parent.clear();

        path = new ArrayList<>();
        done = false;
        found = false;

        gScore.put(grid.start(), 0);
        open.add(new Entry(grid.start(), heuristic(grid.start()), 0));
    }

    /**
     * Expands the open cell with the lowest f. Java's PriorityQueue cannot
     * lower an entry's priority, so a cell reached by a shorter route is
     * simply added again; stale copies are skipped when they reach the
     * front (lazy deletion).
     *
     * @return true if the search has finished (goal found or nothing left)
     */
    @Override
    public boolean step() {
        if (done) {
            return true;
        }

        while (!open.isEmpty() && visited.contains(open.peek().p())) {
            open.poll();
        }

        if (open.isEmpty()) {
            done = true;
            found = false;
            return true;
        }

        Entry e = open.poll();
        Point cur = e.p();
        visited.add(cur);

        if (cur.equals(grid.goal())) {
            done = true;
            found = true;
            buildPath();
            return true;
        }

        int curG = gScore.get(cur);
        for (Point n : grid.neighbors(cur)) {
            int newG = curG + 1;

            if (newG < gScore.getOrDefault(n, Integer.MAX_VALUE)) {
                gScore.put(n, newG);
                parent.put(n, cur);
                open.add(new Entry(n, newG + heuristic(n), newG));
            }
        }

        return false;
    }

    /**
     * Rebuilds the start-to-goal path by following parent links backward
     * from the goal, then reversing.
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
        List<Point> pts = new ArrayList<>();
        for (Entry e : open) {
            pts.add(e.p());
        }
        return pts;
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
