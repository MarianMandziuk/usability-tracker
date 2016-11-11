/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.control.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.taunova.trackers.MouseTracker;

/**
 *
 * @author maryan
 */
public class CleanTrackerListener implements ActionListener {
    private MouseTracker tracker;
    
    
    public CleanTrackerListener(MouseTracker tracker) {
        this.tracker = tracker;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
       tracker.getPosition().clear();
    }
    
}