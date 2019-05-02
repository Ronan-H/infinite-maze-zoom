package ronan_hanley.maze_gen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = -6708754726478345475L;
	private byte[][] grid;
	private BufferedImage gridImage;
	private static final int SCALE = 7;
	
	public Panel(byte[][] g) {
		grid = g;
		setPreferredSize(new Dimension(g[0].length * SCALE, g.length * SCALE));
		setMaximumSize(new Dimension(g[0].length * SCALE, g.length * SCALE));
		setMinimumSize(new Dimension(g[0].length * SCALE, g.length * SCALE));
		gridImage = new BufferedImage(grid[0].length, grid.length, BufferedImage.TYPE_INT_RGB);
		for(int y=0; y<grid.length; y++) {
			for(int x=0; x<grid[y].length; x++) {
				gridImage.setRGB(x, y, 0x000000);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int y=0; y<grid.length; y++) {
			for(int x=0; x<grid[y].length; x++) {
				switch(grid[y][x]) {
				case 0:
					gridImage.setRGB(x, y, 0xFFFFFF);
					break;
				case 1:
					gridImage.setRGB(x, y, 0x000000);
					break;
				case 2:
					break;
				case 3:
					break;
				}
			}
		}
		g.drawImage(gridImage, 0, 0, getWidth(), getHeight(), null);
	}
	
}
