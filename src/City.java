import java.awt.*;

/**
 * This class is used to define a city using a number and its coordinates.
 *
 * @author amaryadav
 * @author kaichen
 * @version 1.0
 * @since 2021-10-06
 */
public class City {

    public static final int width = 100;
    public static final int height = 50;
    String label;
    Rectangle bounds;

    /**
     * Creates an instance of City
     *
     * @param label  The city's name set by user
     * @param x  The city's x-coordinate
     * @param y  The city's y-coordinate
     */
    public City(String label, int x, int y) {

        this.label = label;
        this.bounds = new Rectangle(x, y, width, height);

    }

    /**
     * Draws the city as a bounded rectangle.
     *
     * @param g The graphics instance passed by caller
     */
    public void draw(Graphics g) {

        int x = bounds.x, y = bounds.y, h = bounds.height, w = bounds.width;
        g.drawRect(x, y, w, h);
        Color c = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x + 1, y + 1, w - 1, h - 1);
        g.setColor(Color.red);
        if (!label.isEmpty()) g.drawString(label, center().x - 15, center().y + 5);
        g.setColor(c);

    }

    /**
     * This method is used to move city to new co-ordinates.
     *
     * @param x  The city's new x-coordinate
     * @param y  The city's new y-coordinate
     */
    public void move(int x, int y) {
        bounds.x = x;
        bounds.y = y;
    }

    private Point center() {
        return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
    }

    /**
     * This method is used to draw connection between two cities.
     *
     * @param other  The other city
     * @param g2D  The Graphics2D instance
     */
    public void drawConnection(City other, Graphics2D g2D) {
        g2D.drawLine(center().x, center().y, other.center().x, other.center().y);
    }

    /**
     * Used for conveniently printing a city.
     *
     * @return String Contains city number, x-coordinate and y-coordinate
     */
    @Override
    public String toString() {

        return "City{" +
                "label=" + label +
                ", x=" + bounds.x +
                ", y=" + bounds.y +
                '}';

    }

    /**
     * Used for conveniently converting a city to text-friendly format.
     *
     * @return String Contains city's name and location.
     */
    public String toStorageFormat() {
        return this.label + "," + this.bounds.x + "," + this.bounds.y;
    }
}
