package controller.tsp;

import model.City;

/**
 * This class provides the city model on which the TSPNearestNbr algorithm needs to run
 */
public class TSPCity {

    public double longitude;
    public double latitude;
    public City city;

    /**
     * This constructor initialises the city with name, latitude and longitude
     *
     * @param city:      city
     * @param latitude:  latitude of the city
     * @param longitude: longitude of the city
     */
    public TSPCity(City city, double latitude, double longitude) {
        this.city = city;
        this.longitude = longitude * TSPAlgorithm.DEG_TO_RAD;
        this.latitude = latitude * TSPAlgorithm.DEG_TO_RAD;
    }

    /**
     * This method measures the distance between the calling city and the parameter city
     *
     * @param otherTspCity the location from which the distance needs to be calculated
     * @return the distance value
     */
    public double measureDistance(TSPCity otherTspCity) {
        double delLongitude = (otherTspCity.longitude - this.longitude);
        double delLatitude = (otherTspCity.latitude - this.latitude);
        double a = Math.pow(Math.sin(delLatitude / 2D), 2D) +
                Math.cos(this.latitude) * Math.cos(otherTspCity.latitude) * Math.pow(Math.sin(delLongitude / 2D), 2D);
        return TSPAlgorithm.KM_TO_MILES * TSPAlgorithm.EARTH_EQ_RAD * 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
    }
}
