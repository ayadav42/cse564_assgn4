import java.util.*;
import java.util.stream.Collectors;

/**
 * This class runs the travelling sales person algorithm to find the optimal route between the points.
 * It uses the Simulated Annealing Algorithm.
 *
 * @author : Ishanu Dhar (ID: 1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 */
public class TSPCluster extends Observable implements BaseAlgorthim {
    public TSPRoute tspRoute;
    public TSPRoute shortestRoute;
    public List<List<City>> path = new ArrayList<>(4);


    /**
     * Initializes the cities on which the TSP need to run
     * @param cities list of cities on which the TSP need to run
     */
    public void setCities(List<City> cities) {
        if (cities != null) {
            List<List<TSPCity>> cityList = new ArrayList<>(4);
            cities.forEach(n -> {
                if (n.bounds.x < App.workspaceWidth/2 && n.bounds.y > App.workspaceHeight/2){
                    cityList.get(0).add(new TSPCity(n.label + "", n.bounds.x, n.bounds.y));
                }
                else if (n.bounds.x > App.workspaceWidth/2 && n.bounds.y > App.workspaceHeight/2){
                    cityList.get(1).add(new TSPCity(n.label + "", n.bounds.x, n.bounds.y));
                }
                else if (n.bounds.x > App.workspaceWidth/2 && n.bounds.y < App.workspaceHeight/2){
                    cityList.get(2).add(new TSPCity(n.label + "", n.bounds.x, n.bounds.y));
                }
                else {
                    cityList.get(3).add(new TSPCity(n.label + "", n.bounds.x, n.bounds.y));
                }
            });
            for (int i=0;i<cityList.size();i++){
                tspRoute = new TSPRoute();
                tspRoute.cities = new ArrayList<>();
                tspRoute.cities.addAll(cityList.get(i));
                if (cities.size()>1)
                    findRoute(i);
                else{
                    setChanged();
                    notifyObservers();
                }
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
    public void findRoute(int index) {
        System.out.println("Find route called");
        shortestRoute = createTspRoute(tspRoute);
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
        computeCluster(index);
    }
    public void computeCluster(int clusterIndex){
        shortestRoute.cities.forEach(n->{
            path.get(clusterIndex).add(new City(n.name,(int)(n.latitude/BaseAlgorthim.DEG_TO_RAD),(int)(n.longitude/BaseAlgorthim.DEG_TO_RAD)));
        });
        Blackboard.getInstance().clusteredCity=path;
        System.out.println("Notified obeservers");
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
