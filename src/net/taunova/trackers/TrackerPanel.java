package net.taunova.trackers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

import net.taunova.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private BufferedImage fullscreenImage;
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
    private Grid grid;
    public boolean heatmapEnable = false;
    public boolean dualTrackingEnable = false;
    public static final int DELAY = 10;
    private int i;

    public TrackerPanel(MouseTracker tracker, Grid grid) {
        super(true);
        this.tracker = tracker;
        tracker.setParent(this);
        this.screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        this.grid = grid;
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
                    image = resizeScreenShot();
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

//        this.addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent e) {
//                r.
//            }
//        });

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
        if (this.dualTrackingEnable) {
            i = tracker.trackHeatMap(i);
            this.grid.drawGrid(g, kX, kY);
            drawTracks(g, kX, kY);
        } else {
            if (this.heatmapEnable) {
                i = tracker.trackHeatMap(i);
                this.grid.drawGrid(g, kX, kY);
            } else {
                drawTracks(g, kX, kY);
            }
        }

    }

    private void drawTracks(Graphics g, double kX, double kY) {
        tracker.processPath(new TrackerCallback() {

            @Override
            public void process(Position start, Position control1,
                                Position control2, Position end) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(start.getWidht()));
                g2.setColor(start.getColor());
                CubicCurve2D c = new CubicCurve2D.Double();
                c.setCurve(kX * start.position.x,
                        kY * start.position.y,
                        kX * control1.position.x,
                        kY * control1.position.y,
                        kX * control2.position.x,
                        kY * control2.position.y,
                        kX * end.position.x,
                        kY * end.position.y);

                g2.draw(c);
                Position[] arr = {start, control1, control2};
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].getDelay() > DELAY) {
                        int radius = arr[i].getDelay();
                        if (radius > 20) {
                            radius = 20;


                        g2.drawOval((int) (kX * arr[i].position.x) - radius / 2,
                                (int) (kY * arr[i].position.y) - radius / 2, radius, radius);
                        int x = (int) (kX * arr[i].position.x) - radius / 2;
                        int y = (int) (kY * arr[i].position.y) - radius / 2;
                        if (arr[i].getNumber() != 0) {
                            g2.drawString(Integer.toString(arr[i].getNumber()), x, y);
                        }
                    }}
                }
            }
        });
    }
    public void takeSnapShot() {
        fullscreenImage = robot.createScreenCapture(screenRect);

        BufferedImage tmpImage = new BufferedImage(windowSize.width,
                windowSize.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = tmpImage.createGraphics();
        g2.drawImage(
                fullscreenImage.getScaledInstance(windowSize.width,
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

    private BufferedImage resizeScreenShot() {
        BufferedImage tmpImage = new BufferedImage(windowSize.width,
                windowSize.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = tmpImage.createGraphics();
        g2.drawImage(
                fullscreenImage.getScaledInstance(windowSize.width,
                        windowSize.height,
                        Image.SCALE_FAST),
                0,
                0,
                windowSize.width,
                windowSize.height,
                null);

        g2.dispose();
        return tmpImage;
    }
    
    private void drawScreenShot(Graphics g) {
        if (image != null) {
//            g.drawImage(image, 0, 0, null);
            Dimension dim = getSize();

            dbg = image.getGraphics();

            dbg.setColor(getBackground());
            dbg.fillRect(0, 0, dim.width, dim.height);
            dbg.setColor(getForeground());

            dbg.drawImage(resizeScreenShot(), 0, 0, null);

            g.drawImage(image, 0, 0, null);

            dbg.dispose();
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

    public BufferedImage getFullscreenImage() {
        BufferedImage tmp = new BufferedImage(fullscreenImage.getWidth(),
                fullscreenImage.getHeight(),
                fullscreenImage.getType());
        Graphics g = tmp.getGraphics();
        g.drawImage(fullscreenImage, 0, 0, null);
        g.dispose();
        return tmp;
    }

    public void startTimer() {
        timer.start();
    }

    public void setCounterI(int i) {
        this.i = i;
    }
}
