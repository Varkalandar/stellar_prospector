package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.MultiMesh;
import flyspace.Space;
import flyspace.ogl.SpacePanel;
import flyspace.ogl32.ShaderBank;
import flyspace.ui.Colors;
import flyspace.ui.Fonts;
import flyspace.ui.PixFont;
import flyspace.ui.Mouse;
import flyspace.ui.Sounds;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;
import solarex.ship.Ship;
import solarex.system.Matrix4;
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
    private final Ship ship;
    
    private boolean clicked;
    // private final DecoratedTrigger loungeTrigger;
    
    // make system "lay" at 45° degree angle so one can see the orbits
    // better on screen.
    private final double yscale = 0.5;
    private double scale;
    
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
    private final FlySpace game;
    
    public SystemMapPanel(FlySpace game, Space space, Ship ship) 
    {
        super(null);

        this.game = game;
        this.space = space;
        this.ship = ship;

        scale = 0.00001;
    }

    public void setSystem(Solar system)
    {
        this.system = system;
        
        // Hajo: A new system, can have much larger or smaller orbits than the
        // former one. We need to find a scale that is working well for the
        // system.
        
        // get the radius of the outermost planet
        int i = system.children.size();
        Solar planet = system.children.get(i-1);
        
        scale = 30000.0 / planet.orbit;

        System.err.println("New system, outmost planet found=" + planet.name);
        System.err.println("New system, scale=" + scale);
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
    public void handlePanelInput() 
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
                if(bestMesh != null)
                {
                    game.playSound(Sounds.CLICK, 1f);
                    
                    space.selectedMesh = bestMesh;
                    SpacePanel.setDestination(ship, bestMesh);
                }
                
                clicked = false;
            }
        }
        
        int u = Mouse.getDWheel();

        if(u < 0) 
        {
            scale *= 1/1.2; 
        }
        if(u > 0) 
        {
            scale *= 1.2;
        }        
    }
    

    @Override
    public void displayPanel() 
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
        Fonts.c9.drawString("Click to set autopilot destination.", Colors.CYAN, 18, 170);
        
        displayTriggers();
    }

    
    private void paintOrbit(final int xpos,
                            final int ypos,
                            final int rad)
    {
        for(double u=0; u <= 2*Math.PI; u += Math.PI/128)
        {
            fillRect(xpos + (int)(Math.cos(u)*rad),
                     ypos + (int)(Math.sin(u)*rad * yscale),
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
        int screenY = centerY + (int) (yoff * scale * yscale);
        Solar body = multiMesh.getPeer();

        if(screenX >= 0 && screenY >= 0 && screenX < width && screenY < height && body != null)
        {
            PixFont font = Fonts.g12;

            Matrix4 modelMatrix = new Matrix4();
            Matrix4 viewMatrix = new Matrix4();
            Matrix4 projection = new Matrix4();

            float far = 10000;
            float near = 1;

            projection.m00 = 2.0f / width;
            projection.m11 = 2.0f / height;
            projection.m22 = -2.0f / (far - near);
            //projectionMatrix.m23 = - (far + near) / (far - near);
            projection.m33 = 1;

            Vec3 translation = new Vec3(
                    (xoff * scale),
                    (yoff * scale  * yscale + CockpitPanel.HEIGHT/2),
                    (-body.radius * Space.DISPLAY_SCALE));

            Matrix4.translate(translation, modelMatrix, modelMatrix);

            // Vector3f translation = new Vector3f((float)xoff, (float)yoff, 0);

            // Vector3f planetScale = new Vector3f(1, (float)width/(float)height, 1);
            // planetScale.scale((float)(body.radius * Space.DISPLAY_SCALE * scale));

            // modelMatrix.scale(planetScale);


            float fscale = (float)(scale);
            Matrix4.scale(new Vec3(fscale, fscale, fscale), modelMatrix, modelMatrix);


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
                                   centerY + (int) (yoff * scale * yscale),
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
                       centerY + (int) ((viewY + ship.destination.z) * scale * yscale)-3,
                       6, 6, 1, Colors.MAGENTA);

            Fonts.c9.drawString("Current destination", Colors.MAGENTA,
                          centerX + (int) ((viewX + ship.destination.x) * scale) + 6,
                          centerY + (int) ((viewY + ship.destination.z) * scale * yscale) - 16);
        }

        // Hajo: draw player ship position
        fillBorder(centerX + (int) ((viewX + ship.pos.x) * scale)-3,
                   centerY + (int) ((viewY + ship.pos.z) * scale * yscale)-3,
                   6, 6, 1, Colors.CYAN);

        
        Fonts.c9.drawString("Current location", Colors.CYAN,
                      centerX + (int) ((viewX + ship.pos.x) * scale) + 6,
                      centerY + (int) ((viewY + ship.pos.z) * scale * yscale) - 16);
        
    }
}
