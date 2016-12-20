package net.taunova.control.listeners;

import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Created by maryan on 14.12.16.
 */
public class StopListener implements ActionListener{
    private MouseTracker tracker;
    private TrackerFrame frame;

    public StopListener(MouseTracker tracker, TrackerFrame frame) {
        this.tracker = tracker;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        this.frame.trackerPanel.stopExcution();
        this.tracker.stopExcution();
        this.tracker.getPosition().clear();
    }
}
