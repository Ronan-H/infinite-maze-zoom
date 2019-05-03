package ronan_hanley.maze_gen;

import java.awt.*;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Maze topMaze;
	private int windowWidth;
	private int windowHeight;
	private static final int STARTING_SCALE = 10;
	private double topScale;
	private double zoomSpeed;
	
	public Panel(Maze topMaze) {
		this.topMaze = topMaze;

		windowWidth = topMaze.getWidth() * STARTING_SCALE;
		windowHeight = topMaze.getHeight() * STARTING_SCALE;

		topScale = STARTING_SCALE;
		zoomSpeed = 1.02;

		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
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
	
}
