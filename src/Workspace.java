import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This JPanel class is responsible for drawing the path between the cities
 * and track user's input to ensure latest data. It observes the TSP to be
 * notified when the path is ready.
 *
 * @author amaryadav
 * @author kaichen
 * @version 1.0
 * @since 2021-10-08
 */
public class Workspace extends JPanel implements Observer, MouseListener, MouseMotionListener {

    private City selectedCity;

    /**
     * Creates an instance of graph with customised bounds and background color.
     * Plots cities in the given path and accommodates for cities without
     * default coordinates. If needed, also scales down each city's coordinates.
     *
     * @param x      The x-coordinate bound
     * @param y      The y-coordinate bound
     * @param width  The width of component
     * @param height The height of component
     * @param color  The background color
     */
    public Workspace(int x, int y, int width, int height, Color color) {

        setName("WorkspaceJPanel");
        setBounds(x, y, width, height);
        setBackground(color);
        addMouseListener(this);
        addMouseMotionListener(this);
        resetCityList();

    }

    /**
     * This method resets the city data.
     *
     */
    public void resetCityList() {
        this.updateCityList(new ArrayList<>());
    }

    /**
     * This method updates the city data with the list that's passed to it.
     * It also marks the data as changed using the Blackboard class.
     *
     * @param cityList The new list of cities
     */
    public void updateCityList(List<City> cityList) {
        Blackboard.getInstance().cityList = cityList;
        Blackboard.getInstance().dataChanged = true;
    }

    /**
     * This method updates the city data with the list that's passed to it.
     * It also marks the data as changed using the Blackboard class.
     *
     * @return String The string containing the list of cities
     */
    public String getCitiesInTxtFriendlyFormat() {
        String[] cities = new String[Blackboard.getInstance().path.size()];
        int index = 0;
        for (City city : Blackboard.getInstance().path) {
            cities[index++] = city.toStorageFormat();
        }
        return String.join("\n", cities);
    }

    /**
     * Plots the cities with their respective labels and connects them with a line.
     *
     * @param g The Graphics instance
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLUE);

        List<City> cities = Blackboard.getInstance().path;
        System.out.println("painting cities=" + cities.size());
        City prev = null;

        for (City city : cities) {
            if (prev != null) city.drawConnection(prev, g2D);
            prev = city;
        }

        if (prev != null) prev.drawConnection(cities.get(0), g2D);

        for (City city : cities) city.draw(g);

    }

    private City findCityAround(int x, int y) {
        for (City city : Blackboard.getInstance().cityList) if (city.bounds.contains(x, y)) return city;
        return null;
    }

    /**
     * This method is used to either mark end for drag and drop operation on a city
     * or create a new city at the selected position.
     *
     * @param e The mouse event caught
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        if (selectedCity != null) {
            selectedCity.move(e.getX(), e.getY());
            System.out.println("nulling selectedCity: " + selectedCity);
            selectedCity = null; //this is a follow up of mouse-released
            Blackboard.getInstance().dataChanged = true;

        } else {
            selectedCity = findCityAround(e.getX(), e.getY());
            if (selectedCity == null) {
                String newCityName = (String) JOptionPane.showInputDialog(
                        this,
                        "Enter a name for this new city.",
                        "New City",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "City0"
                );

                if (!newCityName.isEmpty()) {
                    Blackboard.getInstance().cityList.add(new City(newCityName, e.getX(), e.getY()));
                    Blackboard.getInstance().dataChanged = true;
                    System.out.println("added new city");
                }
            }

        }

    }

    /**
     * This method is used to mark the start for drag and drop operation on a city, if found
     *
     * @param e The mouse event caught
     */
    @Override
    public void mousePressed(MouseEvent e) {
        selectedCity = findCityAround(e.getX(), e.getY());
    }

    /**
     * This method is used to mark the end for drag and drop operation on a city, if any
     *
     * @param e The mouse event caught
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        if (selectedCity != null) {
            selectedCity.move(e.getX(), e.getY());
            Blackboard.getInstance().dataChanged = true;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * This method is used to make sure the city is being drawn as it is being dragged
     *
     * @param e The mouse event caught
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        if (selectedCity != null) {
            selectedCity.move(e.getX(), e.getY());
            Blackboard.getInstance().dataChanged = true;
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * This method is run when TSP notifies that the path is ready
     * to be drawn.
     *
     * @param o The Observable TSP object
     * @param arg The arguments passed by it
     */
    @Override
    public void update(Observable o, Object arg) {

        System.out.println("notified by TSP, proceeding to repaint");
        repaint();

    }
}
