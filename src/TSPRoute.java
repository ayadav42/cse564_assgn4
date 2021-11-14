
import java.util.List;

/**
 * This class holds the routing information between cities.
 * @author : Ishanu Dhar (ID: 1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 */

public class TSPRoute {
    public List<TSPCity> cities;

    /**
     * gets the total distance between the cities
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