package net.taunova.usability;


import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.taunova.trackers.TrackerFrame;

/**
 *
 * @author renat
 */
//class ThreadUtil {
//    public static final void sleep(int millis) {
//        try{
//            Thread.sleep(millis); //this will slow the capture rate to 0.1 seconds
//        }catch(Exception e) {
//            //...
//        }        
//    }
//}


//class Position {
//    public Point position;
//    public int delay;           
//    
//    public Position() {
//        
//    }
//    
//    public Position(Point position) {
//        this.position = position;
//    }
//    
//    public void incDelay() {
//        delay++;
//    }
//
//    public int getDelay() {
//        return delay;
//    }
//       
//}

//interface TrackerCallback {
//    void process (Position begin, Position end);
//}


//class ControlPanel extends JPanel implements ActionListener {
//    private MouseTracker tracker;
//    private TrackerFrame frame;
//    public JButton button5;
//    public boolean start = false;
//    
//    public ControlPanel(MouseTracker tracker, TrackerFrame frame) {
//        super(true);
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.tracker = tracker;
//        this.frame = frame;
//        
//        JButton button1 = new JButton("Clear");
//        JButton button2 = new JButton("Mark area");
//        JButton button3 = new JButton("New slide");
//        JButton button4 = new JButton("Take snapshot");
//        button5 = new JButton("Start");
//        
//        add(button5);
//        add(button1);
//        add(button2);
//        add(button3);
//        add(button4);
//        
//        button1.addActionListener(new CleanTracker(tracker));
//        button4.addActionListener(this);
//        button5.addActionListener(new StartButtonListener(frame));
//        
//    }     
//
//    @Override
//    public void actionPerformed(ActionEvent ae) {
////        this.frame.setVisible(false);
//        this.frame.setState(JFrame.ICONIFIED);
//        
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            System.out.println("Error: " + ex);
//        }
//        
//        BufferedImage image = takeSnapShot();
//        this.frame.setState(JFrame.NORMAL);
////        this.frame.setVisible(true);
//        drawTrack(image);
//        try {
//            saveScreen(image, "imageName");
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex);
//        }
//        
//        JOptionPane.showMessageDialog(this,
//            "Image saved");
//    }
//    
//    private BufferedImage takeSnapShot() {
//        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//        BufferedImage im = null;
//        try {
//            im = new Robot().createScreenCapture(screenRect);
//        } catch (AWTException ex) {
//           System.out.println("Error");
//        }
//
//        return im;
//    }
//    
//    private void drawTrack(BufferedImage image) {
//        List<Position> positionList = this.tracker.getPosition();
//        Graphics2D g2 = image.createGraphics();
//        g2.setColor(Color.red);
//        for(int i = 0; i < positionList.size() - 1; i++) {
//            g2.drawLine((int)(positionList.get(i).position.x), 
//                           (int)(positionList.get(i).position.y), 
//                           (int)(positionList.get(i + 1).position.x), 
//                           (int)(positionList.get(i + 1).position.y));
//            if(positionList.get(i).getDelay() > 10) {
//                int radius = positionList.get(i).getDelay();                    
//                if(radius > 50) {
//                    radius = 50;
//                }
//
//                g2.drawOval((int)(positionList.get(i).position.x)-radius/2, 
//                       (int)(positionList.get(i).position.y)-radius/2, radius, radius);
//            }
//        }
//        g2.dispose();
//    }
//    
//    private void saveScreen(BufferedImage image, String name) throws IOException {
//        String format = "jpg";
//        ImageIO.write(image, format, new File(name + "." + format));
//    }
//}

//class CleanTracker implements ActionListener {
//    private MouseTracker tracker;
//    
//    
//    public CleanTracker(MouseTracker tracker) {
//        this.tracker = tracker;
//    }
//    
//    @Override
//    public void actionPerformed(ActionEvent ae) {
//       tracker.getPosition().clear();
//    }
//    
//}

//class StartButtonListener implements ActionListener {
//    private TrackerFrame frame;
//    
//    public StartButtonListener(TrackerFrame frame) {
//        this.frame = frame;
//    }
//    
//    @Override
//    public void actionPerformed(ActionEvent ae) {
//        new Thread(this.frame.tracker).start();
//        Object source = ae.getSource();
//        if (source instanceof JButton) {
//           
//            if (frame.isActive() && ((JButton) source).getText() == "Start") {
//                this.frame.setState(JFrame.ICONIFIED);
////                this.frame.setFocusable(false);
//                ((JButton) source).setText("Pause");
//            }
//        }
//    }
//    
//}

//class TrackerPanel extends JPanel {
//    private MouseTracker tracker;
//    
//    public TrackerPanel(MouseTracker tracker) {
//        super(true);
//        this.tracker = tracker;  
//        tracker.setParent(this);
//                   
//        new Timer(100, new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                repaint();
//            }
//        }).start();
//        
//    }
//    
//    public void paint(final Graphics g) {
//
//        g.drawImage(takeSnapShot(), 0, 0, null);
//        
//        Dimension size = getSize();
//        Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//       
//        final double kX = (double)size.width/dim.width;
//        final double kY = (double)size.height/dim.height;
//        g.setColor(Color.red);
//        tracker.processPath(new TrackerCallback() {
//            
//            @Override
//            public void process(Position begin, Position end) {
//                g.drawLine((int)(kX * begin.position.x), 
//                           (int)(kY * begin.position.y), 
//                           (int)(kX * end.position.x), 
//                           (int)(kY * end.position.y));
//                if(begin.getDelay() > 10) {
//                    int radius = begin.getDelay();                    
//                    if(radius > 20) {
//                        radius = 20;
//                    }
//                    
//                    g.drawOval((int)(kX*begin.position.x)-radius/2, 
//                           (int)(kY*begin.position.y)-radius/2, radius, radius);
//                }
//            }
//        });
//    }
//    
//    private BufferedImage takeSnapShot() {
//        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//        BufferedImage im = null;
//        Dimension size = getSize();
//        
//        try {
//            im = new Robot().createScreenCapture(screenRect);
//        } catch (AWTException ex) {
//           System.out.println("Error");
//        }
//        
//        BufferedImage tmpIm = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = tmpIm.createGraphics();
//        g2.drawImage(im.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH), 0, 0, size.width, size.height, null);
//        g2.dispose();
//        
//        return tmpIm;
//    }
//   
//}


class UsabilityFrame {
    
}


//class MouseTracker implements Runnable  {
//    private final static int DELAY = 5;
//    private final static int BUFFER_SIZE = 5000;
//    private Position current = new Position();
//    private List<Position> positionList = new ArrayList<>(100*1024);
//    private Component parent;
//    private JFrame frame;
//    
//    MouseTracker(TrackerFrame it) {
//       this.frame = it;
//    }
//    
//    public void setParent(Component parent) {
//        this.parent = parent;
//    }
//    
//    public void processPath(TrackerCallback callback) {
//        if(positionList.size() > 2) {
//            Position current = positionList.get(0);
//            for(int i=1; i< positionList.size(); i++) {
//                Position next = positionList.get(i);
//                callback.process(current, next);
//                current = next;                
//            }
//        }        
//    }
//
//     
//    @Override
//    public void run() {
//        current.position = MouseInfo.getPointerInfo().getLocation(); 
//        positionList.add(current);
//        
//        while(true) {
//            if(!this.frame.isActive()) {
//                
//                long time1 = System.currentTimeMillis();
//                PointerInfo info = MouseInfo.getPointerInfo();            
//                Point p = info.getLocation();
//                if(current.position.x != p.x ||
//                   current.position.y != p.y) {
//                    current = new Position(p);
//                    positionList.add(current);                               
//                }else{
//                    current.incDelay();
//                    ThreadUtil.sleep(DELAY);
//                }                    
//
//                long time2 = System.currentTimeMillis();
//    //            System.out.println("it took: " + (time2 - time1));
//
//            }
//        }
//    }
//
//    public List<Position> getPosition() {
//        return this.positionList;
//    }
//}


//class TrackerFrame extends JFrame implements WindowFocusListener {
//    ControlPanel buttonPanel;
//    TrackerPanel trackerPanel;
//    MouseTracker tracker;
//    public boolean startTracking = false;
//    
//    public TrackerFrame() {
//        super("Tracker frame");
//        getContentPane().setLayout(new BorderLayout());
//        
//        tracker = new MouseTracker(this);
//        buttonPanel = new ControlPanel(tracker, this);
//        trackerPanel = new TrackerPanel(tracker);
//        
//        
//        
//        getContentPane().add(BorderLayout.EAST, buttonPanel);
//        getContentPane().add(BorderLayout.CENTER, trackerPanel);
//        
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(400, 400);
//        setVisible(true); 
//        addWindowFocusListener(this);
//    }
//
//
//    @Override
//    public void windowGainedFocus(WindowEvent we) {
//       this.buttonPanel.button5.setText("Start");
//       this.startTracking = true;
//    }
//
//    @Override
//    public void windowLostFocus(WindowEvent we) {
//        this.buttonPanel.button5.setText("Pause");
//        this.startTracking = false;
//    }
//}

/**
 *
 * @author renat
 */
public class UsabilityTracker {
    
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TrackerFrame frame = new TrackerFrame();
                
            }
        });        
    }
}
