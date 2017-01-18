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
    private int rows;

    public List<Hexagon> hexagons = new ArrayList<>();

    public Grid(int width, int height, int rows) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        generate();
    }

    public void generate() {
        double increasedHeight = this.height + height * 0.25;
        double heightHexagon = increasedHeight / (double) this.rows;
//        double increasedWidth = this.width + this.width * 0.25;
//        int numberInRows = (int) (this.width / (Math.sqrt(3) / 2.0*heightHexagon));
        double interspace = 1;
//        double leftW = this.width - (numberInRows * (Math.sqrt(3) / 2.0*heightHexagon));
//        double leftH = this.height - this.rows * (heightHexagon) * 0.75;
//        double r;
//        double min;
//        if (leftH < leftW) {
//            r = this.rows;
//            min = leftH;
//        } else {
//            r = numberInRows;
//            min = leftW;
//        }
//        interspace += (min / r);

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
        g.dispose();
    }
}
