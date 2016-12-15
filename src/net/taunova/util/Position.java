package net.taunova.util;

import java.awt.Point;
import java.awt.Color;

/**
 *
 * @author maryan
 */
public class Position {
    public Point position;
    public int delay; 
    public int number = 0;
    public Color color;
    
    public static final int DELAY = 10;

    public Position() {
        
    }
    
    public Position(Point position) {
        this.position = position;
    }
    
    public void incDelay() {
        delay++;
    }

    public int getDelay() {
        return delay;
    }
    
    public boolean isDelay() {
        return this.delay > DELAY;
    }
       
    public void setNumber(int n) {
        this.number = n;
    }
    
    public int getNumber() {
        return this.number;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public Color getColor() {
        return this.color;
    }
}
