import java.util.*;
import java.util.stream.Collectors;

/**
 * This class runs the travelling sales person algorithm to find the optimal route between the points.
 * It uses the Simulated Annealing Algorithm.
 *
 * @author : Ishanu Dhar (ID: 1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 */
public class TSPAlgorithm extends Observable implements BaseAlgorthim {
    public TSPRoute tspRoute;

    /**
     * Initializes the cities on which the TSP need to run
     * @param cities list of cities on which the TSP need to run
     */
    public void setCities(List<City> cities) {
        if (cities != null) {
            List<TSPCity> cityList = new LinkedList<>();
            int id =0;
            cities.forEach(n -> {
                cityList.add(new TSPCity(n.label + "", n.bounds.x, n.bounds.y));

            });
            tspRoute = new TSPRoute();
            tspRoute.cities = new LinkedList<>();
            tspRoute.cities.addAll(cityList);
            if (cities.size() > 1) {
                findRoute();
            } else {
                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * Updates the city that has been moved with the new coordinates
     * @param cityClicked the city that needs to update
     * @param x contains the x coordinate
     * @param y contains the y coordinate
     */
    public void updateCities(City cityClicked, int x, int y) {
        if (cityClicked != null) {
            tspRoute.cities.stream().map(n -> {
                if (n.name.equals(cityClicked.label + "")) {
                    System.out.println("inside");
                    n.latitude = x * Math.PI / 180D;
                    n.longitude = y * Math.PI / 180D;
                }
                return n;
            }).collect(Collectors.toList());
            System.out.println();
            System.out.println("Printing updated route");
            tspRoute.cities.forEach(n -> {
                        System.out.print(n.name + " ");
                    }
            );
        }
    }


    /**
     * This method  calculates the TSP and fetches the shortest route
     */
    public void findRoute() {
        System.out.println("Find route called");
        TSPRoute shortestRoute = createTspRoute(tspRoute);
        TSPRoute adjacentRoute;
        int initialTemperature = 999;
        while (initialTemperature > BaseAlgorthim.TEMP_MIN) {
            TSPRoute route = createTspRoute(tspRoute);
            adjacentRoute = obtainAdjacentRoute(route);
            if (tspRoute.getTotalDistance() < shortestRoute.getTotalDistance()) {
                shortestRoute = createTspRoute(tspRoute);
            }
            if (acceptRoute(tspRoute.getTotalDistance(), adjacentRoute.getTotalDistance(), initialTemperature)) {
                tspRoute = createTspRoute(adjacentRoute);
            }
            initialTemperature *= 1 - BaseAlgorthim.COOLING_RATE;
        }

        System.out.println("Printing the route");
        shortestRoute.cities.forEach(n -> {
                    System.out.print(n.name + " ");
                }
        );

        /*Blackboard.getInstance().path.clear();

        int id =0;

        //keep an eye, ma should be in the black board class
        shortestRoute.cities.forEach(n -> {
            Blackboard.getInstance().path.add(new City(n.name,(int)(n.latitude/TSPCity.DEG_TO_RAD),(int)(n.longitude/TSPCity.DEG_TO_RAD)));
        });

            Map<City,City> m = new HashMap<>();
            City prev = Blackboard.getInstance().path.get(0);


            for (int i = 1; i < Blackboard.getInstance().path.size(); i++) {
                m.put(prev,Blackboard.getInstance().path.get(i));
                prev = Blackboard.getInstance().path.get(i);
            }*/
        List<City> path = new ArrayList<>();
        shortestRoute.cities.forEach(n->{
            path.add(new City(n.name,(int)(n.latitude/BaseAlgorthim.DEG_TO_RAD),(int)(n.longitude/BaseAlgorthim.DEG_TO_RAD)));
        });

        Blackboard.getInstance().path=path;
        System.out.println("Notified observers");
        setChanged();
        notifyObservers();
    }

    private TSPRoute createTspRoute(TSPRoute tspRoute) {
        TSPRoute route = new TSPRoute();
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