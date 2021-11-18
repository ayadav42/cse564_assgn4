package model.cityshapes;

import java.awt.*;

/**
 * This class represents the circular shaped component of a city's shape.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public class Circle extends ShapeComponent {

    final int diameter;
    int x_corner;
    int y_corner;

    /**
     * Creates a circle shaped component for the city.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param size size of the circle
     * @param color color of the circle
     */
    public Circle(int x, int y, int size, Color color) {

        this.diameter = size - 8;
        moveTo(x, y);
        this.color = color;

    }

    /**
     * Used to move this circular shape component to new coordinates.
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    @Override
    public void moveTo(int x, int y) {

        this.x_corner = x + 4;
        this.y_corner = y + 4;

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

        double radius = diameter / 2.0;
        int x_center = x_corner + (int) radius;
        int y_center = y_corner + (int) radius;

        double distanceFromCenter = Math.sqrt(
                (Math.pow((x - x_center), 2) + Math.pow((y - y_center), 2))
        );

        return distanceFromCenter <= radius;
    }

    /**
     * Draws the circular shape
     *
     * @param g The graphics passed
     */
    @Override
    public void draw(Graphics g) {

        g.setColor(this.color);
        g.fillOval(x_corner, y_corner, diameter, diameter);

    }

    /**
     * Helps convert the shape to a storage friendly format
     *
     * @param arr The array for setting color value at
     */
    @Override
    public void toStorageFormat(String[] arr) {
        arr[0] = String.valueOf(this.color.getRGB());
    }

}
