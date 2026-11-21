
package graph;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.Grid;

public class GridGraph implements SearchGraph<Point> {

    private final Grid grid;

    public GridGraph(Grid grid) {
        this.grid = grid;
    }

    @Override
    public Point start() {
        return grid.start();
    }

    @Override
    public Point goal() {
        return grid.goal();
    }

    @Override
    public List<Edge<Point>> neighbors(Point p) {
        List<Edge<Point>> edges = new ArrayList<>();

        for (Point n : grid.neighbors(p)) {
            edges.add(new Edge<>(n, 1.0));
        }

        return edges;
    }

    @Override
    public double heuristic(Point p) {
        Point g = grid.goal();
        return Math.abs(p.x - g.x) + Math.abs(p.y - g.y);
    }
}
