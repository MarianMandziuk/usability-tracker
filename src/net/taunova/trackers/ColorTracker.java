package net.taunova.trackers;

import java.awt.Color;
/**
 * Created by maryan on 07.12.16.
 */
public class ColorTracker {
    private Color color;
    private boolean switchColor = true;

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
}
