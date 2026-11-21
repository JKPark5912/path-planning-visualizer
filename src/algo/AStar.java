package algo;

import graph.SearchGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A* search on any SearchGraph: expands the node with the lowest f = g + h,
 * where g is the cost so far and h estimates the cost remaining. With an
 * admissible heuristic (one that never overestimates), the path found is
 * a shortest path.
 */
public class AStar<N> implements PathFinder<N> {
    /** One queue entry: a node, its f = g + h, and its g. (changed: generic, double costs) */
    private record Entry<N>(N node, double f, double g) {}

    protected SearchGraph<N> graph;
    /** Nodes waiting to be expanded, lowest f first; ties go to larger g. */
    private final PriorityQueue<Entry<N>> open = new PriorityQueue<>(
            (a, b) -> a.f() != b.f()
                    ? Double.compare(a.f(), b.f())   // lower f first
                    : Double.compare(b.g(), a.g())); // tie: larger g (closer to goal) first
    private final Set<N> visited = new HashSet<>();
    /** Lowest known cost from the start to each node. */
    private final Map<N, Double> gScore = new HashMap<>();
    private final Map<N, N> parent = new HashMap<>();
    private List<N> path = new ArrayList<>();
    private boolean done;
    private boolean found;

    @Override
    public String name() {
        return "A*";
    }

    /**
     * Estimated cost from node to the goal. (changed) The graph supplies it,
     * since only the graph knows its geometry. Dijkstra overrides this with 0.
     */
    protected double heuristic(N node) {
        return graph.heuristic(node);
    }

    /** Clears state from any previous run, then seeds the search with the start at cost 0. */
    @Override
    public void init(SearchGraph<N> graph) {
        this.graph = graph;

        open.clear();
        visited.clear();
        gScore.clear();
        parent.clear();

        path = new ArrayList<>();
        done = false;
        found = false;

        gScore.put(graph.start(), 0.0);
        open.add(new Entry<>(graph.start(), heuristic(graph.start()), 0.0));
    }

    /**
     * Expands the open node with the lowest f. Java's PriorityQueue cannot
     * lower an entry's priority, so a node reached by a cheaper route is
     * added again; stale copies are skipped when they reach the front
     * (lazy deletion).
     *
     * @return true if the search has finished (goal found or nothing left)
     */
    @Override
    public boolean step() {
        if (done) {
            return true;
        }

        while (!open.isEmpty() && visited.contains(open.peek().node())) {
            open.poll();
        }

        if (open.isEmpty()) {
            done = true;
            found = false;
            return true;
        }

        N cur = open.poll().node();
        visited.add(cur);

        if (cur.equals(graph.goal())) {
            done = true;
            found = true;
            buildPath();
            return true;
        }

        double curG = gScore.get(cur);
        // changed: each edge carries its own cost instead of every move costing 1
        for (SearchGraph.Edge<N> edge : graph.neighbors(cur)) {
            N n = edge.to();
            double newG = curG + edge.cost();

            if (newG < gScore.getOrDefault(n, Double.POSITIVE_INFINITY)) {
                gScore.put(n, newG);
                parent.put(n, cur);
                open.add(new Entry<>(n, newG + heuristic(n), newG));
            }
        }

        return false;
    }

    /** Rebuilds the start-to-goal path by following parent links back from the goal. */
    private void buildPath() {
        N cur = graph.goal();

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
    public Set<N> visited() {
        return visited;
    }

    @Override
    public Collection<N> frontier() {
        List<N> nodes = new ArrayList<>();
        for (Entry<N> e : open) {
            nodes.add(e.node());
        }
        return nodes;
    }

    @Override
    public List<N> path() {
        return path;
    }

    @Override
    public int nodesExpanded() {
        return visited.size();
    }
}