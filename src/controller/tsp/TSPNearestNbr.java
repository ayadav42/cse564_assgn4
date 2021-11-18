package controller.tsp;

import controller.Logger;
import model.Blackboard;
import model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for calculating the shortest hamiltonian path
 * for visiting all cities using Nearest Neighbor. It is also an observable
 * that notifies observers when it's done calculating. It runs on it's own thread.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @author Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @version 2.0
 * @since 2021-10-08
 */
public class TSPNearestNbr extends TSPAlgorithm {

    public static final String name = "TSP - Nearest Neighbor";

    /**
     * Creates an instance of TSPNearestNbr class initialized
     * to keep running until requested to stop.
     */
    public TSPNearestNbr() {
        super(TSPTypes.TSP_NEAREST_NBR);
        this.keepRunning = true;
    }

    /**
     * Implementation of the 'calculate' method inherited.
     *
     * @param cityList The list of cities to calculate the path for
     */
    @Override
    public void calculate(List<City> cityList) { //reorder the cities

        Logger.getInstance().log("Calculating shortest path using TSP Nearest Neighbor");

        List<List<City>> path = new ArrayList<>();

        if (cityList.isEmpty()) {
            Blackboard.getInstance().path = path;
            Logger.getInstance().log("Path updated.");
            setChanged();
            notifyObservers();
            return;
        }

        int visitedCount = 0;
        boolean[] visited = new boolean[cityList.size()];
        int[][] distance = new int[cityList.size()][cityList.size()];

        for (int i = 0; i < cityList.size(); i++) {
            City city1 = cityList.get(i);
            for (int j = 0; j < cityList.size(); j++) {
                City city2 = cityList.get(j);
                if (i != j) {
                    distance[i][j] = (int) Math.sqrt(Math.pow(city1.bounds.x - city2.bounds.x, 2) + Math.pow(city1.bounds.y - city2.bounds.y, 2));
                    distance[j][i] = distance[i][j];
                }
            }
        }

        List<City> connections = new ArrayList<>();
        connections.add(cityList.get(0));
        visitedCount++;
        visited[0] = true;
        int prevCityIndex = 0;

        while (visitedCount != cityList.size()) {
            //find next unvisited closest nbr
            int closestNbrIndex = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < cityList.size(); i++) {
                if (!visited[i] && distance[prevCityIndex][i] < minDistance) {
                    minDistance = distance[prevCityIndex][i];
                    closestNbrIndex = i;
                }
            }

            connections.add(cityList.get(closestNbrIndex));
            visitedCount++;
            visited[closestNbrIndex] = true;
        }

        path.add(connections);
        Blackboard.getInstance().path = path;
        Logger.getInstance().log("Path updated.");
        setChanged();
        notifyObservers();

    }

}
