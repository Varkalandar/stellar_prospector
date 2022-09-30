package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.MeshFactory;
import flyspace.StringUtils;
import flyspace.ogl.Mesh;
import flyspace.ogl.Texture;
import flyspace.ogl.TextureCache;
import flyspace.ui.Colors;
import flyspace.ui.Fonts;
import flyspace.ui.Trigger;
import flyspace.ui.UiPanel;
import static flyspace.ui.UiPanel.fillRect;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import flyspace.ui.Mouse;
import flyspace.ui.Sounds;
import static org.lwjgl.opengl.GL11.*;
import solarex.ship.Ship;
import solarex.util.ClockThread;

/**
 *
 * @author Hj. Malthaner
 */
public class CockpitPanel extends UiPanel
{
    public static final int HEIGHT = 160;
    
    
    private final FlySpace game;
    private final Texture cockpit;
    private final Ship ship;
    private final FloatBuffer lightPos;
    private final Mesh shipMesh;
    private boolean clicked;
    private final Trigger sysInfoTrigger;
    private final Trigger sysMapTrigger;
    private final Trigger galMapTrigger;
    private final Trigger spaceTrigger;
    private final Trigger optionsTrigger;
    private final Trigger shipTrigger;
    
    public CockpitPanel(FlySpace game, Ship ship) throws IOException
    {
        super(null);
        
        this.game = game;
        this.ship = ship;
        // this.shipMesh = MeshFactory.createShip();

        URL url = getClass().getResource("/flyspace/resources/3d/ship.obj");        
        this.shipMesh = MeshFactory.createMesh(url);
        
        
        lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(0.0f).put(500.0f).put(100.0f).put(0.0f).flip();
        
        cockpit = TextureCache.loadTexture("/flyspace/resources/cockpit.jpg");
        
        sysInfoTrigger = new Trigger();
        sysInfoTrigger.setArea(50, 15, 170, 50);
        
        // sysMapTrigger = new DecoratedTrigger(Fonts.g17, "Test", 0x22FFFFFF, 0x77FFFFFF);
        sysMapTrigger = new Trigger();
        sysMapTrigger.setArea(110, 66, 160, 45);
        
        // galMapTrigger = new DecoratedTrigger(Fonts.g24, "Test", 0x22FFFFFF, 0x77FFFFFF);
        galMapTrigger = new Trigger();
        galMapTrigger.setArea(152, 110, 145, 40);

        // spaceTrigger = new DecoratedTrigger(Fonts.g24, "Test", 0x22FFFFFF, 0x77FFFFFF);
        spaceTrigger = new Trigger();
        spaceTrigger.setArea(330, 15, 530, 130);

        // optionsTrigger = new DecoratedTrigger(Fonts.g17, "Options", 0x77224466, 0x77337799);
        optionsTrigger = new Trigger();
        optionsTrigger.setArea(1050, 108, 130, 35);

        // shipTrigger = new DecoratedTrigger(Fonts.g17, "Ship", 0x77224466, 0x77337799);
        shipTrigger = new Trigger();
        shipTrigger.setArea(884, 108, 130, 35);

        addTrigger(sysInfoTrigger);
        addTrigger(sysMapTrigger);
        addTrigger(galMapTrigger);
        addTrigger(spaceTrigger);
        addTrigger(optionsTrigger);
        addTrigger(shipTrigger);
    }
    
    
    @Override
    public void activate() 
    {
    }

    
    @Override
    public void handlePanelInput() 
    {
        int mx = Mouse.getX();
        int my = Mouse.getY();
        
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }

        if(Mouse.isButtonDown(0) == false && clicked)
        {
            clicked = false;
            Trigger t = trigger(mx, my);
            
            if(t == sysInfoTrigger)
            {
                game.playSound(Sounds.CLICK, 1f);
                game.showSystemInfoPanel();
            }
            else if(t == sysMapTrigger)
            {
                game.playSound(Sounds.CLICK, 1f);
                game.showSystemMapPanel();
            }
            else if(t == galMapTrigger)
            {
                game.playSound(Sounds.CLICK, 1f);
                game.showGalacticMapPanel();
            }
            else if(t == spaceTrigger)
            {
                if(ship.getState() == Ship.State.DOCKED)
                {
                    game.playSound(Sounds.CLICK, 1f);
                    game.showStationPanel();
                }
                else
                {
                    game.playSound(Sounds.CLICK, 1f);
                    game.showSpacePanel();
                }
            }
            else if(t == optionsTrigger)
            {
                game.playSound(Sounds.CLICK, 1f);
                game.showOptionsPanel();
            }
            else if(t == shipTrigger)
            {
                game.playSound(Sounds.CLICK, 1f);
                game.showShipPanel();
            }
        }
    }

    @Override
    public void displayPanel() 
    {
        glMatrixMode(GL_PROJECTION); 
        glLoadIdentity(); 

	glOrtho(0, width, 0, height, 100, -100);
        
        glMatrixMode(GL_MODELVIEW); 
        glLoadIdentity(); 
        glViewport(0, 0, width, height);
        
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        
        glBindTexture(GL_TEXTURE_2D, cockpit.id);
        
        int x = 0;
        int y = 0;
        
        glBegin(GL_QUADS);
        
        glColor3f(0.99f, 0.99f, 0.99f);

        glTexCoord2f(0.0f, 1.0f);
        glVertex2i(x, y);

        glTexCoord2f(1.0f, 1.0f);
        glVertex2i(x+width, y);

        glTexCoord2f(1.0f, 0.0f);
        glVertex2i(x+width, y+HEIGHT);

        glTexCoord2f(0.0f, 0.0f);
        glVertex2i(x, y+HEIGHT);

        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        
        int stateX = 17;
        int stateW;
        int statusY = 145;


        String shipState;
        
        if(null == ship.getState())
        {
            shipState = "Unknown";
        }
        else switch (ship.getState()) 
        {
            case DOCKED:
                shipState = "Docked at " + ship.loca.name;
                break;
            case ORBIT:
                shipState = "Orbiting " + ship.loca.name;
                break;
            case FLIGHT:
                shipState = "In flight";
                break;
            default:
                shipState = "Unknown";
                break;
        }

        int chars = shipState.length();
        stateW = 70 + 10 + chars + Fonts.c9.getStringWidth(shipState);
        
        fillBorder(stateX, y+statusY, stateW, 14, 1, 0x22FFFFFF);
        fillRect(stateX+1, y+statusY+1, stateW-2, 14-2, 0x99000000);
        Fonts.c9.drawStringScaled("Ship state:", Colors.LABEL, stateX + 10, y + statusY-16, 1.0f, 1);
        Fonts.c9.drawStringScaled(shipState, Colors.FIELD, stateX + 70, y + statusY-16, 1.0f, 1);
        
        final int year = ClockThread.getYear();
        final int month = ClockThread.getMonthOfYear() + 1;
        final int day = ClockThread.getDayOfMonth() + 1;
        final int hour = ClockThread.getHourOfDay();
        final int minute = ClockThread.getMinuteOfHour();

        String timeString =
                "" +
                StringUtils.format10(hour) + ":" + 
                StringUtils.format10(minute);
        
        String dateString = 
                year + "/" + 
                StringUtils.format10(month) + "/" + 
                StringUtils.format10(day);

        int timeX = width - 125;
        fillBorder(timeX, y+statusY, 108, 14, 1, 0x22FFFFFF);
        fillRect(timeX+1, y+statusY+1, 108-2, 14-2, 0x99000000);
        Fonts.c9.drawStringScaled(timeString, Colors.LABEL, timeX + 10, y + statusY-16, 1.0f, 1);
        Fonts.c9.drawStringScaled(dateString, Colors.LABEL, timeX + 43, y + statusY-16, 1.0f, 1);

        glBindTexture(GL_TEXTURE_2D, 0);

        // glBlendFunc(GL_SRC_COLOR, GL_DST_COLOR);

        glEnable(GL_LIGHTING);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
        
        glEnable(GL_NORMALIZE);
        
        glPushMatrix();
        glTranslatef(width/2, 80, -10);
        glScalef(20, 20, 20);
        glRotatef(20, 1, 0, 0);
        
        shipMesh.setAngleY(shipMesh.getAngleY() + 1);
        shipMesh.display();
        glPopMatrix();

        glDisable(GL_NORMALIZE);
        glDisable(GL_LIGHTING);
        
        displayTriggers();
    }
}
