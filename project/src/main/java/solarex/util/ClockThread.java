/*
 * ClockThread.java
 *
 * Created: 11-Feb-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A clock that allows callbacks, will call back ever 100ms.
 *
 * @author Hj. Malthaner
 */
public class ClockThread extends Thread 
{
    private static final Logger LOGGER = Logger.getLogger(ClockThread.class.getName());

    private final ArrayList <ClockCallback> callbacks;
    private volatile boolean go = true;

    private static int gameTicks = 0;

    public static int gameTimeInMinutes()
    {
        return gameTicks >> 3;
    }

    public static int getMinuteOfHour()
    {
        return gameTimeInMinutes() % 60;
    }

    public static int getHourOfDay()
    {
        int time = gameTimeInMinutes();
        time /= 60;
        return time % 24;
    }

    public static int getDayOfGame()
    {
        int time = gameTimeInMinutes();
        time /= 60;
        time /= 24;
        return time;
    }

    public static int getDayOfMonth()
    {        
        return getDayOfGame() % 30;
    }

    public static int getMonthOfYear()
    {
        int time = gameTimeInMinutes();
        time /= 60;
        time /= 24;
        time /= 30;
        return time % 12;
    }

    public static int getYear()
    {
        int time = gameTimeInMinutes();
        time /= 60;
        time /= 24;
        time /= 30;
        time /= 12;
        return time + 2160;
    }

    public synchronized void addCallback(ClockCallback cb)
    {
        callbacks.add(cb);
    }

    public synchronized void removeCallback(ClockCallback cb)
    {
        callbacks.remove(cb);
    }


    @Override
    public void run()
    {
        long time = System.currentTimeMillis();

        while(go) 
        {
            long newTime = System.currentTimeMillis();
            int delta = (int)(newTime - time);
            time = newTime;
            
            // System.err.println("delta = " + delta);
            
            for(ClockCallback cb : callbacks) 
            {
                try 
                {
                    cb.ping100(delta);
                }
                catch(Exception ex) 
                {
                    LOGGER.log(Level.WARNING, ex.getMessage(), ex);
                }
            }
            
            int delay = (int)(System.currentTimeMillis() - time);
            
            safeSleep(100 - delay);
            
            gameTicks ++;
        }
    }

    public ClockThread()
    {
        setDaemon(true);
        callbacks = new ArrayList();
    }

    private void safeSleep(final int delay)
    {
        if(delay > 0)
        {
            try 
            {
                sleep(delay);
            }
            catch(InterruptedException ex) 
            {
                LOGGER.log(Level.INFO, ex.getMessage(), ex);
            }
        }
    }
}
