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
import javax.swing.*;

import net.taunova.util.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.taunova.util.Selection;
import net.taunova.util.SelectionUtil;
/**
 *
 * @author maryan
 */
public class TrackerPanel extends JPanel {
    private MouseTracker tracker;
    private Dimension windowSize;
    private final Logger logger = LoggerFactory.getLogger(TrackerPanel.class);
    private Rectangle screenRect;
    private Image image;
    private Graphics dbg;
    private Robot robot;
    private Point p1;
    private Point p2;
    private boolean selectedRects;
    private int privWidth;
    private int privHeight;
    private Selection selectionNew;
    private Timer timer;
    public boolean start = false;

    public static final int DELAY = 10;

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

        timer = new Timer(100, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                    repaint();

            }
        });

        timer.start();

        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (start) {
                    p1 = e.getPoint();

                    if (selectionNew == null) {
                        selectionNew = new Selection(e.getX(), e.getY(), 0, 0);
                    } else {
                        selectedRects = selectionNew.isSelectedSensitiveArea(p1);
                    }

                    if (!selectedRects) {
                        selectionNew.checkOnSelected(p1);
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (start) {
                    p2 = e.getPoint();
                    if (p2 == null) {
                        logger.warn("Second point can't be null");
                    }
                    if (!SelectionUtil.isPointInWindowBoundary(p1,
                            windowSize.width,
                            windowSize.height)) {
                        SelectionUtil.setCorrectPointBoundary(p1,
                                windowSize.width,
                                windowSize.height);
                    }
                    if (!SelectionUtil.isPointInWindowBoundary(p2,
                            windowSize.width,
                            windowSize.height)) {
                        SelectionUtil.setCorrectPointBoundary(p2,
                                windowSize.width,
                                windowSize.height);
                    }

                    if (selectedRects) {
                        selectionNew.resizeSelection(p2);
                    } else if (!selectionNew.isSelected()) {
                        selectionNew.setSidesSelection(p1, p2);
                    } else {
                        selectionNew.setSelectedSelection(
                                p1,
                                p2,
                                windowSize.width,
                                windowSize.height
                        );
                    }
                    tracker.setSelection(
                            new Rectangle(
                                    (int) ((screenRect.width * selectionNew.getX())
                                            / (double) windowSize.width),
                                    (int) ((screenRect.height * selectionNew.getY())
                                            / (double) windowSize.height),
                                    (int) ((screenRect.width * selectionNew.getWidth())
                                            / (double) windowSize.width),
                                    (int) ((screenRect.height * selectionNew.getHeight())
                                            / (double) windowSize.height)),
                            true);
                    privWidth = windowSize.width;
                    privHeight = windowSize.height;
                }
            }
        };

        ComponentAdapter resizeComponent = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                try {

                    double w = (windowSize.width /(double) privWidth) * selectionNew.getWidth();
                    double h = (windowSize.height /(double) privHeight) * selectionNew.getHeight();
                    double x1 = (windowSize.width /(double) privWidth) * selectionNew.getX();
                    double y1 = (windowSize.height /(double) privHeight) * selectionNew.getY();
                    x1 = boundariesCorrectionX(x1, screenRect.width);
                    y1 = boundariesCorrectionY(y1, screenRect.height);
                    w = boundariesCorrectionW(w, x1);
                    h = boundariesCorrectionH(h, y1);

                    selectionNew.setDoubleToIntRectangle(x1, y1, w, h);

                    privWidth = windowSize.width;
                    privHeight = windowSize.height;

                } catch(NullPointerException ex) {
                    // do nothing
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
        drawScreenShot(g);


        if (selectionNew != null) {
            selectionNew.drawSelection(g);
        }

        final double kX = (double)this.windowSize.width/this.screenRect.width;
        final double kY = (double)this.windowSize.height/this.screenRect.height;


        tracker.processPath(new TrackerCallback() {

            @Override
            public void process(Position begin, Position end) {
                g.setColor(begin.getColor());
                g.drawLine((int)(kX * begin.position.x),
                           (int)(kY * begin.position.y),
                           (int)(kX * end.position.x),
                           (int)(kY * end.position.y));
                if(begin.getDelay() > DELAY) {
                    int radius = begin.getDelay();
                    if(radius > 20) {
                        radius = 20;
                    }

                    g.drawOval((int)(kX*begin.position.x)-radius/2,
                           (int)(kY*begin.position.y)-radius/2, radius, radius);
                    int x = (int)(kX*begin.position.x)-radius/2;
                    int y = (int)(kY*begin.position.y)-radius/2;
                    if(begin.getNumber() != 0) {
                        g.drawString(Integer.toString(begin.getNumber()), x, y);
                    }
                }
            }
        });

    }
    
    public void takeSnapShot() {
       
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
        this.image = tmpImage;
    }

    
//    private void hideTunnel(Graphics g) {
//        g.setColor(Color.GRAY);
//        g.fillRect((int)((this.getLocationOnScreen().x * this.getSize().width)
//                        / (double) this.screenRect.width),
//                   (int)((this.getLocationOnScreen().y * this.getSize().height)
//                        / (double) this.screenRect.height),
//                   (int)(this.getSize().width / (this.screenRect.width
//                           / (double) this.getSize().width)),
//                   (int)(this.getSize().height / ( this.screenRect.height
//                           / (double) this.getSize().height)));
//    }
    
    
    private void drawScreenShot(Graphics g) {
        if (image != null) {
//            Dimension dim = getSize();
//            image = takeSnapShot();
//            dbg = image.getGraphics();
//
//            dbg.setColor(getBackground());
//            dbg.fillRect(0, 0, dim.width, dim.height);
//            dbg.setColor(getForeground());
//
//
//            dbg.drawImage(takeSnapShot(), 0, 0, null);
            g.drawImage(image, 0, 0, null);

//            dbg.dispose();
//            hideTunnel(g);
        }
    }
    
    private double boundariesCorrectionX(double x, int width) {
        if (x < 1.0) {
            x = 1.0;
        } else if (x > width) {
            x = width;
        }

        return x;
    }

    private double boundariesCorrectionY(double y, int height) {
        if (y < 1.0) {
            y = 1.0;
        } else if (y > height) {
            y = height;
        }
        return y;
    }

    private double boundariesCorrectionW(double w, double x) {
        if (w < 1.0) {
            w = 1.0;
        } else if ((x + w) > windowSize.width) {
            w = w - ((x+w) - windowSize.width);
        }
        return w;
    }

    private double boundariesCorrectionH(double h, double y) {
        if (h < 1.0) {
            h = 1.0;
        } else if ((y + h) > windowSize.height) {
            h = h - ((h + y) - windowSize.height);
        }
        return h;
    }

    public void stopExcution() {
        this.timer.stop();
        this.start = false;
    }

    public void setSelectionNull() {
        this.selectionNew = null;
        this.tracker.setSelection(null, false);
    }
}
