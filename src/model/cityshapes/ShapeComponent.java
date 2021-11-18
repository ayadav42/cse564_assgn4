package model.cityshapes;

import java.awt.*;

/**
 * This class represents the a shape component of a city's shape.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public abstract class ShapeComponent {

    Color color;

    /**
     * Each child can be moved using this method
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public abstract void moveTo(int x, int y);

    /**
     * Each child can be asked if they contain a point
     * using this method
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public abstract boolean containsPoint(int x, int y);

    /**
     * Each child should be able to draw itself
     *
     * @param g The graphics passed
     */
    public abstract void draw(Graphics g);

    /**
     * Helps convert the shape to a storage friendly format
     *
     * @param arr The array for setting color value at
     */
    public abstract void toStorageFormat(String[] arr);

}
