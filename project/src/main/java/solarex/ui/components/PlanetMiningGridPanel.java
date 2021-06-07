/*
 * PlanetMiningGridPanel.java
 *
 * Created on 2012/11/06
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ui.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observer;
import java.util.Random;
import javax.swing.JPanel;
import solarex.ship.Ship;
import solarex.ship.components.ShipComponent;
import solarex.system.PlanetMiningData;
import solarex.system.PlanetResources;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.panels.PlanetDetailPanel;
import solarex.ui.FontFactory;

/**
 * Planet display for mining actions.
 * 
 * @author Hj. Malthaner
 */
public class PlanetMiningGridPanel extends JPanel
{
    private ImageCache imageCache;
    private Color highlight = new Color(200, 255, 200, 128);
    private Color gridColor = new Color(0, 255, 0, 128);
    
    private int activeGridX = 5;
    private int activeGridY = 4;
    private int droneGridX = -1;
    private int droneGridY = -1;
    private final int grid = 8;
    private final int yoff = 8;
    
    private ShipComponent drone;
    private Solar planet;
    private Ship ship;
    
    private PlanetMiningData planetMiningData;
    
    private Observer observer;
    
    final int [] deposits;
    final long [] depositPositions;
    private final boolean[] harvested;
    
    final int [] gases;
    private final int[] pools;
    private final long[] poolPositions;
    
    public PlanetMiningGridPanel(Solar planet, Ship ship, ImageCache imageCache, ShipComponent drone, Observer observer)
    {
        this.ship = ship;
        this.observer = observer;
        this.planet = planet;
        this.imageCache = imageCache;
        this.drone = drone;
        
        planetMiningData = new PlanetMiningData();
        
        setBackground(new Color(0, 9, 17));

        // Hajo: order must be preserved.
        Random rng = PlanetResources.getPlanetRng(planet);
        gases = new int [PlanetResources.Gases.values().length];
        PlanetResources.calculateAtmosphere(planet, rng, gases);

        
        final int fluidsCount = PlanetResources.Fluids.values().length;
        
        pools = new int [fluidsCount];
        poolPositions = new long [fluidsCount];
        PlanetResources.calculateFluids(planet, gases, rng, pools, poolPositions);
        
        final int metalsCount = PlanetResources.Metals.values().length;

        deposits = new int [metalsCount];
        depositPositions = new long [metalsCount];
        PlanetResources.calculateMetals(planet, rng, deposits, depositPositions);
        
        harvested = new boolean[64];
        
        MiningMouseHandler handler = new MiningMouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        
        updateSurfaceObserver();
        updateDroneObserver();
    }
    
    @Override
    public void paint(Graphics gr)
    {
        super.paint(gr);
        
        ((Graphics2D)gr).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        
        final Image img;
        
        if(planet != null)
        {
            img = imageCache.planets[planet.ptype.ordinal()].getImage();
        }
        else
        {
            img = imageCache.planets[Solar.PlanetType.ATM_ROCK.ordinal()].getImage();
        }
        final int w = getWidth();
        final int h = getHeight();
        
        gr.drawImage(img, 8, 8, w-48, h-16, null);
        
        gr.setColor(gridColor);
        
        final int xstep = w/grid;
        final int ystep = h/grid;

        final int ww = ((w-2*xstep) / xstep) * xstep;
        final int hh = ((h-2*ystep) / ystep) * ystep;
        
        for(int x=0; x<=ww; x += xstep)
        {
            gr.fillRect(x+xstep, ystep+1+yoff, 1, hh-1);
        }
        for(int y=0; y<=hh; y += ystep)
        {
            gr.fillRect(xstep+1, y+ystep+yoff, ww-1, 1);
        }
        
        Font font = FontFactory.getSmallest();
        gr.setFont(font);
        
        gr.setColor(Color.WHITE);
        gr.drawString(drone.getName(), droneGridX*xstep + xstep/2 - 5, droneGridY*ystep + ystep/2 - 5);
        gr.drawOval(droneGridX*xstep + xstep/2 - 5, droneGridY*ystep + ystep/2 - 5 + yoff, 10, 10);
        gr.setColor(Color.BLACK);
        gr.drawOval(droneGridX*xstep + xstep/2 - 10, droneGridY*ystep + ystep/2 - 10 + yoff, 20, 20);
        
        gr.setColor(highlight);
        
        gr.fillRect(activeGridX*xstep+1, activeGridY*ystep+1+yoff, xstep-1, ystep-1);
        
        font = FontFactory.getNormal();
        gr.setFont(font);
        gr.setColor(Color.CYAN);
        
        gr.drawString("Click quadrant to explore", 7, 30);
    }

    private void updateSurfaceObserver()
    {
        PlanetMiningData.Surface surface = 
                planetMiningData.getSurfaceData(planet, activeGridX-1, activeGridY-1);

        if(observer != null)
        {
            observer.update(null, surface);
        }
    }

    private void updateDroneObserver()
    {
        final int gridIndex = (droneGridY * 6 + droneGridX);

        if(ship != null && ship.player != null && 
           droneGridX >= 1 && droneGridY >= 1 && droneGridX < 7 && droneGridY < 7)
        {
            ship.player.setExplored(planet, gridIndex);
        }

        if(observer != null)
        {
            // Hajo: drone type?
            if(drone.getName().contains("Gas Filtration Drone"))
            {
                String s = "Normal atmosphere.";
                observer.update(null, s);
            }
            else if(drone.getName().contains("Fluid Distillation Drone"))
            {
                String s = buildFluidsReport(gridIndex);
                observer.update(null, s);                
            }
            else
            {
                String s = buildMininghReport(gridIndex);
                observer.update(null, s);
            }
        }
    }

    public void doMining()
    {
        final int gridIndex = (droneGridY * 6 + droneGridX);
        
        // Hajo: drone type?
        if(drone.getName().contains("Gas Filtration Drone"))
        {
            if(Math.random() < 0.01)
            {
                // drone took damage
                observer.update(null, "A sudden storm damaged the drone!");
            }
            else
            {
                int [] amounts = new int [gases.length];

                for(int i=0; i<gases.length; i++) 
                {
                    if(gases[i] != 0) 
                    {
                        amounts[i] += gases[i] + (int)(Math.random() * gases[i]);
                    }
                }

                observer.update(null, amounts);
            }
        }
        else if(drone.getName().contains("Fluid Distillation Drone"))
        {
            int [] amounts = new int [pools.length];
            
            if(!harvested[gridIndex])
            {
                for(int i=0; i<pools.length; i++) 
                {
                    if(pools[i] != 0) 
                    {
                        amounts[i] += pools[i] + (int)(Math.random() * pools[i]);
                    }
                }
                harvested[gridIndex] = true;
            }

            observer.update(null, amounts);                        
        }
        else
        {
            if(Math.random() < 0.01)
            {
                // drone took damage
                drone.setCurrentDurability(drone.getMaxDurability() - 5);
                observer.update(null, "<html>A sudden meteor shower damaged the " +
                        drone.getName() + 
                        " !</html>");
            }
            else if(Math.random() < 0.10)
            {
                // drone took damage
                drone.setCurrentDurability(drone.getMaxDurability() - 2);
                observer.update(null, "The surface caved in under the drone!");
            }
            else if(Math.random() < 0.10)
            {
                // drone took damage
                drone.setCurrentDurability(drone.getMaxDurability() - 1);
                observer.update(null, "<html>A landslide was caused by the mining attempt<br>and damaged the " + 
                        drone.getName() + 
                        " !</html>");
            }
            else
            {
                final int [] amounts = new int [deposits.length];
                final int bit = 1 << gridIndex;

                if(!harvested[gridIndex])
                {
                    for(int i=0; i<deposits.length; i++) 
                    {
                        if(deposits[i] != 0) 
                        {
                            if((depositPositions[i] & bit) != 0)
                            {
                                amounts[i] += deposits[i] + (int)(Math.random() * deposits[i]);
                            }
                        }
                    }

                    harvested[gridIndex] = true;
                }

                observer.update(null, amounts);
            }
        }
    }

    private String buildFluidsReport(int gridIndex)
    {
        final StringBuilder builder = new StringBuilder();

        if(droneGridX == -1 || droneGridY == -1)
        {
            builder.append("<html>Drone Report:<br><br>Not landed yet.</html>");
        }
        else if(harvested[gridIndex])
        {
            builder.append("<html>Drone Report:<br><br>Quadrant was harvested already.</html>");
        }
        else
        {
            PlanetResources.Fluids [] values = PlanetResources.Fluids.values();
            ArrayList <String> alist = new ArrayList();
            for(int i=0; i<pools.length; i++) 
            {
                if(pools[i] != 0) 
                {
                    final int bit = 1 << (droneGridY * 6 + droneGridX);
                    if((poolPositions[i] & bit) != 0)
                    {
                        String s;                
                        s = PlanetDetailPanel.richness[Math.min(pools[i], PlanetDetailPanel.richness.length-1)];
                        s = s.replaceFirst("%1", values[i].toString().toLowerCase());
                        s = s.replaceFirst("%2", values[i].color);
                        s = s.replace("depsits", "pools");

                        // Hajo: add "sort order" hint as first character
                        char order = (char)('z' - deposits[i]);
                        alist.add("" + order + s);
                    }
                }
            }

            Collections.sort(alist);


            builder.append("<html>Drone Report:<br><br>");

            if(alist.isEmpty())
            {
                builder.append("No suitable fluid pools found in this quadrant.<br>");
            }
            else
            {
                for(String s : alist)
                {
                    builder.append(s.substring(1));
                    builder.append("<br>");
                }
            }
            builder.append("</html>");
        }

        return builder.toString();
    }

    private String buildMininghReport(int gridIndex)
    {
        final StringBuilder builder = new StringBuilder();

        if(droneGridX == -1 || droneGridY == -1)
        {
            builder.append("<html>Drone Report:<br><br>Not landed yet.</html>");
        }
        else if(harvested[gridIndex])
        {
            builder.append("<html>Drone Report:<br><br>Quadrant was mined already.</html>");
        }
        else
        {
            final int bit = 1 << (droneGridY * 6 + droneGridX);
            PlanetResources.Metals [] values = PlanetResources.Metals.values();
            ArrayList <String> alist = new ArrayList();
            
            for(int i=0; i<deposits.length; i++) 
            {
                if(deposits[i] != 0) 
                {
                    if((depositPositions[i] & bit) != 0)
                    {
                        String s;                
                        s = PlanetDetailPanel.richness[Math.min(deposits[i], PlanetDetailPanel.richness.length-1)];
                        s = s.replaceFirst("%1", values[i].toString().toLowerCase());
                        s = s.replaceFirst("%2", values[i].color);

                        // Hajo: add "sort order" hint as first character
                        char order = (char)('z' - deposits[i]);
                        alist.add("" + order + s);
                    }
                }
            }

            Collections.sort(alist);


            builder.append("<html>Drone Report:<br><br>");

            if(alist.isEmpty())
            {
                builder.append("No minable resources found in this quadrant.<br>");
            }
            else
            {
                for(String s : alist)
                {
                    builder.append(s.substring(1));
                    builder.append("<br>");
                }
            }
            builder.append("</html>");
        }

        return builder.toString();
    }

    private class MiningMouseHandler implements MouseListener, MouseMotionListener
    {
        private final int [] xy = new int [2];

        @Override
        public void mouseClicked(MouseEvent e)
        {
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        /**
         * If a grid was clicked, place drone there.
         * 
         * @param e Mouse button press event.
         */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            mouseToGrid(e.getX(), e.getY(), xy);
            
            if(xy[0] >= 1 && xy[0] < 7 && xy[1] >= 1 && xy[1] < 7 &&
               (xy[0] != droneGridX || xy[1] != droneGridY))
            {
                droneGridX = xy[0];
                droneGridY = xy[1];
                
                updateDroneObserver();
                
                repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
        }

        /**
         * Display surface info while mouse hovers over
         * a grid.
         * 
         * @param e Mouse move event.
         */
        @Override
        public void mouseMoved(MouseEvent e)
        {
            mouseToGrid(e.getX(), e.getY(), xy);
            
            if(planet != null &&
               xy[0] >= 0 && xy[0] < 7 && xy[1] >= 0 && xy[1] < 7 &&
               (xy[0] != activeGridX || xy[1] != activeGridY))
            {
                activeGridX = xy[0];
                activeGridY = xy[1];
                
                updateSurfaceObserver();
                
                repaint();
            }            
        }

        private void mouseToGrid(int mx, int my, int [] result)
        {
            final int w = getWidth();
            final int h = getHeight();
                
            final int xstep = w/grid;
            final int ystep = h/grid;

            my -= yoff;

            result[0] = mx / xstep;
            result[1] = my / ystep;
        }
    }
}
