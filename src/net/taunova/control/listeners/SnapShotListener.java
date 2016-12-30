package net.taunova.control.listeners;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private static float LINE_WIDTH = 1.602F;

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
        for(int i = 3; i < positionList.size() - 1; i+=3) {
            g2.setStroke(new BasicStroke(LINE_WIDTH));
            g2.setColor(positionList.get(i).getColor());
            CubicCurve2D c = new CubicCurve2D.Double();
            c.setCurve(positionList.get(i-3).position.x,
                    positionList.get(i-3).position.y,
                    positionList.get(i-2).position.x,
                    positionList.get(i-2).position.y,
                    positionList.get(i-1).position.x,
                    positionList.get(i-1).position.y,
                    positionList.get(i).position.x,
                    positionList.get(i).position.y);

            g2.draw(c);

            Position[] arr = {positionList.get(i-3),
                    positionList.get(i-2),
                    positionList.get(i-1)};
            for(int j = 0; j < arr.length; j++) {
                if (arr[j].getDelay() > 10) {
                    int radius = arr[j].getDelay();
                    if (radius > 50) {
                        radius = 50;
                    }

                    g2.drawOval((int) (arr[j].position.x) - radius / 2,
                            (int) ( arr[j].position.y) - radius / 2, radius, radius);
                    int x = (int) (arr[j].position.x) - radius / 2;
                    int y = (int) (arr[j].position.y) - radius / 2;
                    if (arr[j].getNumber() != 0) {
                        g2.drawString(Integer.toString(arr[j].getNumber()), x, y);
                    }
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
            }
            else {
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
        int pointPerFrame = positionList.size() / 3 / frameRate;
        if (pointPerFrame == 0) {
            pointPerFrame = (positionList.size() - 2) / 3;
        }

        int stepCounter = 0;
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = null;
        Graphics2D g2 = null;
        for(int i = 3; i < positionList.size() - 1; i+=3) {

            if (stepCounter == pointPerFrame || i==3) {
                bufferedImage = new BufferedImage(screenRect.width, screenRect.height,
                        BufferedImage.TYPE_INT_ARGB);
                g2 = bufferedImage.createGraphics();
                stepCounter = 0;

            }

            g2.setStroke(new BasicStroke(LINE_WIDTH));
            g2.setColor(new Color(positionList.get(i).getColor().getRGB()));
            CubicCurve2D c = new CubicCurve2D.Double();
            c.setCurve(positionList.get(i-3).position.x,
                    positionList.get(i-3).position.y,
                    positionList.get(i-2).position.x,
                    positionList.get(i-2).position.y,
                    positionList.get(i-1).position.x,
                    positionList.get(i-1).position.y,
                    positionList.get(i).position.x,
                    positionList.get(i).position.y);

            g2.draw(c);

            Position[] arr = {positionList.get(i-3),
                    positionList.get(i-2),
                    positionList.get(i-1)};

            for(int j = 0; j < arr.length; j++) {
                if (arr[j].getDelay() > 10) {
                    int radius = arr[j].getDelay();
                    if (radius > 50) {
                        radius = 50;
                    }

                    g2.drawOval((int) (arr[j].position.x) - radius / 2,
                            (int) ( arr[j].position.y) - radius / 2, radius, radius);
                    int x = (int) (arr[j].position.x) - radius / 2;
                    int y = (int) (arr[j].position.y) - radius / 2;
                    if (arr[j].getNumber() != 0) {
                        g2.drawString(Integer.toString(arr[j].getNumber()), x, y);
                    }
                }
            }

            stepCounter++;
            if (stepCounter == pointPerFrame) {
                bufferedTracks.add(bufferedImage);
            }

        }
        g2.dispose();
        return bufferedTracks;
    }
}
