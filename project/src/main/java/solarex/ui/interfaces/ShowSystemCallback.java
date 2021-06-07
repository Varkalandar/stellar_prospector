/*
 * ShowSystemCallback.java
 *
 * Created: 13-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.interfaces;

import solarex.galaxy.SystemLocation;
import solarex.system.Solar;

/**
 *
 * @author Hj. Malthaner
 */
public interface ShowSystemCallback 
{

    public void showSystem(SystemLocation loca);
    
    /**
     * Create star system.
     * 
     * @param loca the system location
     * @return The newly created system
     */
    public Solar makeSystem(SystemLocation loca);
    
    public void switchToNavigationView();    
}
