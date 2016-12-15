package net.taunova.util;

/**
 *
 * @author maryan
 */
public class ThreadUtil {
    public static final void sleep(int millis) {
        try{
            Thread.sleep(millis); //this will slow the capture rate to 0.1 seconds
        }catch(Exception e) {
            //...
        }        
    }
}
