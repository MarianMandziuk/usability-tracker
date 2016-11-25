/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maryan
 */
public class Selection   {
    
    public Rectangle selection;
    private List<Rect> listRects = new ArrayList<>();
    
    
    public Selection() {
        

    }
    
    public void setPoint(Point p) {
        this.selection = new Rectangle(p);
    }
    
    public void generateRects() {
        int sizeConerRect = 9;
        int centered = sizeConerRect/2;
        listRects.clear();
        listRects.add(new Rect(selection.x - centered,
                   selection.y - centered,
                   sizeConerRect,
                   sizeConerRect));
        listRects.add(new Rect((selection.x + selection.width / 2) - centered,
                   selection.y - centered,
                   sizeConerRect, sizeConerRect));
        listRects.add(new Rect((selection.x + selection.width) - centered,
                   selection.y - centered,
                   sizeConerRect, sizeConerRect));
        listRects.add(new Rect(selection.x - centered,
                  (selection.y + selection.height / 2) - centered,
                   sizeConerRect, sizeConerRect));
        listRects.add(new Rect(selection.x - centered,
                  (selection.y + selection.height) - centered,
                   sizeConerRect, sizeConerRect));
        listRects.add(new Rect((selection.x + selection.width) - centered,
                  (selection.y + selection.height / 2) - centered,
                   sizeConerRect, sizeConerRect));
        listRects.add(new Rect((selection.x + selection.width / 2)- centered,
                  (selection.y + selection.height) - centered,
                   sizeConerRect, sizeConerRect));
        listRects.add(new Rect((selection.x + selection.width)- centered,
                  (selection.y + selection.height) - centered,
                   sizeConerRect, sizeConerRect));
    }
    
    
    public void drawSelection(Graphics g) {
        if(this.selection !=null) {
            g.setColor(Color.blue);
            g.drawRect(selection.x, selection.y, selection.width, selection.height);

            for(Rect r : this.listRects) {
                g.drawRect(r.rect.x, r.rect.y, r.rect.width, r.rect.height);
            }
        }
    }
    
//    @Override
//    public void mouseDragged(MouseEvent me) {
//
////        for(Rect r: this.listRects){
////            if(r.selected) {
////                r.rect.x = me.getPoint().x;
////                r.rect.y = me.getPoint().y;
////            }
////        }
//    }
//
//    @Override
//    public void mousePressed(MouseEvent me) {
//
//        for(Rect r: this.listRects){
//            if(r.rect.contains(me.getPoint())) {
////                r.x = me.getPoint().x;
////                r.y = me.getPoint().y;
//                r.selected = true;
//            }
//        }
//    }
    
}


class Rect {
    public Rectangle rect;
    public boolean selected;
    
    public Rect(int x, int y, int w, int h) {
        this.rect = new Rectangle(x, y, w, h);
    }
}
