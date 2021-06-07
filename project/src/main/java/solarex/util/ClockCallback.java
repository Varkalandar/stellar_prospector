/*
 * ClockCallback.java
 *
 * Created: 11-Feb-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.util;

/**
 * Clock callback interface.
 * 
 * @author Hj. Malthaner
 */
public interface ClockCallback
{
    /**
     * Will be called roughly every 100ms
     * 
     * @param deltaT the real time passed since last call.
     */
    public void ping100(int deltaT);
}
