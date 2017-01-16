package net.taunova.control.listeners;

import net.taunova.trackers.TrackerFrame;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by maryan on 16.01.17.
 */
public class HeatMapEanbleListener implements ItemListener {
    private TrackerFrame frame;

    public HeatMapEanbleListener(TrackerFrame frame ) {
        this.frame = frame;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            this.frame.trackerPanel.heatmapEnable = true;
        } else {
            this.frame.trackerPanel.heatmapEnable = false;
        }
    }
}
