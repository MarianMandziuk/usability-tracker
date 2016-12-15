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
    public JButton button5;
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
        JButton button6 = new JButton("Stop");
        button5 = new JButton("Start");
        fc.addChoosableFileFilter(new FileNameExtensionFilter("image png","png"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("animation gif","gif"));
        add(button5);
        add(button1);
        add(button2);
        add(button3);
        add(button4);
        add(button6);
        button1.addActionListener(new CleanTrackerListener(tracker));
        button2.addActionListener(new MarkAreaListener(frame));
        button3.addActionListener(new SnapShotCleanListener(this));
        button4.addActionListener(new SnapShotListener(this));
        button5.addActionListener(new StartButtonListener(frame, tracker));
        button6.addActionListener(new StopListener(tracker, frame));
    }     
    
    public MouseTracker getMouseTracker() {
        return this.tracker;
    }
    
    public TrackerFrame getTrackerFrame() {
        return this.frame;
    }

}


