package algo;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Grid;

/**
 * Breadth-first search: explores outward in rings, so the first time it
 * reaches the goal, that path is the shortest (every move costs 1).
 *
 * TODO (Justin): implement init, step, and path reconstruction.
 */
public class BFS implements PathFinder {
    private Grid grid;
    private final ArrayDeque<Point> queue = new ArrayDeque<>();   // the frontier
    private final Set<Point> visited = new HashSet<>();           // already expanded
    private final Set<Point> discovered = new HashSet<>();        // ever added to queue
    private final Map<Point, Point> parent = new HashMap<>();     // child -> the cell we came from
    private List<Point> path = new ArrayList<>();
    private boolean done;
    private boolean found;

    @Override public String name() { return "BFS"; }

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
        // TODO: put the start cell into the queue and mark it discovered.
    }

    @Override
    public boolean step() {
        if (done) return true;
        // TODO:
        //  1. If the queue is empty: done = true, found = false, return true.
        //  2. Remove the next cell from the FRONT of the queue; add it to visited.
        //  3. If it is the goal: done = true, found = true, build the path, return true.
        //  4. For each neighbor (grid.neighbors(cell)) not yet discovered:
        //       mark discovered, record parent.put(neighbor, cell), add to the BACK of the queue.
        //  5. return false.
        return false;
    }

    /**
     * TODO: walk backward from the goal using the parent map until you reach
     * the start, then reverse so the list goes start -> goal.
     */
    private void buildPath() {
    }

    @Override public boolean isDone() { return done; }
    @Override public boolean foundPath() { return found; }
    @Override public Set<Point> visited() { return visited; }
    @Override public Collection<Point> frontier() { return queue; }
    @Override public List<Point> path() { return path; }
    @Override public int nodesExpanded() { return visited.size(); }
}
