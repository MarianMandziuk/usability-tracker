/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.control;

import net.taunova.util.Position;
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.taunova.control.listeners.CleanTrackerListener;
import net.taunova.control.listeners.SnapShotListener;
import net.taunova.control.listeners.StartButtonListener;
import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 *
 * @author maryan
 */
public class ControlPanel extends JPanel {
    private MouseTracker tracker;
    private TrackerFrame frame;
    public JButton button5;
    public boolean start = false;
    public JFileChooser fc;
    
    public ControlPanel(MouseTracker tracker, TrackerFrame frame) {
        super(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.tracker = tracker;
        this.frame = frame;
        fc = new JFileChooser();
        JButton button1 = new JButton("Clear");
        JButton button2 = new JButton("Mark area");
        JButton button3 = new JButton("New slide");
        JButton button4 = new JButton("Take snapshot");
        button5 = new JButton("Start");
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
        add(button5);
        add(button1);
        add(button2);
        add(button3);
        add(button4);
        
        button1.addActionListener(new CleanTrackerListener(tracker));
        button4.addActionListener(new SnapShotListener(this));
        button5.addActionListener(new StartButtonListener(frame));
    }     
    
    public MouseTracker getMouseTracker() {
        return this.tracker;
    }
    
    public TrackerFrame getTrackerFrame() {
        return this.frame;
    }

}


