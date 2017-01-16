package net.taunova.control;


import javax.swing.*;
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
    public JButton cleanButton;
    public JButton markAreaButton;
    public JButton newSlideButton;
    public JButton takeSnapShotButton;
    public JButton stopButton;
    public JFileChooser saveSnapshotWindow;
    public JCheckBox heatmapEnableBox;
    
    public ControlPanel(MouseTracker tracker, TrackerFrame frame) {
        super(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.tracker = tracker;
        this.frame = frame;
        saveSnapshotWindow = new JFileChooser();
        cleanButton = new JButton("Clear");
        markAreaButton = new JButton("Mark area");
        newSlideButton = new JButton("New slide");
        takeSnapShotButton = new JButton("Save snapshot");
        stopButton = new JButton("Stop");
        startButton = new JButton("Start");
        heatmapEnableBox = new JCheckBox("HeatMap");

        saveSnapshotWindow.addChoosableFileFilter(new FileNameExtensionFilter("image png","png"));
        saveSnapshotWindow.addChoosableFileFilter(new FileNameExtensionFilter("animation gif","gif"));

        add(startButton);
        add(stopButton);
        add(cleanButton);
        add(markAreaButton);
        add(newSlideButton);
        add(takeSnapShotButton);
        add(heatmapEnableBox);

        stopButton.setEnabled(false);
        saveSnapshotWindow.setEnabled(false);
        cleanButton.setEnabled(false);
        markAreaButton.setEnabled(false);
        newSlideButton.setEnabled(false);
        takeSnapShotButton.setEnabled(false);
        heatmapEnableBox.setEnabled(false);

        cleanButton.addActionListener(new CleanTrackerListener(this));
        markAreaButton.addActionListener(new MarkAreaListener(frame));
        newSlideButton.addActionListener(new SnapShotCleanListener(this));
        takeSnapShotButton.addActionListener(new SnapShotListener(this));
        startButton.addActionListener(new StartButtonListener(this));
        stopButton.addActionListener(new StopListener(this));
        heatmapEnableBox.addItemListener(new HeatMapEanbleListener(this.frame));
    }     
    
    public MouseTracker getMouseTracker() {
        return this.tracker;
    }
    
    public TrackerFrame getTrackerFrame() {
        return this.frame;
    }

}


