package net.taunova.control.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.taunova.trackers.MouseTracker;

/**
 *
 * @author maryan
 */
public class CleanTrackerListener implements ActionListener {
    private MouseTracker tracker;
    
    
    public CleanTrackerListener(MouseTracker tracker) {
        this.tracker = tracker;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
       tracker.getPosition().clear();
    }
    
}