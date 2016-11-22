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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private int opposideX;
    private int opposideY;
    private Dimension windowSize;
    private final Logger logger = LoggerFactory.getLogger(TrackerPanel.class);
    private Rectangle screenRect;
    private Graphics dbg;
    private Image image;


    public TrackerPanel(MouseTracker tracker) {
        super(true);
        this.tracker = tracker;  
        tracker.setParent(this);
        this.screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        
        new Timer(100, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                
            }
        }).start();
        
        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//                System.out.println("Mouse pressed");
                if (selection != null && selection.contains(e.getPoint())) {
                    selectedSelection = true;
                    privX = e.getPoint().x;
                    privY = e.getPoint().y;
                } else {
                    selection = new Rectangle(e.getPoint());
                    selectedSelection = false;
                    opposideX = e.getX();
                    opposideY = e.getY();
                }
//                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
//                System.out.println("mouseDragged");
                Point p = e.getPoint();
                int width;
                int height;
                int valX;
                int valY;
                if (e.getX() > windowSize.width) {
                    valX = windowSize.width;
                } else if(e.getX() < 0){
                    valX = 0;
                } else {
                    valX = e.getX();
                }
                if (e.getY() > windowSize.height) {
                    valY = windowSize.height;
                } else if(e.getY() < 0) {
                    valY = 0;
                } else {
                    valY = e.getY();
                }
                
                if (selectedSelection) {
                    if(selection.x +e.getPoint().x - privX + selection.width > windowSize.width) {
                        selection.x = windowSize.width - selection.width;
                    } else if (selection.x + e.getPoint().x - privX < 0) {
                        selection.x = 0;
                    } else if (selection.y + e.getPoint().y - privY + selection.height > windowSize.height) {
                        selection.y = windowSize.height - selection.height;
                    } else if (selection.y + e.getPoint().y - privY < 0) {
                        selection.y = 0;
                    } else {
                        selection.x += e.getPoint().x - privX;
                        selection.y += e.getPoint().y - privY;
                        privX = e.getPoint().x;
                        privY = e.getPoint().y;
                    }
                    
                } else if (e.getX() > selection.x && e.getY() > selection.y){
                    
                    
                    width = Math.max(selection.x - valX, valX - selection.x);
                    height = Math.max(selection.y - valY, valY - selection.y);
                    selection.setSize(width, height);
                } else if (e.getX() < selection.x && e.getY()  > selection.y) {
                    width = opposideX - valX;
                    height = valY - opposideY;
                    selection.x = valX;
                    selection.setSize(width, height);
                } else if (e.getX() < selection.x && e.getY() < selection.y) {
                    width = opposideX - valX;
                    height = opposideY - valY;
                    selection.x = valX;
                    selection.y = valY;
                    selection.setSize(width, height);
                } else if (e.getX() > selection.x && e.getY() < selection.y) {
                    width = valX - opposideX;
                    height = opposideY - valY;
                    selection.y = valY;
                    selection.setSize(width, height);
                }
//                }
//                repaint();
            }
        };

        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
        
    }

    @Override
    public void paintComponent(Graphics g) {
        this.windowSize = getSize();

        g.drawImage(this.takeSnapShot(), 0, 0, null);
//        drawScreenShot(g);
        hideTunnel(g);
        drawSelection(g);

        
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
       
        BufferedImage im = null;
        try {
            im = new Robot().createScreenCapture(screenRect);
        } catch (AWTException ex) {
            logger.error("Error: " + ex);
        }

        BufferedImage tmpImage = new BufferedImage(windowSize.width,
                windowSize.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = tmpImage.createGraphics();
        g2.drawImage(
                im.getScaledInstance(windowSize.width,
                        windowSize.height,
                        Image.SCALE_SMOOTH),
                        0, 
                        0, 
                        windowSize.width, 
                        windowSize.height,
                        null);
        
        g2.dispose();
        
        return tmpImage;
    }
    
    private void drawSelection(Graphics g) {
        if (true) {
            if (selection != null) {
                int sizeConerRect = 5;
//                g.setColor(new Color(225, 225, 255, 1));
//                g.fillRect(selection.x, selection.y, selection.width, selection.height);
                g.setColor(Color.RED);
                g.drawRect(selection.x, selection.y, selection.width, selection.height);
                
                g.drawRect(selection.x - 2,
                           selection.y - 2,
                           sizeConerRect,
                           sizeConerRect);
                g.drawRect((selection.x + selection.width / 2) - 2,
                           selection.y - 2,
                           sizeConerRect, sizeConerRect);
                g.drawRect((selection.x + selection.width) - 2,
                           selection.y - 2,
                           sizeConerRect, sizeConerRect);
                g.drawRect(selection.x - 2,
                          (selection.y + selection.height / 2) - 2,
                           sizeConerRect, sizeConerRect);
                g.drawRect(selection.x - 2,
                          (selection.y + selection.height) - 2,
                           sizeConerRect, sizeConerRect);
                g.drawRect((selection.x + selection.width) - 2,
                          (selection.y + selection.height / 2) - 2,
                           sizeConerRect, sizeConerRect);
                g.drawRect((selection.x + selection.width / 2)- 2,
                          (selection.y + selection.height) - 2,
                           sizeConerRect, sizeConerRect);
                g.drawRect((selection.x + selection.width)- 2,
                          (selection.y + selection.height) - 2,
                           sizeConerRect, sizeConerRect);

                tracker.setSelection(
                        new Rectangle(
                                selection.x*2,
                                selection.y*2,
                                selection.width*2,
                                selection.height*2),
                        true);
//                g.dispose();
            }
        }
    }
    
    private void hideTunnel(Graphics g) {
        g.setColor(Color.GRAY);
        int x1 = this.getLocationOnScreen().x;
        int y1 = this.getLocationOnScreen().y;
        
        g.fillRect(((int)(this.getLocationOnScreen().x/2.0) ),
                   ((int)(this.getLocationOnScreen().y/2.0) ), 
                   this.getSize().width/2, this.getSize().height/2);
    }
    
    
    private void drawScreenShot(Graphics g) {
        Dimension dim = getSize();

        image = createImage (dim.width, dim.height);
        dbg = image.getGraphics();

        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, dim.width, dim.height);
        dbg.setColor(getForeground());
        
        
        dbg.drawImage(takeSnapShot(), 0, 0, null);

        g.drawImage(image, 0, 0, null);
//        g.dispose();
        dbg.dispose();
    }
}

