/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.util;

import java.awt.Rectangle;

/**
 *
 * @author maryan
 */
public class Rect {
    public Rectangle rect;
    public boolean selected;
    
    public Rect(int x, int y, int w, int h) {
        this.rect = new Rectangle(x, y, w, h);
    }
}
