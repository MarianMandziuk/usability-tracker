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
//    private static float LINE_WIDTH = 1.602F;

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
            g2.setStroke(new BasicStroke(positionList.get(i).getWidht()));
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
        if (this.cp.heatmapEnableBox.isSelected()) {
            drawHeatMap(image);
        } else if (this.cp.dualTrackingEnableBox.isSelected()) {
            drawHeatMap(image);
//            drawTrack(image);
            drawTrackStraightLine(image);
        } else {
//            drawTrack(image);
            drawTrackStraightLine(image);
        }

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
        List<BufferedImage> bufferedTracks = bufferedImageStraightLine();
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

            if (stepCounter == pointPerFrame || i == 3) {
                bufferedImage = new BufferedImage(screenRect.width, screenRect.height,
                        BufferedImage.TYPE_INT_ARGB);
                g2 = bufferedImage.createGraphics();
                stepCounter = 0;

            }

            g2.setStroke(new BasicStroke(positionList.get(i).getWidht()));
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

    private void drawHeatMap(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        tracker.grid.drawGrid(g2, 1, 1);
        g2.dispose();
    }

    protected void drawTrackStraightLine(BufferedImage image) {
        List<Position> positionList = this.tracker.getPosition();
        Graphics2D g2 = image.createGraphics();
        Position current = null;
        Position next = null;
        boolean t = false;
        int ovalCount = 0;
        for(int i = 0; i < positionList.size(); i++) {
            if(current != null && positionList.get(i).isDelay()) {
                next = positionList.get(i);
                ovalCount++;
                next.setNumber(ovalCount);
                t = true;
            } else if(current == null && positionList.get(i).isDelay()) {
                current = positionList.get(i);
                ovalCount++;
                current.setNumber(ovalCount);
            }
            if(t) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(current.getColor());
                g2.drawLine(current.position.x,
                        current.position.y,
                        next.position.x,
                        next.position.y);


                int radius = current.getDelay() * 3;
                if (radius > 20 * 3) {
                    radius = 20 * 3;
                }

                g2.setColor(new Color(201, 250, 231, 90));
                g2.drawOval(current.position.x - radius / 2,
                        current.position.y - radius / 2, radius, radius);
                g2.setColor(current.getColor());
                g2.fillOval(current.position.x - radius / 2,
                        current.position.y - radius / 2, radius, radius);
                if (current.getNumber() != 0) {
                    g2.setColor(Color.BLACK);
                    FontMetrics metrics = g2.getFontMetrics(g2.getFont());
                    int x = (current.position.x
                            - metrics.stringWidth(Integer.toString(current.getNumber())) / 2);
                    int y = (current.position.y - (metrics.getHeight()) / 2) + metrics.getAscent();
                    g2.drawString(Integer.toString(current.getNumber()), x, y);
                }

                current = next;
                t = false;
            }
        }

        g2.dispose();
    }

    protected List bufferedImageStraightLine() {
        List<BufferedImage> bufferedTracks = new ArrayList<>();
        List<Position> positionList = this.tracker.getPosition();
        final int frameRate = 30;
        int pointPerFrame = positionList.size()  / frameRate;
        if (pointPerFrame == 0) {
            pointPerFrame = (positionList.size() - 2) ;
        }

        int stepCounter = 0;
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = null;
        Graphics2D g2 = null;

        Position current = null;
        Position next = null;
        boolean t = false;
        int ovalCount = 0;

        for(int i = 0; i < positionList.size() - 1; i++) {
            if(current != null && positionList.get(i).isDelay()) {
                next = positionList.get(i);
                ovalCount++;
                next.setNumber(ovalCount);
                t = true;
            } else if(current == null && positionList.get(i).isDelay()) {
                current = positionList.get(i);
                ovalCount++;
                current.setNumber(ovalCount);
            }

            if (stepCounter == pointPerFrame || i == 0) {
                bufferedImage = new BufferedImage(screenRect.width, screenRect.height,
                        BufferedImage.TYPE_INT_ARGB);
                g2 = bufferedImage.createGraphics();
                stepCounter = 0;
            }


            if(t) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(current.getColor().getRGB()));
                g2.drawLine(current.position.x,
                        current.position.y,
                        next.position.x,
                        next.position.y);


                int radius = current.getDelay() * 3;
                if (radius > 20 * 3) {
                    radius = 20 * 3;
                }

                g2.setColor(new Color(201, 250, 231 ));
                g2.drawOval(current.position.x - radius / 2,
                        current.position.y - radius / 2, radius, radius);
                g2.setColor(new Color(current.getColor().getRGB()));
                g2.fillOval(current.position.x - radius / 2,
                        current.position.y - radius / 2, radius, radius);
                if (current.getNumber() != 0) {
                    g2.setColor(Color.BLACK);
                    FontMetrics metrics = g2.getFontMetrics(g2.getFont());
                    int x = (current.position.x
                            - metrics.stringWidth(Integer.toString(current.getNumber())) / 2);
                    int y = (current.position.y - (metrics.getHeight()) / 2) + metrics.getAscent();
                    g2.drawString(Integer.toString(current.getNumber()), x, y);
                }

                current = next;
                t = false;


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
