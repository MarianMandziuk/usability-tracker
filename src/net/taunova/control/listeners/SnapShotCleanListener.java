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
        this.frame.setVisible(false);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            logger.error("Error: " + ex);
        }

        BufferedImage image = takeSnapShot();
        this.frame.setVisible(true);

        saveScreen(image);
        tracker.setTrack(true);

        tracker.getPosition().clear();
    }
}
