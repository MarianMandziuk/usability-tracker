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
    private static final int TRANSPARENCY = 90;
    private List<Color> colorList = new ArrayList<>(
            Arrays.asList(new Color(255,0,0, TRANSPARENCY),
                    new Color(0,0,255, TRANSPARENCY),
                    new Color(128, 128, 128, TRANSPARENCY),
                    new Color(0, 255, 255, TRANSPARENCY),
                    new Color(0, 255, 0, TRANSPARENCY),
                    new Color(255, 255, 0, TRANSPARENCY),
                    new Color(255, 0, 255, TRANSPARENCY),
                    new Color(255, 175, 175, TRANSPARENCY),
                    new Color(255, 200, 0, TRANSPARENCY)));
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
