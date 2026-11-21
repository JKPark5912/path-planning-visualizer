package algo;

import graph.SearchGraph;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/** A search that advances one node per step() call, on any SearchGraph. */
public interface PathFinder<N> {
    String name();
    void init(SearchGraph<N> graph);
    boolean step();
    boolean isDone();
    boolean foundPath();
    Set<N> visited();
    Collection<N> frontier();
    List<N> path();
    int nodesExpanded();
}