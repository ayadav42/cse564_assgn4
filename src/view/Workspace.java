package view;

import controller.Logger;
import model.cityshapes.*;
import model.Blackboard;
import model.City;
import model.cityfactory.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * This JPanel class is responsible for drawing the path between the cities
 * and track user's input to ensure latest data. It observes the controller.tsp.TSPNearestNbr to be
 * notified when the path is ready.
 *
 * @author amaryadav
 * @author navya
 * @version 1.0
 * @since 2021-10-08
 */
public class Workspace extends JPanel implements Observer, MouseListener, MouseMotionListener {

    private City selectedCity;
    private boolean userConnect;
    private boolean canMoveCities;
    private boolean canConnectCities;
    private boolean canCreateCities;

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

        disableUserConnect();
        resetFlags();
        setName("WorkspaceJPanel");
        setBounds(x, y, width, height);
        setBackground(color);
        addMouseListener(this);
        addMouseMotionListener(this);
        resetCityList();

    }

    /**
     * Resets the move, connect and create flags to false.
     */
    public void resetFlags() {

        this.canMoveCities = false;
        this.canConnectCities = false;
        this.canCreateCities = false;

    }

    /**
     * Enables user connect option for the current user
     */
    public void enableUserConnect() {
        this.userConnect = true;
        resetFlags();
        enableCreateNewCity();
    }

    /**
     * Disables user connect option for the current user
     */
    public void disableUserConnect() {
        this.userConnect = false;
    }

    /**
     * Enables move feature while disabling connect and create features.
     */
    public void enableMove() {
        Logger.getInstance().log("Moving cities enabled");
        this.canMoveCities = true;
        this.canConnectCities = false;
        this.canCreateCities = false;
    }

    /**
     * Enables connect city feature while disabling move and create features.
     */

    public void enableConnect() {
        Logger.getInstance().log("Connecting cities enabled");
        this.canMoveCities = false;
        this.canConnectCities = true;
        this.canCreateCities = false;
    }

    /**
     * Enables the user to create new city while disabling move and connect features.
     */

    public void enableCreateNewCity() {
        Logger.getInstance().log("Creating cities enabled");
        this.canMoveCities = false;
        this.canConnectCities = false;
        this.canCreateCities = true;
    }

    /**
     * This method resets the city data.
     */
    public void resetCityList() {
        this.updateCityList(new ArrayList<>());
    }

    /**
     * This method updates the city data with the list that's passed to it.
     * It also marks the data as changed using the database.Blackboard class.
     *
     * @param cityList The new list of cities
     */
    public void updateCityList(List<City> cityList) {
        Blackboard.getInstance().cityList = cityList;
        Blackboard.getInstance().dataChanged = true;
    }

    /**
     * This method prepares the cities to be saved in a text friendly format.
     *
     * @return String The string containing the list of cities in text format
     */
    public String getCitiesInTxtFriendlyFormat() {

        Logger.getInstance().log("Converting cities to storage friendly text format.");

        String[] cities = new String[Blackboard.getInstance().cityList.size() + Blackboard.getInstance().path.size()];
        int index = 0;

        for (List<City> cluster : Blackboard.getInstance().path) {
            for (City city : cluster) {
                cities[index++] = city.toStorageFormat();
            }
            if (index < cities.length) {
                cities[index] = "$$";
                index++;
            }
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

        for (List<City> cluster : Blackboard.getInstance().path) {
            City prev = null;

            for (City curr : cluster) {
                curr.connectNextCity(null);
                if (prev != null) prev.drawConnection(curr, g2D);
                prev = curr;
            }

            if (prev != null && !this.userConnect) prev.drawConnection(cluster.get(0), g2D);

            for (City city : cluster) city.draw(g);
        }

    }

    /**
     * Creates new city on mouse click on the interface
     * @param x Latitude identified by the position on mouse click
     * @param y Longitude identified by the position on mosue click
     * @return calls the createNewCity method which creates the city of desired shape, size and color and displays
     * on the interface
     */
    public City takeNewCityInput(int x, int y) {

        String[] colors = {"Blue", "Cyan", "Red", "Orange", "Yellow", "Gray"};

        JPanel panel = new JPanel(new GridLayout(6, 3));

        JTextField cityName = new JTextField("");
        panel.add(new JLabel("City Name"));
        panel.add(cityName);
        panel.add(new JPanel());

        JTextField citySize = new JTextField("");
        JLabel sizeMsg = new JLabel("");
        panel.add(new JLabel("City Size"));
        panel.add(citySize);
        panel.add(sizeMsg);

        panel.add(new JLabel("Shape Details"));
        panel.add(new JPanel());
        panel.add(new JPanel());

        JCheckBox circleShape = new JCheckBox();
        JComboBox<String> circleShapeColor = new JComboBox<>(colors);
        circleShapeColor.setSelectedIndex(4);
        panel.add(new JLabel("Circle City Shape"));
        panel.add(circleShape);
        panel.add(circleShapeColor);

        JCheckBox squareShape = new JCheckBox();
        JComboBox<String> squareShapeColor = new JComboBox<>(colors);
        squareShapeColor.setSelectedIndex(2);
        panel.add(new JLabel("Box City Shape"));
        panel.add(squareShape);
        panel.add(squareShapeColor);

        JCheckBox hollowPlusShape = new JCheckBox();
        JComboBox<String> hollowPlusShapeColor = new JComboBox<>(colors);
        hollowPlusShapeColor.setSelectedIndex(1);
        panel.add(new JLabel("Hollow Plus City Shape"));
        panel.add(hollowPlusShape);
        panel.add(hollowPlusShapeColor);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enter new city's details.",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            boolean[] shapes = {circleShape.isSelected(), squareShape.isSelected(), hollowPlusShape.isSelected()};
            Color[] colorsSelected = {
                    getColor((String) Objects.requireNonNull(circleShapeColor.getSelectedItem())),
                    getColor((String) Objects.requireNonNull(squareShapeColor.getSelectedItem())),
                    getColor((String) Objects.requireNonNull(hollowPlusShapeColor.getSelectedItem())),
            };


            for (City city : Blackboard.getInstance().cityList) {
                if (city.label.equals(cityName.getText())) {
                    Logger.getInstance().log("City already exists.");
                    return null;
                }
            }

            return createNewCityFromInput(x, y, cityName.getText(), citySize.getText(), shapes, colorsSelected);

        }

        String errorMsg = "Cancelled";
        Logger.getInstance().log(errorMsg);
        return null;

    }

    /**
     * Creates new city from the interface and display the same
     * @param x latitude of the city identified by the position where mouse is clicked
     * @param y longitude of the city identified by the position where mouse is clicked
     * @param name city name given by user
     * @param citySize size of the city given by user (must be within 30 and 80)
     * @param shapes shape of the city chosen by user (available shapes: circle, box, hollow plus the circle or box or both)
     * @param colors color of the city chosen by user (Red, Blue, Cyan, Orange, Yellow, Gray)
     * @return creates a city of desired shape, size and color and display the same in the interface
     */
    public City createNewCityFromInput(int x, int y, String name, String citySize, boolean[] shapes, Color[] colors) {

        String errorMsg = null;

        if (name == null) {
            errorMsg = "Please enter a name for the city.";
            Logger.getInstance().log(errorMsg);
            return null;
        }

        int size;
        try {
            size = Integer.parseInt(citySize);
            if (size < City.minSize) {
                errorMsg = "Please enter a minimum size of " + City.minSize + ".";
                Logger.getInstance().log(errorMsg);
                return null;
            } else if (size > City.maxSize) {
                errorMsg = "Please enter a size less than " + City.maxSize + ".";
                Logger.getInstance().log(errorMsg);
                return null;
            }
        } catch (NumberFormatException e) {
            errorMsg = "Please enter an integer for city size.";
            Logger.getInstance().log(errorMsg);
            return null;
        }

        ShapeComponent circleCityShape = null;
        if (shapes[0]) {
            circleCityShape = new Circle(x, y, size, colors[0]);
        }

        ShapeDecorator squareCityShape = null;
        if (shapes[1]) {
            squareCityShape = new Square(x, y, size, colors[1]);
        }

        ShapeDecorator hollowPlusCityShape = null;
        if (shapes[2]) {
            hollowPlusCityShape = new HollowPlus(x, y, size, colors[2]);
        }

        CityAbstractFactory cityFactory = new CityFactory();
        return cityFactory.createCity(name, x, y, size, circleCityShape, squareCityShape, hollowPlusCityShape);
    }

    private Color getColor(String color) {

        Color retVal;

        switch (color) {
            case "Blue":
                retVal = Color.BLUE;
                break;
            case "Cyan":
                retVal = Color.CYAN;
                break;
            case "Red":
                retVal = Color.RED;
                break;
            case "Orange":
                retVal = Color.ORANGE;
                break;
            case "Yellow":
                retVal = Color.YELLOW;
                break;
            default:
                retVal = Color.GRAY;
                break;
        }

        return retVal;
    }

    private City findCityContaining(int x, int y) {

        for (City city : Blackboard.getInstance().cityList) {
            if (city.containsPoint(x, y)) {
                return city;
            }
        }

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

        if (!userConnect) return;

        if (this.canCreateCities) {
            selectedCity = findCityContaining(e.getX(), e.getY());

            if (selectedCity == null) {
                City newCity = takeNewCityInput(e.getX(), e.getY());
                if (newCity != null) {
                    Blackboard.getInstance().cityList.add(newCity);
                    Blackboard.getInstance().dataChanged = true;
                    Logger.getInstance().log("Added new city at x=" + e.getX() + ", y=" + e.getY());
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
        selectedCity = findCityContaining(e.getX(), e.getY());
    }

    /**
     * This method is used to make sure the city is being drawn as it is being dragged
     *
     * @param e The mouse event caught
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        if (selectedCity != null && this.canMoveCities) {
            selectedCity.move(e.getX(), e.getY());
            Blackboard.getInstance().dataChanged = true;
        }

    }

    /**
     * This method is used to mark the end for drag and drop operation on a city, if any
     *
     * @param e The mouse event caught
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        if (selectedCity != null) {
            if (this.canMoveCities) {
                selectedCity.move(e.getX(), e.getY());
            } else { //this.canConnectCities
                City nextCity = findCityContaining(e.getX(), e.getY());
                if (nextCity != null) {
                    Blackboard.getInstance().connectTwoCities(selectedCity, nextCity);
                }
            }

            Blackboard.getInstance().dataChanged = true;
            selectedCity = null; //this is a follow up of mouse-released
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * This method is run when a TSPAlgorithm notifies that the path is ready
     * to be drawn.
     *
     * @param o   The Observable controller.tsp.TSPNearestNbr object
     * @param arg The arguments passed by it
     */
    @Override
    public void update(Observable o, Object arg) {

        repaint();

    }
}