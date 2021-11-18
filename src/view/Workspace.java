package view;

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
 * and track user's input to ensure latest data. It observes the controller.tsp.TSP to be
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

        String[] cities = new String[Blackboard.getInstance().cityList.size()];
        int index = 0;

        for (City city : Blackboard.getInstance().cityList) {
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

        for (List<City> cityList : Blackboard.getInstance().path) {
            System.out.println("painting cities=" + cityList.size());
            City prev = null;

            for (City city : cityList) {
                if (prev != null) city.drawConnection(prev, g2D);
                prev = city;
            }

            if (prev != null) prev.drawConnection(cityList.get(0), g2D);

            for (City city : cityList) city.draw(g);
        }

    }

    private City findCityAround(int x, int y) {
        for (City city : Blackboard.getInstance().cityList) {
            if (city.containsPoint(x, y)) {
                System.out.println("found x=" + x + ", y=" + y + ", inside" + city);
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

        if (selectedCity != null) {
            selectedCity.move(e.getX(), e.getY());
            System.out.println("nulling selectedCity: " + selectedCity);
            selectedCity = null; //this is a follow up of mouse-released
            Blackboard.getInstance().dataChanged = true;

        } else {
            selectedCity = findCityAround(e.getX(), e.getY());
            if (selectedCity == null) {

                City newCity = takeNewCityInput(e.getX(), e.getY());
                if (newCity != null) {
                    Blackboard.getInstance().cityList.add(newCity);
                    Blackboard.getInstance().dataChanged = true;
                    System.out.println("added new city"); //TODO
                }

            }

        }

    }

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

            return createNewCityFromInput(x, y, cityName.getText(), citySize.getText(), shapes, colorsSelected);

        }

        String errorMsg = "Cancelled"; //TODO
        System.out.println("Cancelled");
        return null;

    }

    public City createNewCityFromInput(int x, int y, String name, String citySize, boolean[] shapes, Color[] colors) {

        String errorMsg = null; //TODO

        if (name == null) {
            errorMsg = "Please enter a name for the city.";
            return null;
        }

        for (City city : Blackboard.getInstance().cityList) {
            if (city.label.equals(name)) {
                errorMsg = "City already exists.";
                return null;
            }
        }

        int size;
        try {
            size = Integer.parseInt(citySize);
            if (size < City.minSize) {
                errorMsg = "Please enter a minimum size of " + City.minSize + ".";
                return null;
            } else if (size > City.maxSize) {
                errorMsg = "Please enter a size less than " + City.maxSize + ".";
                return null;
            }
        } catch (NumberFormatException e) {
            errorMsg = "Please enter an integer for city size.";
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
     * This method is run when controller.tsp.TSP notifies that the path is ready
     * to be drawn.
     *
     * @param o   The Observable controller.tsp.TSP object
     * @param arg The arguments passed by it
     */
    @Override
    public void update(Observable o, Object arg) {

        System.out.println("notified by controller.tsp.TSP, proceeding to repaint");
        repaint();

    }
}
