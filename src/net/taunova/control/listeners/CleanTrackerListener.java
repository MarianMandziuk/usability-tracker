package net.taunova.control.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.taunova.control.ControlPanel;
import net.taunova.trackers.MouseTracker;

/**
 *
 * @author maryan
 */
public class CleanTrackerListener implements ActionListener {
    private MouseTracker tracker;
    private ControlPanel controlPanel;
    
    public CleanTrackerListener(ControlPanel cp) {
        this.tracker = cp.getMouseTracker();
        this.controlPanel = cp;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
       tracker.getPosition().clear();
       controlPanel.cleanButton.setEnabled(false);
       tracker.grid.clearGridTrack();
       controlPanel.getTrackerFrame().trackerPanel.setCounterI(this.tracker.getPosition().size());
    }
    
}