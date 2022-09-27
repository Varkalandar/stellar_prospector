/*
 * GalacticMapPanel.java
 *
 * Created: ??-May-2015
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.Space;
import flyspace.SystemBuilder;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import static flyspace.ui.UiPanel.drawRoundRect;
import static flyspace.ui.UiPanel.fillCircle;
import static flyspace.ui.UiPanel.fillRect;
import java.util.List;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.ship.Ship;
import solarex.ship.Ship.State;
import solarex.system.Solar;


/**
 * Galactic map display.
 * 
 * @author Hj. Malthaner
 */
public class GalacticMapPanel extends DecoratedUiPanel
{
    /** Player ship, used for location */
    private final Ship ship;

    private final int gridColor = 0xFF002255;
    private final int mapBackgroundColor = 0xFF000911;
    private final int hyperRangeColor = 0xFF001224;
    private final int colorBrown = 0xFFCC5000;
    
    private int zoom;
    private int centerX = -300;
    private int centerY = 0;

    private int dragStartX;
    private int dragStartY;
    private int dragCenterX;
    private int dragCenterY;

    private final Galaxy galaxy;

    private String systemDistance;
    private boolean clicked;
    private final DecoratedTrigger jumpTrigger;
    private final FlySpace game;
    
    public GalacticMapPanel(FlySpace game, Galaxy galaxy, final Ship ship)
    {
        super(null);

        this.game = game;
        this.galaxy = galaxy;
        this.ship = ship;
        
        zoom = 2;
        
        jumpTrigger = new DecoratedTrigger(Fonts.g17, "Engage Jumpdrive", 
                Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        
        jumpTrigger.setArea(990, 190, 180, 32);
        
        addTrigger(jumpTrigger);
    }

    
    /*
     * Each galactical sector contains up to 6 solar systems
     * @author Hj. Malthaner
     */
    private void showGalacticalSector(
            final int xpos,
            final int ypos,
            final int i,
            final int j)
    {
        List <SystemLocation> list = galaxy.buildSector(i, j);

        if(zoom < 12) 
        {
            int size = (512 + zoom - 1) / zoom;
            fillBorder(xpos, ypos, size, size, 1, gridColor);
            Fonts.c9.drawString("" + i + "/" + j, Colors.GRAY, xpos+2, ypos+size-32);
        }
        
        final int anz = list.size();
        for (int n = 0; n < anz; n++) 
        {
            SystemLocation loca = list.get(n);

            final int x = xpos + loca.ioff * 4 / zoom;
            final int y = ypos + loca.joff * 4 / zoom;

            Solar system = SystemBuilder.create(loca, false);

            final int size = 1+Math.max((system.radius >> 19) / zoom, 2);
            int sunColor;
            
            switch(system.stype) 
            {
                case S_YELLOW:
                    sunColor = 0xFFEEDD22;
                    break;
                case S_ORANGE:
                    sunColor = 0xFFEE8822;
                    break;
                case S_WHITE_DWARF:
                    sunColor = Colors.WHITE;
                    break;
                case S_BLUE_GIANT:
                    sunColor = 0xFFCCEEFF;
                    break;
                case S_RED_GIANT:
                    sunColor = 0xFFCC2211;
                    break;
                case S_NEUTRON:
                    sunColor = Colors.DARK_GRAY;
                    break;
                case S_BLACK_HOLE:
                    sunColor = Colors.GRAY; // Hajo: outline
                    break;
                case S_BROWN_DWARF:
                    sunColor = colorBrown;
                    break;
                default:
                    sunColor = Colors.YELLOW;
            }

            drawSun(x, y, size, system.stype, sunColor);

            // Hajo: in low zoom modes, show names
            if(zoom < 8) 
            {
                final int margin = Math.max(size/2+4, 12);
                final int bw = Fonts.c9.getStringWidth(system.name) + 12;
                
                if(ship.loca.equals(loca))
                {
                    drawRoundRect(x-margin-4, y-margin-1, margin * 2 + bw, margin*2, Colors.YELLOW);
                }

                if(loca.equals(ship.hyperjumpDestination))
                {
                    drawRoundRect(x-margin-4, y-margin-1, margin * 2 + bw, margin*2, Colors.GREEN);
                }

                Fonts.c9.drawString(system.name, Colors.WHITE, x + size/2 + 4, y - 23);
                
                if(clicked && Mouse.isButtonDown(0) == false)
                {
                    int mx = Mouse.getX();
                    int my = Mouse.getY();
                    
                    if(mx >= x-margin-4 && mx < x+margin+bw && my >= y-margin-1 && my <= y+margin)
                    {
                        if(ship.hyperjumpDestination != null && ship.hyperjumpDestination.equals(loca))
                        {
                            game.showDistantSystemInfoPanel(loca);
                        }
                        else
                        {
                            setHyperjumpDestination(loca);
                        }
                    }
                }
            } 
            else 
            {
                final int margin = 9;

                if(ship.loca.equals(loca))
                {
                    drawRoundRect(x-margin-4, y-margin-2, 100, margin*2, Colors.YELLOW);
                }

                if(loca.equals(ship.hyperjumpDestination))
                {
                    drawRoundRect(x-margin-4, y-margin-2, 100, margin*2, Colors.GREEN);
                }
            }
            
        }
    }

    
    private void drawSun(int x, int y, int size, Solar.SunType stype, int color)
    {
        if(stype == Solar.SunType.S_BLACK_HOLE)
        {            
            drawCircle(x, y, (size+1)/2, color);
        }
        else
        {
            fillCircle(x, y, size/2, color, 1.0f/255f);
        }
    }

    
    private void drawHyperJumpRange(int width, int height)
    {
        final int maxRange = (int)(ship.equipment.getEffectiveDriveRange(ship.getCurrentMass())*512.0/(zoom*10));
        
        final int x = width / 2 + (ship.loca.galacticSectorI * 128 - 64 - centerX) * 4 / zoom;
        final int y = height / 2 + (ship.loca.galacticSectorJ * 128 - 64 - centerY) * 4 / zoom;
        
        final int shipX = x + ship.loca.ioff * 4 / zoom;
        final int shipY = y + ship.loca.joff * 4 / zoom;
        
        fillCircle(shipX, shipY, maxRange, hyperRangeColor);
        drawCircle(shipX, shipY, maxRange, 0x33FFFFFF);
    }            
    
    /*
    private void openSystem(int x, int y, int clicks)
    {
        final int n = systemInfoList.size();

        for(int i=0; i<n; i++)
        {
            SystemInfo inf = systemInfoList.get(i);

            final int dx = inf.xpos - x;
            final int dy = inf.ypos - y;

            if(Math.abs(dx) < 10 && Math.abs(dy) < 10)
            {
                // Hajo: System clicked.
                
                // System.err.println("Seed = " + inf.loca.systemSeed);
                
                if(inf.loca.equals(ship.loca)) 
                {
                    ship.hyperjumpDestination = null;
                }
                else 
                {
                    ship.hyperjumpDestination = inf.loca;
                    
                    double maxRange = ship.equipment.getEffectiveDriveRange(ship.getCurrentMass());
                    
                    Solar system = new Solar(inf.loca, false);
                    double d = ship.loca.distance(inf.loca);
                    
                    systemDistance = 
                            system.name + 
                            ", distance " + (((int)(d*100))/100.0) + " ly, " + 
                            "max drive range " + (((int)(maxRange*100))/100.0) + " ly";
                }

                if(clicks == 2) 
                {
                    showSystemCallback.showSystem(inf.loca);
                }
                
                break;
            }
        }
    }
    */
    
    public final void updateControls()
    {
        SystemLocation there = ship.hyperjumpDestination;
        boolean ok = false;
        
        if(there != null)
        {
            SystemLocation here = ship.loca;
            
            double sectorDistance = there.distance(here);

            ok = ship.equipment.getEffectiveDriveRange(ship.getCurrentMass()) >= sectorDistance;
        }
        
        jumpTrigger.setEnabled(ok && ship.getState() != State.DOCKED);
    }

    
    @Override
    public void activate()
    {
        glMatrixMode(GL_PROJECTION); 
        glLoadIdentity(); 

	glOrtho(0, width, 0, height, 1, -1);
        
        glMatrixMode(GL_MODELVIEW); 
        glLoadIdentity(); 
        glViewport(0, 0, width, height);
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        // Hajo: clear wheel history
        Mouse.getDWheel();        
    }

    
    @Override
    public void handlePanelInput()
    {
        int mx = Mouse.getX();
        int my = Mouse.getY();
        
        if(Mouse.isButtonDown(0))
        {
            if(!clicked)
            {
                dragStartX = mx;
                dragStartY = my;
                dragCenterX = centerX;
                dragCenterY = centerY;
            }
            else
            {
                int dx = dragStartX - mx;
                int dy = dragStartY - my;

                centerX = dragCenterX + dx*zoom / 2;
                centerY = dragCenterY + dy*zoom / 2;
            }
            
            clicked = true;
        }
        
        int u = Mouse.getDWheel();

        if(u > 0 && zoom > 1) 
        {
            zoom --;
        }
        
        if(u < 0 && zoom < 40) 
        {
            zoom ++;
        }

        updateControls();
        
        if(Mouse.isButtonDown(0))
        {
            Trigger t = trigger(mx, my);
        
            if(t == jumpTrigger)
            {
                if(ship.hyperjumpDestination != null)
                {
                    performHyperjump();
                }
            }
        }
    }

    
    @Override
    public void displayPanel()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        drawHyperJumpRange(width, height);
        
        for (int y = (centerY/128) - zoom * 2 - 1; y <= (centerY/128) + zoom * 2 + 1; y++) 
        {
            for (int x = (centerX/128) - zoom * 3 -1; x <= (centerX/128) + zoom * 3 + 1; x++) 
            {
                showGalacticalSector(
                        width / 2 + (x * 128 - 64 - centerX) * 4 / zoom,
                        height / 2 + (y * 128 - 64 - centerY) * 4 / zoom,
                        x,
                        y);
            }
        }        

        PixFont font = Fonts.g12;
        
        fillBorder(10, height-70, 410, 48, 1, Colors.GRAY);
        fillRect(11, height-69, 408, 46, 0x99001020);

        Fonts.g32.drawStringBold("Galactic Map", Colors.FIELD, 126, height - 64, 0.9f);

        final int left = width - 430;

        fillBorder(left+10, height-70, 410, 48, 1, Colors.GRAY);
        fillRect(left+11, height-69, 408, 46, 0x99001020);
        
        fillBorder(10, 180, 310, 39, 1, Colors.GRAY);
        fillRect(11, 181, 308, 37, 0x99001020);

        Fonts.c9.drawString("Drag map by mouse. Zoom with mouse wheel.", Colors.CYAN, 18, 185);
        Fonts.c9.drawString("Click system to set hyperjump destination. Double click to inspect.", Colors.CYAN, 18, 170);

        font.drawString("Map sector:", Colors.GREEN, left+18, height-59);
        font.drawString("Jump dest.:", Colors.GREEN, left+18, height-79);

        font.drawString(
                (centerX/128) + " ' " + (centerX & 127) +
                " / " +
                (centerY/128) + " ' " + (centerY & 127) +
                "", Colors.WHITE, left+100, height-59);

        if(ship.hyperjumpDestination == null) 
        {
            font.drawString("Not set", Colors.WHITE, left+100, height-79);
        } 
        else 
        {
            font.drawString(systemDistance, Colors.WHITE, left+100, height-79);
        }

        displayTriggers();

        if(clicked && Mouse.isButtonDown(0) == false)
        {
            clicked = false;
        }

    }

    
    private void setHyperjumpDestination(SystemLocation loca) 
    {
        if(loca.equals(ship.hyperjumpDestination))
        {
            game.showDistantSystemInfoPanel(loca);
        }
                
        ship.hyperjumpDestination = loca;
        double maxRange = ship.equipment.getEffectiveDriveRange(ship.getCurrentMass());

        Solar system = SystemBuilder.create(loca, false);
        double d = ship.loca.distance(loca);

        systemDistance = 
                system.name + 
                ", distance " + (((int)(d*100))/100.0) + " ly, " + 
                "max drive range " + (((int)(maxRange*100))/100.0) + " ly";
    }

    
    private void performHyperjump() 
    {
        ship.loca = ship.hyperjumpDestination;
        ship.hyperjumpDestination = null;
        ship.setState(Ship.State.FLIGHT);
        ship.spaceBodySeed = 0;

        // Hajo: stop moving after jump
        ship.destination.set(ship.pos);

        Solar system = game.changeSystem(ship.loca);

        // Hajo: give the ship a resonable return location.
        double  safeDistance;
        if(system.children.isEmpty())
        {
            // nothing here but a star
            // arrive outside the star radius.
            safeDistance = system.radius * 5;
        }
        else
        {
            // planets are present
            // arrive beyond last planet
            Solar outmostPlanet = system.children.get(system.children.size() - 1);
            safeDistance = outmostPlanet.orbit * 1.2;
        }
        
        double angle = Math.random() * Math.PI * 2;

        ship.pos.x = Math.cos(angle) * safeDistance;
        ship.pos.y = 0;
        ship.pos.z = Math.sin(angle) * safeDistance;
        ship.pos.scale(Space.DISPLAY_SCALE);
        
        ship.destination.set(ship.pos);
        
        // game.showSystemInfoPanel();
        game.showSpacePanel();
    }
}
