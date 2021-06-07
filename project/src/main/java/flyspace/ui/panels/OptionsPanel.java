package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.Trigger;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import solarex.ship.Ship;

/**
 *
 * @author Hj. Malthaner
 */
public class OptionsPanel extends DecoratedUiPanel
{
    private final FlySpace game;
    private final Ship ship;
    
    private boolean clicked;
    private final DecoratedTrigger loungeTrigger;
    private final DecoratedTrigger loadTrigger;
    private final Trigger saveTrigger;
    
    public OptionsPanel(FlySpace game, Ship ship) 
    {
        this.game = game;
        this.ship = ship;

        int left = (1200 - 185)/2;
        int yOff = 600;
        
        loadTrigger = new DecoratedTrigger(Fonts.g17, "Load Game", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        loadTrigger.setArea(left, yOff, 185, 24);
        addTrigger(loadTrigger);
        yOff -=32;
        
        saveTrigger = new DecoratedTrigger(Fonts.g17, "Save Game", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        saveTrigger.setArea(left, yOff, 185, 24);
        addTrigger(saveTrigger);
        yOff -=32;
        
        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        loungeTrigger.setArea(934, 200, 185, 24);
        addTrigger(loungeTrigger);
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
                else if(t == loadTrigger)
                {
                    game.loadGame();
                }
                else if(t == saveTrigger)
                {
                    game.saveGame();
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
        displayBackground(0xFF220000, 0xFF330000, 0xFF440000);

        displayTitle("Options");
        
        displayTriggers();
    }
}
