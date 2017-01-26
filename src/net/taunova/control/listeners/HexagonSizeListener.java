package net.taunova.control.listeners;

import net.taunova.trackers.MouseTracker;
import net.taunova.util.Grid;
import net.taunova.util.Hexagon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by maryan on 26.01.17.
 */
public class HexagonSizeListener implements ChangeListener{
    private MouseTracker tracker;

    public HexagonSizeListener(MouseTracker tracker) {
        this.tracker = tracker;
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int radius = (int)source.getValue();
            this.tracker.grid = new Grid(Toolkit.getDefaultToolkit().getScreenSize().width,
                    Toolkit.getDefaultToolkit().getScreenSize().height, radius, 3);
        }
    }
}
