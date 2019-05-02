package ronan_hanley.maze_gen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int[][] grid;
	private BufferedImage gridImage;
	private static final int SCALE = 7;
	
	public Panel(int[][] grid) {
		this.grid = grid;

		int gridWidth = grid[0].length;
		int gridHeight = grid.length;

		int windowWidth = gridWidth * SCALE;
		int windowHeight = gridHeight * SCALE;

		Dimension size = new Dimension(windowWidth, windowHeight);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);

		gridImage = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < grid.length; y++) {
			for(int x = 0; x < grid[y].length; x++) {
				gridImage.setRGB(x, y, 0x000000);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int y=0; y<grid.length; y++) {
			for(int x=0; x<grid[y].length; x++) {
				switch(grid[y][x]) {
				case MazeGen.BLANK:
					gridImage.setRGB(x, y, 0xFFFFFF);
					break;
				case MazeGen.WALL:
					gridImage.setRGB(x, y, 0x000000);
					break;
				}
			}
		}
		g.drawImage(gridImage, 0, 0, getWidth(), getHeight(), null);
	}
	
}
