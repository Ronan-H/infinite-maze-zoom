package ronan_hanley.maze_gen;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MazeGen {
	private long nsPerUpdate;
	private Maze topMaze;
	private Panel panel;
	private long nextUpdate;
	
	public MazeGen(int width, int height, long nsPerUpdate) {
		this.nsPerUpdate = nsPerUpdate;

		topMaze = new Maze(width, height, 1, 1, 3);
	}
	
	public void go() {
		JFrame frame = new JFrame("Maze Generator by Ronan-H");

		panel = new Panel(topMaze);
		frame.getContentPane().setSize(new Dimension(panel.getWidth(), panel.getHeight()));
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		panel.setIgnoreRepaint(true);
		frame.setResizable(false);
		frame.setVisible(true);
		panel.repaint();

		nextUpdate = System.nanoTime();

		try {
			genMaze();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void genMaze() throws InterruptedException {
		while (true) {
			for (int i = 0; i < 20; i++) {
				topMaze.generateStep();
				if (topMaze.isFinishedGenerating()) {
					break;
				}
			}

			repaintAndSleep();
		}
	}

	private long lastTimer = System.currentTimeMillis();
	private int fps = 0;
	public void repaintAndSleep() throws InterruptedException {
		panel.paintImmediately(0, 0, panel.getWindowWidth(), panel.getWindowHeight());

		long sleepNs = nextUpdate - System.nanoTime();

		if (sleepNs > 0) {
			Thread.sleep(nsPerUpdate / 1000000, (int) (nsPerUpdate % 1000000));
		}

		nextUpdate += nsPerUpdate;
		fps++;
		if (System.currentTimeMillis() > lastTimer + 1000) {
			System.out.printf("%-5dfps%n", fps);
			lastTimer += 1000;
			fps = 0;
		}
	}

	public static void main(String[] args) {
		new MazeGen(73, 73, 50 * 1000000).go();
	}

}
