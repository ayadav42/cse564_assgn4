package controller;

import view.App;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the routing information between cities.
 *
 * @author Shashi Varan Reddy (ID: 1222911911, saloor@asu.edu)
 * @version 1.0
 * @since 2021-11-17
 */
public class Logger {

    private static Logger _instance;
    private final List<String> messages;

    private Logger() {
        this.messages = new ArrayList<>();
    }

    public static Logger getInstance() {

        if (_instance == null) {
            _instance = new Logger();
        }

        return _instance;
    }

    public void log(String message) {

        if (this.messages.size() > 5) this.messages.remove(this.messages.size() - 1);

        this.messages.add(0, message);
        App.status.setText(message);

    }

}