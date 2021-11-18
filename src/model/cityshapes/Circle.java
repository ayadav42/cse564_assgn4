package model.cityshapes;

import java.awt.*;

public class Circle extends ShapeComponent {

    final int diameter;
    int x;
    int y;

    public Circle(int x, int y, int size, Color color) {

        this.diameter = size - 8;
        moveTo(x, y);
        this.color = color;

    }

    @Override
    public void moveTo(int x, int y) {

        this.x = x + 4;
        this.y = y + 4;

    }

    @Override
    public boolean containsPoint(int x, int y) {

        int x_center = x + diameter / 2;
        int y_center = y + diameter / 2;

        double distanceFromCenter = Math.sqrt(
                (Math.pow((x - x_center), 2) + Math.pow((y - y_center), 2))
        );

        double radius = diameter / 2.0;
        System.out.print("checking for inside circle with bounds x=" + x + ", y=" + y + ", x_center=" + x_center + ", y_center=" + y_center + ", radius=" + radius + ", distanceFromCenter=" + distanceFromCenter);
        if (distanceFromCenter <= radius) {
            System.out.println("  :  yes\n");
        }

        return distanceFromCenter <= radius;
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(this.color);
        g.fillOval(x, y, diameter, diameter);

    }

    @Override
    public void toStorageFormat(String[] arr) {
        arr[0] = String.valueOf(this.color.getRGB());
    }

}
