/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.util;

import java.awt.Point;

/**
 *
 * @author maryan
 */
public class Position {
    public Point position;
    public int delay;           
    
    public Position() {
        
    }
    
    public Position(Point position) {
        this.position = position;
    }
    
    public void incDelay() {
        delay++;
    }

    public int getDelay() {
        return delay;
    }
       
}
