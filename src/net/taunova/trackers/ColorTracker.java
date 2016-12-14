package net.taunova.trackers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maryan on 07.12.16.
 */
public class ColorTracker {
    private Color color;
    private boolean switchColor = true;
    private List<Color> colorList = new ArrayList<>(
            Arrays.asList(Color.RED, Color.BLUE, Color.GRAY,
                    Color.CYAN, Color.GREEN, Color.YELLOW,
                    Color.MAGENTA, Color.PINK, Color.ORANGE));
    private int i = 0;

    public void setColor(Color c) {
        this.color = c;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isSwitchColor() {
        return this.switchColor;
    }

    public void setSwitchColor(boolean b) {
        this.switchColor = b;
    }

    public void nextColor() {
        if (this.i >= this.colorList.size()) {
            this.i = 0;
        }
        this.color = colorList.get(i);
        this.i++;
    }
}
