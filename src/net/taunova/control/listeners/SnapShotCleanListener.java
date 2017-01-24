package net.taunova.control.listeners;

import net.taunova.control.ControlPanel;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Created by maryan on 15.12.16.
 */
public class SnapShotCleanListener extends SnapShotListener {

    public SnapShotCleanListener(ControlPanel cp) {
        super(cp);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        tracker.setTrack(false);
        this.frame.stopTrackWhileDeactivated = true;
        this.frame.dropDeactivetedCount();
        BufferedImage image = frame.trackerPanel.getFullscreenImage();
        saveScreen(image);
        tracker.getPosition().clear();
        tracker.grid.clearGridTrack();
        cp.getTrackerFrame().trackerPanel.setCounterI(this.tracker.getPosition().size());
    }
}
