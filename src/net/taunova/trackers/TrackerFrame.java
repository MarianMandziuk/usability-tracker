/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.trackers;

import net.taunova.trackers.TrackerPanel;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;
import net.taunova.control.ControlPanel;



/**
 *
 * @author maryan
 */
public class TrackerFrame extends JFrame implements WindowFocusListener {
    ControlPanel buttonPanel;
    TrackerPanel trackerPanel;
    public MouseTracker tracker;
    public boolean startTracking = false;
    
    public TrackerFrame() {
        super("Tracker frame");
        getContentPane().setLayout(new BorderLayout());
        
        tracker = new MouseTracker(this);
        buttonPanel = new ControlPanel(tracker, this);
        trackerPanel = new TrackerPanel(tracker);
        
        
        
        getContentPane().add(BorderLayout.EAST, buttonPanel);
        getContentPane().add(BorderLayout.CENTER, trackerPanel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true); 
        addWindowFocusListener(this);
    }


    @Override
    public void windowGainedFocus(WindowEvent we) {
       this.buttonPanel.button5.setText("Start");
       this.startTracking = true;
    }

    @Override
    public void windowLostFocus(WindowEvent we) {
        this.buttonPanel.button5.setText("Pause");
        this.startTracking = false;
    }
}
