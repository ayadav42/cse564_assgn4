package model.cityfactory;

import model.cityshapes.*;
import model.City;

public abstract class CityAbstractFactory {

    public abstract City createCity(String name, int x, int y, int size, ShapeComponent circleCityShape, ShapeDecorator squareCityShape, ShapeDecorator hollowPlusCityShape);

}
