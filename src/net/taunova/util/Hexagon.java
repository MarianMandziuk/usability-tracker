package net.taunova.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maryan on 10.01.17.
 */
public class Hexagon {

    private Point2D center;
    private double radius;
    private Color color = new Color(0,0,255, 30);
    public Shape hexagon;
    private List<Point2D> hexagonPoints = new ArrayList();

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

    private void calculatePoints() {
        for(int i = 0; i < 6; i++) {
            hexagonPoints.add(new Point2D.Double(getCoordinateX(i), getCoordinateY(i)));
        }
    }

    private Shape createHexagon() {
        this.calculatePoints();
        GeneralPath hexagon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 6);
        hexagon.moveTo(hexagonPoints.get(0).getX(), hexagonPoints.get(0).getY());
        for (int i = 1; i < hexagonPoints.size(); i++) {
            hexagon.lineTo(hexagonPoints.get(i).getX(), hexagonPoints.get(i).getY());
        }
        hexagon.closePath();
        return hexagon;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void drawHexagon(Graphics g, double scaleX, double scaleY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);
        GeneralPath hexagonDraw = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 6);
        hexagonDraw.moveTo(hexagonPoints.get(0).getX() * scaleX,
                hexagonPoints.get(0).getY() * scaleY);
        for (int i = 1; i < hexagonPoints.size(); i++) {
            hexagonDraw.lineTo(hexagonPoints.get(i).getX() * scaleX,
                    hexagonPoints.get(i).getY() * scaleY);
        }
        hexagonDraw.closePath();
        g2.fill(hexagonDraw);
    }
}
