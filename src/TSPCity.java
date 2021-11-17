/**
 * This class provides the city model on which the TSP algorithm needs to run
 */
public class TSPCity {
    public double longitude;
    public double latitude;
    public String name;

    /**
     * This constructor initialises the city with name, latitude and longitude
     * @param name: name of the city
     * @param latitude: latitude of the city
     * @param longitude: longitude of the city
     */
    public TSPCity(String name, double latitude, double longitude) {
        this.name=name;
        this.longitude=longitude*BaseAlgorthim.DEG_TO_RAD;
        this.latitude=latitude*BaseAlgorthim.DEG_TO_RAD;
    }

    /**
     * This method measures the distance between the calling city and the parameter city
     * @param city the location from which the distance needs to be calculated
     * @return the distance value
     */
    public double measureDistance(TSPCity city) {
        double delLongitude = (city.longitude - this.longitude);
        double delLatitude = (city.latitude - this.latitude);
        double a = Math.pow(Math.sin(delLatitude/2D), 2D) +
                Math.cos(this.latitude)*Math.cos(city.latitude)*Math.pow(Math.sin(delLongitude/2D), 2D);
        return BaseAlgorthim.KM_TO_MILES*BaseAlgorthim.EARTH_EQ_RAD*2D*Math.atan2(Math.sqrt(a), Math.sqrt(1D-a));
    }
}
