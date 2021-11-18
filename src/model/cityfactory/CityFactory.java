package model.cityfactory;

import controller.Logger;
import model.cityshapes.*;
import model.City;

/**
 * This class is a concrete implementation of the city
 * abstract factory
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public class CityFactory extends CityAbstractFactory {

    /**
     * An implementation of the fatory method for creating cities.
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
    @Override
    public City createCity(String name, int x, int y, int size, ShapeComponent circleCityShape, ShapeDecorator squareCityShape, ShapeDecorator hollowPlusCityShape) {

        Logger.getInstance().log("Creating city " + name + ", at (" + x + "," + y + ")");

        ShapeComponent cityShape = null;

        if (circleCityShape != null) {
            cityShape = circleCityShape;
        }

        if (squareCityShape != null) {
            if (cityShape != null) {
                squareCityShape.setInnerCityShape(cityShape);
            }
            cityShape = squareCityShape;
        }

        if (hollowPlusCityShape != null) {
            if (cityShape != null) {
                hollowPlusCityShape.setInnerCityShape(cityShape);
            }
            cityShape = hollowPlusCityShape;
        }

        return new City(name, x, y, cityShape, size);
    }
}
