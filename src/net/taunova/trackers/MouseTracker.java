package net.taunova.trackers;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import net.taunova.util.Position;
import net.taunova.util.ThreadUtil;


/**
 *
 * @author maryan
 */
public class MouseTracker implements Runnable  {
    private final static int DELAY = 5;
    private final static int BUFFER_SIZE = 5000;
    private Position current = new Position();
    private List<Position> positionList = new ArrayList<>(100*1024);
    private Component parent;
    private JFrame frame;
    private Rectangle selection;
    private boolean selectionTraking;
    private ColorTracker colorTracker;
    public Thread thread;
    public boolean startTrack = false;

    MouseTracker(TrackerFrame it, ColorTracker t) {
        this.frame = it;
        this.colorTracker = t;
    }
    
    public void setSelection(Rectangle r, boolean sT) {
        this.selection = r;
        this.selectionTraking = sT;
    }
    
    public void setParent(Component parent) {
        this.parent = parent;
    }
    
    public void processPath(TrackerCallback callback) {

        int ovalCount = 0;
        if(positionList.size() > 2) {
            Position current = positionList.get(0);
            for(int i=1; i< positionList.size(); i++) {
                if(current.isDelay()) {
                    ovalCount++;
                    current.setNumber(ovalCount);
                }

                Position next = positionList.get(i);
                callback.process(current, next);
                current = next;                
            }
        }        
    }

     
    @Override
    public void run() {
        current.position = MouseInfo.getPointerInfo().getLocation();
        current.setColor(colorTracker.getColor());
        positionList.add(current);
        while (true) {
            if (!this.frame.isActive() && this.startTrack) {
                long time1 = System.currentTimeMillis();
                PointerInfo info = MouseInfo.getPointerInfo();
                Point p = info.getLocation();
                if (this.selectionTraking) {
                    if (selection.contains(p)) {
                        addPosition(p);
                        addPosition(p);
                    }
                } else {
                    addPosition(p);
                }
                long time2 = System.currentTimeMillis();
                //            System.out.println("it took: " + (time2 - time1));
            }
        }

    }

    private void addPosition(Point p) {
        if(current.position.x != p.x ||
                current.position.y != p.y) {
            current = new Position(p);
            current.setColor(colorTracker.getColor());
            positionList.add(current);
        } else {
            current.incDelay();
            ThreadUtil.sleep(DELAY);
        }
    }

    public List<Position> getPosition() {
        return this.positionList;
    }

    public void setTrack(boolean startTrack) {
        this.startTrack = startTrack;
    }

    public void stopExcution() {

        this.startTrack = false;
    }

    public void createThread(Thread t) {
        this.thread = t;
        this.thread.start();
    }
}
