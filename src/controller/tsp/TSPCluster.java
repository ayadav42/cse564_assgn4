package controller.tsp;

import controller.Logger;
import model.Blackboard;
import model.City;
import view.App;

import java.util.*;

/**
 * This class runs the travelling sales person algorithm to find the optimal route between the points.
 * It uses the Simulated Annealing Algorithm.
 *
 * @author Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @version 1.0
 * @since 2021-11-16
 */
public class TSPCluster extends TSPAlgorithm {

    public TSPRoute tspRoute;
    public TSPRoute shortestRoute;
    public static final String name = "TSP - Cluster";

    /**
     * Creates an instance of controller.tsp.TSPNearestNbr class initialized
     * to keep running when requested.
     */
    public TSPCluster() {
        super(TSPTypes.TSP_CLUSTER);
        this.keepRunning = true;
    }

    /**
     * Implementation of the 'calculate' method inherited.
     *
     * @param cityList The list of cities to calculate the path for
     */
    @Override
    public void calculate(List<City> cityList) {

        Logger.getInstance().log("Calculating shortest path using TSP Cluster");

        List<List<City>> path = new ArrayList<>();

        if (cityList.isEmpty()) {
            Blackboard.getInstance().path = path;
            Logger.getInstance().log("Path updated.");
            setChanged();
            notifyObservers();
            return;
        }

        List<List<TSPCity>> tspClusterList = new ArrayList<>();
        tspClusterList.add(new ArrayList<>());
        tspClusterList.add(new ArrayList<>());
        tspClusterList.add(new ArrayList<>());
        tspClusterList.add(new ArrayList<>());

        cityList.forEach(city -> {
            if (city.bounds.x < App.workspaceWidth / 2 && city.bounds.y > App.workspaceHeight / 2) {
                tspClusterList.get(0).add(new TSPCity(city, city.bounds.x, city.bounds.y));
            } else if (city.bounds.x > App.workspaceWidth / 2 && city.bounds.y > App.workspaceHeight / 2) {
                tspClusterList.get(1).add(new TSPCity(city, city.bounds.x, city.bounds.y));
            } else if (city.bounds.x > App.workspaceWidth / 2 && city.bounds.y < App.workspaceHeight / 2) {
                tspClusterList.get(2).add(new TSPCity(city, city.bounds.x, city.bounds.y));
            } else {
                tspClusterList.get(3).add(new TSPCity(city, city.bounds.x, city.bounds.y));
            }
        });

        calculateRoute(path, tspClusterList);
        Blackboard.getInstance().path = path;
        Logger.getInstance().log("Path updated.");
        setChanged();
        notifyObservers();

    }

    private void calculateRoute(List<List<City>> path, List<List<TSPCity>> tspClusterList) {

        for (List<TSPCity> tspCities : tspClusterList) {
            tspRoute = new TSPRoute(new ArrayList<>());
            tspRoute.cities = new ArrayList<>();
            tspRoute.cities.addAll(tspCities);
            findRoute();
            List<City> connections = new ArrayList<>();
            shortestRoute.cities.forEach(tspCity -> {
                connections.add(tspCity.city);
            });
            path.add(connections);
        }

    }

    private void findRoute() {

        shortestRoute = createTspRoute(tspRoute);

        if (tspRoute.cities.size() <= 1) return;

        TSPRoute adjacentRoute;
        int initialTemperature = 999;

        while (initialTemperature > TSPAlgorithm.TEMP_MIN) {
            TSPRoute route = createTspRoute(tspRoute);
            adjacentRoute = obtainAdjacentRoute(route);
            if (tspRoute.getTotalDistance() < shortestRoute.getTotalDistance()) {
                shortestRoute = createTspRoute(tspRoute);
            }
            if (acceptRoute(tspRoute.getTotalDistance(), adjacentRoute.getTotalDistance(), initialTemperature)) {
                tspRoute = createTspRoute(adjacentRoute);
            }
            initialTemperature *= 1 - TSPAlgorithm.COOLING_RATE;
        }

    }

    private TSPRoute createTspRoute(TSPRoute tspRoute) {
        TSPRoute route = new TSPRoute(new ArrayList<>());
        route.cities = new LinkedList<>();
        route.cities.addAll(tspRoute.cities);
        return route;
    }

    private boolean acceptRoute(double currentDistance, double adjacentDistance, double temperature) {
        double acceptanceProb = 1.0;
        if (adjacentDistance >= currentDistance) {
            acceptanceProb = Math.exp(-(adjacentDistance - currentDistance) / temperature);
        }
        double random = Math.random();
        return acceptanceProb >= random;
    }

    private TSPRoute obtainAdjacentRoute(TSPRoute route) {
        int x1 = 0, x2 = 0;
        while (x1 == x2) {
            x1 = (int) (route.cities.size() * Math.random()); //1 * 0.121241251313 = 0.21345235235 -> 0
            x2 = (int) (route.cities.size() * Math.random());
        }
        TSPCity city1 = route.cities.get(x1);
        TSPCity city2 = route.cities.get(x2);
        route.cities.set(x2, city1);
        route.cities.set(x1, city2);
        return route;
    }

}