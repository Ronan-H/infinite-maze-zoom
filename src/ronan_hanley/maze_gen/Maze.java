package ronan_hanley.maze_gen;

import java.awt.image.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

public class Maze {
    // (x, y) pairs of directional offset values
    private static final int[][] DIR_OFFSETS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final byte WALL = 0x000000, PATH = (byte) 0xFFFFFF;

    private int width;
    private int height;
    private int[] grid;
    private BufferedImage gridImage;
    private Deque<int[]> genStack = new ArrayDeque<>();
    private Random random;
    private Maze subMaze;

    public Maze(int width, int height, int genStartX, int genStartY, int numSubMazes) {
        this.width = width;
        this.height = height;

        gridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        grid = ((DataBufferInt) gridImage.getRaster().getDataBuffer()).getData();

        genStack.push(new int[] {genStartX, genStartY, genStartX, genStartY});
        random = new Random();

        if (numSubMazes > 0) {
            subMaze = new Maze(width, height, genStartX, genStartY, numSubMazes - 1);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Maze getSubMaze() {
        return subMaze;
    }

    public void setSubMaze(Maze subMaze) {
        this.subMaze = subMaze;
    }


    public boolean isPathAt(int x, int y) {
        return grid[y * height + x] == PATH;
    }

    /**
     * @return True if more of the maze was carved out (sometimes it only backtracks in a single step)
     */
    public boolean generateStep() {
        boolean subMazeCarved = false;

        if (subMaze != null) {
            subMazeCarved = subMaze.generateStep();
        }

        if (genStack.isEmpty()) {
            return subMazeCarved;
        }

        int[] top;
        int x, y;
        int nextDir;
        int xOff, yOff;
        int adjX, adjY;
        int destX, destY;

        top = genStack.pop();
        x = top[0];
        y = top[1];

        if (isPathAt(x, y)) {
            // already visited
            return subMazeCarved;
        }

        // carve out space on the maze
        grid[top[3] * height + top[2]] = PATH;
        grid[y * height + x] = PATH;

        // indexes of directional offsets
        ArrayList<Integer> dirs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            dirs.add(i);
        }

        // for each direction (up, down, left, right)...
        for (int i = 0; i < 4; i++) {
            nextDir = dirs.remove(random.nextInt(dirs.size()));
            // direction normal
            xOff = DIR_OFFSETS[nextDir][0];
            yOff = DIR_OFFSETS[nextDir][1];
            // location of adjacent cell along the chosen direction
            adjX = x + xOff;
            adjY = y + yOff;
            // location of destination cell along the chosen direction
            destX = x + xOff * 2;
            destY = y + yOff * 2;

            // check destination is in bounds and not yet visited
            if (destX > 0 && destX < width
                    && destY > 0 && destY < height
                    && grid[destY * height + destX] == WALL) {
                // push destination cell to the stack
                genStack.push(new int[] {destX, destY, adjX, adjY});
            }
        }

        return true;
    }

    public boolean isFinishedGenerating() {
        return genStack.isEmpty() && (subMaze == null || subMaze.isFinishedGenerating());
    }

    public BufferedImage getGridImage() {
        return gridImage;
    }
}
