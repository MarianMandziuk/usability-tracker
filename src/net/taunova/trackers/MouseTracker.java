package net.taunova.trackers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import net.taunova.util.Grid;
import net.taunova.util.Hexagon;
import net.taunova.util.Position;
import net.taunova.util.ThreadUtil;

import static net.taunova.util.LengthUtil.getCubicCurveLength;

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
    public List<Double> points = new ArrayList<>(100*1024);
    public boolean frameActive;
    private Grid grid;

    MouseTracker(TrackerFrame it, ColorTracker t, Grid grid) {
        this.frame = it;
        this.colorTracker = t;
        this.grid = grid;
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
        float length;
        float privLen = 0;

        if(positionList.size() > 4) {
            Position start = positionList.get(0);
            start.setWidth(0);
            for(int i=3; i< positionList.size(); i+=3) {
                Position end = positionList.get(i);
                Position control1 = positionList.get(i - 2);
                Position control2 = positionList.get(i - 1);

                length = getCubicCurveLength(start.position,
                        control1.position,
                        control2.position,
                        end.position);
                end.setWidth(start.getWidht());
                if (length > privLen) {
                    end.decreaseLineWidth();
                } else if (length < privLen){
                    end.increaseLineWidth();
                }
//                if (start.isDelay() || control1.isDelay() || control2.isDelay()) {
//                    end.increaseLineWidth();
//                }

                ovalCount = ovalCountIncrement(start, ovalCount);
                ovalCount = ovalCountIncrement(control1, ovalCount);
                ovalCount = ovalCountIncrement(control2, ovalCount);
//                ovalCount = ovalCountIncrement(end, ovalCount);

                callback.process(start, control1, control2, end);
                start = end;
                privLen = length;
            }
        }        
    }

     
    @Override
    public void run() {
        current.position = MouseInfo.getPointerInfo().getLocation();
        current.setColor(colorTracker.getColor());
        positionList.add(current);
        while (true) {
            if(Thread.currentThread().isInterrupted()) {
                break;
            }

            if (!this.frame.isActive() && this.startTrack) {
                long time1 = System.currentTimeMillis();
                PointerInfo info = MouseInfo.getPointerInfo();
                Point p = info.getLocation();
                if (this.selectionTraking) {
                    if (selection.contains(p)) {
                        addPosition(p);
                    }
                } else {
                    addPosition(p);
                }
                long time2 = System.currentTimeMillis();
//                            System.out.println("it took: " + (time2 - time1));
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

    private int ovalCountIncrement(Position p, int ovalCount) {
        if(p.isDelay()) {
            ovalCount++;
            p.setNumber(ovalCount);
        }
        return ovalCount;
    }

    public void trackHeatMap() {
        for(int i = 0; i < this.positionList.size(); i++) {
            for (Hexagon h : grid.hexagons) {
                if (h.hexagon.contains(positionList.get(i).position.x, positionList.get(i).position.y)) {
                    if (positionList.get(i).isDelay()) {
                        h.setColor(new Color(255, 0, 0, 40));
                    }
                }
            }
        }
    }
}
