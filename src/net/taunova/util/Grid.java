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
    private List<Hexagon> grid = new ArrayList<>();

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
        double diameter = (heightHexagon / (3/4.0)) - interspace;
        double x = ((Math.sqrt(3) / 2 * diameter)+ interspace) / 2;
        double y = diameter / 2 + interspace;
        for(int i = 0; i < this.rows; i++) {
            if (i % 2 != 0) {
                x = Math.sqrt(3) / 2 * diameter + interspace;
            }
            for(int j = 0; j < numberInRows - 1; j++) {
                grid.add(new Hexagon(new Point2D.Double(x, y), diameter / 2.0));
                x += Math.sqrt(3) / 2 * diameter + interspace;
            }
            y += (diameter * 3 / 4) + interspace;
            x = ((Math.sqrt(3) / 2 * diameter)+ interspace) / 2;
        }
    }

    private List<Hexagon> generateForDraw(double height, double width) {
        List<Hexagon> grid = new ArrayList<>();
        double heightHexagon = height / (double) this.rows;
        double interspace = heightHexagon / 20.0;
        int numberInRows = (int) (width / heightHexagon);
        double left = width - (numberInRows * heightHexagon);
        interspace += (left / (double)numberInRows);
        double diameter = (heightHexagon / (3/4.0)) - interspace;
        double x = ((Math.sqrt(3) / 2 * diameter)+ interspace) / 2;
        double y = diameter / 2 + 0;
        for(int i = 0; i < this.rows; i++) {
            if (i % 2 != 0) {
                x = Math.sqrt(3) / 2 * diameter + interspace;
            }
            for(int j = 0; j < numberInRows - 1; j++) {
                grid.add(new Hexagon(new Point2D.Double(x, y), diameter / 2.0));
                x += Math.sqrt(3) / 2 * diameter + interspace;
            }
            y += (diameter * 3 / 4) + interspace;
            x = ((Math.sqrt(3) / 2 * diameter)+ interspace) / 2;
        }
        return grid;
    }

    public void drawGrid(Graphics g, double width, double height) {
        for(Hexagon h : generateForDraw(width, height)) {
            h.drawHexagon(g);
        }
    }
}
