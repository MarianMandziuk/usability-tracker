package net.taunova.control;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.taunova.control.listeners.*;
import net.taunova.trackers.MouseTracker;
import net.taunova.trackers.TrackerFrame;




/**
 *
 * @author maryan
 */
public class ControlPanel extends JPanel {
    private MouseTracker tracker;
    private TrackerFrame frame;
    public JButton startButton;
    public JFileChooser saveSnapshotWindow;
    
    public ControlPanel(MouseTracker tracker, TrackerFrame frame) {
        super(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.tracker = tracker;
        this.frame = frame;
        saveSnapshotWindow = new JFileChooser();
        JButton cleanButton = new JButton("Clear");
        JButton markAreaButton = new JButton("Mark area");
        JButton newSlideButton = new JButton("New slide");
        JButton takeSnapShotButton = new JButton("Save snapshot");
        JButton stopButton = new JButton("Stop");
        startButton = new JButton("Start");

        saveSnapshotWindow.addChoosableFileFilter(new FileNameExtensionFilter("image png","png"));
        saveSnapshotWindow.addChoosableFileFilter(new FileNameExtensionFilter("animation gif","gif"));

        add(startButton);
        add(stopButton);
        add(cleanButton);
        add(markAreaButton);
        add(newSlideButton);
        add(takeSnapShotButton);


        cleanButton.addActionListener(new CleanTrackerListener(tracker));
        markAreaButton.addActionListener(new MarkAreaListener(frame));
        newSlideButton.addActionListener(new SnapShotCleanListener(this));
        takeSnapShotButton.addActionListener(new SnapShotListener(this));
        startButton.addActionListener(new StartButtonListener(frame, tracker));
        stopButton.addActionListener(new StopListener(tracker, frame));
    }     
    
    public MouseTracker getMouseTracker() {
        return this.tracker;
    }
    
    public TrackerFrame getTrackerFrame() {
        return this.frame;
    }

}


