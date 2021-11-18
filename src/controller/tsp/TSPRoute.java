package controller.tsp;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the routing information between cities.
 *
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @version 1.0
 * @since 2021-11-16
 */

public class TSPRoute {

    public List<TSPCity> cities;

    /**
     * Creates an instance of TSPRoute used by TSPAlgorithms
     *
     * @param cities The list of TSP cities
     */
    public TSPRoute(List<TSPCity> cities) {
        this.cities = new ArrayList<>();
        this.cities.addAll(cities);
    }

    /**
     * gets the total distance between the cities
     *
     * @return the total distance
     */
    public double getTotalDistance() {

        int citiesSize = this.cities.size();

        return (int) (this.cities.stream().mapToDouble(x->{
            int cityIndex = this.cities.indexOf(x);
            double returnValue = 0;
            if (cityIndex<citiesSize-1) returnValue = x.measureDistance(this.cities.get(cityIndex+1));
            return returnValue;
        }).sum() + this.cities.get(citiesSize-1).measureDistance(this.cities.get(0)));

    }

}