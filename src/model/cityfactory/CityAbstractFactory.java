package model.cityfactory;

import model.cityshapes.*;
import model.City;

/**
 * This class serves as the abstract factory for creating cities.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public abstract class CityAbstractFactory {

    /**
     * The factory method for creating a city with any complex shape
     *
     * @param name city name
     * @param x x coordinate
     * @param y y coordinate
     * @param size city size
     * @param circleCityShape circular shape component
     * @param squareCityShape square shape component
     * @param hollowPlusCityShape hollow plus shape component
     * @return City The new city
     */
    public abstract City createCity(String name, int x, int y, int size, ShapeComponent circleCityShape, ShapeDecorator squareCityShape, ShapeDecorator hollowPlusCityShape);

}
