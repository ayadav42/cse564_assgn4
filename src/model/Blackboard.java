package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class stores the list of cities and another one with these
 * cities in the order of shortest traversal. It also maintains a flag
 * to mark change in city data.
 *
 * @author amaryadav, greeshma
 * @version 1.0
 * @since 2021-10-08
 */
public class Blackboard {

    private static Blackboard _instance;

    public List<City> cityList;
    public List<List<City>> path;
    public boolean dataChanged;

    private Blackboard() {

        this.cityList = new ArrayList<>();
        this.path = new ArrayList<>();
        dataChanged = true;

    }

    /**
     * This method fetches the only instance of blackboard.
     */
    public static Blackboard getInstance() {
        if (_instance == null) _instance = new Blackboard();
        return _instance;
    }

    public void printCities() {
        System.out.println("\nPrinting all cities");
        System.out.println(this.cityList);
        System.out.println("---------------------");
    }

    public void printClusters() {
        System.out.println("\nPrinting clusters");
        for (List<City> cluster : this.path) {
            System.out.println(cluster);
        }
        System.out.println("---------------------");
    }

    public City findCityInPath(City city) {
        for (List<City> cluster : path) {
            for (City existingCity : cluster) {
                if (existingCity.equals(city)) {
                    return city;
                }
            }
        }
        return null;
    }

    public List<City> getCluster(City city) {
        for (List<City> cluster : path) {
            for (City existingCity : cluster) {
                if (existingCity.equals(city)) {
                    return cluster;
                }
            }
        }
        return null;
    }

    public City getPrevCity(City city) {
        for (List<City> cluster : path) {
            for (City existingCity : cluster) {
                if (existingCity.nextCity != null && existingCity.nextCity.equals(city)) {
                    return existingCity;
                }
            }
        }
        return null;
    }

    public int getClusterIndex(City city) {
        int index = 0;
        for (List<City> cluster : path) {
            for (City existingCity : cluster) {
                if (existingCity.equals(city)) {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    public void connectTwoCities(City currCity, City otherCity) {

        if (currCity.nextCity != null) return;

        City prevOfOtherCity = getPrevCity(otherCity);
        if (prevOfOtherCity != null) {
            return;
        }

        int currCityClusterIndex = getClusterIndex(currCity);
        List<City> currCityCluster = path.get(currCityClusterIndex);

        int otherCityClusterIndex = getClusterIndex(otherCity);
        List<City> otherCityCluster = path.get(otherCityClusterIndex);

        if (currCityClusterIndex == otherCityClusterIndex) {
            return;
        }

        int otherCityIndex = otherCityCluster.indexOf(otherCity);
        List<City> otherCityNewCluster = new ArrayList<>();
        for (int i = otherCityCluster.size() - 1; i >= otherCityIndex; i--) {
            otherCityNewCluster.add(otherCityCluster.remove(otherCityCluster.size() - 1));
        }
        if (otherCityCluster.size() == 0) path.remove(otherCityClusterIndex);
        currCity.connectNextCity(otherCity);
        int currCityIndex = currCityCluster.indexOf(currCity);
        currCityCluster.addAll(currCityIndex, otherCityNewCluster);

        printClusters();

        this.dataChanged = true;
    }

}
