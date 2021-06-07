/*
 * GalaxyViewPanel.java
 *
 * Created: 12-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import flyspace.SystemBuilder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.interfaces.ShowSystemCallback;
import solarex.ui.ComponentFactory;
import solarex.ui.FontFactory;

/**
 * Galactic map display.
 * 
 * @author Hj. Malthaner
 */
public class GalaxyViewPanel extends JPanel
{
    private final ImageCache imageCache;
    private final JButton jumpButton;

    /** Player ship, used for location */
    private Ship ship;

    private Color gridColor = new Color(0, 128, 0);
    private Color mapBackgroundColor = new Color(0, 9, 17);
    private Color hyperRangeColor = new Color(0, 18, 43);
    private Color colorBrown = new Color(160, 80, 0);
    
    private int zoom;
    private ArrayList <SystemInfo> systemInfoList = new ArrayList();
    private int centerX = -300;
    private int centerY = 0;

    private int dragStartX;
    private int dragStartY;
    private int dragCenterX;
    private int dragCenterY;

    private ShowSystemCallback showSystemCallback;
    private HyperjumpCallback hyperjumpCallback;

    private Galaxy galaxy;

    private String systemDistance;
    
    public void setCallbacks(ShowSystemCallback c, HyperjumpCallback h)
    {
        showSystemCallback = c;
        hyperjumpCallback = h;
    }
    

    public GalaxyViewPanel(Galaxy galaxy, final Ship ship, ImageCache imageCache)
    {
        this.galaxy = galaxy;
        this.ship = ship;
        this.imageCache = imageCache;
        
        zoom = 1;

        // setBackground(Color.DARK_GRAY);
        setLayout(null);

        jumpButton = new JButton("Engage Jumpdrive");
        jumpButton.setSize(180, 24);
        jumpButton.setLocation(792, 600);

        ComponentFactory.customizeButton(jumpButton);
        
        add(jumpButton);

        jumpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(ship.hyperjumpDestination != null)
                {
                    showSystemCallback.switchToNavigationView();
                    hyperjumpCallback.triggerJump();
                    updateControls();
                }
            }
        });
        
        addMouseListener(new MouseListener() 
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                openSystem(e.getX(), e.getY(), e.getClickCount());
            }

            @Override
            public void mousePressed(MouseEvent e) 
            {
                dragStartX = e.getX();
                dragStartY = e.getY();
                dragCenterX = centerX;
                dragCenterY = centerY;
            }

            @Override
            public void mouseReleased(MouseEvent e) 
            {
                /*
                centerX = dragCenterX;
                centerY = dragCenterY;
                repaint(50);
                 */
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


        addMouseMotionListener(new MouseMotionListener() 
        {

            @Override
            public void mouseDragged(MouseEvent e)
            {
                int x = e.getX();
                int y = e.getY();

                int dx = dragStartX - x;
                int dy = dragStartY - y;

                centerX = dragCenterX + dx*zoom;
                centerY = dragCenterY + dy*zoom;

                // System.err.println("Beep dx=" + dx);

                repaint(50);
            }

            @Override
            public void mouseMoved(MouseEvent e) 
            {
            }
        });


        addMouseWheelListener(new MouseWheelListener() 
        {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) 
            {
                int u = e.getWheelRotation();
                zoom += u;

                if(zoom < 1) 
                {
                    zoom = 1;
                }
                if(zoom > 11) 
                {
                    zoom = 11;
                }

                repaint();
            }
        });

    }

    /*
     * Each galactical sector contains up to 6 solar systems
     * @author Hj. Malthaner
     */
    private void showGalacticalSector(final Graphics gr,
            final int xpos,
            final int ypos,
            final int i,
            final int j)
    {
        List <SystemLocation> list = galaxy.buildSector(i, j);

        if(zoom < 6) 
        {
            gr.setColor(gridColor);
            gr.drawRect(xpos, ypos, (128*2) / zoom, (128*2) / zoom);
            gr.drawString("" + i + "/" + j, xpos+2, ypos+16);
        }
        
        final int anz = list.size();
        for (int n = 0; n < anz; n++) 
        {
            SystemLocation loca = list.get(n);

            final int x = xpos + loca.ioff*2 / zoom;
            final int y = ypos + loca.joff*2 / zoom;

            SystemInfo inf = new SystemInfo();

            inf.xpos = x;
            inf.ypos = y;
            inf.loca = loca;

            systemInfoList.add(inf);

            Solar system = SystemBuilder.create(inf.loca, false);

            final int size = 1+Math.max((system.radius >> 19) / zoom, 3);

            switch(system.stype) 
            {
                case S_YELLOW:
                    gr.setColor(Color.YELLOW);
                    break;
                case S_ORANGE:
                    gr.setColor(Color.ORANGE);
                    break;
                case S_WHITE_DWARF:
                    gr.setColor(Color.WHITE);
                    break;
                case S_BLUE_GIANT:
                    gr.setColor(Color.CYAN);
                    break;
                case S_RED_GIANT:
                    gr.setColor(Color.RED);
                    break;
                case S_NEUTRON:
                    gr.setColor(Color.DARK_GRAY);
                    break;
                case S_BLACK_HOLE:
                    gr.setColor(Color.WHITE); // Hajo: outline
                    break;
                case S_BROWN_DWARF:
                    gr.setColor(colorBrown);
                    break;
                default:
                    gr.setColor(Color.YELLOW);
            }

            // Hajo: in low zoom modes, show names
            if(zoom < 4) 
            {
                drawSun(gr, x, y, size, system.stype);

                final int margin = Math.max(size/2+4, 12);

                if(ship.loca.equals(loca))
                {
                    gr.setColor(Color.ORANGE);
                    gr.drawRoundRect(x-margin-4, y-margin-2, 100, margin*2, 8, 8);
                }

                if(loca.equals(ship.hyperjumpDestination))
                {
                    gr.setColor(Color.GREEN);
                    gr.drawRoundRect(x-margin-4, y-margin-2, 100, margin*2, 8, 8);
                }

                gr.setColor(Color.WHITE);
                gr.drawString(system.name, x + size/2 + 2, y + 2);
            } 
            else 
            {
                drawSun(gr, x, y, size, system.stype);
                
                final int margin = 9;

                if(ship.loca.equals(loca))
                {
                    gr.drawRoundRect(x - margin, y - margin, margin*2, margin*2, 8, 8);
                }

                if(loca.equals(ship.hyperjumpDestination))
                {
                    gr.setColor(Color.GREEN);
                    gr.drawRoundRect(x - margin, y - margin, margin*2, margin*2, 8, 8);
                }
            }
            
        }
    }

    private void drawSun(Graphics gr, int x, int y, int size, Solar.SunType stype)
    {
        if(stype == Solar.SunType.S_BLACK_HOLE)
        {            
            // suns are too small ... makes no sense to use images
            if(zoom < 0)
            {
                Image img = imageCache.suns[stype.ordinal()].getImage();
                gr.drawImage(img, x - size/2, y - size/2, size*2, size, null);
            }
            else
            {
                gr.drawOval(x - size/2, y - size/2, size, size);
            }
        }
        else
        {
            // suns are too small ... makes no sense to use images
            if(zoom < 0)
            {
                Image img = imageCache.suns[stype.ordinal()].getImage();
                gr.drawImage(img, x - size/2, y - size/2, size, size, null);
            }
            if(zoom < 7) 
            {
                gr.fillOval(x - size/2, y - size/2, size, size);
            }
            else
            {
                gr.fillRect(x, y, 2, 2);
            }
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
        gr.drawString("Galactic Map", 136, 43);

        final int left = width/2;

        gr.setColor(Color.GRAY);
        gr.drawRect(left+10, 11, 410, 48);
        gr.setColor(Color.BLACK);
        gr.fillRect(left+11, 12, 408, 46);
        
        gr.setColor(Color.GRAY);
        gr.drawRect(10, height-61, 410, 42);
        gr.setColor(Color.BLACK);
        gr.fillRect(11, height-60, 408, 40);

        gr.setFont(FontFactory.getNormal());
        gr.setColor(Color.CYAN);
        gr.drawString("Drag map by mouse. Zoom with mouse wheel.", 18, height-44);
        gr.drawString("Click system to set hyperjump destination. Double click to inspect.", 18, height-28);

        gr.setFont(FontFactory.getNormal());
        gr.setColor(Color.GREEN);
        gr.drawString("Map sector:", left+18, 30);
        gr.drawString("Jump dest.:", left+18, 50);

        // gr.setFont(FontFactory.getLarger());
        gr.setColor(Color.WHITE);
        gr.drawString(
                (centerX/128) + " ' " + (centerX & 127) +
                " / " +
                (centerY/128) + " ' " + (centerY & 127) +
                "", left+100, 30);

        if(ship.hyperjumpDestination == null) 
        {
            gr.drawString("Not set", left+100, 50);
        } 
        else 
        {
            gr.drawString(systemDistance, left+100, 50);
        }

        if(zoom <= 2) 
        {
            gr.setFont(FontFactory.getNormal());
        }
        else if(zoom == 3) 
        {
            gr.setFont(FontFactory.getSmaller());
        }

        // Hajo: list will be filled in showGalacticalSector()
        systemInfoList.clear();

        gr.setColor(mapBackgroundColor);
        gr.fillRect(100, 75, width-200, height-150);

        gr.setColor(Color.GRAY);
        gr.drawRect(100-1, 75-1, width-200+2, height-150+2);

        Shape clip = gr.getClip();
        gr.setClip(100, 75, width-200, height-150);

        drawHyperJumpRange(gr, width, height);
        
        for (int y = (centerY/128) - zoom * 2 - 1; y <= (centerY/128) + zoom * 2 + 1; y++) 
        {
            for (int x = (centerX/128) - zoom * 3 -1; x <= (centerX/128) + zoom * 3 + 1; x++) 
            {
                showGalacticalSector(gr,
                        width / 2 + (x * 128 - 64 - centerX)*2 / zoom,
                        height / 2 + (y * 128 - 64 - centerY)*2 / zoom,
                        x,
                        y);
            }
        }        
        
        gr.setClip(clip);
    }

    private void drawHyperJumpRange(Graphics gr, int width, int height)
    {
        final int maxRange = (int)(ship.equipment.getEffectiveDriveRange(ship.getCurrentMass())*256.0/(zoom*5));
        
        final int x = width / 2 + (ship.loca.galacticSectorI * 128 - 64 - centerX)*2 / zoom;
        final int y = height / 2 + (ship.loca.galacticSectorJ * 128 - 64 - centerY)*2 / zoom;
        
        final int shipX = x + ship.loca.ioff*2 / zoom;
        final int shipY = y + ship.loca.joff*2 / zoom;
        
        gr.setColor(hyperRangeColor);
        gr.fillOval(shipX - maxRange/2, shipY - maxRange/2, maxRange, maxRange);
        gr.setColor(Color.GRAY);
        gr.drawOval(shipX - maxRange/2, shipY - maxRange/2, maxRange, maxRange);
    }            
    
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
                else
                {
                    repaint();
                }
                
                break;
            }
        }
    }

    public final void updateControls()
    {
        SystemLocation there = ship.hyperjumpDestination;
        boolean ok = false;
        
        if(there != null)
        {
            SystemLocation here = ship.loca;
            
            double sectorDistance = there.distance(here);

            ok = ship.equipment.getEffectiveDriveRange(ship.type.totalMass) >= sectorDistance;
        }
        
        jumpButton.setEnabled(ok);
    }

        
    private class SystemInfo
    {
        SystemLocation loca;
        int xpos, ypos;   // Hajo: display position on map
    };
}
