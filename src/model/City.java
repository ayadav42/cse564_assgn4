package model;

import model.cityshapes.ShapeComponent;

import java.awt.*;

/**
 * This class is used to define a city using a label, its coordinates and shape.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-10-06
 */
public class City {

    public static final int minSize = 30;
    public static final int maxSize = 80;
    public Rectangle bounds;
    public String label;
    ShapeComponent cityShape;
    City nextCity;

    /**
     * Creates an instance of model.City
     *
     * @param label The city's name set by user
     * @param x     The city's x-coordinate
     * @param y     The city's y-coordinate
     * @param cityShape The shape of the city
     * @param size size of the city
     */
    public City(String label, int x, int y, ShapeComponent cityShape, int size) {

        this.label = label;
        this.bounds = new Rectangle(x, y, size, size);
        this.cityShape = cityShape;

    }

    /**
     * Draws the city using the stored shape component.
     *
     * @param g The graphics instance passed by caller
     */
    public void draw(Graphics g) {

        Color c = g.getColor();

        cityShape.draw(g);

        if (!label.isEmpty()) {
            Color textColor = Color.BLACK;
            g.setColor(textColor);
            g.drawString(label, center().x - 15, center().y + 5);
        }

        g.setColor(c);

    }

    /**
     * This method is used to move city to new co-ordinates.
     *
     * @param x The city's new x-coordinate
     * @param y The city's new y-coordinate
     */
    public void move(int x, int y) {
        bounds.x = x;
        bounds.y = y;
        cityShape.moveTo(x, y);
    }

    private Point center() {
        return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
    }

    /**
     * To check if the city contains a point
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return true if found
     */
    public boolean containsPoint(int x, int y) {
        return this.cityShape.containsPoint(x, y);
    }

    /**
     * This method is used to draw connection between two cities.
     *
     * @param other The other city
     * @param g2D   The Graphics2D instance
     */
    public void drawConnection(City other, Graphics2D g2D) {
        connectNextCity(other);
        g2D.drawLine(center().x, center().y, other.center().x, other.center().y);
    }

    /**
     * To connect to next city in path.
     * This helps with TSP User connect algorithm
     *
     * @param nextCity the next city
     */
    public void connectNextCity(City nextCity) {
        this.nextCity = nextCity;
    }

    /**
     * Used for conveniently printing a city.
     *
     * @return String Contains city number, x-coordinate and y-coordinate
     */
    @Override
    public String toString() {
        String retVal = "City{" +
                "bounds=" + bounds +
                ", label='" + label + '\'' +
                ", cityShape=" + cityShape;

        if (this.nextCity != null) {
            retVal += (", nextCity=" + nextCity.label);
        }

        retVal += "}";
        return retVal;
    }

    /**
     * Used for conveniently converting a city to text-friendly format.
     *
     * @return String Contains city's name, location and shape.
     */
    public String toStorageFormat() {

        String[] shapes = {"$", "$", "$"};
        this.cityShape.toStorageFormat(shapes);

        return this.label + "," + this.bounds.x + "," + this.bounds.y + "," + this.bounds.width + "," + String.join(",", shapes) + ",";

    }

}
