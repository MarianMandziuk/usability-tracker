package net.taunova.usability;


import javax.swing.JFrame;
import net.taunova.trackers.TrackerFrame;

/**
 *
 * @author renat
 */

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
