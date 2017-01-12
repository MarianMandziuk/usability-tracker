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
//    private double interspace;
    public List<Hexagon> hexagons = new ArrayList<>();

    public Grid(int width, int height, int rows) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        generate();
    }

    public void generate() {
        double heightHexagon = this.height / (double) this.rows;
        double interspace = heightHexagon / 20.0;
        int numberInRows = (int) (this.width / heightHexagon);
        double left = this.width - (numberInRows * heightHexagon);
        interspace += (left / (double)numberInRows);
        double diameter = (heightHexagon - interspace)/(3/4.0);
        double x = ((Math.sqrt(3) / 2.0 * diameter)+ interspace) / 2.0;
        double y = diameter / 2.0 + 0;
//        System.out.println("h: "+height + " w:"+width);
//        System.out.println("heightHexagon: "+heightHexagon);
//        System.out.println("number in rows: "+numberInRows);
//        System.out.println("diameter: "+diameter);
        for(int i = 0; i < this.rows; i++) {
            if (i % 2 != 0) {
                x = Math.sqrt(3) / 2.0 * diameter + interspace;
            }
            for(int j = 0; j < numberInRows - 1; j++) {
                hexagons.add(new Hexagon(new Point2D.Double(x, y), diameter / 2.0));
                x += Math.sqrt(3) / 2.0 * diameter + interspace;
            }
            y += (diameter * 3 / 4.0) + interspace;
            x = ((Math.sqrt(3) / 2.0 * diameter)+ interspace) / 2.0;
        }
    }

//    private List<Hexagon> generateForDraw(double width, double height) {
//        List<Hexagon> grid = new ArrayList<>();
//        double heightHexagon = height / (double) this.rows;
//        double interspace = heightHexagon / 20.0;
//        int numberInRows = (int) (width / (heightHexagon));
//        double left = width - (numberInRows * heightHexagon);
//        interspace += (left / (double)numberInRows);
//        double diameter = (heightHexagon  - interspace) /(3/4.0);
//        double x = ((Math.sqrt(3) / 2 * diameter)+ interspace) / 2;
//        double y = heightHexagon / 2 + 0;
//        System.out.println("intersace: "+interspace);
//        System.out.println("h: "+height + " w:"+width);
//        System.out.println("heightHexagon: "+heightHexagon);
//        System.out.println("number in rows: "+numberInRows);
//        System.out.println("diameter: "+diameter);
//        System.out.println("res: " + (diameter)*this.rows);
//        for(int i = 0; i < this.rows; i++) {
//            if (i % 2 != 0) {
//                x = Math.sqrt(3) / 2 * diameter + interspace;
//            }
//            for(int j = 0; j < numberInRows - 1; j++) {
//                grid.add(new Hexagon(new Point2D.Double(x, y), diameter / 2.0));
//                x += Math.sqrt(3) / 2 * diameter + interspace;
//            }
//            y += heightHexagon;
//            x = ((Math.sqrt(3) / 2 * diameter)+ interspace) / 2;
//        }
//        return grid;
//    }

    public void drawGrid(Graphics g, double scaleX, double scaleY) {
        for(Hexagon h : this.hexagons) {
            h.drawHexagon(g, scaleX, scaleY);
        }
    }
}
