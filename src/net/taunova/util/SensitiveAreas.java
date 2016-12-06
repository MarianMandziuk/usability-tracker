package net.taunova.util;

import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Point;
/**
 * Created by maryan on 06.12.16.
 */
public class SensitiveAreas {
    List<Rectangle> areas = new ArrayList<>();
    private final Rectangle parentSelection;
    private int selectedAreaIndex;
    private Point selectedAreaPoint;

    private static final int SIZE_AREA = 9;
    private static final int CENTERED_AREA = SIZE_AREA / 2;


    public SensitiveAreas(Rectangle parentSelection) {
        this.parentSelection = parentSelection;
    }

    private List genereateAreas() {
        List<Rectangle> rects = new ArrayList<>();
        rects.add(new Rectangle(parentSelection.x - CENTERED_AREA,
                parentSelection.y - CENTERED_AREA,
                SIZE_AREA,
                SIZE_AREA));
        rects.add(new Rectangle((parentSelection.x + parentSelection.width / 2) - CENTERED_AREA,
                parentSelection.y - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));
        rects.add(new Rectangle((parentSelection.x + parentSelection.width) - CENTERED_AREA,
                parentSelection.y - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));
        rects.add(new Rectangle(parentSelection.x - CENTERED_AREA,
                (parentSelection.y + parentSelection.height / 2) - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));
        rects.add(new Rectangle(parentSelection.x - CENTERED_AREA,
                (parentSelection.y + parentSelection.height) - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));
        rects.add(new Rectangle((parentSelection.x + parentSelection.width) - CENTERED_AREA,
                (parentSelection.y + parentSelection.height / 2) - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));
        rects.add(new Rectangle((parentSelection.x + parentSelection.width / 2)- CENTERED_AREA,
                (parentSelection.y + parentSelection.height) - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));
        rects.add(new Rectangle((parentSelection.x + parentSelection.width)- CENTERED_AREA,
                (parentSelection.y + parentSelection.height) - CENTERED_AREA,
                SIZE_AREA, SIZE_AREA));

        return rects;
    }

    public void setUpAreas() {
        this.areas = this.genereateAreas();
    }

    public void drawSensitiveAreas(final Graphics g) {
        for (Rectangle area : this.areas) {
            g.drawRect(area.x,
                        area.y,
                        area.width,
                        area.height);
        }
    }

    public boolean checkSensitiveArea(Point p) {
        boolean selectedArea = false;
        if(this.areas != null) {
            int x = 0;
            int y = 0;
            for (int i = 0; i < this.areas.size(); i++) {
                if (this.areas.get(i).contains(p)) {
                    selectedArea = true;
                    this.setAreaIndex(i);
                    switch (i) {
                        case 0:
                            x = this.parentSelection.x + this.parentSelection.width;
                            y = this.parentSelection.y + this.parentSelection.height;
                            break;
                        case 2:
                        case 1:

                            x = this.parentSelection.x;
                            y = this.parentSelection.y + this.parentSelection.height;
                            break;
                        case 7:
                        case 6:
                        case 5:
                            x = this.parentSelection.x;
                            y = this.parentSelection.y;
                            break;
                        case 4:
                        case 3:
                            x = this.parentSelection.x + this.parentSelection.width;
                            y = this.parentSelection.y;
                            break;
                        default:
                            break;
                    }
                    if (selectedArea) {
                        this.setAreaPoint(x, y);
                        break;
                    }
                }
            }
        }
        return selectedArea;
    }

    public void changeParentSelection(Point p2) {
        int width;
        int height;
        switch(this.selectedAreaIndex) {
            case 0:
            case 2:
            case 4:
            case 7:
                    if (p2.x < selectedAreaPoint.x && p2.y  > selectedAreaPoint.y) {
                        width = selectedAreaPoint.x - p2.x;
                        height = p2.y - selectedAreaPoint.y;
                        this.parentSelection.setRect(p2.x, selectedAreaPoint.y, width, height);
                    } else if (p2.x < selectedAreaPoint.x && p2.y < selectedAreaPoint.y) {
                        width = selectedAreaPoint.x - p2.x;
                        height = selectedAreaPoint.y - p2.y;
                        this.parentSelection.setRect(p2.x, p2.y, width, height);
                    } else if (p2.x > selectedAreaPoint.x && p2.y < selectedAreaPoint.y) {
                        width = p2.x - selectedAreaPoint.x;
                        height = selectedAreaPoint.y - p2.y;
                        this.parentSelection.setRect(selectedAreaPoint.x, p2.y, width, height);
                    }  else {
                        width = p2.x - selectedAreaPoint.x;
                        height = p2.y - selectedAreaPoint.y;
                        this.parentSelection.setRect(selectedAreaPoint.x, selectedAreaPoint.y, width, height);
                    }
                break;
            case 1:
            case 6:
                    if (p2.y < selectedAreaPoint.y) {
                        height = selectedAreaPoint.y - p2.y;
                        this.parentSelection.setRect(selectedAreaPoint.x, p2.y, this.parentSelection.width, height);
                    }  else {
                        height = p2.y - selectedAreaPoint.y;
                        this.parentSelection.setRect(selectedAreaPoint.x, selectedAreaPoint.y, this.parentSelection.width, height);
                    }
                break;
            case 3:
            case 5:
                    if (p2.x < selectedAreaPoint.x ) {
                        width = selectedAreaPoint.x - p2.x;
                        this.parentSelection.setRect(p2.x, selectedAreaPoint.y, width, this.parentSelection.height);
                    } else  {
                        width = p2.x - selectedAreaPoint.x;
                        this.parentSelection.setRect(selectedAreaPoint.x, selectedAreaPoint.y, width, this.parentSelection.height);
                    }
                break;
        }
    }
    private void setAreaIndex(int index) {
        this.selectedAreaIndex = index;
    }

    private void setAreaPoint(int x, int y) {
        this.selectedAreaPoint = new Point(x, y);
    }


}
