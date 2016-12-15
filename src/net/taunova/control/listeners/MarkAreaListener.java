package net.taunova.control.listeners;

import net.taunova.trackers.TrackerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by maryan on 15.12.16.
 */
public class MarkAreaListener implements ActionListener {
    private TrackerFrame frame;

    public MarkAreaListener(TrackerFrame frame) {
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.frame.trackerPanel.setSelectionNull();
    }
}
