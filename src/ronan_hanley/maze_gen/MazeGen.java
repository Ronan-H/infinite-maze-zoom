package ronan_hanley.maze_gen;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MazeGen {
	private int width, height;
	private int sleepTime;
	private Maze maze;
	private Panel panel;
	
	public MazeGen(int width, int height, int sleepTime) {
		this.width = width;
		this.height = height;
		this.sleepTime = sleepTime;

		maze = new Maze(width, height, 1, 1);
	}
	
	public void go() {
		JFrame frame = new JFrame("Maze Generator by Ronan-H");

		panel = new Panel(maze);
		frame.getContentPane().setSize(new Dimension(panel.getWidth(), panel.getHeight()));
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		panel.repaint();
		frame.setResizable(false);
		frame.setVisible(true);

		try {
			genMaze();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void genMaze() throws InterruptedException {
		while (!maze.isFinishedGenerating()) {
			boolean madeVisualProgress = maze.generateStep();

			if (madeVisualProgress) {
				updateAndPause();
			}
		}
	}

	private void updateAndPause() throws InterruptedException {
		panel.repaint();
		Thread.sleep(sleepTime);
	}

	public static void main(String[] args) {
		new MazeGen(101, 101, 10).go();
	}

}
