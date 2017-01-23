package net.taunova.usability;


import net.taunova.trackers.TrackerFrame;

import javax.swing.*;


public class UsabilityTracker {
    
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TrackerFrame frame = new TrackerFrame();
            }
        });        
    }
}
