package model.cityshapes;

import java.awt.*;

/**
 * This class represents the square shaped component of a city's shape.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public class Square extends ShapeDecorator {

    int x;
    int y;
    Rectangle bounds;
    int size;

    /**
     * Creates a square shaped component for the city.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param size size of the circle
     * @param color color of the circle
     */
    public Square(int x, int y, int size, Color color) {

        this.size = size;
        this.color = color;
        this.bounds = new Rectangle(x, y, size, size);
        moveTo(x, y);

    }

    /**
     * Used to move this square shape component to new coordinates.
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    @Override
    public void moveTo(int x, int y) {

        super.moveTo(x, y);
        this.x = x;
        this.y = y;
        this.bounds.x = x;
        this.bounds.y = y;

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
        return super.containsPoint(x, y) || this.bounds.contains(x, y);
    }

    /**
     * Draws the square shape
     *
     * @param g The graphics passed
     */
    @Override
    public void draw(Graphics g) {

        g.setColor(this.color);
        g.fillRect(x, y, size, size);
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
        arr[1] = String.valueOf(this.color.getRGB());
    }
}
