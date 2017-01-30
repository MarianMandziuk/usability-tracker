package net.taunova.control.listeners;

import net.taunova.trackers.MouseTracker;
import net.taunova.util.Grid;
import net.taunova.util.Hexagon;
import net.taunova.util.Position;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

import static net.taunova.util.LengthUtil.getLength;

/**
 * Created by maryan on 26.01.17.
 */
public class HexagonSizeListener implements ChangeListener{
    private MouseTracker tracker;

    public HexagonSizeListener(MouseTracker tracker) {
        this.tracker = tracker;
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int radius = (int)source.getValue();
            this.tracker.grid = new Grid(Toolkit.getDefaultToolkit().getScreenSize().width,
                    Toolkit.getDefaultToolkit().getScreenSize().height, radius, 3);
            Hexagon trackRedHexagon = null;
            Position priv = null;
            float length = 0;
            float privLen = 0;
            List<Position> positionList = this.tracker.getPosition();
            for(int i = 0;i < positionList.size(); i++) {
                if(priv != null)
                    length = getLength(priv.position, positionList.get(i).position);

                for (Hexagon h : this.tracker.grid.hexagons) {
                    if (h.hexagon.contains(positionList.get(i).position.x, positionList.get(i).position.y)) {
                        if (positionList.get(i).getDelay() > 10) {
                            h.setColor(new Color(255, 0, 0, 40));
                            trackRedHexagon = h;
                        }

                        if((trackRedHexagon == null
                                || !trackRedHexagon.hexagon.contains(positionList.get(i).position.x, positionList.get(i).position.y))) {
                            if (length > privLen) {
                                h.setColor(new Color(255, 255, 0, 40));
                            } else if (length < privLen) {
                                h.setColor(new Color(0, 255, 0, 40));
                            }
                        }
                    }
                }


                priv = positionList.get(i);
                privLen = length;
            }
        }
    }
}
