package ronan_hanley.maze_gen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MazeGen extends JPanel {
	private static final long serialVersionUID = 1L;
	private int windowWidth;
	private int windowHeight;
	private int canvasWidth;
	private int canvasHeight;
	private static final double UPSCALE_FACTOR = 2;
	private static final int STARTING_SCALE = 17;
	private double topScale;
	private double zoomSpeed;
	private double zoomAccel;
	private double zoomSpeedLimit;

	private long nsPerUpdate;
	private Maze topMaze;
	private long nextUpdate;
	private boolean saveFrames;
	private long frameCounter;
	private static final String FRAMES_DIR = "./frames/";
	private BufferedImage nextFrame;
	
	public MazeGen(int width, int height, long nsPerUpdate, boolean saveFrames) {
		this.nsPerUpdate = nsPerUpdate;
		this.saveFrames = saveFrames;

		if (saveFrames) {
			new File(FRAMES_DIR).mkdirs();
		}

		windowWidth = width * STARTING_SCALE;
		windowHeight = height * STARTING_SCALE;

		canvasWidth = (int) Math.round(windowWidth * UPSCALE_FACTOR);
		canvasHeight = (int) Math.round(windowHeight * UPSCALE_FACTOR);

		topScale = STARTING_SCALE * width;
		zoomSpeed = 1.02;
		zoomAccel = 0.000005;
		zoomSpeedLimit = 1.08;

		topMaze = new Maze(width, height, 1, 1, 4);
		frameCounter = 0;
	}
	
	private void go() {
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

	private void genMazes() throws InterruptedException {
		while (true) {
			for (int i = 0; i < 20; i++) {
				topMaze.generateStep();
				if (topMaze.isFinishedGenerating()) {
					break;
				}
			}

			zoomSpeed += zoomAccel;
			if (zoomSpeed > zoomSpeedLimit) {
				zoomSpeed = zoomSpeedLimit;
			}

			pruneMazes();
			repaintAndSleep();
		}
	}

	private long lastTimer = System.currentTimeMillis();
	private int fps = 0;
	private void repaintAndSleep() throws InterruptedException {
		nextFrame = drawFrame();
		paintImmediately(0, 0, windowWidth, windowHeight);

		long sleepNs = nextUpdate - System.nanoTime();

		if (sleepNs > 0) {
			Thread.sleep(sleepNs / 1000000, (int) (sleepNs % 1000000));
		}

		nextUpdate += nsPerUpdate;
		fps++;
		if (System.currentTimeMillis() > lastTimer + 1000) {
			System.out.printf("%-5dfps%n", fps);
			lastTimer += 1000;
			fps = 0;
		}

		frameCounter++;
	}

	private BufferedImage drawFrame() {
		BufferedImage frame = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) frame.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Maze nextMaze = topMaze;
		double scale = topScale;
		for (int i = 0; i < 3 && nextMaze != null; i++) {
			double drawStart = Math.round((windowWidth / 2d) - (scale / 2d));

			int drawStartInt = (int) Math.round(drawStart);
			int scaleInt = (int) Math.round(scale);

			g.drawImage(nextMaze.getGridImage(), drawStartInt, drawStartInt, scaleInt, scaleInt, null);

			nextMaze = nextMaze.getSubMaze();
			scale /= topMaze.getWidth();
		}

		topScale *= zoomSpeed;

		g.dispose();

		if (saveFrames) {
			String imageSavePath = FRAMES_DIR + frameCounter + ".png";
			try {
				ImageIO.write(frame, "PNG", new File(imageSavePath));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return frame;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(nextFrame, 0, 0, windowWidth, windowHeight, null);
	}

	private void pruneMazes() {
		if (topScale > canvasWidth) {
			topMaze = topMaze.getSubMaze();
			topScale /= topMaze.getWidth();

			Maze nextMaze = topMaze;

			while (nextMaze.getSubMaze() != null) {
				nextMaze = nextMaze.getSubMaze();
			}

			nextMaze.setSubMaze(new Maze(topMaze.getWidth(), topMaze.getHeight(), 21, 21, 0));
		}
	}

	public static void main(String[] args) {
		new MazeGen(53, 53, 16 * 1000000, false).go();
	}
}
