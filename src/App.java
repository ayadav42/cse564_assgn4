import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class orchestrates various classes and functions to calculate the shortest hamiltonian path
 * in the list of cities read from a file or entered by user manually.
 * The main method inside this class shows a window to the user.
 *
 * @author amaryadav
 * @author kaichen
 * @version 1.0
 * @since 2021-10-08
 */
public class App extends JFrame implements ActionListener {

    private final Workspace workspace;

    /**
     * Creates an instance of the app with a menu bar and one panel inside.
     * The menu bar contains options to reset workspace, load from existing and save current.
     *
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
        menuBar.add(fileMenu);
        menuBar.setVisible(true);
        add(menuBar, BorderLayout.NORTH);

        workspace = new Workspace(20, 20, 1560, 840, Color.WHITE);
        add(workspace, BorderLayout.CENTER);
        workspace.setVisible(true);

        TSP tsp = new TSP();
        tsp.addObserver(workspace);
        Thread thread = new Thread(tsp); //Polling for resources, NIO.2
        thread.start();

    }

    /**
     * This main method creates an instance of App and display the app.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {

        App app = new App();
        app.setVisible(true);

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
                    workspace.resetCityList();
                    break;
                case "Open":
                    openExistingWorkspace();
                    break;
                case "Save":
                    saveCurrentWorkspace();
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
        System.out.println("You chose " + filepath);
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

        ArrayList<City> cities = new ArrayList<>();
        System.out.println("reading txt file: " + filepath);
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line = br.readLine();

        while (line != null) {
            String[] arr = line.split(",");
            City newCity = new City(arr[0], Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
            cities.add(newCity);
            line = br.readLine();
        }

        br.close();
        System.out.println("Read file with " + cities.size() + " cities.");

        workspace.updateCityList(cities);

    }

    private void saveCurrentWorkspace() throws IOException {

        String filename = (String) JOptionPane.showInputDialog(
                this,
                "Enter a name for this workspace.",
                "Save Workspace",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Workspace0"
        );

        String data = workspace.getCitiesInTxtFriendlyFormat();
        FileWriter myWriter = new FileWriter(filename + ".txt");
        myWriter.write(data);
        myWriter.close();
        System.out.println("Successfully wrote to the file.");

    }

}
