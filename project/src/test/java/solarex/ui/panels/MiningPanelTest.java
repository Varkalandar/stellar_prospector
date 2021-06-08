/*
 * MiningPanelTest.java
 *
 * Created on 2012/11/05
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
import solarex.ship.Ship;
import solarex.ship.components.EquipmentFactory;
import solarex.ship.components.ShipComponent;
import solarex.system.Solar;
import solarex.ui.ImageCache;

/**
 *
 * @author Hj. Malthaner
 */
public class MiningPanelTest extends TestCase
{
    @Override
    public void setUp()
    {
        
    }
    
    public void testMiningPanel()
    {
        EquipmentFactory factory = new EquipmentFactory();
        
        ImageCache imageCache = new ImageCache();
        ShipComponent drone = factory.create(EquipmentFactory.Component.DRILLDOWN_DYNAMOS);

        JDialog dialog = new JDialog((JFrame)null, "Mining Panel Test", true);
        
        Ship ship = new Ship();

        SystemLocation loca = new SystemLocation();
        loca.systemNumber = 1;
        loca.systemSeed = 1;
        Solar system = SystemBuilder.create(loca, true);
        
        dialog.add(new MiningPanel(system.children.get(2), ship, imageCache, drone, null));
        
        dialog.setTitle(TitlePanel.titleBase);
        dialog.setSize(1000, 740-64);
        dialog.setVisible(true);
    }
}
