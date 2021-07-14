package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.Trigger;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import solarex.ship.Ship;
import solarex.system.Solar;

/**
 *
 * @author Hj. Malthaner
 */
public class ProspectorsAgencyPanel extends DecoratedUiPanel
{
    /**
     * Station good storage
     */
    private Solar station;
    private final FlySpace game;
    private final Ship ship;
    
    private boolean clicked;
    private final DecoratedTrigger loungeTrigger;
    
    public ProspectorsAgencyPanel(FlySpace game, Ship ship) 
    {
        this.game = game;
        this.ship = ship;

        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", 
                Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        
        loungeTrigger.setArea(934, 200, 185, 24);
        addTrigger(loungeTrigger);
    }

    public void setStation(Solar station)
    {
        this.station = station;
    }
    
    
    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
    }

    @Override
    public void handleInput() 
    {
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        
        if(Mouse.isButtonDown(0) == false)
        {
            if(clicked)
            {
                int mx = Mouse.getX();
                int my = Mouse.getY();
                
                Trigger t = trigger(mx, my);
                
                if(t == loungeTrigger)
                {
                    game.showStationPanel();
                }
                
                clicked = false;
            }
        }
    }

    @Override
    public void display() 
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        displayBackground(0xFF331100, 0xFF442211, 0xFF553311);

        displayTitle(station.name + " Prospectors Office");
        
        Fonts.g32.drawStringScaled("The prospectors office is currently closed.", Colors.TEXT, 250, 500, 0.8f);

        
        displayTriggers();
        
    }
}
