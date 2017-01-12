package net.taunova.trackers;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import net.taunova.control.ControlPanel;
import net.taunova.util.Grid;

import java.awt.event.WindowAdapter;

/**
 *
 * @author maryan
 */
public class TrackerFrame extends JFrame {
    ControlPanel buttonPanel;
    public TrackerPanel trackerPanel;
    public MouseTracker tracker;

    private static final int DIVIDER = 2;

    public ColorTracker colorTracker = new ColorTracker();
    public boolean startThread = false;
    public boolean stopTrackWhileDeactivated = false;
    public Thread thread;
    private int deactivatedCount = 0;

    public TrackerFrame() {
        super("Tracker frame");
        getContentPane().setLayout(new BorderLayout());
        Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int baseWidth = dim.width/DIVIDER;
        int baseHeight = dim.height/DIVIDER;
        int buttonPanelWidth = 150;
        Grid grid = new Grid(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height, 20);
        tracker = new MouseTracker(this, colorTracker, grid);
        buttonPanel = new ControlPanel(tracker, this);
        trackerPanel = new TrackerPanel(tracker, grid);
        buttonPanel.setPreferredSize(new Dimension(120,
                                       baseHeight));
        trackerPanel.setPreferredSize(new Dimension(baseWidth, baseHeight));
        System.out.println(dim);
        getContentPane().add(BorderLayout.EAST, buttonPanel);
        getContentPane().add(BorderLayout.CENTER, trackerPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(baseWidth + buttonPanelWidth, baseHeight);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent arg0) {
                clearButtonActivate();
                buttonPanel.startButton.setText("Start");
                if(startThread) {
                    if (colorTracker.isSwitchColor()) {
                        colorTracker.nextColor();
                        colorTracker.setSwitchColor(false);
                    } else {
                        colorTracker.nextColor();
                        colorTracker.setSwitchColor(true);
                    }
                }
                tracker.frameActive = true;
            }

            public void windowDeactivated(WindowEvent e) {
                clearButtonActivate();
                tracker.frameActive = false;
                buttonPanel.startButton.setText("Pause");
                int deactivatedTimes = 3;
                if(stopTrackWhileDeactivated) {
                    deactivatedCount++;
                        if (deactivatedCount == deactivatedTimes) {
                        tracker.setTrack(true);
                        stopTrackWhileDeactivated = false;
                    }

                }

                if(startThread && !thread.isAlive()) {
                    tracker.setTrack(true);
                    thread.start();
                }
                else {
                    startThread = true;
                }
            }

        });
    }

    public void dropDeactivetedCount() {
        this.deactivatedCount = 0;
    }

    private void clearButtonActivate() {
        if (tracker.getPosition().isEmpty()) {
            buttonPanel.cleanButton.setEnabled(false);
        } else {
            buttonPanel.cleanButton.setEnabled(true);
        }
    }
}

