package model.cityshapes;

import java.awt.*;

public abstract class ShapeComponent {

    Color color;

    public abstract void moveTo(int x, int y);

    public abstract boolean containsPoint(int x, int y);

    public abstract void draw(Graphics g);

    public abstract void toStorageFormat(String[] arr);

}
