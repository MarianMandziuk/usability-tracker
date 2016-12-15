package net.taunova.trackers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;
import net.taunova.control.ControlPanel;
import java.awt.event.WindowAdapter;
import java.awt.Color;

/**
 *
 * @author maryan
 */
public class TrackerFrame extends JFrame implements WindowFocusListener {
    ControlPanel buttonPanel;
    public TrackerPanel trackerPanel;
    public MouseTracker tracker;
    private static final int DIVIDER = 2;
    public ColorTracker colorTracker = new ColorTracker();

    public TrackerFrame() {
        super("Tracker frame");
        getContentPane().setLayout(new BorderLayout());
        Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int baseWidth = dim.width/DIVIDER;
        int baseHeight = dim.height/DIVIDER;
        int buttonPanelWidth = 150;
        tracker = new MouseTracker(this, colorTracker);
        buttonPanel = new ControlPanel(tracker, this);
        trackerPanel = new TrackerPanel(tracker);
        buttonPanel.setPreferredSize(new Dimension(120,
                                       baseHeight));
        trackerPanel.setPreferredSize(new Dimension(baseWidth, baseHeight));

        getContentPane().add(BorderLayout.EAST, buttonPanel);
        getContentPane().add(BorderLayout.CENTER, trackerPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(baseWidth + buttonPanelWidth, baseHeight);
        setVisible(true); 
        addWindowFocusListener(this);

        this.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent arg0) {
                if (colorTracker.isSwitchColor()) {
                    colorTracker.nextColor();
                    colorTracker.setSwitchColor(false);
                } else {
                    colorTracker.nextColor();
                    colorTracker.setSwitchColor(true);
                }
            }

        });
    }


    @Override
    public void windowGainedFocus(WindowEvent we) {
       this.buttonPanel.button5.setText("Start");
    }

    @Override
    public void windowLostFocus(WindowEvent we) {
        this.buttonPanel.button5.setText("Pause");
    }

}

