package ronan_hanley.maze_gen;

import java.awt.*;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Maze maze;
	private int windowWidth;
	private int windowHeight;
	private static final int STARTING_SCALE = 10;
	private double scale;
	private double zoomSpeed;
	
	public Panel(Maze maze) {
		this.maze = maze;

		windowWidth = maze.getWidth() * STARTING_SCALE;
		windowHeight = maze.getHeight() * STARTING_SCALE;

		scale = STARTING_SCALE;
		zoomSpeed = 1.001;

		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, windowWidth, windowHeight);


		int cellSize = (int) Math.ceil(scale);

		double drawStart = (windowWidth / 2d) - (scale * (maze.getWidth() / 2d));

		g.setColor(Color.WHITE);
		for(int y = 0; y < maze.getHeight(); y++) {
			for(int x = 0; x < maze.getWidth(); x++) {
				if (maze.isPathAt(x, y)) {
					g.fillRect((int) Math.floor(drawStart + x * scale),
								(int) Math.floor(drawStart + y * scale), cellSize, cellSize);
				}
			}
		}

		scale *= zoomSpeed;
	}
	
}
