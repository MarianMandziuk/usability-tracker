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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
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
    private Robot robot;
    private int orderRect;
    private Point p1;
    private Point p2;
    private List<Rectangle> rectangles;
    boolean selectedRects;
    private int privWidth;
    private int privHeight;
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
                            switch (i) {
                                case 0:                               
                                    opposideX = selection.x+selection.width;
                                    opposideY = selection.y+selection.height;
                                    break;
                                case 2:
                                case 1:
                                
                                    opposideX = selection.x;
                                    opposideY = selection.y+selection.height;
                                    break;
                                case 7:
                                case 6:
                                case 5:
                                    opposideX = selection.x;
                                    opposideY = selection.y;
                                    break;
                                case 4:
                                case 3:    
                                    opposideX = selection.x+selection.width;
                                    opposideY = selection.y;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                
                if(!selectedRects) {
                    if (selection != null && selection.contains(p1)) {
                        selectedSelection = true;
                        privX = p1.x;
                        privY = p1.y;
                    } else {
                        selection = new Rectangle(p1.x, p1.y, p1.x - p1.x, p1.y - p1.y);
                        selectedSelection = false;
                        opposideX = p1.x;
                        opposideY = p1.y;
                    }
                }
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
                        case 2:
                        case 4:
                        case 7:
                        
                                if (p2.x < opposideX && p2.y  > opposideY) {
                                    width = opposideX - valX;
                                    height = valY - opposideY;
                                    selection.setRect(valX, opposideY, width, height);
                                } else if (p2.x < opposideX && p2.y < opposideY) {
                                    width = opposideX - valX;
                                    height = opposideY - valY;
                                    selection.setRect(valX, valY, width, height);
                                } else if (p2.x > opposideX && p2.y < opposideY) {
                                    width = valX - opposideX;
                                    height = opposideY - valY;
                                    selection.setRect(opposideX, valY, width, height);
                                }  else {
                                    width = valX - opposideX;
                                    height = valY - opposideY;
                                    selection.setRect(opposideX, opposideY, width, height);
                                } 
                            break;
                        case 1:
                        case 6:
                                if (p2.x > opposideX && p2.y < opposideY) {
                                  height = opposideY - valY;
                                  selection.setRect(opposideX, valY, selection.width, height);
                                  System.out.println("3");
                                }  else {
                                    height = valY - opposideY;
                                    selection.setRect(opposideX, opposideY, selection.width, height);
                                } 
                            break;                           
                        case 3:
                        case 5:
                                if (p2.x < opposideX && p2.y  > opposideY) {
                                    width = opposideX - valX;
                                    selection.setRect(valX, opposideY, width, selection.height);
                                } else  {
                                    width = valX - opposideX;
                                    selection.setRect(opposideX, opposideY, width, selection.height);
                                } 
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
                    width = valX - p1.x;
                    height = valY - p1.y;
                    selection.setRect(p1.x, p1.y, width, height);
                }  
//            proportionWidth = selection.width /(double) windowSize.width;
//            proportionHeight = selection.height /(double) windowSize.height;
            privWidth = windowSize.width;
            privHeight = windowSize.height;
            }
        };
        
        ComponentAdapter resizeComponent = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                try {

                    double w = (windowSize.width /(double) privWidth) * selection.width;
                    double h = (windowSize.height /(double) privHeight) * selection.height;
                    double x1 = (windowSize.width /(double) privWidth) * selection.x;
                    double y1 = (windowSize.height /(double) privHeight) * selection.y;
                    x1 = boundariesCorrectionX(x1);
                    y1 = boundariesCorrectionY(y1);
                    w = boundariesCorrectionW(w, x1);
                    h = boundariesCorrectionH(h, y1);
//                    selection.setRect(scalerX, scalerY, w, h);

                    setDoubleToIntRectangle(selection, x1, y1, w, h);

                    privWidth = windowSize.width;
                    privHeight = windowSize.height;
                    
                } catch(NullPointerException ex) {

                }
            }
        };
        
        
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
        this.addComponentListener(resizeComponent);
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
                g.setColor(Color.red);
//                System.out.println("x: " + scalerX + " y: " + scalerY);
                
                g.drawRect(selection.x, selection.y, selection.width, selection.height);


                this.rectangles = this.generateRects();
                for(Rectangle r: this.rectangles) {
                    g.drawRect(r.x, r.y, r.width, r.height);
                }

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
    
    public void setDoubleToIntRectangle(Rectangle rect,
                                        final double x,
                                        final double y, 
                                        final double w, 
                                        final double h) {

        rect.x = isRemainderHigher(x) ? (int)x + 1 : (int)x;
        rect.y = isRemainderHigher(y) ? (int)y + 1 : (int)y;
        rect.width = isRemainderHigher(w) ? (int)w + 1 : (int)w;
        rect.height = isRemainderHigher(h) ? (int)h + 1 : (int)h; 
    }
    
    public boolean isRemainderHigher(final double number) {
        double remainder = number - (int)(number);
        return remainder > 0.4;
    }
    
    public double boundariesCorrectionX(double x) {
        if (x < 1.0) {
            x = 1.0;
        } else if (x > screenRect.width) {
            x = screenRect.width;
        }

        return x;
    }
    
    public double boundariesCorrectionY(double y) {
        if (y < 1.0) {
            y = 1.0;
        } else if (y > screenRect.height) {
            y = screenRect.height;
        }
        return y;
    }
    
    public double boundariesCorrectionW(double w, double x) {
        if (w < 1.0) {
            w = 1.0;
        } else if ((x + w) > windowSize.width) {
            w = w - ((x+w) - windowSize.width);
        }
        return w;
    }
    
    public double boundariesCorrectionH(double h, double y) {
        if (h < 1.0) {
            h = 1.0;
        } else if ((y + h) > windowSize.height) {
            h = h - ((h + y) - windowSize.height);
        }
        return h;
    }
}
