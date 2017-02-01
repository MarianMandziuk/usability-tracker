package net.taunova.control.listeners;

import net.taunova.control.ControlPanel;
import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;

/**
 *
 * @author maryan
 */
public class StartButtonListener implements ActionListener {
    private TrackerFrame frame;
    private MouseTracker tracker;
    private ControlPanel controlPanel;
    private final Logger logger = LoggerFactory.getLogger(SnapShotListener.class);
    public StartButtonListener(ControlPanel cp) {
        this.frame = cp.getTrackerFrame();
        this.tracker = cp.getMouseTracker();
        this.controlPanel = cp;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        Object source = ae.getSource();
        if (source instanceof JButton) {
           
            if (frame.isActive() && ((JButton) source).getText() == "Start" &&
                    !this.frame.tracker.startTrack) {

                this.frame.setVisible(false);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    logger.error("Error: " + ex);
                    Thread.currentThread().interrupt();
                }
                this.frame.trackerPanel.takeSnapShot();

                this.frame.setVisible(true);

                this.frame.trackerPanel.startTimer();
                this.frame.trackerPanel.start = true;
                this.frame.start = true;
                this.frame.thread = new Thread(this.frame.tracker);

                this.frame.trackerPanel.setCounterI(this.tracker.getPosition().size());


                this.controlPanel.startButton.setEnabled(false);
                this.controlPanel.stopButton.setEnabled(true);
                this.controlPanel.markAreaButton.setEnabled(true);
                this.controlPanel.newSlideButton.setEnabled(true);
                this.controlPanel.takeSnapShotButton.setEnabled(true);
                this.controlPanel.heatmapEnableBox.setEnabled(true);
                this.controlPanel.dualTrackingEnableBox.setEnabled(true);
            }
        }
    }
}