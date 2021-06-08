package flyspace.ui.panels;

import flyspace.FlySpace;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.lwjgl.LWJGLException;

/**
 * Starts the game and immediately shows the galactic map panel. This is a manual
 * test.
 * @author hjm
 */
public class GalacticMapPanelTest extends TestCase
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
            Logger.getLogger(GalacticMapPanelTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(GalacticMapPanelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void tearDown()
    {
        game.destroyGL();
    }
    
    
    public void testProspectorAgencyPanel()
    {
        game.showGalacticMapPanel();
        game.run();
        
    }
    
}
