/*
 * NavigationPanel.java
 *
 * Created: 04-Feb-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import solarex.galaxy.Galaxy;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.ui.ImageCache;
import solarex.ui.components.HyperjumpPainter;
import solarex.ui.components.StardrivePainter;
import solarex.ui.interfaces.ShowPlanetCallback;
import solarex.ui.interfaces.ShowSystemCallback;
import solarex.util.ClockCallback;
import solarex.ui.ComponentFactory;
import solarex.ui.FontFactory;

/**
 * Navigation map panel.
 * 
 * @author Hj. Malthaner
 */
public class NavigationPanel extends JPanel implements ClockCallback, HyperjumpCallback
{
    private final JButton driveButton;
    private final HyperjumpPainter hyperjumpPainter;
    private final StardrivePainter stardrivePainter;
            
    private double scale;
    private int zoom;
    private double viewX, viewY;

    private double dragStartX;
    private double dragStartY;
    private double dragCenterX;
    private double dragCenterY;

    private String destString = "Not set";
    private String speedString = "0.0";
    private Solar destBody;

    private Solar system;
    private ImageCache imageCache;
    private ShowSystemCallback showSystemCallback;
    
    private double currentSpeed = 0.0;
    
    /**
     * Callback to trigger when ship arrives at a body
     */
    private ShowPlanetCallback showPlanetCallback;
    
    /**
     * The players ship
     */
    private Ship ship;
    private final Galaxy galaxy;

    public NavigationPanel(final Galaxy galaxy,
                           final Ship ship,
                           ImageCache imageCache,
                           ShowPlanetCallback showPlanetCallback,
                           final ShowSystemCallback showSystemCallback)
    {
        this.galaxy = galaxy;
        this.showPlanetCallback = showPlanetCallback;
        this.imageCache = imageCache;
        this.ship = ship;
        this.showSystemCallback = showSystemCallback;
        
        hyperjumpPainter = new HyperjumpPainter(imageCache);
        stardrivePainter = new StardrivePainter();

        zoom = 10;
        rescale();
        
        setLayout(null);

        driveButton = new JButton("Engage Stardrive");
        driveButton.setSize(180, 24);
        driveButton.setLocation(792, 600);

        ComponentFactory.customizeButton(driveButton);
        
        add(driveButton);

        driveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run()
                    {
                        stardrivePainter.start();
                    }
                });
            }
        });

        addMouseListener(new MouseListener() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Hajo: only if we are really there
                if(ship.loca.equals(system.loca))
                {
                    setDestination(e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) 
            {
                dragStartX = e.getX();
                dragStartY = e.getY();
                dragCenterX = viewX;
                dragCenterY = viewY;
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
            }

            @Override
            public void mouseEntered(MouseEvent e) 
            {
            }

            @Override
            public void mouseExited(MouseEvent e) 
            {
            }
        });


        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e)
            {
                int x = e.getX();
                int y = e.getY();

                double dx = dragStartX - x;
                double dy = dragStartY - y;

                viewX = (dragCenterX - dx/scale);
                viewY = (dragCenterY - dy/scale);

                repaint(50);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });


        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int u = e.getWheelRotation();
                zoom += u * (1 + (zoom/9));

                if(zoom < 1) 
                {
                    zoom = 1;
                }
                if(zoom > 4000)
                {
                    zoom = 4000;
                }

                rescale();
                repaint();
            }
        });
    }

    private void rescale()
    {
        scale = zoom*zoom * 0.00000001;
    }

    private void paintOrbit(final Graphics gr,
                            final int xpos,
                            final int ypos,
                            final int rad)
    {
        for(double u=0; u <= 2*Math.PI; u += Math.PI/128)
        {
            gr.fillRect(xpos + (int)(Math.cos(u)*rad),
                        ypos + (int)(Math.sin(u)*rad),
                        1,
                        1);
        }
    }

    private void paintSystem(final Graphics gr,
                             final double xoff,
                             final double yoff,
                             final Solar sys)
    {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        final int w = 2 + (int) (sys.radius * scale);

        Image img = null;
        
        if (sys.btype == Solar.BodyType.PLANET) 
        {
            img = imageCache.planets[sys.ptype.ordinal()].getImage();
        }
        else if(sys.btype == Solar.BodyType.STATION) 
        {
            img = imageCache.station.getImage();
        }
        else if(sys.btype == Solar.BodyType.SPACEPORT) 
        {
            img = imageCache.spaceport.getImage();
        }
        else if (sys.btype == Solar.BodyType.SUN) 
        {
            img = imageCache.suns[sys.stype.ordinal()].getImage();
        }
        
        if(img != null)
        {
            gr.drawImage(img,
                         centerX - w/2 + (int)(xoff * scale),
                         centerY - w/2 + (int)(yoff * scale),
                         w, w, null);
        }

        if(sys.btype == Solar.BodyType.SPACEPORT)
        {
            gr.setColor(Color.ORANGE);
            gr.drawString(sys.name,
                    centerX + (int) (xoff * scale) + 4,
                    centerY + (int) (yoff * scale) + 8);
        }
        else
        {
            if(sys.btype == Solar.BodyType.STATION)
            {
                gr.setColor(Color.ORANGE);
            }
            else
            {
                gr.setColor(Color.WHITE);
            }
            gr.drawString(sys.name,
                    centerX + (int) (xoff * scale),
                    centerY + (int) (yoff * scale) - 4);
        }

        for (int i = 0; i < sys.children.size(); i++)
        {
            final Solar body = sys.children.get(i);

            // Hajo: too small bodies shouldn't be displayed
            if (body.btype == Solar.BodyType.SUN ||
                body.orbit * scale > 10.0 ||
                body.btype == Solar.BodyType.SPACEPORT)
            {
                if(body.btype != Solar.BodyType.SPACEPORT)
                {
                    gr.setColor(Color.GRAY);
                    paintOrbit(gr,
                            centerX + (int) (xoff * scale),
                            centerY + (int) (yoff * scale),
                            (int) (body.orbit * scale));
                }

                paintSystem(gr,
                        (xoff + body.pos.x),
                        (yoff + body.pos.z),
                        body);
            }
        }
    }

    private void paintShip(final Graphics gr)
    {
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;

        if(ship.destination.x != ship.pos.x ||
           ship.destination.y != ship.pos.y)
        {
            // Hajo: draw player ship destination
            gr.setColor(Color.PINK);
            gr.drawRect(centerX + (int) ((viewX + ship.destination.x) * scale)-3,
                        centerY + (int) ((viewY + ship.destination.y) * scale)-3,
                        6, 6);

            gr.setColor(Color.GREEN);
            gr.drawString("Current destination",
                          centerX + (int) ((viewX + ship.destination.x) * scale) + 8,
                          centerY + (int) ((viewY + ship.destination.y) * scale));
        }

        // Hajo: draw player ship position
        gr.setColor(Color.CYAN);
        gr.drawRect(centerX + (int) ((viewX + ship.pos.x) * scale)-3,
                    centerY + (int) ((viewY + ship.pos.y) * scale)-3,
                    6, 6);

        gr.setColor(Color.GREEN);
        gr.drawString("Your ship",
                      centerX + (int) ((viewX + ship.pos.x) * scale) + 8,
                      centerY + (int) ((viewY + ship.pos.y) * scale));
    }

    @Override
    public void paint(Graphics gr)
    {
        if(hyperjumpPainter.isActive())
        {
            hyperjumpPainter.paint(gr, getWidth(), getHeight());
        }
        else if(stardrivePainter.isActive())
        {
            stardrivePainter.paint(gr, getWidth(), getHeight());
        }
        else
        {
            super.paint(gr);
        }
    }
    
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

    /**
     * Set new ship destination.
     * 
     * @param x Mouse X on screen.
     * @param y Mouse Y on screen.
     */
    private void setDestination(int x, int y)
    {
        final int w = getWidth();
        final int h = getHeight();

        // for testing we set ship coordinates
        ship.destination.x = -viewX + (x-w/2)/scale;
        ship.destination.y = -viewY + (y-h/2)/scale;
        
        ship.depart();

        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        destString = "X=" +
                nf.format(ship.destination.x) +
                " Y=" +
                nf.format(ship.destination.y);


        final double range = 27/scale;
        // System.err.println("Testing range: " + range);
        
        final Solar body = system.findInRange(ship.destination, range);
        destBody = body;

        if(destBody != null) 
        {
            destString = destBody.name;
            
            Vec3 pos = destBody.getAbsolutePosition();
            
            viewX = -pos.x;
            viewY = -pos.y;
            
            ship.destination.x = pos.x;
            ship.destination.y = pos.y;
        }

        repaint();
    }


    public void setSystem(Solar system)
    {
        this.system = system;
        viewX = 0;
        viewY = 0;

        zoom = 10;
        rescale();

        repaint();
    }

    @Override
    public void ping100(int deltaT)
    {
        if(hyperjumpPainter.isActive()) 
        {
            moveJump(deltaT);
        }
        else if(stardrivePainter.isActive()) 
        {
            driveShip(deltaT);
        }
        else
        {
            moveShip(deltaT);
        }
    }

    private void moveJump(int deltaT)
    {
        hyperjumpPainter.moveJump(deltaT);
        repaint(50);

        if(hyperjumpPainter.isDone()) 
        {
            hyperjumpPainter.stop();
                    
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run() 
                {
                    // showSystemCallback.showSystem(ship.loca);
                    Solar system = showSystemCallback.makeSystem(ship.loca);
                    // setSystem(system);
                }
            });
        }
    }

    private void driveShip(int deltaT)
    {
        stardrivePainter.moveJump(deltaT);
        repaint(50);

        if(stardrivePainter.isDone()) 
        {
            stardrivePainter.stop();

            ship.pos.x = ship.destination.x;
            ship.pos.y = ship.destination.y;

            currentSpeed = 0;
            speedString = "" + currentSpeed + " (arrived)";

            arrived();

            // Hajo: update screen after move
            repaint(10);
        }
    }
    
    private void moveShip(int deltaT)
    {
        double dx = ship.destination.x - ship.pos.x;
        double dy = ship.destination.y - ship.pos.y;

        double dist = Math.sqrt(dx*dx + dy*dy);

        final double accel = 150;

        if(dist > 0) 
        {
            // Hajo: Normalize vector
            dx /= dist;
            dy /= dist;

            // Hajo: find breaking distance
            final double breakingDistance = 
                    (currentSpeed * currentSpeed) / (2*accel);
            
            // Hajo: Do we need to break yet?
            // We add a bit of a safety margin for soft approaches.
            if(breakingDistance*1.4 < dist) 
            {
                currentSpeed += accel;
                speedString = "" + (currentSpeed/100.0) + " (accelerating)";
            }
            else if(breakingDistance*1.2 > dist)
            {
                currentSpeed -= accel;                
                speedString = "" + (currentSpeed/100.0) + " (decelerating)";
            }
            else 
            {
                speedString = "" + (currentSpeed/100.0) + " (floating)";
            }

            // Hajo: min. speed to be sure we arrive
            if(currentSpeed < 1000) 
            {
                currentSpeed = 1000;
                speedString = "" + (currentSpeed/100.0) + " (approaching)";
            }

            ship.pos.x += dx*currentSpeed;
            ship.pos.y += dy*currentSpeed;

            // Hajo: check if we reached the destination
            double dxLeft = ship.destination.x - ship.pos.x;
            double dyLeft = ship.destination.y - ship.pos.y;

            if(Math.abs(dxLeft) < currentSpeed &&
               Math.abs(dyLeft) < currentSpeed)
            {

                // Hajo: clean up remaining distance
                ship.pos.x = ship.destination.x;
                ship.pos.y = ship.destination.y;
                currentSpeed = 0.0;
                speedString = "" + currentSpeed + " (arrived)";
                
                arrived();
            }

            // Hajo: update screen after move
            repaint(10);
        }
    }

    /**
     * Called when the ship arrived its destination
     */
    private void arrived()
    {
        if(destBody != null) 
        {
            if(destBody.btype != Solar.BodyType.SUN) 
            {
                final Solar selectedBody = destBody;

                // Hajo: Space ports are clickable now
                // so this doesn't seem to be needed anymore

                /*
                if(destBody.children.size() > 0 &&
                   destBody.children.get(0).btype == Solar.BType.SPACEPORT) {

                   int choice =
                        JOptionPane.showOptionDialog(
                            this,
                            "This planet has a spaceport.",
                            "Land or enter orbit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String [] {"Land", "Orbit"},
                            null);

                    if(choice == 0) {
                        chosenBody = destBody.children.get(0);
                    }
                }
                */

                if(selectedBody.btype == Solar.BodyType.PLANET ||
                   selectedBody.btype == Solar.BodyType.STATION ||
                   selectedBody.btype == Solar.BodyType.SPACEPORT)
                {
                    ship.arrive(galaxy, selectedBody);
                }
                else
                {
                    ship.spaceBodySeed = 0;
                    ship.loca.name = "";
                    ship.setState(Ship.State.FLIGHT);
                }

                showPlanetCallback.showSpaceBody(selectedBody);
            }
        }
    }
    
    @Override
    public void triggerJump() 
    {
        ship.loca = ship.hyperjumpDestination;
        ship.hyperjumpDestination = null;
        ship.setState(Ship.State.FLIGHT);
        ship.spaceBodySeed = 0;

        ship.destination.x = ship.pos.x;
        ship.destination.y = ship.pos.y;
        currentSpeed = 0;

        hyperjumpPainter.start();
    }
}
