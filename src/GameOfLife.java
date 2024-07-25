import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class GameOfLife extends JFrame {
    private static final int ROWS = 20;
    private static final int COLS = 20;
    private static final int CELL_SIZE = 20;

    private boolean[][] grid = new boolean[ROWS][COLS];
    private JPanel gridPanel;

    public GameOfLife() {
        setTitle("Game of Life");
        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE + 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (grid[row][col]) {
                            g.setColor(Color.BLACK);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        g.setColor(Color.GRAY);
                        g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        };
        gridPanel.setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));

        JButton nextButton = new JButton("Next Generation");
        nextButton.addActionListener(e -> {
            nextGeneration();
            gridPanel.repaint();
        });

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            private Timer timer;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer == null) {
                    timer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nextGeneration();
                            gridPanel.repaint();
                        }
                    });
                    timer.start();
                    startButton.setText("Stop");
                } else {
                    timer.stop();
                    timer = null;
                    startButton.setText("Start");
                }
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            grid = new boolean[ROWS][COLS];
            initializeGrid(getInitialLiveCells());
            gridPanel.repaint();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(nextButton);
        controlPanel.add(startButton);
        controlPanel.add(resetButton);

        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        initializeGrid(getInitialLiveCells());
    }

    private Set<Point> getInitialLiveCells() {
        return new HashSet<>(Set.of(
                new Point(10, 10),
                new Point(11, 10),
                new Point(12, 10),
                new Point(10, 11),
                new Point(11, 12)
        ));
    }

    private void initializeGrid(Set<Point> initialLiveCells) {
        for (Point cell : initialLiveCells) {
            grid[cell.x][cell.y] = true;
        }
    }

    private void nextGeneration() {
        boolean[][] newGrid = new boolean[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int liveNeighbours = countLiveNeighbours(row, col);

                if (grid[row][col]) {
                    newGrid[row][col] = liveNeighbours == 2 || liveNeighbours == 3;
                } else {
                    newGrid[row][col] = liveNeighbours == 3;
                }
            }
        }
        grid = newGrid;
    }

    private int countLiveNeighbours(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && grid[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameOfLife game = new GameOfLife();
            game.setVisible(true);
        });
    }
}