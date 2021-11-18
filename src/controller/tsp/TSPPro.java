package controller.tsp;

import controller.Logger;
import model.Blackboard;
import model.City;

import java.util.*;

/**
 * This class runs the travelling sales person algorithm to find the optimal route between the points.
 * It uses the Simulated Annealing Algorithm.
 *
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @version 1.0
 * @since 2021-11-16
 */
public class TSPPro extends TSPAlgorithm {

    public static final String name = "TSP - Pro";
    public TSPRoute tspRoute;
    public TSPRoute shortestRoute;

    public TSPPro() {
        super(TSPTypes.TSP_PRO);
        this.keepRunning = true;
    }

    /**
     * Initializes the cities on which the TSPNearestNbr need to run
     *
     * @param cityList list of cities on which the TSPNearestNbr need to run
     */
    @Override
    public void calculate(List<City> cityList) {

        Logger.getInstance().log("Calculating shortest path using TSP Pro");

        List<List<City>> path = new ArrayList<>();

        if (cityList.isEmpty()) {
            Blackboard.getInstance().path = path;
            Logger.getInstance().log("Path updated.");
            setChanged();
            notifyObservers();
            return;
        }

        List<TSPCity> tspCities = new ArrayList<>();
        cityList.forEach(city -> {
            tspCities.add(new TSPCity(city, city.bounds.x, city.bounds.y));
        });

        tspRoute = new TSPRoute(tspCities);
        findRoute();

        List<City> connections = new ArrayList<>();
        shortestRoute.cities.forEach(tspCity -> {
            connections.add(tspCity.city);
        });

        path.add(connections);
        Blackboard.getInstance().path = path;
        Logger.getInstance().log("Path updated.");
        setChanged();
        notifyObservers();

    }


    /**
     * This method  calculates the TSPNearestNbr and fetches the shortest route
     */
    public void findRoute() {

        System.out.println("Find route called");
        shortestRoute = createTspRoute(tspRoute);
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

        System.out.println("Printing the route");
        shortestRoute.cities.forEach(tspCity -> {
            System.out.print(tspCity.city.label + " ");
        });

    }


    private TSPRoute createTspRoute(TSPRoute tspRoute) {
        TSPRoute route = new TSPRoute(new ArrayList<>());
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
            x1 = (int) (route.cities.size() * Math.random());
            x2 = (int) (route.cities.size() * Math.random());
        }
        TSPCity city1 = route.cities.get(x1);
        TSPCity city2 = route.cities.get(x2);
        route.cities.set(x2, city1);
        route.cities.set(x1, city2);
        return route;
    }

}