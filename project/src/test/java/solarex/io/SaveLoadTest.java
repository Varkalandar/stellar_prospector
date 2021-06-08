package solarex.io;

import junit.framework.TestCase;
import solarex.galaxy.SystemLocation;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.ship.components.EquipmentFactory;
import solarex.ship.components.ShipComponent;

/**
 *
 * @author Hj. Malthaner
 */
public class SaveLoadTest extends TestCase
{

    public void testSaveLoad()
    {
        EquipmentFactory factory = new EquipmentFactory();
        ShipComponent drone = factory.create(EquipmentFactory.Component.DRILLDOWN_DYNAMOS);
        
        Ship ship = new Ship();
        ship.equipment.addComponent(drone);
        ship.cargo.goods[Good.Type.Hydrogen.ordinal()].units = 10; 
        ship.loca = new SystemLocation();
        
        LoadSave loadSave = new LoadSave();
        
        try
        {
            loadSave.saveGame(ship);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            assertNull("Exception during saving test: " + ex, ex);
        }

        try
        {
            loadSave.loadGame(ship);
            
            assertEquals("Drive Durability", 1000, ship.equipment.components.get(0).getCurrentDurability());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            assertNull("Exception during loading test: " + ex, ex);
        }
    }
            
}
