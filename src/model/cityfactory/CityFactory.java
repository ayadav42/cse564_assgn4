package model.cityfactory;

import model.cityshapes.*;
import model.City;

public class CityFactory extends CityAbstractFactory{

    @Override
    public City createCity(String name, int x, int y, int size, ShapeComponent circleCityShape, ShapeDecorator squareCityShape, ShapeDecorator hollowPlusCityShape) {
        ShapeComponent cityShape = null;

        if(circleCityShape != null){
            cityShape = circleCityShape;
        }

        if(squareCityShape != null){
            if(cityShape != null){
                squareCityShape.setInnerCityShape(cityShape);
            }
            cityShape = squareCityShape;
        }

        if(hollowPlusCityShape != null){
            if(cityShape != null){
                hollowPlusCityShape.setInnerCityShape(cityShape);
            }
            cityShape = hollowPlusCityShape;
        }

        return new City(name, x, y, cityShape, size);
    }
}
