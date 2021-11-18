package controller.tsp;

import model.Blackboard;
import model.City;

import java.util.ArrayList;
import java.util.List;

public class TSPUserConnect extends TSPAlgorithm {

    public static final String name = "TSP - User Connect";

    public TSPUserConnect() {
        super(TSPTypes.USER_CONNECT);
        this.keepRunning = true;
    }

    @Override
    public void calculate(List<City> cityList) {

        City newCity = null;
        for (City city : cityList) {
            City cityFound = Blackboard.getInstance().findCityInPath(city);
            if (cityFound == null) newCity = city;
        }

        if (newCity != null) {
            List<City> newCluster = new ArrayList<>();
            newCluster.add(newCity);
            Blackboard.getInstance().path.add(newCluster);
        }

        setChanged();
        notifyObservers();

    }


}
