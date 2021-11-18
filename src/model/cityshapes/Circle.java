package model.cityshapes;

import java.awt.*;

public class Circle extends ShapeComponent {

    final int diameter;
    int x_corner;
    int y_corner;

    public Circle(int x, int y, int size, Color color) {

        this.diameter = size - 8;
        moveTo(x, y);
        this.color = color;

    }

    @Override
    public void moveTo(int x, int y) {

        this.x_corner = x + 4;
        this.y_corner = y + 4;

    }

    @Override
    public boolean containsPoint(int x, int y) {

        double radius = diameter / 2.0;
        int x_center = x_corner + (int) radius;
        int y_center = y_corner + (int) radius;

        double distanceFromCenter = Math.sqrt(
                (Math.pow((x - x_center), 2) + Math.pow((y - y_center), 2))
        );

        System.out.print("checking for inside circle with bounds x=" + x + ", y=" + y + ", x_center=" + x_center + ", y_center=" + y_center + ", radius=" + radius + ", distanceFromCenter=" + distanceFromCenter);
        if (distanceFromCenter <= radius) {
            System.out.println("  :  yes\n");
        }else{
            System.out.println("  :  no\n");
        }

        return distanceFromCenter <= radius;
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(this.color);
        g.fillOval(x_corner, y_corner, diameter, diameter);

    }

    @Override
    public void toStorageFormat(String[] arr) {
        arr[0] = String.valueOf(this.color.getRGB());
    }

}
