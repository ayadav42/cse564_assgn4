package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class stores the list of cities and another one with these
 * cities in the order of shortest traversal. It also maintains a flag
 * to mark change in city data.
 *
 * @author amaryadav
 * @author kaichen
 * @version 1.0
 * @since 2021-10-08
 */
public class Blackboard {

    private static Blackboard _instance;

    public List<City> cityList;
    public List<List<City>> path;
    public boolean dataChanged;

    private Blackboard(){

        this.cityList = new ArrayList<>();
        this.path = new ArrayList<>();
        dataChanged = true;

    }

    /**
     * This method fetches the only instance of blackboard.
     *
     */
    public static Blackboard getInstance(){
        if(_instance == null) _instance = new Blackboard();
        return _instance;
    }

}
