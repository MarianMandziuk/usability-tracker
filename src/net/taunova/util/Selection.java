package net.taunova.util;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
/**
 * Created by maryan on 05.12.16.
 */
public class Selection {
    private Rectangle area;
    private boolean selected = false;
    private SensitiveAreas sensitiveAreas;


    public Selection(int x, int y, int width, int height) {
        this.area = new Rectangle(x, y, width, height);
        this.sensitiveAreas = new SensitiveAreas(this.area);

    }

    public void setSidesSelection(Point p1, Point p2) {
        int width;
        int height;
        if (p2.x < p1.x && p2.y  > p1.y) {
            width = p1.x - p2.x;
            height = p2.y - p1.y;
            this.area.setRect(p2.x, p1.y, width, height);
        } else if (p2.x < p1.x && p2.y < p1.y) {
            width = p1.x - p2.x;
            height = p1.y - p2.y;
            this.area.setRect(p2.x, p2.y, width, height);
        } else if (p2.x > p1.x && p2.y < p1.y) {
            width = p2.x - p1.x;
            height = p1.y - p2.y;
            this.area.setRect(p1.x, p2.y, width, height);
        }  else {
            width = p2.x - p1.x;
            height = p2.y - p1.y;
            this.area.setRect(p1.x, p1.y, width, height);
        }
    }

    public void setSelectedSelection(Point p1, Point p2, int windowWidth, int windowHeight) {
        if(this.area.x +p2.x - p1.x + this.area.width > windowWidth) {
            this.area.x = (windowWidth - this.area.width) - 1;
        } else if (this.area.x + p2.x - p1.x < 0) {
            this.area.x = 0;
        } else if (this.area.y + p2.y - p1.y + this.area.height > windowHeight) {
            this.area.y = (windowHeight - this.area.height) - 1;
        } else if (this.area.y + p2.y - p1.y < 0) {
            this.area.y = 0;
        } else {
            this.area.x += p2.x - p1.x;
            this.area.y += p2.y - p1.y;
            p1.x = p2.x;
            p1.y = p2.y;
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void drawSelection(final Graphics g) {
        g.setColor(Color.red);
        g.drawRect(this.area.x, this.area.y, this.area.width, this.area.height);
        this.sensitiveAreas.setUpAreas();
        this.sensitiveAreas.drawSensitiveAreas(g);
    }

    public void checkOnSelected(Point p) {
        this.selected = this.area.contains(p);
    }

    public void resizeSelection(Point p2) {
        this.sensitiveAreas.changeParentSelection(p2);
    }

    public boolean isSelectedSensitiveArea(Point p) {
        return this.sensitiveAreas.checkSensitiveArea(p);
    }

    public int getX() {
        return this.area.x;
    }

    public int getY() {
        return this.area.y;
    }

    public int getWidth() {
        return this.area.width;
    }

    public int getHeight() {
        return this.area.height;
    }

    public void setDoubleToIntRectangle(final double x,
                                        final double y,
                                        final double w,
                                        final double h) {

        this.area.x = SelectionUtil.isRemainderHigher(x) ? (int)x + 1 : (int)x;
        this.area.y = SelectionUtil.isRemainderHigher(y) ? (int)y + 1 : (int)y;
        this.area.width = SelectionUtil.isRemainderHigher(w) ? (int)w + 1 : (int)w;
        this.area.height = SelectionUtil.isRemainderHigher(h) ? (int)h + 1 : (int)h;
    }
}
