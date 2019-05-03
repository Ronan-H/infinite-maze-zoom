package ronan_hanley.maze_gen;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Maze maze;
	private static final int SCALE = 7;
	
	public Panel(Maze maze) {
		this.maze = maze;

		int windowWidth = maze.getWidth() * SCALE;
		int windowHeight = maze.getHeight() * SCALE;

		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for(int y = 0; y < maze.getHeight(); y++) {
			for(int x = 0; x < maze.getWidth(); x++) {
				g.setColor(maze.isPathAt(x, y) ? Color.WHITE : Color.BLACK);
				g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
			}
		}
	}
	
}
