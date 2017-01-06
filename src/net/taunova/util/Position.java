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
    private int number = 0;
    private Color color;
    private float lineWidth;
    
    public static final int DELAY = 10;
    private static final float LOW_WIDTH = 1.0F;
    private static final float HIGH_WIDTH = 5.0F;
    private static final float STEP = 0.3F;
    private static final float ROUND_CONSTANT = 10f;

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

    public void increaseLineWidth() {
        if (lineWidth < Math.round(HIGH_WIDTH * ROUND_CONSTANT) / ROUND_CONSTANT) {
            lineWidth += STEP;
        }
    }

    public void decreaseLineWidth() {
        if (lineWidth > Math.round(LOW_WIDTH * ROUND_CONSTANT) / ROUND_CONSTANT) {
            lineWidth -= STEP;
        }
    }

    public void setWidth(float width) {
        if (width == 0) {
            lineWidth = HIGH_WIDTH;
        } else {
            lineWidth = width;
        }
    }

    public float getWidht() {
        return Math.round(lineWidth * ROUND_CONSTANT) / ROUND_CONSTANT;
    }
}
