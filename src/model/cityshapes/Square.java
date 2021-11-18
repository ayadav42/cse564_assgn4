package model.cityshapes;

import java.awt.*;

public class Square extends ShapeDecorator {

    int x;
    int y;
    Rectangle bounds;
    int size;

    public Square(int x, int y, int size, Color color) {

        this.size = size;
        this.color = color;
        this.bounds = new Rectangle(x, y, size, size);
        moveTo(x, y);

    }

    @Override
    public void moveTo(int x, int y) {

        super.moveTo(x, y);
        this.x = x;
        this.y = y;
        this.bounds.x = x;
        this.bounds.y = y;

    }

    @Override
    public boolean containsPoint(int x, int y) {
        System.out.println("checking inside single box if x=" + x + ", y=" + y + " is inside of bounds=" + this.bounds + ", ans : " + this.bounds.contains(x, y));
        return super.containsPoint(x, y) || this.bounds.contains(x, y);
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(this.color);
        g.fillRect(x, y, size, size);
        super.draw(g);

    }

    @Override
    public void toStorageFormat(String[] arr) {
        super.toStorageFormat(arr);
        arr[1] = String.valueOf(this.color.getRGB());
    }
}
