package net.taunova.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Created by maryan on 10.01.17.
 */
public class Hexagon {

    private Point2D center;
    private double radius;
    private Color color = Color.BLUE;
    private Shape hexagon;

    public Hexagon(Point2D center, double radius) {
        this.center = center;
        this.radius = radius;
        this.hexagon = createHexagon();
    }

    private double getCoordinateX(int position) {
        int angle = 60 * position + 30;
        double angleRad = Math.PI / 180 * angle;
        return this.center.getX() + this.radius * Math.cos(angleRad);
    }

    private double getCoordinateY(int position) {
        int angle = 60 * position + 30;
        double angleRad = Math.PI / 180 * angle;
        return this.center.getY() + this.radius * Math.sin(angleRad);
    }

    private Shape createHexagon() {
        GeneralPath hexagon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 6);
        hexagon.moveTo(getCoordinateX(0), getCoordinateY(0));
        for (int i = 1; i < 6; i++) {
            hexagon.lineTo(getCoordinateX(i), getCoordinateY(i));
        }
        hexagon.closePath();
        return hexagon;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void drawHexagon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);
        g2.fill(this.hexagon);
    }
}
