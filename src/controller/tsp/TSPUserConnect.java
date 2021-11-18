package controller.tsp;

import controller.Logger;
import model.Blackboard;
import model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the routing information between cities.
 *
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @version 1.0
 * @since 2021-11-16
 */
public class TSPUserConnect extends TSPAlgorithm {

    public static final String name = "TSP - User Connect";

    /**
     * Creates an instance of TSPUserConnect class initialized
     * to keep running until requested to stop.
     */
    public TSPUserConnect() {
        super(TSPTypes.USER_CONNECT);
        this.keepRunning = true;
    }

    /**
     * Implementation of the 'calculate' method inherited.
     *
     * @param cityList The list of cities to calculate the path for
     */
    @Override
    public void calculate(List<City> cityList) {

        Logger.getInstance().log("Calculating shortest path using TSP User Connect.");

        City newCity = null;
        for (City city : cityList) {
            City cityFound = Blackboard.getInstance().findCityInPath(city);
            if (cityFound == null) newCity = city;
        }

        if (newCity != null) {
            List<City> newCluster = new ArrayList<>();
            newCluster.add(newCity);
            Blackboard.getInstance().path.add(newCluster);
        }

        Logger.getInstance().log("Path updated.");
        setChanged();
        notifyObservers();

    }


}
