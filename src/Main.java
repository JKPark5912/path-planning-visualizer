import algo.AStar;
import algo.BFS;
import algo.Dijkstra;
import algo.PathFinder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.Grid;
import ui.GridPanel;

/** Entry point: builds the window, the grid, and the control bar. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShow);
    }

    private static void createAndShow() {
        Grid grid = new Grid(25, 40);
        GridPanel panel = new GridPanel(grid);

        // Algorithms available in the dropdown.
        Map<String, Supplier<PathFinder>> algorithms = new LinkedHashMap<>();
        algorithms.put("BFS", BFS::new);
        algorithms.put("Dijkstra", Dijkstra::new);
        algorithms.put("A*", AStar::new);

        JComboBox<String> choice = new JComboBox<>(algorithms.keySet().toArray(new String[0]));
        JButton run = new JButton("Run");
        JButton clearPath = new JButton("Clear search");
        JButton clearWalls = new JButton("Clear walls");
        JLabel stats = new JLabel(" ");
        panel.setStatsListener(stats::setText);

        run.addActionListener(e -> panel.run(algorithms.get((String) choice.getSelectedItem()).get()));
        clearPath.addActionListener(e -> panel.clearSearch());
        clearWalls.addActionListener(e -> { grid.clearWalls(); panel.clearSearch(); });

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(choice);
        controls.add(run);
        controls.add(clearPath);
        controls.add(clearWalls);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(stats);

        JFrame frame = new JFrame("Path Planning Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(controls, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
