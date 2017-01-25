package net.taunova.util;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maryan on 10.01.17.
 */
public class Grid {

    private int width;
    private int height;
    private int radius;
    private double interspace;

    public List<Hexagon> hexagons = new ArrayList<>();

    public Grid(int width, int height, int radius, double interspace) {
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.interspace = interspace;
        generate();
    }

    public void generate() {

        double heightHexagon = this.radius * 2;
        double diameter = (heightHexagon - interspace);
        double x = ((Math.sqrt(3) / 2.0 * diameter)+ interspace) / 2.0;
        double y = diameter / 2.0 + 0;
        int i = 0;

        while(y <= this.height) {
            if (i % 2 != 0) {
                x = Math.sqrt(3) / 2.0 * diameter + interspace;
            }
            while(x <= this.width) {
                hexagons.add(new Hexagon(new Point2D.Double(x, y), diameter / 2.0));
                x += Math.sqrt(3) / 2.0 * diameter + interspace;
            }
            y += (diameter * 3 / 4.0) + interspace;
            x = ((Math.sqrt(3) / 2.0 * diameter)+ interspace) / 2.0;
            i++;
        }

    }


    public void drawGrid(Graphics g, double scaleX, double scaleY) {
        for(Hexagon h : this.hexagons) {
            h.drawHexagon(g, scaleX, scaleY);
        }
//        g.dispose();
    }

    public void clearGridTrack() {
        for(Hexagon h : this.hexagons) {
            h.setColor(new Color(0,0,255, 30));
        }
    }
}
