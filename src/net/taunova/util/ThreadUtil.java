/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
