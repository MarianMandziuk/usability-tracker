/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.control.listeners;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.taunova.control.ControlPanel;
import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;
import net.taunova.util.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author maryan
 */
public class SnapShotListener implements ActionListener {
    private TrackerFrame frame;
    private MouseTracker tracker;
    private ControlPanel cp;
    private final Logger logger = LoggerFactory.getLogger(SnapShotListener.class);
    public SnapShotListener(ControlPanel cp) {
        this.cp = cp;
        this.frame = cp.getTrackerFrame();
        this.tracker = cp.getMouseTracker();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.frame.setVisible(false);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            logger.error("Error: " + ex);
        }

        BufferedImage image = takeSnapShot();

        this.frame.setVisible(true);
        
        drawTrack(image);
        try {
            saveScreen(image);
        } catch (IOException ex) {
            logger.error("Error: " + ex);
        }
    }
    
    private BufferedImage takeSnapShot() {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage im = null;
        try {
            im = new Robot().createScreenCapture(screenRect);
        } catch (AWTException ex) {
            logger.error("Error: " + ex);
        }

        return im;
    }
    
    private void drawTrack(BufferedImage image) {
        List<Position> positionList = this.tracker.getPosition();
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.red);
        for(int i = 0; i < positionList.size() - 1; i++) {
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
            }
        }
        g2.dispose();
    }
    
    private void saveScreen(BufferedImage image) throws IOException {
        
        int returnVal = cp.fc.showSaveDialog(cp);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String format = "png";
            File file = cp.fc.getSelectedFile();
            if (!file.getName().endsWith(".png")) {
                file = new File(cp.fc.getSelectedFile() + ".png");
            }
            ImageIO.write(image, format, file);
            JOptionPane.showMessageDialog(cp,
            "Image saved");
        }
    }
}
