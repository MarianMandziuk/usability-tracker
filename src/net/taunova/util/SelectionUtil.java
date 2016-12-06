package net.taunova.util;

import java.awt.Point;
/**
 * Created by maryan on 06.12.16.
 */
public class SelectionUtil {
    public static boolean isPointInWindowBoundary(Point p,
                                           int windowWidth, int windowHeight) {
        if (p.x > windowWidth
                || p.x < 0
                || p.y > windowHeight
                || p.y < 0) {
            return false;
        }
        return true;
    }

    public static void setCorrectPointBoundary(Point p,
                                        int windowWidth, int windowHeight) {

        if (p.x > windowWidth) {
            p.x = windowWidth - 1;
        } else if (p.x < 0) {
            p.x = 0;
        }

        if (p.y > windowHeight) {
            p.y = windowHeight - 1;
        } else if (p.y < 0)  {
            p.y = 0;
        }
    }

    public static boolean isRemainderHigher(final double number) {
        double remainder = number - (int)(number);
        return remainder > 0.4;
    }
}
