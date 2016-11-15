/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.trackers;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.taunova.util.Position;


/**
 *
 * @author maryan
 */
public class TrackerPanel extends JPanel {
    private MouseTracker tracker;
    private Rectangle selection;
    private boolean selectedSelection = false;
    private int privX;
    private int privY;
    public TrackerPanel(MouseTracker tracker) {
        super(true);
        this.tracker = tracker;  
        tracker.setParent(this);
                   
        new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }).start();
        
        
        MouseAdapter handler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("Mouse pressed");
                    if (selection != null && selection.contains(e.getPoint())) {
                        selectedSelection = true;
                        privX = e.getPoint().x;
                        privY = e.getPoint().y;
                    } else {
                        selection = new Rectangle(e.getPoint());
                        selectedSelection = false;
                    }
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");
                    Point p = e.getPoint();
                    int width;
                    int height;
                    
                    
                    if (selectedSelection) {
                        System.out.println(e.getPoint() + "  " + selection.x  + " " + selection.y);
                        selection.x += e.getPoint().x - privX;
                        selection.y += e.getPoint().y - privY;


                        privX = e.getPoint().x;
                        privY = e.getPoint().y;
                    } else {
                        System.out.println("else");
                        width = Math.max(selection.x - e.getX(), e.getX() - selection.x);
                        height = Math.max(selection.y - e.getY(), e.getY() - selection.y);

                        selection.setSize(width, height);
                    }
                    
                    repaint();
                }
            };
            
            this.addMouseListener(handler);
            this.addMouseMotionListener(handler);
    }

        
    public void paint(final Graphics g) {

        g.drawImage(takeSnapShot(), 0, 0, null);
        if (true) {
            Graphics2D g2d = (Graphics2D) g.create();

            if (selection != null) {
//                g.drawRect(selection.x, selection.y, selection.x + 10, selection.y + 10);
                g2d.setColor(new Color(225, 225, 255, 128));
                g2d.fill(selection);
                g2d.setColor(Color.GRAY);
                g2d.draw(selection);
            }
            g2d.dispose();
        }
        
        Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        
        Dimension size = getSize();
        final double kX = (double)size.width/dim.width;
        final double kY = (double)size.height/dim.height;
        g.setColor(Color.red);
        tracker.processPath(new TrackerCallback() {
            
            @Override
            public void process(Position begin, Position end) {
                g.drawLine((int)(kX * begin.position.x), 
                           (int)(kY * begin.position.y), 
                           (int)(kX * end.position.x), 
                           (int)(kY * end.position.y));
                if(begin.getDelay() > 10) {
                    int radius = begin.getDelay();                    
                    if(radius > 20) {
                        radius = 20;
                    }
                    
                    g.drawOval((int)(kX*begin.position.x)-radius/2, 
                           (int)(kY*begin.position.y)-radius/2, radius, radius);
                }
                
                 
            }
        });
    }
    
    private BufferedImage takeSnapShot() {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage im = null;
        Dimension size = getSize();
        
        try {
            im = new Robot().createScreenCapture(screenRect);
        } catch (AWTException ex) {
           System.out.println("Error");
        }
        
        BufferedImage tmpIm = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tmpIm.createGraphics();
        g2.drawImage(im.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH), 0, 0, size.width, size.height, null);
        g2.dispose();
        
        return tmpIm;
    }
   
}
