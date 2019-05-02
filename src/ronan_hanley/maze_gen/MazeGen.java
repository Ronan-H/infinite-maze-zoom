package ronan_hanley.maze_gen;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class MazeGen {
	public int x, y;
	private int sleepTime;
	public int[][] grid;
	// 0 = Blank, 1 = WALL, 2 = START, 3 = FINISH
	public static final int BLANK = 0, WALL = 1, START = 2, FINISH = 3;
	private Random rand = new Random();
	private Panel panel;
	
	public MazeGen(int x, int y, int sleepTime) {
		this.x = x;
		this.y = y;
		this.sleepTime = sleepTime;

		grid = new int[y][x];
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = WALL;
			}
		}
	}
	
	public void go() {
		JFrame frame = new JFrame("Maze Generator by Ronan-H");
		grid[1][1] = FINISH;

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
	
	public static void main(String[] args) {
		new MazeGen(101, 101, 1).go();
	}
	
	public void genMaze(int[][] grid, int x, int y) throws InterruptedException {
		grid[y][x] = BLANK;

		ArrayList<Integer> dirs = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			dirs.add(i);
		}

		int nextDir;

		for (int i = 0; i < 4; i++) {
			nextDir = dirs.remove(rand.nextInt(dirs.size()));
			switch (nextDir) {
			case 0:
				// left
				if(x - 2 > 0 && grid[y][x - 2] != BLANK) {
					grid[y][x - 1] = BLANK;
					grid[y][x - 2] = BLANK;
					Thread.sleep(sleepTime);
					panel.repaint();
					genMaze(grid, x - 2, y);
				}
				break;
			case 1:
				// right
				if(x + 2 < grid[0].length && grid[y][x + 2] != BLANK) {
					grid[y][x + 1] = BLANK;
					grid[y][x + 2] = BLANK;
					Thread.sleep(sleepTime);
					panel.repaint();
					genMaze(grid, x + 2, y);
				}
				break;
			case 2:
				// up
				if(y - 2 > 0 && grid[y - 2][x] != BLANK) {
					grid[y - 1][x] = BLANK;
					grid[y - 2][x] = BLANK;
					Thread.sleep(sleepTime);
					panel.repaint();
					genMaze(grid, x, y -2);
				}
				break;
			case 3:
				// down
				if(y + 2 < grid.length && grid[y +2][x] != BLANK) {
					grid[y + 1][x] = BLANK;
					grid[y + 2][x] = BLANK;
					Thread.sleep(sleepTime);
					panel.repaint();
					genMaze(grid, x, y + 2);
				}
				break;
			}
		}
	}
	
}
