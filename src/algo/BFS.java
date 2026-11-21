package algo;

import graph.SearchGraph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Breadth-first search on any SearchGraph. Nodes are explored in rings of
 * increasing hop count from the start, so the first time the goal is reached,
 * the path found has the fewest edges. Edge costs are ignored: BFS minimizes
 * hops, not distance.
 */
public class BFS<N> implements PathFinder<N> {
    private SearchGraph<N> graph;
    private final ArrayDeque<N> queue = new ArrayDeque<>();
    private final Set<N> visited = new HashSet<>();
    /** Marked when queued, not when expanded, so each node is queued at most once. */
    private final Set<N> discovered = new HashSet<>();
    private final Map<N, N> parent = new HashMap<>();
    private List<N> path = new ArrayList<>();
    private boolean done;
    private boolean found;

    @Override
    public String name() {
        return "BFS";
    }

    @Override
    public void init(SearchGraph<N> graph) {
        this.graph = graph;
        queue.clear();
        visited.clear();
        discovered.clear();
        parent.clear();
        path = new ArrayList<>();
        done = false;
        found = false;

        queue.add(graph.start());
        discovered.add(graph.start());
    }

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

        N cur = queue.poll();
        visited.add(cur);

        if (cur.equals(graph.goal())) {
            done = true;
            found = true;
            buildPath();
            return true;
        }

        for (SearchGraph.Edge<N> edge : graph.neighbors(cur)) {
            N n = edge.to();
            if (!discovered.contains(n)) {
                discovered.add(n);
                parent.put(n, cur);
                queue.add(n);
            }
        }
        return false;
    }

    private void buildPath() {
        N cur = graph.goal();
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
    }

    @Override public boolean isDone() { return done; }
    @Override public boolean foundPath() { return found; }
    @Override public Set<N> visited() { return visited; }
    @Override public Collection<N> frontier() { return queue; }
    @Override public List<N> path() { return path; }
    @Override public int nodesExpanded() { return visited.size(); }
}