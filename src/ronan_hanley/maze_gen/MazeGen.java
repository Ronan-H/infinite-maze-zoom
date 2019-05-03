package ronan_hanley.maze_gen;

import java.awt.Dimension;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

import javax.swing.JFrame;

public class MazeGen {
	private int width, height;
	private int sleepTime;
	public boolean[][] grid;
	// (x, y) pairs of directional offset values
	private static final int[][] DIR_OFFSETS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	private Random rand = new Random();
	private Panel panel;
	
	public MazeGen(int width, int height, int sleepTime) {
		this.width = width;
		this.height = height;
		this.sleepTime = sleepTime;

		grid = new boolean[height][width];
	}
	
	public void go() {
		JFrame frame = new JFrame("Maze Generator by Ronan-H");

		panel = new Panel(grid);
		frame.getContentPane().setSize(new Dimension(panel.getWidth(), panel.getHeight()));
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		panel.repaint();
		frame.setResizable(false);
		frame.setVisible(true);

		try {
			genMaze(grid, 1, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void genMaze(boolean[][] grid, int startX, int startY) throws InterruptedException {
		Deque<int[]> stack = new ArrayDeque<>();
		stack.push(new int[] {startX, startY, startX, startY});
		int[] top;
		int x, y;
		int nextDir;
		int xOff, yOff;
		int adjX, adjY;
		int destX, destY;


		while (!stack.isEmpty()) {
			top = stack.pop();
			x = top[0];
			y = top[1];

			if (grid[y][x]) {
				// already visited
				continue;
			}

			// carve out space on the maze
			grid[top[3]][top[2]] = true;
			updateUI();
			grid[y][x] = true;
			updateUI();

			// indexes of directional offsets
			ArrayList<Integer> dirs = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				dirs.add(i);
			}

			// for each direction (up, down, left, right)...
			for (int i = 0; i < 4; i++) {
				nextDir = dirs.remove(rand.nextInt(dirs.size()));
				// direction normal
				xOff = DIR_OFFSETS[nextDir][0];
				yOff = DIR_OFFSETS[nextDir][1];
				// location of adjacent cell along the chosen direction
				adjX = x + xOff;
				adjY = y + yOff;
				// location of destination cell along the chosen direction
				destX = x + xOff * 2;
				destY = y + yOff * 2;

				// check destination is in bounds and not yet visited
				if (destX > 0 && destX < width
						&& destY > 0 && destY < height
						&& !grid[destY][destX]) {
					// push destination cell to the stack
					stack.push(new int[] {destX, destY, adjX, adjY});
				}
			}
		}
	}

	private void updateUI() throws InterruptedException {
		panel.repaint();
		Thread.sleep(sleepTime);
	}

	public static void main(String[] args) {
		new MazeGen(101, 101, 5).go();
	}

}
