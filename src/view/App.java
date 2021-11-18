package view;

import controller.Logger;
import model.Blackboard;
import model.City;
import controller.tsp.*;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * This class orchestrates various classes and functions to calculate the shortest hamiltonian path
 * in the list of cities read from a file or entered by user manually.
 * The main method inside this class shows a window to the user.
 *
 * @author amaryadav
 * @author navya
 * @version 1.0
 * @since 2021-10-08
 */
public class App extends JFrame implements ActionListener {

    public static final int workspaceWidth = 1560;
    public static final int workspaceHeight = 840;
    private final Workspace workspace;
    private final JMenu connectionsMenu;
    private final JMenu actionMenu;
    private TSPAlgorithm tspAlgorithm;
    private Thread tspThread;
    public static JLabel status;

    /**
     * Creates an instance of the app with a menu bar and one panel inside.
     * The menu bar contains options to reset workspace, load from existing and save current.
     */
    public App() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 1600;
        int height = 900;
        setBounds(50, 50, width, height);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem newWorkspace = new JMenuItem("New/Reset");
        newWorkspace.addActionListener(this);
        fileMenu.add(newWorkspace);

        JMenuItem openWorkspace = new JMenuItem("Open");
        openWorkspace.addActionListener(this);
        fileMenu.add(openWorkspace);

        JMenuItem saveWorkspace = new JMenuItem("Save");
        saveWorkspace.addActionListener(this);
        fileMenu.add(saveWorkspace);

        connectionsMenu = new JMenu("Connections");

        JMenuItem tspNN = new JMenuItem(TSPNearestNbr.name);
        tspNN.addActionListener(this);
        connectionsMenu.add(tspNN);

        JMenuItem tspPro = new JMenuItem(TSPPro.name);
        tspPro.addActionListener(this);
        connectionsMenu.add(tspPro);

        JMenuItem clusters = new JMenuItem(TSPCluster.name);
        clusters.addActionListener(this);
        connectionsMenu.add(clusters);

        JMenuItem userConnect = new JMenuItem(TSPUserConnect.name);
        userConnect.addActionListener(this);
        connectionsMenu.add(userConnect);

        ImageIcon moveIcon = new ImageIcon("icons/move.JPG");
        ImageIcon connectIcon = new ImageIcon("icons/Connect.JPG");
        ImageIcon createIcon = new ImageIcon("icons/create.JPG");

        actionMenu = new JMenu("Action");

        JMenuItem move = new JMenuItem("Move", moveIcon);
        move.addActionListener(this);
        actionMenu.add(move);

        JMenuItem connect = new JMenuItem("Connect", connectIcon);
        connect.addActionListener(this);
        actionMenu.add(connect);

        JMenuItem create = new JMenuItem("Create", createIcon);
        create.addActionListener(this);
        actionMenu.add(create);

        menuBar.add(fileMenu);
        menuBar.add(connectionsMenu);
        actionMenu.setEnabled(false);
        menuBar.add(actionMenu);
        menuBar.setVisible(true);
        add(menuBar, BorderLayout.NORTH);

        workspace = new Workspace(20, 20, workspaceWidth, workspaceHeight, Color.WHITE);
        add(workspace, BorderLayout.CENTER);
        workspace.setVisible(true);

        status = new JLabel();
        status.setSize(workspaceWidth, 100);
        add(status, BorderLayout.SOUTH);

        setTspAlgorithm(TSPNearestNbr.name);

    }

    /**
     * This main method creates an instance of gui.view.App and display the app.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {

        App app = new App();
        app.setVisible(true);

    }

    private String getTspName(TSPTypes type) {

        String retVal;

        switch (type) {
            case TSP_PRO:
                retVal = TSPPro.name;
                break;
            case TSP_NEAREST_NBR:
                retVal = TSPNearestNbr.name;
                break;
            case TSP_CLUSTER:
                retVal = TSPCluster.name;
                break;
            default:
                retVal = TSPUserConnect.name;
                break;
        }

        return retVal;

    }

    private String getTspName(TSPAlgorithm tspAlgorithm) {

        TSPTypes type = tspAlgorithm.type;
        return getTspName(type);

    }

    private void setTspAlgorithm(String name) {

        TSPAlgorithm tspAlgorithm;

        switch (name) {
            case TSPNearestNbr.name:
                tspAlgorithm = new TSPNearestNbr();
                break;
            case TSPPro.name:
                tspAlgorithm = new TSPPro();
                break;
            case TSPCluster.name:
                tspAlgorithm = new TSPCluster();
                break;
            default:
                tspAlgorithm = new TSPUserConnect();
        }

        setTspAlgorithm(tspAlgorithm);

    }

    private void setTspAlgorithm(TSPAlgorithm tspAlgorithm) {

        if (this.tspThread != null && !this.tspThread.isInterrupted()) {
            this.tspThread.stop();
        }

        if (this.tspAlgorithm != null) {
            this.tspAlgorithm.deleteObserver(this.workspace);
        }

        this.tspAlgorithm = tspAlgorithm;
        Logger.getInstance().log("Setting the new TSPAlgorithm type=" + this.tspAlgorithm.type + ", name=" + getTspName(this.tspAlgorithm));
        this.tspAlgorithm.addObserver(workspace);
        this.tspThread = new Thread(tspAlgorithm); //Polling for resources, NIO.2
        this.tspThread.start();
        Blackboard.getInstance().dataChanged = true;

        if (this.tspAlgorithm.type == TSPTypes.USER_CONNECT) {

            if (this.actionMenu.isEnabled()) {
                Logger.getInstance().log("Action menu was enabled, disabling Action menu and enabling Connections menu");
                disableUserConnect();
                enableTSPConnectionMenuItems();

            } else {
                Logger.getInstance().log("Action menu was disabled, enabling Action menu and disabling Connections menu");
                enableUserConnect();
                disableTSPConnectionMenuItems();

            }
        }

    }

    public void enableTSPConnectionMenuItems() {
        for (Component connectionMenuItem : this.connectionsMenu.getMenuComponents()) {
            connectionMenuItem.setEnabled(true);
        }
    }

    public void disableTSPConnectionMenuItems() {
        for (Component connectionMenuItem : this.connectionsMenu.getMenuComponents()) {
            connectionMenuItem.setEnabled(false);
        }
        this.connectionsMenu.getMenuComponent(3).setEnabled(true);
    }

    public void enableUserConnect() {

        this.workspace.enableUserConnect();
        this.actionMenu.setEnabled(true);

    }

    public void disableUserConnect() {

        this.workspace.disableUserConnect();
        this.actionMenu.setEnabled(false);

    }

    /**
     * This method is fired each time any action event is triggered.
     * It helps to identify relevant actions such as file menu item functions.
     *
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem item = (JMenuItem) e.getSource();
        try {
            switch (item.getText()) {
                case "New/Reset":
                    Blackboard.getInstance().path = new ArrayList<>();
                    workspace.resetCityList();
                    break;
                case "Open":
                    openExistingWorkspace();
                    break;
                case "Save":
                    saveCurrentWorkspace();
                    break;

                case TSPNearestNbr.name:
                    setTspAlgorithm(TSPNearestNbr.name);
                    break;
                case TSPPro.name:
                    setTspAlgorithm(TSPPro.name);
                    break;
                case TSPCluster.name:
                    setTspAlgorithm(TSPCluster.name);
                    break;
                case TSPUserConnect.name:
                    setTspAlgorithm(TSPUserConnect.name);
                    break;

                case "Move":
                    this.workspace.enableMove();
                    break;
                case "Connect":
                    this.workspace.enableConnect();
                    break;
                case "Create":
                    this.workspace.enableCreateNewCity();
                    break;
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

    }

    private String chooseFile() {

        FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
        fd.setVisible(true);
        String filepath = fd.getDirectory() + fd.getFile();
        Logger.getInstance().log("You chose " + filepath);
        return filepath;

    }

    private void showDialog(String message) {

        Dialog dialog = new Dialog(this, "Warning", true);
        dialog.setLayout(new FlowLayout());
        Button button = new Button("OK");
        button.addActionListener(e -> dialog.setVisible(false));
        dialog.add(new Label(message));
        dialog.add(button);
        dialog.setSize(300, 300);
        dialog.setVisible(true);

    }

    private void openExistingWorkspace() throws IOException {

        String filepath = chooseFile();
        if (filepath.isEmpty()) {
            showDialog("No file selected, please select a file to continue.");
            return;
        } else if (!filepath.contains(".txt")) {
            showDialog("Please select a .txt file to continue.");
            return;
        }

        Logger.getInstance().log("Reading txt file: " + filepath);

        List<City> cities = new ArrayList<>();
        List<List<City>> path = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));

        String tspAlgo = br.readLine(); //first line contains the algo

        int noOfCities = 0;
        String line = br.readLine();

        while (line != null) {

            List<City> cluster = new ArrayList<>();

            while (!line.equals("$$")) {
                String[] arr = line.split(",");
                String cityName = arr[0];
                int x = Integer.parseInt(arr[1]);
                int y = Integer.parseInt(arr[2]);
                String size = arr[3];

                boolean[] shapes = {false, false, false};
                Color[] colors = {null, null, null};
                if (arr[4] != null && !arr[4].isEmpty() && !arr[4].equals("$")) {
                    shapes[0] = true;
                    colors[0] = new Color(Integer.parseInt(arr[4]));
                }
                if (arr[5] != null && !arr[5].isEmpty() && !arr[5].equals("$")) {
                    shapes[1] = true;
                    colors[1] = new Color(Integer.parseInt(arr[5]));
                }
                if (arr[6] != null && !arr[6].isEmpty() && !arr[6].equals("$")) {
                    shapes[2] = true;
                    colors[2] = new Color(Integer.parseInt(arr[6]));
                }

                City newCity = this.workspace.createNewCityFromInput(x, y, cityName, size, shapes, colors);
                cities.add(newCity);
                cluster.add(newCity);
                noOfCities++;

                line = br.readLine();
            }

            path.add(cluster);

            line = br.readLine();
        }

        br.close();
        Logger.getInstance().log("Read file with \" + noOfCities + \" cities.");

        Blackboard.getInstance().path = path;
        workspace.updateCityList(cities);
        Blackboard.getInstance().printCities();
        Blackboard.getInstance().printClusters();
        setTspAlgorithm(tspAlgo);

    }

    private void saveCurrentWorkspace() throws IOException {

        String filename = (String) JOptionPane.showInputDialog(
                this,
                "Enter a name for this workspace.",
                "Save gui.Workspace",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Workspace0"
        );

        String data = getTspName(this.tspAlgorithm) + "\n";
        data += workspace.getCitiesInTxtFriendlyFormat();
        FileWriter myWriter = new FileWriter(filename + ".txt");
        myWriter.write(data);
        myWriter.close();
        Logger.getInstance().log("Successfully wrote to the file.");

    }

}
