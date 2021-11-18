package model.cityshapes;

import java.awt.*;

/**
 * This class represents a shape decorator for a city's shape.
 *
 * @author Amar Yadav (ID: 1219650510, ayadav42@asu.edu)
 * @version 1.0
 * @since 2021-11-15
 */
public abstract class ShapeDecorator extends ShapeComponent {

    protected ShapeComponent shape;

    /**
     * To set the inner shape of this decorator
     *
     * @param shapeComponent the innder shape
     */
    public void setInnerCityShape(ShapeComponent shapeComponent){
        this.shape = shapeComponent;
    }

    /**
     * Each child can be moved using this method,
     * moves inner shape first.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    @Override
    public void moveTo(int x, int y){
        if(shape != null) shape.moveTo(x, y);
    }

    /**
     * Each child should be able to draw itself,
     * draws inner shape first
     *
     * @param g The graphics passed
     */
    @Override
    public void draw(Graphics g) {
        if(shape != null) shape.draw(g);
    }


    /**
     * Each child can be asked if they contain a point
     * using this method, asks inner shape first.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    @Override
    public boolean containsPoint(int x, int y){
        return shape != null && shape.containsPoint(x, y);
    }

    /**
     * Helps convert the shape to a storage friendly format,
     * converts inner shape first.
     *
     * @param arr The array for setting color value at
     */
    @Override
    public void toStorageFormat(String[] arr) {
        if(shape != null) shape.toStorageFormat(arr);
    }
}
