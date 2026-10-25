package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular grid of cells. Point.x is the column, Point.y is the row.
 * Exactly one START and one GOAL exist at all times.
 */
public class Grid {
    private final int rows;
    private final int cols;
    private final CellType[][] cells;
    private Point start;
    private Point goal;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = CellType.EMPTY;
            }
        }
        start = new Point(1, rows / 2);
        goal = new Point(cols - 2, rows / 2);
        cells[start.y][start.x] = CellType.START;
        cells[goal.y][goal.x] = CellType.GOAL;
    }

    public int rows() { return rows; }
    public int cols() { return cols; }
    public Point start() { return new Point(start); }
    public Point goal() { return new Point(goal); }

    public CellType get(int row, int col) { return cells[row][col]; }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /** Toggles a wall on an EMPTY cell (or clears a WALL). START and GOAL are never overwritten. */
    public void setWall(int row, int col, boolean wall) {
        if (!inBounds(row, col)) return;
        CellType t = cells[row][col];
        if (t == CellType.START || t == CellType.GOAL) return;
        cells[row][col] = wall ? CellType.WALL : CellType.EMPTY;
    }

    public void moveStart(int row, int col) {
        if (!inBounds(row, col) || cells[row][col] != CellType.EMPTY) return;
        cells[start.y][start.x] = CellType.EMPTY;
        start = new Point(col, row);
        cells[row][col] = CellType.START;
    }

    public void moveGoal(int row, int col) {
        if (!inBounds(row, col) || cells[row][col] != CellType.EMPTY) return;
        cells[goal.y][goal.x] = CellType.EMPTY;
        goal = new Point(col, row);
        cells[row][col] = CellType.GOAL;
    }

    public void clearWalls() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c] == CellType.WALL) cells[r][c] = CellType.EMPTY;
            }
        }
    }

    /**
     * Returns the cells you can move to from p in one step:
     * up, down, left, right, staying in bounds and skipping walls.
     *
     * TODO (Justin): implement this.
     *   - Check the 4 directions (no diagonals for now).
     *   - Use inBounds(...) and skip any cell whose type is WALL.
     *   - Keep a FIXED order (e.g. up, right, down, left) so runs are reproducible.
     */
    public List<Point> neighbors(Point p) {
        List<Point> result = new ArrayList<>();
        // your code here
        return result;
    }
}
