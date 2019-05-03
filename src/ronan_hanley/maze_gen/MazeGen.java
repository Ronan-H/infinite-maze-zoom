package ronan_hanley.maze_gen;

import java.awt.*;

import javax.swing.*;

public class MazeGen extends JPanel {
	private static final long serialVersionUID = 1L;
	private int windowWidth;
	private int windowHeight;
	private static final int STARTING_SCALE = 10;
	private double topScale;
	private double zoomSpeed;

	private long nsPerUpdate;
	private Maze topMaze;
	private long nextUpdate;
	
	public MazeGen(int width, int height, long nsPerUpdate) {
		this.nsPerUpdate = nsPerUpdate;

		windowWidth = width * STARTING_SCALE;
		windowHeight = height * STARTING_SCALE;

		topScale = STARTING_SCALE;
		zoomSpeed = 1.05;

		topMaze = new Maze(width, height, 1, 1, 2);
	}
	
	public void go() {
		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);

		JFrame frame = new JFrame("Maze Generator by Ronan-H");

		frame.getContentPane().setSize(new Dimension(getWidth(), getHeight()));
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		setIgnoreRepaint(true);
		frame.setResizable(false);
		frame.setVisible(true);
		repaint();

		nextUpdate = System.nanoTime();

		try {
			genMazes();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void genMazes() throws InterruptedException {
		while (true) {
			for (int i = 0; i < 30; i++) {
				topMaze.generateStep();
				if (topMaze.isFinishedGenerating()) {
					break;
				}
			}

			pruneMazes();
			repaintAndSleep();
		}
	}

	private long lastTimer = System.currentTimeMillis();
	private int fps = 0;
	public void repaintAndSleep() throws InterruptedException {
		paintImmediately(0, 0, windowWidth, windowHeight);

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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, windowWidth, windowHeight);

		Maze nextMaze = topMaze;
		double scale = topScale;
		while (nextMaze != null) {
			int cellSize = (int) Math.ceil(scale);

			double drawStart = (windowWidth / 2d) - (scale * (nextMaze.getWidth() / 2d));

			g.setColor(Color.WHITE);
			for (int y = 0; y < nextMaze.getHeight(); y++) {
				for (int x = 0; x < nextMaze.getWidth(); x++) {
					if (nextMaze.isPathAt(x, y)) {
						g.fillRect((int) Math.floor(drawStart + x * scale),
								(int) Math.floor(drawStart + y * scale), cellSize, cellSize);
					}
				}
			}

			nextMaze = nextMaze.getSubMaze();
			scale /= topMaze.getWidth();
		}

		topScale *= zoomSpeed;
	}

	public void pruneMazes() {
		if (topScale > windowWidth) {
			topMaze = topMaze.getSubMaze();
			topScale /= topMaze.getWidth();

			Maze nextMaze = topMaze;

			while (nextMaze.getSubMaze() != null) {
				nextMaze = nextMaze.getSubMaze();
			}

			nextMaze.setSubMaze(new Maze(topMaze.getWidth(), topMaze.getHeight(), 1, 1, 0));
		}
	}

	public static void main(String[] args) {
		new MazeGen(73, 73, 50 * 1000000).go();
	}
}
