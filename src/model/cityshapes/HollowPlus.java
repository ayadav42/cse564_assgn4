package model.cityshapes;

import java.awt.*;

/**
 * This class represents the hollow plus shaped component of a city's shape.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public class HollowPlus extends ShapeDecorator {

    int[] xs;
    int[] ys;
    Rectangle[] bounds;
    int size;

    /**
     * Creates an instance of the Hollow Plus shape decorator
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param size size of shape
     * @param color color of shape
     */
    public HollowPlus(int x, int y, int size, Color color) {

        this.xs = new int[4];
        this.ys = new int[4];
        this.bounds = new Rectangle[4];
        this.size = size;
        this.color = color;
        moveTo(x, y);

    }

    /**
     * Used to move this hollow plus shape component to new coordinates.
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    @Override
    public void moveTo(int x, int y) {
        super.moveTo(x, y);

        //left
        this.xs[0] = x - size - 2;
        this.ys[0] = y;
        this.bounds[0] = new Rectangle(xs[0], ys[0], size, size);

        //top
        this.xs[1] = x;
        this.ys[1] = y - size - 2;
        this.bounds[1] = new Rectangle(xs[1], ys[1], size, size);

        //right
        this.xs[2] = x + size + 2;
        this.ys[2] = y;
        this.bounds[2] = new Rectangle(xs[2], ys[2], size, size);

        //bottom
        this.xs[3] = x;
        this.ys[3] = y + size + 2;
        this.bounds[3] = new Rectangle(xs[3], ys[3], size, size);

    }

    private boolean doesAnyBoxContainPoint(int x, int y) {

        for (Rectangle r : this.bounds) {
            if (r.contains(x, y)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Used to check if the point lies inside
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return boolean true if found
     */
    @Override
    public boolean containsPoint(int x, int y) {
        return super.containsPoint(x, y) || doesAnyBoxContainPoint(x, y);
    }

    /**
     * Draws the hollow plus shape
     *
     * @param g The graphics passed
     */
    @Override
    public void draw(Graphics g) {

        g.setColor(this.color);
        for (int i = 0; i < 4; i++) {
            g.fillRect(xs[i], ys[i], size, size);
        }
        super.draw(g);

    }

    /**
     * Helps convert the shape to a storage friendly format
     *
     * @param arr The array for setting color value at
     */
    @Override
    public void toStorageFormat(String[] arr) {
        super.toStorageFormat(arr);
        arr[2] = String.valueOf(this.color.getRGB());
    }
}
