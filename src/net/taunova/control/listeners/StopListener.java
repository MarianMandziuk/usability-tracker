package net.taunova.control.listeners;

import net.taunova.control.ControlPanel;
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
    private ControlPanel controlPanel;

    public StopListener(ControlPanel cp) {
        this.tracker = cp.getMouseTracker();
        this.frame = cp.getTrackerFrame();
        this.controlPanel = cp;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        this.frame.trackerPanel.stopExcution();
        this.tracker.stopExcution();
        this.tracker.getPosition().clear();
        this.frame.trackerPanel.start = false;
        this.frame.trackerPanel.setSelectionNull();
        this.frame.startThread = false;
        this.frame.thread.interrupt();
        this.tracker.grid.clearGridTrack();

        this.controlPanel.startButton.setEnabled(true);
        this.controlPanel.stopButton.setEnabled(false);
        this.controlPanel.newSlideButton.setEnabled(false);
        this.controlPanel.takeSnapShotButton.setEnabled(false);
        this.controlPanel.markAreaButton.setEnabled(false);
        this.controlPanel.cleanButton.setEnabled(false);
    }
}
