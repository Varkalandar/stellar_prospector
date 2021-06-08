/*
 * PlanetViewPanelTest.java
 *
 * Created on 2012/11/06
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import flyspace.SystemBuilder;
import javax.swing.JDialog;
import javax.swing.JFrame;
import junit.framework.TestCase;
import solarex.galaxy.SystemLocation;
import solarex.ship.Player;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.ImageCache;

/**
 *
 * @author Hj. Malthaner
 */
public class PlanetViewPanelTest extends TestCase
{
    public void testPanelViewPanel()
    {
        JDialog frame = new JDialog((JFrame)null, "Planet View Panel Test", true);

        ImageCache imageCache = new ImageCache();

        Player player = new Player();
        Ship ship = new Ship();
        ship.player = player;
        
        PlanetDetailPanel planetViewPanel = new PlanetDetailPanel(ship, imageCache);
        frame.add(planetViewPanel);
        
        SystemLocation loca = new SystemLocation();
        loca.systemNumber = 1;
        loca.systemSeed = 1;
        Solar system = SystemBuilder.create(loca, true);
        
        planetViewPanel.update(system.children.get(2));
        
        frame.setSize(1000, 740-64);
        frame.setVisible(true);
    }
}
