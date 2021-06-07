/*
 * TabularSystemPanel.java
 *
 * Created: 10-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
 

package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.MultiMesh;
import flyspace.Space;
import flyspace.ogl32.ShaderBank;
import flyspace.ui.Colors;
import flyspace.ui.Fonts;
import flyspace.ui.PixFont;
import static flyspace.ui.UiPanel.fillBorder;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.ImageCache;


/**
 * Tabular system info panel.
 * 
 * @author Hj. Malthaner
 */
public class SystemInfoPanel extends DecoratedUiPanel
{
    private static Point right = new Point (1, 0);
    private static Point up = new Point (0, 1);
    
    private Solar system;
    private Space space;
    
    /** The players ship */
    private Ship ship;

    /**
     * Maximum coordinates reached while laying out the system.
     */
    private int maxX, maxY;

    private double globalScale;
    private boolean clicked;
    private final FlySpace game;
    
    private Solar selectedBody;
    private int bestSelectionDist;
    private Rectangle selectionFrame = new Rectangle();
    
    private int scale(Solar body)
    {
        return (int)(Math.max(Math.pow(body.radius, 0.5)/3.2, 4) * globalScale);
    }


    private void layoutBody(final Solar body,
                            final int startX, final int startY, int spacing)
    {
        // Hajo: Handle starports
        if(body.children.size() > 0) 
        {
            Solar mightBeStarport = body.children.get(0);
            if(mightBeStarport.btype == Solar.BodyType.SPACEPORT) 
            {
                layoutBody(mightBeStarport, startX, startY, spacing);
            }
        }

        // Hajo: Now lay out the planet
        int bodyX, bodyY, bodyW, bodyH;
        int textColor = 0xFFFFFFFF;

        int scale = scale(body);
        final int w = Math.max(scale, spacing);
        final int h = Math.max(scale+24, spacing);

        if(body.btype == Solar.BodyType.SPACEPORT ||
           body.btype == Solar.BodyType.STATION) 
        {
            textColor = Colors.ORANGE;
        }

        if(body.btype == Solar.BodyType.SUN) 
        {
            // Hajo: accretiation disk is much wider than the black hole itself.
            if(body.stype == Solar.SunType.S_BLACK_HOLE)
            {
                bodyW = 254;
                bodyH = 63;
                
                bodyX = startX;
                bodyY = startY+h/4-4;
                
            }
            else
            {
                bodyW = scale;
                bodyH = scale;
                
                bodyX = startX;
                bodyY = startY;
            }
        } 
        else if(body.btype == Solar.BodyType.PLANET) 
        {
            if(body.ptype == Solar.PlanetType.RINGS)
            {
                // Hajo: ringed planets must be shown wider than usual

                bodyW = scale*20/16;
                bodyH = scale*17/16;
                
                bodyX = startX;
                bodyY = startY;
            }
            else
            {
                bodyW = scale;
                bodyH = scale;
                
                bodyX = startX;
                bodyY = startY;
            }
        }
        else if(body.btype == Solar.BodyType.SPACEPORT) 
        {
            bodyW = scale;
            bodyH = scale;

            bodyX = startX;
            bodyY = startY;
        } 
        else 
        {
            bodyW = scale;
            bodyH = scale;

            bodyX = startX;
            bodyY = startY;
        }

        if(ship.spaceBodySeed == body.seed)
        {
            textColor = Colors.YELLOW;
        }

        drawSolarBody(body, bodyX, bodyY, bodyW, bodyH, body.name, textColor, spacing);

        // Hajo: track maximum coordinates

        int mx = startX + bodyW;
        int my = startY + bodyH;

        if(mx > maxX) 
        {
            maxX = mx;
        }
        if(my > maxY) 
        {
            maxY = my;
        }
    }


    private void layoutChildren(final Solar system,
                                final Point dir,
                                int startX, int startY,
                                final int spacing)
    {

        Point p = new Point(startX, startY);

        ArrayList <Solar> bodies = system.children;

        for(int i=0; i<bodies.size(); i++) 
        {
            Solar body = bodies.get(i);

            if(body.btype != Solar.BodyType.SPACEPORT) 
            {
                layoutBody(body, p.x, p.y, spacing);

                if(dir == up) 
                {
                    layoutChildren(body, right, p.x+spacing/2, p.y, spacing);
                }
                else 
                {
                    layoutChildren(body, up, p.x, p.y + spacing/2, spacing/2);
                }

                p.x += dir.x * spacing;
                p.y += dir.y * spacing;

                if(dir == right && maxX > p.x) 
                {
                    p.x = maxX;
                }
            }
        }
    }

    /**
     * Layout system tree in alternating right - up
     * movements
     * @param system
     */
    public void layoutSystem(Solar system)
    {
        // Hajo: init size tracking
        maxX = 0;
        maxY = 0;

        // Hajo: layout defaults
        int h = 520;
        int spacing = (int)(128 * globalScale);
        int bottom = 210;

        int scale = scale(system);
        Point cur;
        if(scale < spacing) 
        {
            cur = new Point(spacing, h - bottom);
        }
        else 
        {
            // Hajo: a very big sun. It must move to the left
            cur = new Point(spacing, h - bottom);
        }

        layoutBody(system, cur.x, cur.y, spacing);
        
        if(system.stype == Solar.SunType.S_BLACK_HOLE)
        {
            cur.x += 256;
        }
        
        layoutChildren(system, right, cur.x + Math.max(scale, spacing), h-bottom, spacing);
    }


    public SystemInfoPanel(FlySpace game, Ship ship, ImageCache imageCache) throws IOException
    {
        this.game = game;
        this.ship = ship;
    }

    public void setSystem(Space space, Solar system)
    {
        this.space = space;
        this.system = system;
        globalScale = 0;
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
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        if(globalScale == 0)
        {
            globalScale = 1.0;
            layoutSystem(system);

            double factorW = (double)width / (double)maxX;
            double factorH = (double)height / (double)maxY;

            double factor = Math.min(factorW, factorH);
            
            // Hajo: give right margin
            globalScale = factor * 0.9;
        }
    
    
        ShaderBank.setupMatrices(width, height);
        ShaderBank.updateViewMatrix(new Matrix4f());
        ShaderBank.updateLightPos(-10000.0f, 0.0f, 10000.0f);
    }

    @Override
    public void handleInput()
    {
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        selectedBody = null;
        bestSelectionDist = 999; 
    }

    @Override
    public void display()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        layoutSystem(system);
        
        Fonts.g32.drawStringBold(system.baseName + " System Information", Colors.FIELD, 32, height-60, 0.9f);
        
        if(selectedBody != null)
        {
            highlightBodyAndShowBodyInfo(selectedBody, selectionFrame);
            
            if(clicked && Mouse.isButtonDown(0) == false)
            {
                clicked = false;

                if(selectedBody.btype == Solar.BodyType.STATION || 
                   selectedBody.btype == Solar.BodyType.SPACEPORT)     
                {
                    // Hajo: only show if ship is actually in that system
                    if(ship.loca.equals(system.loca))
                    {
                        game.showStationPanel(selectedBody);
                    }
                }
                else if(selectedBody.btype == Solar.BodyType.PLANET)
                {
                    if(ship.loca.equals(system.loca))
                    {
                        game.showLocalPlanetDetailPanel(selectedBody);
                    }
                    else
                    {
                        game.showDistantPlanetDetailPanel(selectedBody);
                    }
                }
            }
        }
        if(clicked && Mouse.isButtonDown(0) == false)
        {
            clicked = false;
        }
    }

    public void drawSolarBody(Solar body, int bodyX, int bodyY, int bodyW, int bodyH, String name, int textColor, int spacing)
    {
        displayPlanetMesh(body, bodyX, bodyY);
                
        if(body.btype == Solar.BodyType.SPACEPORT) 
        {
            int sw = Fonts.g12.getStringWidth(name);
            Fonts.g12.drawString(name, textColor == Colors.YELLOW ? textColor : Colors.CYAN, bodyX-sw, bodyY + bodyH/2 - 14);
        }
        else
        {
            int sw = Fonts.g12.getStringWidth(name);
            int left = bodyX - sw/2;
            Fonts.g12.drawString(name, textColor, left, bodyY - bodyH/2 - 36);
        }

        checkSelection(body, bodyX, bodyY, bodyW, bodyH, spacing);
    }

    public void displayBodyInfo(Solar body, boolean isExplored, int x, int y)
    {
        if(isExplored) 
        {
            displayKnownBodyInfo(body, x, y);
        }
        else 
        {
            displayStrangeBodyInfo(body, x, y);
        }
    }

    public void displayStrangeBodyInfo(Solar body, int x, int y)
    {
        PixFont font = Fonts.g12;
        int lineOffset = y;
        int lineSpace = 14;
        
        font.drawString(((body.btype == Solar.BodyType.SUN) ?
                    Solar.sunDescription[body.stype.ordinal()] :
                    Solar.planetDescription[body.ptype.ordinal()]), Colors.ORANGE, x, lineOffset);
        lineOffset -= lineSpace;

        font.drawString("Unexplored space body.", Colors.ORANGE, x, lineOffset);
        lineOffset -= lineSpace;
        
        font.drawString("No further data available.", Colors.ORANGE, x, lineOffset);
        lineOffset -= lineSpace;
    }

    public void displayKnownBodyInfo(Solar body, int x, int y)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        PixFont font = Fonts.g12;
        int lineOffset = y;
        int lineSpace = 14;
        int columnOffset = 70;
        
        if(body.society.population > 0) 
        {
            font.drawStringBold(body.name + " - " + body.society.race, Colors.LABEL, x, lineOffset, 1.0f);
            lineOffset -= lineSpace;
            
            nf.setMaximumFractionDigits(0);

            long pop = body.society.population;

            if(pop < 1000) {
                font.drawString("Population:", Colors.LABEL, x, lineOffset);
                font.drawString("< 1000", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            } else if(pop < 10000) {
                font.drawString("Population:", Colors.LABEL, x, lineOffset);
                font.drawString("< 10000", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            } else {
                font.drawString("Population:", Colors.LABEL, x, lineOffset);
                font.drawString(nf.format((pop/1000)*1000), Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
        }
        else 
        {
            font.drawStringBold(body.name, Colors.LABEL, x, lineOffset, 1.0f);
            lineOffset -= lineSpace;
        }


        if(body.btype == Solar.BodyType.PLANET) 
        {
            double m = Solar.massToEarthMasses(body.mass);

            if(m < 0.0005) 
            {
                nf.setMaximumFractionDigits(0);
                font.drawString("Mass:", Colors.LABEL, x, lineOffset);
                font.drawString(nf.format(body.mass/1000000000) + " gt", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
            else 
            {
                nf.setMaximumFractionDigits(4);
                font.drawString("Mass:", Colors.LABEL, x, lineOffset);
                font.drawString(nf.format(m) + " earth masses", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
        } 
        else if(body.btype == Solar.BodyType.SUN) 
        {
            nf.setMaximumFractionDigits(4);
            double m = Solar.massToSunMasses(body.mass);
            font.drawString("Mass:", Colors.LABEL, x, lineOffset);
            font.drawString(nf.format(m) + " sun masses", Colors.FIELD, x+columnOffset, lineOffset);
            lineOffset -= lineSpace;
        }

        nf.setMaximumFractionDigits(2);

        font.drawString("Radius:", Colors.LABEL, x, lineOffset);
        font.drawString((body.btype == Solar.BodyType.STATION) ? "< 1 km" : nf.format(body.radius) + " km", Colors.FIELD, x+columnOffset, lineOffset);
        lineOffset -= lineSpace;

        if(body.btype != Solar.BodyType.SUN &&
           body.btype != Solar.BodyType.SPACEPORT)
        {
            double au = Solar.distanceToAU(body.orbit);

            if(au < 0.01) 
            {
                font.drawString("Orbit:", Colors.LABEL, x, lineOffset);
                font.drawString(nf.format(body.orbit) + " km", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
            else 
            {
                font.drawString("Orbit:", Colors.LABEL, x, lineOffset);
                font.drawString(nf.format(au) + " au", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
        }

        if(body.btype == Solar.BodyType.PLANET)
        {
                font.drawString("Temp.:", Colors.LABEL, x, lineOffset);
                font.drawString(body.eet + " Kelvin", Colors.FIELD, x+columnOffset, lineOffset);
                lineOffset -= lineSpace;
        }
    }

    private void checkSelection(Solar body, int bodyX, int bodyY, int bodyW, int bodyH, int spacing)
    {
        int mx = Mouse.getX();
        int my = Mouse.getY();
        
        int cx, cy;
        if(body.btype ==  Solar.BodyType.SPACEPORT)
        {
            cx = bodyX - spacing/8;
            cy = bodyY + 10;
        }
        else
        {
            cx = bodyX;
            cy = bodyY;
        }
        
        int dx = mx - cx;
        int dy = my - cy;
        
        int dist = dx*dx + dy*dy;
        
        if(dist < bestSelectionDist)
        {
            selectedBody = body;
            bestSelectionDist = dist;            

            if(selectedBody.btype ==  Solar.BodyType.SPACEPORT)
            {
                int sw = Fonts.g12.getStringWidth(selectedBody.name);
                selectionFrame.x = bodyX - sw - 8;
                selectionFrame.y = cy - 12;
                selectionFrame.width = sw + 16;
                selectionFrame.height = 24;
            }
            else
            {
                selectionFrame.width = 128;
                selectionFrame.height = Math.min(100, spacing - spacing/8);
                selectionFrame.x = cx - 64;
                selectionFrame.y = cy - selectionFrame.height / 2 - 4;
            }
        }
    }
    
    private void highlightBodyAndShowBodyInfo(Solar body, Rectangle frame)
    {
        fillBorder(frame.x, frame.y, frame.width, frame.height, 1, Colors.LABEL);
        displayBodyInfo(body, ship.player.isExplored(body), 
                        Math.max(frame.x-20, 20), 218);
    }

    
    private void displayPlanetMesh(Solar planet, int xpos, int ypos)
    {
        MultiMesh planetMesh = space.findMeshForPeer(planet);
        
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);  
        glBindTexture(GL_TEXTURE_2D, 0);
        
        planetMesh.planetRot();
        
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();
        Matrix4f projection = new Matrix4f();

        float far = 10000;
        float near = 1;

        projection.m00 = 2.0f / width;
        projection.m11 = 2.0f / height;
        projection.m22 = -2.0f / (far - near);
        //projectionMatrix.m23 = - (far + near) / (far - near);
        projection.m33 = 1;

        float size = scale(planet) * 64f;
        
        Vector3f translation = new Vector3f(
                (float) (xpos - width/2),
                (float) (ypos - height/2),
                0f);

        modelMatrix.translate(translation);

        float scale = size / (float)planet.radius;

        // System.err.println("Scale=" + scale);
        
        modelMatrix.scale(new Vector3f(scale, scale, scale));
        
        ShaderBank.updateProjectionMatrix(projection);
        ShaderBank.updateModelMatrix(modelMatrix);
        ShaderBank.updateViewMatrix(viewMatrix);
        
        if(planet.btype == Solar.BodyType.SUN)
        {
            GL20.glUseProgram(ShaderBank.brightProgId);
            ShaderBank.uploadBrightMatrices();
        }
        else
        {
            GL20.glUseProgram(ShaderBank.shadedProgId);
            ShaderBank.uploadShadedMatrices();
        }

        planetMesh.display();

        GL20.glUseProgram(0);
        glDisable(GL11.GL_DEPTH_TEST);
        
    }
}
