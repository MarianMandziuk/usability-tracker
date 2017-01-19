package net.taunova.control.listeners;

import net.taunova.trackers.TrackerFrame;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by maryan on 19.01.17.
 */
public class DualTrackingEnableListener implements ItemListener {
    private TrackerFrame frame;

    public DualTrackingEnableListener(TrackerFrame frame ) {
        this.frame = frame;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            this.frame.trackerPanel.dualTrackingEnable = true;
        } else {
            this.frame.trackerPanel.dualTrackingEnable = false;
        }
    }
}
