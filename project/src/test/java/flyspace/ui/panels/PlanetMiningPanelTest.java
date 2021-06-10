package flyspace.ui.panels;

import flyspace.FlySpace;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.lwjgl.LWJGLException;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.system.Solar;
import solarex.ui.ImageCache;

/**
 * Starts the game and immediately shows the galactic map panel. This is a manual
 * test.
 * @author hjm
 */
public class PlanetMiningPanelTest extends TestCase
{
    private FlySpace game;
    
    static
    {
        try 
        {
            flyspace.LibraryPathExtender.addLibraryPath("lwjgl-2.9.1\\native\\windows");
            flyspace.LibraryPathExtender.addLibraryPath("lwjgl-2.9.1/native/linux");
        }
        catch (Exception ex) 
        {
            Logger.getLogger(FlySpace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void setUp()
    {
        game = new FlySpace();
        try 
        {
            game.createGL();
            game.createPanels();
        } 
        catch (LWJGLException ex) 
        {
            Logger.getLogger(PlanetMiningPanelTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(PlanetMiningPanelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void tearDown()
    {
        game.destroyGL();
    }
    
    
    public void testPlanetMiningPanel()
    {
        ImageCache imageCache = new ImageCache();
        
        Galaxy galaxy = new Galaxy(imageCache.spiral.getImage());
        List <SystemLocation> list = galaxy.buildSector(-2, 0);
        SystemLocation loca = list.get(2);
        
        Solar system = game.changeSystem(loca);
        
        for(Solar body : system.children)
        {
            System.err.println(body.name);
        }
        
        // game.showPlanetMiningPanel(system.children.get(1));
        game.showPlanetMiningPanel(system.children.get(1).children.get(3));
        game.run();
        
    }
    
}
