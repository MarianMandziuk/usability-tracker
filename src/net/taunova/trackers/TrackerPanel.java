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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.taunova.util.Position;
import net.taunova.util.Selection;
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
    private Robot robot;
    private int orderRect;
    private Point p1;
    private Point p2;
    private List<Rectangle> rectangles;
    boolean selectedRects;
    public TrackerPanel(MouseTracker tracker) {
        super(true);
        this.tracker = tracker;  
        tracker.setParent(this);
        this.screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        

        
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            logger.error("Robot excepion: " + ex);
        }
        

        new Timer(100, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                
            }
        }).start();
        
        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                p1 = e.getPoint();
                selectedRects = false;
                
                if(rectangles!=null) {
                    for(int i = 0; i < rectangles.size(); i++) {
                        if(rectangles.get(i).contains(p1)) {
                            selectedRects = true;
                            orderRect = i;
                        }
                    }
                }
                
                if(!selectedRects) {
                    if (selection != null && selection.contains(p1)) {
                        selectedSelection = true;
                        privX = p1.x;
                        privY = p1.y;
//                        System.out.println("1");
                    } else {
                        selection = new Rectangle(p1.x, p1.y, p1.x - p1.x, p1.y - p1.y);
                        selectedSelection = false;
                        opposideX = p1.x;
                        opposideY = p1.y;
//                        System.out.println("2");
                    }
//                    System.out.println("3");
                }
//                for(Rect r: selectionLi.listRects){
//                    if(r.rect.contains(e.getPoint())) {
////                r.x = me.getPoint().x;
////                r.y = me.getPoint().y;
//                        System.out.println("slec");
//                        r.selected = true;
//                        check = true;
//                    }
//                }
//                
//                if (!check) {
//                    if (selectionLi.selection != null && selectionLi.selection.contains(e.getPoint())) {
//                        selectedSelection = true;
//                        privX = e.getPoint().x;
//                        privY = e.getPoint().y;
//                    } else {
//                        selectionLi.setPoint(e.getPoint());
//                        selectedSelection = false;
//                        opposideX = e.getX();
//                        opposideY = e.getY();
//                    }
//                }
//                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                p2 = e.getPoint();
                int width;
                int height;
                int valX;
                int valY;
                if (p2.x > windowSize.width) {
                    valX = windowSize.width;
                } else if(p2.x < 0){
                    valX = 0;
                } else {
                    valX = p2.x;
                }
                if (p2.y > windowSize.height) {
                    valY = windowSize.height;
                } else if(p2.y < 0) {
                    valY = 0;
                } else {
                    valY = p2.y;
                }
                if(selectedRects) {                    
                    switch(orderRect) {
                        case 0: 
                            width = ((selection.x + selection.width) - p2.x);
                            height = ((selection.y + selection.height) - p2.y);
                            selection.setRect(p2.x, p2.y, width, height);                           
                            break;
                        case 1:
                            height = (selection.y + selection.height) - p2.y;
                            selection.setRect(selection.x, p2.y, selection.width, height);
                            break;
                        case 2:
                            width = (p2.x - selection.x);
                            height = (selection.y + selection.height) - p2.y;
                            selection.setRect(selection.x, p2.y, width, height);
                            break;
                    }
                    
                } else if (selectedSelection) {
                    if(selection.x +p2.x - privX + selection.width > windowSize.width) {
                        selection.x = windowSize.width - selection.width;
                    } else if (selection.x + p2.x - privX < 0) {
                        selection.x = 0;
                    } else if (selection.y + p2.y - privY + selection.height > windowSize.height) {
                        selection.y = windowSize.height - selection.height;
                    } else if (selection.y + p2.y - privY < 0) {
                        selection.y = 0;
                    } else {
                        selection.x += p2.x - privX;
                        selection.y += p2.y - privY;
                        privX = p2.x;
                        privY = p2.y;
                    }
                    
               
                } else if (p2.x < p1.x && p2.y  > p1.y) {
                    width = opposideX - valX;
                    height = valY - opposideY;
                    selection.setRect(valX, p1.y, width, height);
                } else if (p2.x < p1.x && p2.y < p1.y) {
                    width = opposideX - valX;
                    height = opposideY - valY;
                    selection.setRect(valX, valY, width, height);
                } else if (p2.x > p1.x && p2.y < p1.y) {
                    width = valX - opposideX;
                    height = opposideY - valY;
                    selection.setRect(p1.x, valY, width, height);
                }  else {
                    width = Math.max(p1.x - valX, valX - p1.x);
                    height = Math.max(p1.y - valY, valY - p1.y);
                    selection.setRect(p1.x, p1.y, width, height);
                }
                
            }
        };

        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
        
    }

    @Override
    public void paintComponent(Graphics g) {
        this.windowSize = getSize();

//        g.drawImage(this.takeSnapShot(), 0, 0, null);
        drawScreenShot(g);
       
        hideTunnel(g);
//        g.setColor(getBackground());
//        g.fillRect(0, 0, windowSize.width, windowSize.height);
//        g.setColor(getForeground());
        drawSelection(g);
//        if(this.selectionLi!=null) {
////            this.selectionLi.generateRects();
//            this.selectionLi.drawSelection(g);
//        }
//        Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        
//        Dimension size = getSize();
//        final double kX = (double)size.width/dim.width;
//        final double kY = (double)size.height/dim.height;
        
        final double kX = (double)this.windowSize.width/this.screenRect.width;
        final double kY = (double)this.windowSize.height/this.screenRect.height;

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
       
        BufferedImage im;
        im = robot.createScreenCapture(screenRect);

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
                int sizeConerRect = 9;
                int centered = sizeConerRect/2;
                
//                g.setColor(new Color(225, 225, 255, 1));
//                g.fillRect(selection.x, selection.y, selection.width, selection.height);
                g.setColor(Color.red);
                g.drawRect(selection.x, selection.y, selection.width, selection.height);
                this.rectangles = this.generateRects();
                for(Rectangle r: this.rectangles) {
                    g.drawRect(r.x, r.y, r.width, r.height);
                }
//                g.drawRect(selection.x - centered,
//                           selection.y - centered,
//                           sizeConerRect,
//                           sizeConerRect);
//                g.drawRect((selection.x + selection.width / 2) - centered,
//                           selection.y - centered,
//                           sizeConerRect, sizeConerRect);
//                g.drawRect((selection.x + selection.width) - centered,
//                           selection.y - centered,
//                           sizeConerRect, sizeConerRect);
//                g.drawRect(selection.x - centered,
//                          (selection.y + selection.height / 2) - centered,
//                           sizeConerRect, sizeConerRect);
//                g.drawRect(selection.x - centered,
//                          (selection.y + selection.height) - centered,
//                           sizeConerRect, sizeConerRect);
//                g.drawRect((selection.x + selection.width) - centered,
//                          (selection.y + selection.height / 2) - centered,
//                           sizeConerRect, sizeConerRect);
//                g.drawRect((selection.x + selection.width / 2)- centered,
//                          (selection.y + selection.height) - centered,
//                           sizeConerRect, sizeConerRect);
//                g.drawRect((selection.x + selection.width)- centered,
//                          (selection.y + selection.height) - centered,
//                           sizeConerRect, sizeConerRect);

           
                tracker.setSelection(
                        new Rectangle(
                                (int)((this.screenRect.width * selection.x)
                                        / (double) this.getSize().width),
                                (int)((this.screenRect.height * selection.y)
                                        / (double) this.getSize().height),
                                (int)((this.screenRect.width * selection.width)
                                        / (double) this.getSize().width),
                                (int)((this.screenRect.height * selection.height)
                                        / (double) this.getSize().height)),
                        true);
           


//                g.dispose();
            }
        }
    }
    
    private List generateRects() {
        List<Rectangle> rects = new ArrayList<>();
        int sizeConerRect = 9;
        int centered = sizeConerRect/2;
        rects.add(new Rectangle(selection.x - centered,
                   selection.y - centered,
                   sizeConerRect,
                   sizeConerRect));
        rects.add(new Rectangle((selection.x + selection.width / 2) - centered,
                   selection.y - centered,
                   sizeConerRect, sizeConerRect));
        rects.add(new Rectangle((selection.x + selection.width) - centered,
                   selection.y - centered,
                   sizeConerRect, sizeConerRect));
        rects.add(new Rectangle(selection.x - centered,
                  (selection.y + selection.height / 2) - centered,
                   sizeConerRect, sizeConerRect));
        rects.add(new Rectangle(selection.x - centered,
                  (selection.y + selection.height) - centered,
                   sizeConerRect, sizeConerRect));
        rects.add(new Rectangle((selection.x + selection.width) - centered,
                  (selection.y + selection.height / 2) - centered,
                   sizeConerRect, sizeConerRect));
        rects.add(new Rectangle((selection.x + selection.width / 2)- centered,
                  (selection.y + selection.height) - centered,
                   sizeConerRect, sizeConerRect));
        rects.add(new Rectangle((selection.x + selection.width)- centered,
                  (selection.y + selection.height) - centered,
                   sizeConerRect, sizeConerRect));        
        return rects;
    }
    
    private Rectangle getSelectedRect() {
        Rectangle rect = null;
        for(Rectangle r: this.rectangles){
            if(r.contains(p1)) {
                rect = r;
                break;
            }
        }
        return rect;
    }
    
    private void hideTunnel(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect((int)((this.getLocationOnScreen().x * this.getSize().width)
                        / (double) this.screenRect.width),
                   (int)((this.getLocationOnScreen().y * this.getSize().height)
                        / (double) this.screenRect.height), 
                   (int)(this.getSize().width / (this.screenRect.width
                           / (double) this.getSize().width)),
                   (int)(this.getSize().height / ( this.screenRect.height 
                           / (double) this.getSize().height)));
    }
    
    
    private void drawScreenShot(Graphics g) {
        Dimension dim = getSize();
//        if (image == null) {
//            image = createImage (dim.width, dim.height);
//            image = takeSnapShot();
//        }
        image = takeSnapShot();
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

