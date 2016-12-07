/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.control.listeners;

import net.taunova.trackers.TrackerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author maryan
 */
public class StartButtonListener implements ActionListener {
    private TrackerFrame frame;
    public StartButtonListener(TrackerFrame frame) {
        this.frame = frame;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        new Thread(this.frame.tracker).start();
        Object source = ae.getSource();
        if (source instanceof JButton) {
           
            if (frame.isActive() && ((JButton) source).getText() == "Start") {
                this.frame.setState(JFrame.ICONIFIED);
//                this.frame.setFocusable(false);
                ((JButton) source).setText("Pause");
            }
        }
    }
}