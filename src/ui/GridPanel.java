package ui;

import algo.PathFinder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.CellType;
import model.Grid;

/**
 * Draws the grid and handles mouse editing.
 *   Left-drag: draw walls      Right-drag: erase walls
 *   Shift+click: move start    Ctrl+click: move goal
 */
public class GridPanel extends JPanel {
    private static final int CELL = 24;

    private static final Color EMPTY = Color.WHITE;
    private static final Color WALL = new Color(40, 40, 40);
    private static final Color START = new Color(46, 160, 67);
    private static final Color GOAL = new Color(207, 34, 46);
    private static final Color VISITED = new Color(173, 216, 230);
    private static final Color FRONTIER = new Color(144, 238, 144);
    private static final Color PATH = new Color(255, 215, 0);
    private static final Color LINES = new Color(220, 220, 220);

    private final Grid grid;
    private PathFinder finder;          // the search currently being shown (may be null)
    private Timer timer;                // drives the animation
    private int delayMs = 20;           // time between steps
    private Consumer<String> statsListener = s -> { };

    public GridPanel(Grid grid) {
        this.grid = grid;
        setPreferredSize(new Dimension(grid.cols() * CELL, grid.rows() * CELL));
        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { edit(e); }
            @Override public void mouseDragged(MouseEvent e) { edit(e); }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void setStatsListener(Consumer<String> listener) { this.statsListener = listener; }
    public void setDelay(int ms) { this.delayMs = ms; }

    private void edit(MouseEvent e) {
        if (isRunning()) return;
        int col = e.getX() / CELL;
        int row = e.getY() / CELL;
        if (!grid.inBounds(row, col)) return;
        finder = null; // editing the grid invalidates the old search
        if (e.isShiftDown()) grid.moveStart(row, col);
        else if (e.isControlDown()) grid.moveGoal(row, col);
        else grid.setWall(row, col, !SwingUtilities.isRightMouseButton(e));
        statsListener.accept(" ");
        repaint();
    }

    public boolean isRunning() { return timer != null && timer.isRunning(); }

    /**
     * Animates the given search one step per timer tick, updating the stats
     * label as it goes. Any search already running is stopped first.
     */
    public void run(PathFinder newFinder) {
        if (timer != null) {
            timer.stop();
        }

        finder = newFinder;
        finder.init(grid);

        final long[] searchNanos = {0};

        timer = new Timer(delayMs, e -> {
            long t0 = System.nanoTime();
            finder.step();
            searchNanos[0] += System.nanoTime() - t0;

            repaint();
            statsListener.accept(formatStats(searchNanos[0]));

            if (finder.isDone()) {
                timer.stop();
            }
        });
        timer.start();
    }

    /** Builds the stats line, e.g. "BFS | expanded: 57 | path length: 23 | 0.41 ms". */
    private String formatStats(long nanos) {
        String pathText;

        if (!finder.isDone()) {
            pathText = "searching...";
        } else if (finder.foundPath()) {
            pathText = "path length: " + (finder.path().size() - 1);
        } else {
            pathText = "no path";
        }

        return String.format("%s | expanded: %d | %s | %.2f ms",
                finder.name(), finder.nodesExpanded(), pathText,
                nanos / 1_000_000.0);
    }

    public void clearSearch() {
        if (timer != null) timer.stop();
        finder = null;
        statsListener.accept(" ");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < grid.rows(); r++) {
            for (int c = 0; c < grid.cols(); c++) {
                fill(g, c, r, colorFor(grid.get(r, c)));
            }
        }
        if (finder != null) {
            for (Point p : finder.visited()) fillIfEmpty(g, p, VISITED);
            for (Point p : finder.frontier()) fillIfEmpty(g, p, FRONTIER);
            List<Point> path = finder.path();
            for (Point p : path) fillIfEmpty(g, p, PATH);
        }
        g.setColor(LINES);
        for (int r = 0; r <= grid.rows(); r++) g.drawLine(0, r * CELL, grid.cols() * CELL, r * CELL);
        for (int c = 0; c <= grid.cols(); c++) g.drawLine(c * CELL, 0, c * CELL, grid.rows() * CELL);
    }

    private void fillIfEmpty(Graphics g, Point p, Color color) {
        if (grid.get(p.y, p.x) == CellType.EMPTY) fill(g, p.x, p.y, color);
    }

    private void fill(Graphics g, int col, int row, Color color) {
        g.setColor(color);
        g.fillRect(col * CELL, row * CELL, CELL, CELL);
    }

    private static Color colorFor(CellType t) {
        switch (t) {
            case WALL: return WALL;
            case START: return START;
            case GOAL: return GOAL;
            default: return EMPTY;
        }
    }
}
