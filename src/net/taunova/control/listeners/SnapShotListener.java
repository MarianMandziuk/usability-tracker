package net.taunova.control.listeners;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.taunova.control.ControlPanel;
import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;
import net.taunova.util.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import giffer.Giffer;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author maryan
 */
public class SnapShotListener implements ActionListener {
    protected TrackerFrame frame;
    protected MouseTracker tracker;
    protected ControlPanel cp;
    protected final Logger logger = LoggerFactory.getLogger(SnapShotListener.class);


    public SnapShotListener(ControlPanel cp) {
        this.cp = cp;
        this.frame = cp.getTrackerFrame();
        this.tracker = cp.getMouseTracker();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        tracker.setTrack(false);
        this.frame.stopTrackWhileDeactivated = true;
        this.frame.dropDeactivetedCount();
        BufferedImage image = frame.trackerPanel.getFullscreenImage();
        saveScreen(image);

    }


    protected void drawTrack(BufferedImage image) {
        List<Position> positionList = this.tracker.getPosition();
        Graphics2D g2 = image.createGraphics();
        for(int i = 0; i < positionList.size() - 1; i++) {
            g2.setColor(positionList.get(i).getColor());
            g2.drawLine((int)(positionList.get(i).position.x), 
                           (int)(positionList.get(i).position.y), 
                           (int)(positionList.get(i + 1).position.x), 
                           (int)(positionList.get(i + 1).position.y));
            if(positionList.get(i).getDelay() > 10) {
                int radius = positionList.get(i).getDelay();                    
                if(radius > 50) {
                    radius = 50;
                }

                g2.drawOval((int)(positionList.get(i).position.x)-radius/2, 
                       (int)(positionList.get(i).position.y)-radius/2, radius, radius);
                int x = (int)(positionList.get(i).position.x)-radius/2;
                int y = (int)(positionList.get(i).position.y)-radius/2;
                if(positionList.get(i).getNumber() != 0) {
                    g2.drawString(Integer.toString(positionList.get(i).getNumber()), x, y);
                }
            }
        }
        g2.dispose();
    }

    protected void saveScreen(BufferedImage image)  {

        int returnVal = cp.saveSnapshotWindow.showSaveDialog(this.frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            if(cp.saveSnapshotWindow.getFileFilter().getDescription().equals("image png")) {
                savePNG(image);
            } else if(cp.saveSnapshotWindow.getFileFilter().getDescription().equals("animation gif")) {
                saveGIF(image);
            } else {
                savePNG(image);
            }
            JOptionPane.showMessageDialog(this.frame,
                    "Image saved");
        }
    }

    protected void savePNG(BufferedImage image) {
        drawTrack(image);
        String format = "png";
        File file = cp.saveSnapshotWindow.getSelectedFile();
        if (!file.getName().endsWith("." + format)) {
            file = new File(cp.saveSnapshotWindow.getSelectedFile() + "." + format);
        }
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            logger.error("Error: " + e);
        }
    }

    protected void saveGIF(BufferedImage image) {
        List<BufferedImage> bufferedTracks = createBufferTrackImages();
        bufferedTracks.add(0, image);
        BufferedImage ar[] = new BufferedImage[bufferedTracks.size()];

        for(int i = 0; i < ar.length; i++) {
            ar[i] = bufferedTracks.get(i);
        }

        String format = "gif";
        File file = cp.saveSnapshotWindow.getSelectedFile();
        String fileName = cp.saveSnapshotWindow.getSelectedFile().getPath();
        if (!file.getName().endsWith("." + format)) {
            fileName += ("." + format);
        }

        try
        {
            Giffer.generateFromBI(ar, fileName, 40, true);
        } catch (Exception ex)
        {
            logger.error("Error: " + ex);
        }
    }

    protected List createBufferTrackImages() {
        List<BufferedImage> bufferedTracks = new ArrayList<>();
        List<Position> positionList = this.tracker.getPosition();
        final int frameRate = 30;
        int pointPerFrame = positionList.size() / frameRate;
        if (pointPerFrame == 0) {
            pointPerFrame = positionList.size() - 2;
        }
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = null;
        Graphics g = null;
        for(int i = 0; i < positionList.size() - 1; i++) {

            if (i % pointPerFrame == 0) {
                 bufferedImage = new BufferedImage(screenRect.width, screenRect.height,
                        BufferedImage.TYPE_INT_ARGB);
                g = bufferedImage.createGraphics();

            }
            g.setColor(positionList.get(i).getColor());
            g.drawLine((int)(positionList.get(i).position.x),
                    (int)(positionList.get(i).position.y),
                    (int)(positionList.get(i + 1).position.x),
                    (int)(positionList.get(i + 1).position.y));
            if(positionList.get(i).getDelay() > 10) {
                int radius = positionList.get(i).getDelay();
                if(radius > 50) {
                    radius = 50;
                }

                g.drawOval((int)(positionList.get(i).position.x)-radius/2,
                        (int)(positionList.get(i).position.y)-radius/2, radius, radius);
                int x = (int)(positionList.get(i).position.x)-radius/2;
                int y = (int)(positionList.get(i).position.y)-radius/2;
                if(positionList.get(i).getNumber() != 0) {
                    g.drawString(Integer.toString(positionList.get(i).getNumber()), x, y);
                }
            }

            if (i % pointPerFrame == 0 && i != 0) {
                bufferedTracks.add(bufferedImage);
            }

        }
        return bufferedTracks;
    }
}
