package net.taunova.control.listeners;

import net.taunova.control.ControlPanel;
import net.taunova.trackers.TrackerFrame;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by maryan on 16.01.17.
 */
public class HeatMapEanbleListener implements ItemListener {
    private TrackerFrame frame;
    private ControlPanel controlPanel;

    public HeatMapEanbleListener(ControlPanel cp ) {
        this.frame = cp.getTrackerFrame();
        this.controlPanel = cp;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            this.frame.trackerPanel.heatmapEnable = true;
            this.controlPanel.hexagonSizeSlider.setEnabled(true);
        } else {
            this.frame.trackerPanel.heatmapEnable = false;
            this.controlPanel.hexagonSizeSlider.setEnabled(false);
        }
    }
}
