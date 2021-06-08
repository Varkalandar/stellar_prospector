/*
 * MiningPanelTest.java
 *
 * Created on 2012/11/06
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ui.panels;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import junit.framework.TestCase;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.ship.Ship;
import solarex.ui.ImageCache;

/**
 *
 * @author Hj. Malthaner
 */
public class GalaxyViewPanelTest extends TestCase
{
    public void testGalaxyViewPanel()
    {
        JDialog dialog = new JDialog((JFrame)null, "Galactical Map", true);
        
        ImageCache imageCache = new ImageCache();
        Galaxy galaxy = new Galaxy(imageCache.spiral.getImage());
        
        Ship ship = new Ship();
        ship.loca = new SystemLocation();
        
        GalaxyViewPanel galacticalMap = new GalaxyViewPanel(galaxy, ship, imageCache);
        dialog.add(galacticalMap, BorderLayout.CENTER);

        dialog.setSize(1000, 740-64);
        dialog.setVisible(true);
    }
}
