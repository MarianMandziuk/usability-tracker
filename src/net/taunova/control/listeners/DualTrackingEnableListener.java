package net.taunova.control.listeners;

import net.taunova.control.ControlPanel;
import net.taunova.trackers.TrackerFrame;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by maryan on 19.01.17.
 */
public class DualTrackingEnableListener implements ItemListener {
    private TrackerFrame frame;
    private ControlPanel controlPanel;

    public DualTrackingEnableListener(ControlPanel cp ) {
        this.frame = cp.getTrackerFrame();
        this.controlPanel = cp;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            this.frame.trackerPanel.dualTrackingEnable = true;
            this.controlPanel.hexagonSizeSlider.setEnabled(true);
        } else {
            this.frame.trackerPanel.dualTrackingEnable = false;
            this.controlPanel.hexagonSizeSlider.setEnabled(false);
        }
    }
}
