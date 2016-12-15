package net.taunova.control.listeners;

import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author maryan
 */
public class StartButtonListener implements ActionListener {
    private TrackerFrame frame;
    private MouseTracker tracker;
    public StartButtonListener(TrackerFrame frame, MouseTracker tracker) {
        this.frame = frame;
        this.tracker = tracker;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        Object source = ae.getSource();
        if (source instanceof JButton) {
           
            if (frame.isActive() && ((JButton) source).getText() == "Start" &&
                    !this.frame.tracker.startTrack) {
                this.frame.setState(JFrame.ICONIFIED);
//                this.frame.setFocusable(false);
//                ((JButton) source).setText("Pause");
                this.frame.tracker.setTrack(true);
                this.tracker.createThread(new Thread(this.frame.tracker));
            } else if(frame.isActive() && ((JButton) source).getText() == "Start")  {
                this.frame.setState(JFrame.ICONIFIED);
            }
        }
    }
}