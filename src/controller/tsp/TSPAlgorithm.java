package controller.tsp;

import model.Blackboard;
import model.City;

import java.util.List;
import java.util.Observable;

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

    public abstract void calculate(List<City> cityList);

    /**
     * This method helps controller.tsp.TSPNearestNbr scan for data changes in the list of cities
     * and calculate the new path if needed.
     */
    public void run() {
        while (this.keepRunning) {
            try {
                Thread.sleep(1); //without this code won't work, need to ask prof. why
                if (Blackboard.getInstance().dataChanged) {
                    System.out.println("dataChanged=" + Blackboard.getInstance().dataChanged);
                    this.calculate(Blackboard.getInstance().cityList);
                    Blackboard.getInstance().dataChanged = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
