/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.trackers;

import net.taunova.trackers.TrackerFrame;
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
    MouseTracker(TrackerFrame it) {
       this.frame = it;
       
    }
    
    public void setSelection(Rectangle r, boolean sT) {
        this.selection = r;
        this.selectionTraking = sT;
    }
    
    public void setParent(Component parent) {
        this.parent = parent;
    }
    
    public void processPath(TrackerCallback callback) {
        if(positionList.size() > 2) {
            Position current = positionList.get(0);
            for(int i=1; i< positionList.size(); i++) {
                Position next = positionList.get(i);
                callback.process(current, next);
                current = next;                
            }
        }        
    }

     
    @Override
    public void run() {
        current.position = MouseInfo.getPointerInfo().getLocation(); 
        positionList.add(current);
        
        while(true) {
            if(!this.frame.isActive()) {
                
                long time1 = System.currentTimeMillis();
                PointerInfo info = MouseInfo.getPointerInfo();            
                Point p = info.getLocation();
                if(this.selectionTraking == true) {
                    if(  selection.contains(p)) {
                        if(current.position.x != p.x ||
                           current.position.y != p.y) {
                            current = new Position(p);
                            positionList.add(current);                               
                        }else{
                            current.incDelay();
                            ThreadUtil.sleep(DELAY);
                        }                    
                    }
                } else {
                    if(current.position.x != p.x ||
                       current.position.y != p.y) {
                        current = new Position(p);
                        positionList.add(current);                               
                    } else {
                        current.incDelay();
                        ThreadUtil.sleep(DELAY);
                    }   
                }
                long time2 = System.currentTimeMillis();
    //            System.out.println("it took: " + (time2 - time1));

            }
        }
    }

    public List<Position> getPosition() {
        return this.positionList;
    }
}
