package ronan_hanley.maze_gen;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean[][] grid;
	private static final int SCALE = 7;
	
	public Panel(boolean[][] grid) {
		this.grid = grid;

		int gridWidth = grid[0].length;
		int gridHeight = grid.length;

		int windowWidth = gridWidth * SCALE;
		int windowHeight = gridHeight * SCALE;

		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for(int y = 0; y < grid.length; y++) {
			for(int x = 0; x < grid[y].length; x++) {
				g.setColor(grid[y][x] ? Color.WHITE : Color.BLACK);
				g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
			}
		}
	}
	
}
