package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.MultiMesh;
import flyspace.Space;
import flyspace.ogl.SpacePanel;
import flyspace.ogl32.ShaderBank;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.system.Vec3;

/**
 *
 * @author Hj. Malthaner
 */
public class SystemMapPanel extends DecoratedUiPanel
{
    /**
     * Station good storage
     */
    private Solar system;
    private final FlySpace game;
    private final Ship ship;
    
    private boolean clicked;
    // private final DecoratedTrigger loungeTrigger;
    
    private double scale;
    private int zoom;
    
    private double viewX, viewY;

    private int dragStartX;
    private int dragStartY;
    private double dragCenterX;
    private double dragCenterY;
    private int centerX;
    private int centerY;
    private final Space space;
   
    private MultiMesh bestMesh;
    private int bestDist;
    private int mx;
    private int my;
    
    public SystemMapPanel(FlySpace game, Space space, Ship ship) 
    {
        this.game = game;
        this.space = space;
        this.ship = ship;

        /*
        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", 
                Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        
        loungeTrigger.setArea(934, 200, 185, 24);
        addTrigger(loungeTrigger);
     */   
        zoom = 10;
        rescale();
        
    }

    public void setSystem(Solar system)
    {
        this.system = system;
    }
    
    
    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
        centerX = width / 2;
        centerY = (height  - CockpitPanel.HEIGHT) / 2 + CockpitPanel.HEIGHT;
        
        // Hajo: clear wheel history
        Mouse.getDWheel();        
    }

    @Override
    public void handleInput() 
    {
        mx = Mouse.getX();
        my = Mouse.getY();
        
        if(Mouse.isButtonDown(0))
        {
            if(!clicked)
            {
                dragStartX = mx;
                dragStartY = my;
                dragCenterX = viewX;
                dragCenterY = viewY;
            }
            else
            {
                int dx = mx - dragStartX;
                int dy = my - dragStartY;

                viewX = (dragCenterX + dx/scale);
                viewY = (dragCenterY + dy/scale);
            }
            
            clicked = true;
        }
        
        if(Mouse.isButtonDown(0) == false)
        {
            if(clicked)
            {
                /*                
                Trigger t = trigger(mx, my);
                
                if(t == loungeTrigger)
                {
                    game.showStationPanel();
                }
                else
                {
                */
                    if(bestMesh != null)
                    {
                        space.selectedMesh = bestMesh;
                        SpacePanel.setDestination(ship, bestMesh);
                    }
                // }
                
                clicked = false;
            }
        }
        
        int u = Mouse.getDWheel();

        if(u < 0 && zoom > 1) 
        {
            zoom -=10;
            rescale();
        }
        if(u > 0) 
        {
            zoom +=10;
            rescale();
        }
        
    }

    @Override
    public void display() 
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        displayTitle(system.baseName + " System Map");
        
        bestDist = 900;
        bestMesh = null;
        paintSystem(viewX, viewY, space.findMeshForPeer(system));
        paintShip();
        
        if(space.selectedMesh != null && space.selectedMesh.getPeer() != null)
        {
            Fonts.g12.drawString("Destination:", Colors.LABEL, width-260, 640);
            Fonts.g12.drawString(space.selectedMesh.getPeer().name, Colors.FIELD, width-180, 640);
        }
        
        fillBorder(10, 180, 310, 39, 1, Colors.GRAY);
        fillRect(11, 181, 308, 37, 0x99001020);

        Fonts.c9.drawString("Drag map by mouse. Zoom with mouse wheel.", Colors.CYAN, 18, 185);
        Fonts.c9.drawString("Click to set autopilot destinatoin.", Colors.CYAN, 18, 170);
        
        
        displayTriggers();
    }


    private void rescale()
    {
        scale = zoom*zoom * 0.00000001;
    }

    private void paintOrbit(final int xpos,
                            final int ypos,
                            final int rad)
    {
        for(double u=0; u <= 2*Math.PI; u += Math.PI/128)
        {
            fillRect(xpos + (int)(Math.cos(u)*rad),
                     ypos + (int)(Math.sin(u)*rad),
                     1,
                     1,
                     Colors.GRAY);
        }
    }

    private void paintSystem(final double xoff,
                             final double yoff,
                             MultiMesh multiMesh)
    {
        int screenX = centerX + (int) (xoff * scale);
        int screenY = centerY + (int) (yoff * scale);
        Solar body = multiMesh.getPeer();

        if(screenX >= 0 && screenY >= 0 && screenX < width && screenY < height && body != null)
        {
            PixFont font = Fonts.g12;

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

            Vector3f translation = new Vector3f(
                    (float) (xoff * scale),
                    (float) (yoff * scale + CockpitPanel.HEIGHT/2),
                    (float) (-body.radius * Space.DISPLAY_SCALE));

            modelMatrix.translate(translation);

            // Vector3f translation = new Vector3f((float)xoff, (float)yoff, 0);

            // Vector3f planetScale = new Vector3f(1, (float)width/(float)height, 1);
            // planetScale.scale((float)(body.radius * Space.DISPLAY_SCALE * scale));

            // modelMatrix.scale(planetScale);


            float fscale = (float)(scale);
            modelMatrix.scale(new Vector3f(fscale, fscale, fscale));


            /*
            Matrix4f.rotate(Math3D.degToRad(multiMesh.getAngleZ()), new Vector3f(0, 0, 1), 
                    modelMatrix, modelMatrix);
            Matrix4f.rotate(Math3D.degToRad(multiMesh.getAngleY()), new Vector3f(0, 1, 0), 
                    modelMatrix, modelMatrix);
            Matrix4f.rotate(Math3D.degToRad(multiMesh.getAngleX()), new Vector3f(1, 0, 0), 
                    modelMatrix, modelMatrix);
                    */

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL20.glUseProgram(ShaderBank.shadedProgId);

            ShaderBank.updateProjectionMatrix(projection);
            ShaderBank.updateModelMatrix(modelMatrix);
            ShaderBank.updateViewMatrix(viewMatrix);
            Vec3 meshPos = multiMesh.getPos();
            ShaderBank.updateLightPos((float)-meshPos.x, (float)-meshPos.y, 500000f);
            ShaderBank.uploadShadedMatrices();
            multiMesh.display();

            GL20.glUseProgram(0);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            // Hajo: keep track of system which is closest to the mouse
            int dx = mx - screenX;
            int dy = my - screenY;

            int dist = dx*dx + dy*dy;
            if(dist < bestDist)
            {
                bestDist = dist;
                bestMesh = multiMesh;
            }

            if(body.btype == Solar.BodyType.SPACEPORT)
            {
                font.drawString(body.name, Colors.CYAN, 
                        screenX + 4,
                        screenY + 8);
            }
            else
            {
                if(body.btype == Solar.BodyType.STATION)
                {
                    font.drawString(body.name, Colors.ORANGE, 
                        screenX,
                        screenY - 12);
                }
                else
                {
                    font.drawString(body.name, Colors.WHITE, 
                        screenX,
                        screenY - 12);
                }
            }
        }   
        if(body != null)
        {
            for (int i = 0; i < body.children.size(); i++)
            {
                final Solar trabant = body.children.get(i);

                // Hajo: too small bodies shouldn't be displayed
                if (trabant.btype == Solar.BodyType.SUN ||
                    trabant.orbit * scale > 10.0 ||
                    trabant.btype == Solar.BodyType.SPACEPORT)
                {
                    if(trabant.btype != Solar.BodyType.SPACEPORT)
                    {
                        paintOrbit(centerX + (int) (xoff * scale),
                                   centerY + (int) (yoff * scale),
                                   (int) (trabant.orbit * scale * Space.DISPLAY_SCALE));
                    }

                    paintSystem((xoff + trabant.pos.x * Space.DISPLAY_SCALE),
                                (yoff + trabant.pos.z * Space.DISPLAY_SCALE),
                                space.findMeshForPeer(trabant));
                }
            }
        }           
        // font.drawString("scale=" + scale, Colors.TEXT, 900, 650);
        
    }

    private void paintShip()
    {
        if(ship.destination.x != ship.pos.x ||
           ship.destination.y != ship.pos.y)
        {
            // Hajo: draw player ship destination
            fillBorder(centerX + (int) ((viewX + ship.destination.x) * scale)-3,
                       centerY + (int) ((viewY + ship.destination.z) * scale)-3,
                       6, 6, 1, Colors.MAGENTA);

            Fonts.c9.drawString("Current destination", Colors.MAGENTA,
                          centerX + (int) ((viewX + ship.destination.x) * scale) + 6,
                          centerY + (int) ((viewY + ship.destination.z) * scale) - 16);
        }

        // Hajo: draw player ship position
        fillBorder(centerX + (int) ((viewX + ship.pos.x) * scale)-3,
                   centerY + (int) ((viewY + ship.pos.z) * scale)-3,
                   6, 6, 1, Colors.CYAN);

        
        Fonts.c9.drawString("Current location", Colors.CYAN,
                      centerX + (int) ((viewX + ship.pos.x) * scale) + 6,
                      centerY + (int) ((viewY + ship.pos.z) * scale) - 16);
        
    }

    /*
    @Override
    public void paintComponent(Graphics gr)
    {
        final int width = getWidth();
        final int height = getHeight();

        gr.drawImage(imageCache.metalBand.getImage(), 0, 0, width, height, null);

        gr.setColor(Color.GRAY);
        gr.drawRect(10, 11, 410, 48);
        gr.setColor(Color.BLACK);
        gr.fillRect(11, 12, 408, 46);

        gr.setColor(Color.WHITE);
        gr.setFont(FontFactory.getPanelHeading());
        gr.drawString("Navigation Map", 130, 43);

        final int left = width/2;

        gr.setColor(Color.GRAY);
        gr.drawRect(left+10, 11, 410, 48);
        gr.setColor(Color.BLACK);
        gr.fillRect(left+11, 12, 408, 46);

        gr.setColor(Color.GRAY);
        gr.drawRect(10, height-61, 410, 42);
        gr.setColor(Color.BLACK);
        gr.fillRect(11, height-60, 408, 40);

        gr.setClip(100, 75, width-200, height-150);
        gr.drawImage(imageCache.backdrops[1].getImage(), 100, 75, null);
        gr.setClip(0, 0, width, height);

        gr.setFont(FontFactory.getNormal());
        gr.setColor(Color.CYAN);
        gr.drawString("Drag map by mouse. Zoom with mouse wheel.", 18, height-36);

        gr.setColor(Color.GREEN);
        gr.setFont(FontFactory.getLabelHeading());
        gr.drawString("Destination:", left+18, 30);

        gr.setColor(Color.WHITE);
        gr.setFont(FontFactory.getLarger());
        gr.drawString(destString, left+100, 30);

        gr.setColor(Color.GREEN);
        gr.setFont(FontFactory.getLabelHeading());
        gr.drawString("Speed:", left+18, 50);

        gr.setColor(Color.WHITE);
        gr.setFont(FontFactory.getLarger());
        gr.drawString(speedString, left+100, 50);

        gr.setFont(FontFactory.getNormal());

        gr.setColor(Color.GRAY);
        gr.drawRect(100-1, 75-1, width-200+2, height-150+2);
        
        Shape clip = gr.getClip();
        
        gr.setClip(100, 75, width-200, height-150);

        paintSystem(gr, viewX, viewY, system);

        // Hajo: only paint if we are really there
        if(ship.loca.equals(system.loca))
        {
            paintShip(gr);
        }
        
        gr.setClip(clip);
    }
*/
}
