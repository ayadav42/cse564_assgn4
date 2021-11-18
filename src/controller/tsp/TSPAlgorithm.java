package controller.tsp;

import model.Blackboard;
import model.City;

import java.util.List;
import java.util.Observable;

/**
 * This abstract class serves as the base algorithm for all the TSP algorithms.
 * Each child of the class must implement the method calculate. After they are
 * done calculating, they notify their observers.
 *
 * @author Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @version 1.0
 * @since 2021-11-16
 */
public abstract class TSPAlgorithm extends Observable implements Runnable {

    public boolean keepRunning = true;
    public static final double COOLING_RATE = 0.005;
    public static final double TEMP_MIN = 0.99;
    public static final double EARTH_EQ_RAD = 6378.1370D;
    public static final double DEG_TO_RAD = Math.PI / 180D;
    public static final double KM_TO_MILES = 0.621371;

    public final TSPTypes type;

    protected TSPAlgorithm(TSPTypes type) {
        this.type = type;
    }

    /**
     * Each child of this class implements this method.
     *
     * @param cityList The list of cities to calculate the path for
     */
    public abstract void calculate(List<City> cityList);

    /**
     * This method ensures that the TSP Algorithm keeps calculating the latest path
     * until asked to stop.
     */
    public void run() {
        while (this.keepRunning) {
            try {
                Thread.sleep(1); //without this code won't work, need to ask prof. why
                if (Blackboard.getInstance().dataChanged) {
                    this.calculate(Blackboard.getInstance().cityList);
                    Blackboard.getInstance().dataChanged = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
