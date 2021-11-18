package model.cityshapes;

import java.awt.*;

public abstract class ShapeDecorator extends ShapeComponent {

    protected ShapeComponent shape;

    public void setInnerCityShape(ShapeComponent shapeComponent){
        this.shape = shapeComponent;
    }

    @Override
    public void moveTo(int x, int y){
        if(shape != null) shape.moveTo(x, y);
    }

    @Override
    public void draw(Graphics g) {
        if(shape != null) shape.draw(g);
    }

    @Override
    public boolean containsPoint(int x, int y){
        return shape != null && shape.containsPoint(x, y);
    }

    @Override
    public void toStorageFormat(String[] arr) {
        if(shape != null) shape.toStorageFormat(arr);
    }
}
