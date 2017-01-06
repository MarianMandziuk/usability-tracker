package net.taunova.util;

import java.awt.*;

/**
 * Created by maryan on 06.01.17.
 */
public class LengthUtil {

    public static float getLength(Point p1, Point p2) {
        float result;
        if (p1.x == p2.x) {
            result = Math.abs(p1.y - p2.y);
        } else if (p1.y == p2.y) {
            result = Math.abs(p1.x - p2.x);
        } else {
            if (p1.x > p2.x) {
                result = calculateLenght(p2, p1);
            } else {
                result = calculateLenght(p1, p2);
            }
        }
        return result;
    }

    private static float calculateLenght(Point p1, Point p2) {
        double lineXLen = p2.x - p1.x;
        double lineYLen = Math.abs(p1.y - p2.y);

        return (float) Math.sqrt(Math.pow(lineXLen, 2.0) + Math.pow(lineYLen, 2.0));
    }
}
