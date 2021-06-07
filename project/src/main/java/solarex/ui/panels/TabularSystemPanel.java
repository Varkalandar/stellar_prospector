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
 

package solarex.ui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import static javax.swing.JLayeredPane.DEFAULT_LAYER;
import javax.swing.border.LineBorder;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.components.BodyInfoLabel;
import solarex.ui.components.ImagedPanel;
import solarex.ui.interfaces.ShowPlanetCallback;
import solarex.ui.FontFactory;


/**
 * Tabular system view panel.
 * @author Hj. Malthaner
 */
public class TabularSystemPanel extends JLayeredPane
{

    private static Point right = new Point (1, 0);
    private static Point up = new Point (0, -1);

    // private Font font = FontFactory.getSmallest();
    private Font font = FontFactory.getSmaller();

    private Color clearColor = new Color(255, 255, 255, 0);

    private BodyInfoLabel infoLabel;

    private ImageCache imageCache;

    private ShowPlanetCallback showPlanetCallback;

    /** The players ship */
    private Ship ship;


    public void setShowPlanetCallback(ShowPlanetCallback c)
    {
        showPlanetCallback = c;
    }

    /**
     * Maximum coordinates reached while laying out the system.
     */
    private int maxX, maxY;

    private int scale(Solar body)
    {
        // return (int)Math.sqrt(body.radius)/5 + 4;
        // return (int)Math.max(Math.pow(body.radius, 0.65)/18, 4);
        return (int)Math.max(Math.pow(body.radius, 0.5)/3.2, 4);
        // return (int)Math.max(Math.log1p(body.radius)*25.0, 4);
    }


    private void layoutBody(final Solar body,
                            final int startX, final int startY, int spacing)
    {
        // Hajo: Handle starports
        if(body.children.size() > 0) 
        {
            Solar testee = body.children.get(0);
            if(testee.btype == Solar.BodyType.SPACEPORT) 
            {
                layoutBody(testee, startX, startY, spacing);
            }
        }

        // Hajo: Now lay out the planet
        final JButton button = new JButton();
        button.setText("<html><center>" + body.name + "</center></html>");
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFont(font);
        
        if(ship.spaceBodySeed == body.seed)
        {
            button.setBorder(new LineBorder(Color.YELLOW, 1));
        }
        else
        {
            button.setBorder(new LineBorder(clearColor, 1));
        }
        
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFocusPainted(false);

        int scale = scale(body);
        final int w = Math.max(scale, spacing);
        final int h = Math.max(scale+24, spacing);
        int layer = JLayeredPane.DEFAULT_LAYER;

        if(body.btype == Solar.BodyType.SPACEPORT ||
           body.btype == Solar.BodyType.STATION) 
        {
            button.setForeground(Color.ORANGE);
        }

        if(body.btype == Solar.BodyType.SPACEPORT) 
        {
            button.setVerticalTextPosition(AbstractButton.CENTER);
            button.setHorizontalTextPosition(AbstractButton.LEADING);
            button.setHorizontalAlignment(SwingConstants.RIGHT);
            button.setLocation(startX - 68 + 2 + w/2, startY+(h-58)/2);
            button.setSize(68, 40);
            layer ++;
        }
        else 
        {
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setLocation(startX, startY+h/16);
            button.setSize(w, h-h/8);
        }
        
        ImageIcon icon;

        if(body.btype == Solar.BodyType.SUN) 
        {
            // Hajo: accretiation disk is much wider than the black hole itself.
            if(body.stype == Solar.SunType.S_BLACK_HOLE)
            {
                icon = new ImageIcon(imageCache.suns[body.stype.ordinal()]
                                      .getImage()
                                      .getScaledInstance(254, 63, Image.SCALE_SMOOTH));
                button.setLocation(startX, startY+h/4-4);
                button.setSize(w*3, h*3/4);
            }
            else
            {
                icon = new ImageIcon(imageCache.suns[body.stype.ordinal()]
                                      .getImage()
                                      .getScaledInstance(scale, scale, Image.SCALE_SMOOTH));
                button.setSize(w, h);
                button.setLocation(startX, startY);
            }
        } 
        else if(body.btype == Solar.BodyType.PLANET) 
        {
            if(body.ptype == Solar.PlanetType.RINGS)
            {
                // Hajo: ringed planets must be shown wider than usual
                icon = new ImageIcon(imageCache.planets[body.ptype.ordinal()]
                                      .getImage()
                                      .getScaledInstance(scale*20/16, scale*17/16, Image.SCALE_SMOOTH));
            }
            else
            {
                icon = new ImageIcon(imageCache.planets[body.ptype.ordinal()]
                                      .getImage()
                                      .getScaledInstance(scale, scale, Image.SCALE_SMOOTH));
            }
        }
        else if(body.btype == Solar.BodyType.SPACEPORT) 
        {
            icon = new ImageIcon(imageCache.spaceport
                                  .getImage()
                                  .getScaledInstance(scale, scale, Image.SCALE_SMOOTH));
        } 
        else 
        {
            icon = new ImageIcon(imageCache.station
                                  .getImage()
                                  .getScaledInstance(scale, scale, Image.SCALE_SMOOTH));
        }

        button.setIcon(icon);
        add(button, new Integer(layer));

        // System.err.println("" + body.name + " displayed at " + startX + "," + startY);

        // Hajo: track maximum coordinated

        int mx = startX + button.getWidth();
        int my = startY + button.getHeight();

        if(mx > maxX) 
        {
            maxX = mx;
        }
        if(my > maxY) 
        {
            maxY = my;
        }

        button.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
                infoLabel.update(body, ship.player.isExplored(body));

                infoLabel.setLocation(Math.max(button.getX(), 16),
                                      Math.max(button.getY() + Math.min(button.getHeight()+3, 100), 518));
                
                if(ship.loca.equals(body.loca) || ship.player.canInspect(body))
                {
                    button.setBorder(new LineBorder(Color.GREEN, 1));
                }
                else 
                {
                    button.setBorder(new LineBorder(Color.DARK_GRAY, 1));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                infoLabel.setText("");
                if(ship.spaceBodySeed == body.seed)
                {
                    button.setBorder(new LineBorder(Color.YELLOW, 1));
                }
                else
                {
                    button.setBorder(new LineBorder(clearColor, 1));
                }
            }
        });

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(body.btype == Solar.BodyType.PLANET ||
                   body.btype == Solar.BodyType.SPACEPORT ||
                   body.btype == Solar.BodyType.STATION)
                {
                    // Only in our current system we can access
                    // detail info ...
                    if(ship.loca.equals(body.loca) ||
                       ship.player.canInspect(body))
                    {
                        infoLabel.setText("");
                        button.setBorder(new LineBorder(clearColor, 1));
                        showPlanetCallback.showSpaceBody(body);
                    }
                }
            }
        });
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
                    layoutChildren(body, right, p.x+spacing, p.y, spacing);
                }
                else 
                {
                    layoutChildren(body, up, p.x+spacing/4, p.y-spacing/2, spacing/2);
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
        removeAll();

        // Hajo: init size tracking
        maxX = 0;
        maxY = 0;

        // Hajo: layout defaults
        int h = 620;
        int spacing = 128;
        int bottom = 220;

        int scale = scale(system);
        Point cur;
        if(scale < spacing) {
            cur = new Point(0, h - bottom);
        } else {
            if(scale < spacing * 3) {
                cur = new Point(spacing-scale/2, h - bottom - ((scale+24)-spacing)/2);
            } else {
                cur = new Point(spacing-scale*3/4, h - bottom - ((scale+24)-spacing)/2);
            }
        }


        infoLabel = new BodyInfoLabel();

        infoLabel.setLocation(maxX/2-100, 442);
        infoLabel.setSize(200, 128);
        infoLabel.setFont(FontFactory.getNormal());
        
        add(infoLabel, new Integer(JLayeredPane.DEFAULT_LAYER + 10));

        layoutBody(system, cur.x, cur.y, spacing);
        
        if(system.stype == Solar.SunType.S_BLACK_HOLE)
        {
            cur.x += 256;
        }
        
        layoutChildren(system, right, cur.x + Math.max(scale, spacing), h-bottom, spacing);


        // Hajo: set size as found in layout process, give some margin

        final int width = getWidth();
        final int height = getHeight();
        
        final int prefW = Math.max(maxX+20, width);
        final int prefH = Math.max(maxY+11, height);

        Dimension size = new Dimension(prefW, prefH);
        setMinimumSize(size);
        setPreferredSize(size);

        int layer = DEFAULT_LAYER-1;
        final ImagedPanel background = new ImagedPanel();
        background.setOpaque(false);

        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                adjustBackgroundSize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                adjustBackgroundSize();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
            
            private void adjustBackgroundSize()
            {
                Dimension size = getParent().getSize();
                
                size.width = Math.max(size.width, getWidth());
                size.height = Math.max(size.height, getHeight());
                
                background.setSize(size);
            }
        });
        
        background.setBackgroundImage(imageCache.backdrops[0].getImage());
        add(background, new Integer(layer));

        JLabel systemNameLabel = new JLabel(system.baseName + " System Information");
        systemNameLabel.setFont(FontFactory.getPanelHeading());
        systemNameLabel.setSize(400, 32);
        systemNameLabel.setLocation(32, 24);
        systemNameLabel.setForeground(Color.WHITE);
        add(systemNameLabel);
        
        validate();        
    }


    public TabularSystemPanel(Ship ship, ImageCache imageCache)
    {
        this.ship = ship;
        
        setBackground(Color.BLACK);
        setOpaque(true);
        setLayout(null);

        this.imageCache = imageCache;
    }
}
