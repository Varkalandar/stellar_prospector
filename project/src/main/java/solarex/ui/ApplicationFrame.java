/*
 * ApplicationFrame.java
 *
 * Created: 10-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
 

package solarex.ui;

import flyspace.SystemBuilder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;
import solarex.evolution.World;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.io.LoadSave;
import solarex.ship.Ship;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.ui.components.ImagedPanel;
import solarex.ui.interfaces.ShowPlanetCallback;
import solarex.ui.interfaces.ShowSystemCallback;
import solarex.ui.observables.ObservableToLabelConnector;
import solarex.ui.panels.*;
import solarex.util.ClockCallback;
import solarex.util.ClockThread;

/**
 * Main frame for the stellar system explorer.
 * 
 * @author Hj. Malthaner
 */
public class ApplicationFrame extends JFrame implements ShowSystemCallback, ShowPlanetCallback
{
    private static final Logger LOGGER = Logger.getLogger(ApplicationFrame.class.getName());
    
    /**
     * World master clock
     */
    private ClockThread clockThread;

    private TabularSystemPanel tabSys;
    private NavigationPanel navigationPanel;
    private GalaxyViewPanel galMap;
    private PlanetDetailPanel planetView;
    private SpaceStationPanel stationView;
    private SetupPanel setupPanel;

    private JScrollPane systemScroller;

    private JButton homeButton;
    private JButton setupButton;
    private JButton galacticMapButton;
    private JButton tabSysButton;
    private JButton navigationButton;
    private ImagedPanel titleBorderPanel;


    /**
     * This panel will hold all the situation-specific panels.
     */
    private JPanel panelPanel;

    /**
     * The players ship
     */
    private Ship ship;

    /**
     * The currently visited system
     */
    private Solar system;
    

    private SystemLocation loca = new SystemLocation();


    private World world;
    private Galaxy galaxy;
    
    /**
     * Set explorer mode. If set to true, only data for
     * explored planets and space bodies will be shown
     */
    public void setExplorerMode(boolean yesno)
    {
        ship.player.setExplorerMode(yesno);
    }

    public void makeSystem(long seed)
    {
        loca.systemSeed = seed;
        makeSystemWithHome(loca);
        switchToSystemView();
    }

    /**
     * Create star system.
     * 
     * @param loca the system location
     * @return The newly created system
     */
    @Override
    public Solar makeSystem(SystemLocation loca)
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        system = SystemBuilder.create(loca, true);
        Society.populate(system);
        
        tabSys.layoutSystem(system);
        systemScroller.getHorizontalScrollBar().setValue(0);
        navigationPanel.setSystem(system);
        setCursor(Cursor.getDefaultCursor());
        
        return system;
    }
    
    /**
     * Create star system.
     * 
     * @param loca the system location
     * @return player home, or null if there is no home body
     */
    public Solar makeSystemWithHome(SystemLocation loca)
    {
        makeSystem(loca);
        Solar home = null;
        
        // Hajo: look for home planet ...
        // this is a bit of a hack since the galaxy creation
        // routine may change at times.
        if(system != null)
        {
            ArrayList <Solar> list = new ArrayList<Solar>();
            system.listSettlements(list);
            for(Solar settlement : list)
            {
                // System.err.println(settlement.name + ", " + settlement.seed);
                
                if("Adams Transfer".equals(settlement.name) &&
                   1603634150277149804L == settlement.seed)
                {
                    ship.arrive(galaxy, settlement);
                    home = settlement;
                    break;
                }
            }
        }
        
        return home;
    }


    @Override
    public void showSystem(SystemLocation loca)
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final Solar home = makeSystemWithHome(loca);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setupPanel.setSeed(loca.systemSeed);
        
        if(home != null)
        {
            switchToStationView(home);
        }
        else
        {
            switchToSystemView();
        }
        
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void showSpaceBody(Solar body)
    {
        if(body.btype == Solar.BodyType.PLANET)
        {
            switchToPlanetView(body);
        }
        if(body.btype == Solar.BodyType.STATION ||
           body.btype == Solar.BodyType.SPACEPORT)
        {
            switchToStationView(body);
        }
    }

    private void removePanels()
    {
        panelPanel.removeAll();
    }

    private void switchToSystemView()
    {
        tabSys.layoutSystem(system);
        removePanels();

        galacticMapButton.setVisible(true);
        tabSysButton.setVisible(false);
        homeButton.setVisible(ship.getState() == Ship.State.DOCKED);

        panelPanel.add(systemScroller, BorderLayout.CENTER);
        tabSys.revalidate();
        
        validate();
        repaint();
    }

    @Override
    public void switchToNavigationView()
    {
        removePanels();

        galacticMapButton.setVisible(true);
        tabSysButton.setVisible(true);
        homeButton.setVisible(ship.getState() == Ship.State.DOCKED);

        panelPanel.add(navigationPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void switchToGalacticalView()
    {
        removePanels();

        homeButton.setVisible(ship.getState() == Ship.State.DOCKED);

        galacticMapButton.setVisible(false);
        tabSysButton.setVisible(true);
        panelPanel.add(galMap, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void switchToPlanetView(Solar planet)
    {
        removePanels();

        galacticMapButton.setVisible(true);
        tabSysButton.setVisible(true);
        homeButton.setVisible(ship.getState() == Ship.State.DOCKED);

        planetView.update(planet);
        panelPanel.add(planetView, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void switchToStationView(Solar station)
    {
        // System.err.println("Station seed: " + station.seed);
        
        removePanels();

        galacticMapButton.setVisible(true);
        tabSysButton.setVisible(true);
        homeButton.setVisible(false);

        stationView.update(station);
        panelPanel.add(stationView, BorderLayout.CENTER);
        
        validate();
        repaint();
    }

    private void switchToShipView()
    {
        removePanels();

        galacticMapButton.setVisible(true);
        tabSysButton.setVisible(true);
        homeButton.setVisible(ship.getState() == Ship.State.DOCKED);
        
        panelPanel.add(new ShipInfoPanel(ship), BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void switchToSetupView()
    {
        removePanels();

        tabSysButton.setVisible(true);
        homeButton.setVisible(ship.getState() == Ship.State.DOCKED);
        
        panelPanel.add(setupPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private void setLookAndFeel()
    {
        try
        {
            // Set cross-platform Java L&F (also called "Metal")
            final String lfc = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lfc);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public ApplicationFrame()
    {
        // Hajo: Init super class
        super();

        // Hajo: Init game data
        ship = new Ship();
        ship.cargo.money = 108.35;
        
        world = new World();
        
        // Hajo: Init game UI
        ImageCache imageCache = new ImageCache();
        initUI(imageCache);

        // Hajo: After UI init we also have a home system ...
        ship.loca = loca;

        setIconImage(imageCache.planets[4].getImage());
        showTitleFrame(imageCache);

        final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((size.width-getWidth())/2, (size.height-getHeight())/2 - 24);

        clockThread = new ClockThread();
        clockThread.addCallback(navigationPanel);
    }
    
    public void startClock()
    {
        clockThread.start();
    }

    private void initUI(ImageCache imageCache)
    {
        setLookAndFeel();

        /*
        panelPanel = new JPanel();
        panelPanel.setBackground(Color.BLACK);
        */

        ImagedPanel ipan = new ImagedPanel();
        ipan.setBackgroundImage(ImageCache.createImageIcon("/solarex/resources/backdrop/screen_bg.jpg", "").getImage());

        panelPanel = ipan;
        panelPanel.setLayout(new BorderLayout());
        
        galaxy = new Galaxy(imageCache.spiral.getImage());
        List <SystemLocation> list = galaxy.buildSector(-2, 0);
        loca = list.get(2);

        tabSys = new TabularSystemPanel(ship, imageCache);
        tabSys.setShowPlanetCallback(this);
        systemScroller = new JScrollPane(tabSys);
        ComponentFactory.customizeScrollpane(systemScroller);
        systemScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        
        adjustScrollbarUI(systemScroller.getHorizontalScrollBar());
        adjustScrollbarUI(systemScroller.getVerticalScrollBar());

        navigationPanel = new NavigationPanel(galaxy, ship, imageCache, this, this);
        
        galMap = new GalaxyViewPanel(galaxy, ship, imageCache);
        galMap.setCallbacks(this, navigationPanel);

        planetView = new PlanetDetailPanel(ship, imageCache);

        stationView = new SpaceStationPanel(world, galaxy, ship, imageCache);

        setupPanel = new SetupPanel(this);

        getContentPane().add(panelPanel);

        setTitle(TitlePanel.titleBase);
        setSize(1000, 740);
    }

    private void adjustScrollbarUI(JScrollBar bar)
    {
        bar.setBackground(Color.DARK_GRAY);
        // bar.setForeground(Color.GRAY);
    }

    private void showTitleFrame(ImageCache imageCache)
    {
        final TitlePanel titlePanel = new TitlePanel(imageCache);
        titleBorderPanel = new ImagedPanel();
        titleBorderPanel.setLayout(new GridBagLayout());
        titleBorderPanel.setBackgroundImage(imageCache.backdrops[4].getImage());
        titleBorderPanel.setOpaque(false);

        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        
        borderPanel.add(titlePanel);
        titleBorderPanel.add(borderPanel);
        panelPanel.add(titleBorderPanel);

        titleBorderPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                endTitleFrame();
            }
        });
        titleBorderPanel.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                endTitleFrame();
            }
        });

    }

    private void endTitleFrame()
    {
        removePanels();
        JPanel panel = createButtonPanel();
        setupPanel.setSeed(loca.systemSeed);
        showSystem(loca);
        
        getContentPane().add(panel, BorderLayout.SOUTH);
        getContentPane().validate();
        getContentPane().repaint();
    }


    private JPanel createButtonPanel()
    {
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 10, 5);
        
        ImagedPanel panel = new ImagedPanel();
        panel.setLayout(flow);
        panel.setBackgroundImage(ImageCache.createImageIcon("/solarex/resources/backdrop/metal_band.png", "").getImage());

        final JLabel shipStateLabel = new JLabel("");
        shipStateLabel.setForeground(Color.GREEN);
        shipStateLabel.setBackground(Color.BLACK);
        shipStateLabel.setPreferredSize(new Dimension(290, 28));
        shipStateLabel.setOpaque(true);
        shipStateLabel.setHorizontalAlignment(JLabel.CENTER);
        shipStateLabel.setVerticalAlignment(JLabel.TOP);
        shipStateLabel.setBorder(new LineBorder(Color.GRAY));
        shipStateLabel.setFont(FontFactory.getLabelHeading());
        panel.add(shipStateLabel);

        new ObservableToLabelConnector(ship.stateString,
                                       shipStateLabel)
        {
            @Override
            public void update(Observable o, Object arg)
            {
                String s = o.toString();
                
                StringBuilder text = new StringBuilder("<html>Ship state:&nbsp;");
                text.append("<font color=#FFFFFF>");

                if(ship.getState() == Ship.State.DOCKED)
                {
                    text.append("Docked at ").append(ship.loca.name);
                }
                else if(ship.getState() == Ship.State.ORBIT)
                {
                    text.append("Orbiting ").append(ship.loca.name);
                }
                else if(ship.getState() == Ship.State.FLIGHT)
                {
                    text.append("In flight");
                }
                else
                {
                    text.append(s);
                }

                text.append("</html>");

                shipStateLabel.setText(text.toString());
            }
        };

        homeButton = new JButton("Home");
        ComponentFactory.customizeButton(homeButton, "bt_home.png");
        panel.add(homeButton);

        galacticMapButton = new JButton("Galactic Map");
        ComponentFactory.customizeButton(galacticMapButton, "bt_galmap.png");
        panel.add(galacticMapButton);

        tabSysButton = new JButton("System Info");
        panel.add(tabSysButton);
        tabSysButton.setVisible(false);
        ComponentFactory.customizeButton(tabSysButton, "bt_sysinfo.png");

        navigationButton = new JButton("Navigation Map");
        ComponentFactory.customizeButton(navigationButton, "bt_navi.png");
        panel.add(navigationButton);      

        JButton shipButton = new JButton("Ship");
        ComponentFactory.customizeButton(shipButton, "bt_ship.png");
        panel.add(shipButton);

        JPanel timePanel = createTimePanel();
        panel.add(timePanel);

        setupButton = new JButton("Options");
        ComponentFactory.customizeButton(setupButton, "bt_options.png");
        panel.add(setupButton);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ship.getState() == Ship.State.DOCKED)
                {
                    ArrayList <Solar> list = new ArrayList <Solar> ();
                    system.listSettlements(list);
                    for(Solar settlement : list)
                    {
                        if(settlement.seed == ship.spaceBodySeed)
                        {
                            switchToStationView(settlement);
                            break;
                        }
                    }
                }
            }
        });

        navigationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToNavigationView();
            }
        });

        galacticMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 switchToGalacticalView();
            }
        });

        tabSysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToSystemView();
            }
        });

        shipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToShipView();
            }
        });


        setupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToSetupView();
            }
        });

        return panel;
    }


    private static String format10(int v)
    {
        return v < 10 ? "0" + v : "" + v;
    }

    private JPanel createTimePanel()
    {
        final JPanel timePanel = new JPanel();
        final JLabel timeLabel = new JLabel("00:00 2060/01/01");

        // timeLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        timeLabel.setFont(FontFactory.getLabelHeading());
        timeLabel.setForeground(Color.GREEN);
        timeLabel.setPreferredSize(new Dimension(155, 16));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timePanel.add(timeLabel);
        // timePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        timePanel.setBorder(new LineBorder(Color.GRAY, 1));
        timePanel.setBackground(Color.BLACK);

        clockThread.addCallback(new ClockCallback()
        {
            private int counter = 0;

            @Override
            public void ping100(int deltaT)
            {
                counter ++;

                if(counter > 8)
                {
                    counter = 0;
                    final int year = ClockThread.getYear();
                    final int month = ClockThread.getMonthOfYear() + 1;
                    final int day = ClockThread.getDayOfMonth() + 1;
                    final int hour = ClockThread.getHourOfDay();
                    final int minute = ClockThread.getMinuteOfHour();

                    String label =
                            "" +
                            format10(hour) + ":" + format10(minute) + "  " +
                            year + "/" + format10(month) + "/" + format10(day);

                    timeLabel.setText(label);
                }
            }
        });

        return timePanel;
    }

    public boolean saveGame()
    {
        boolean ok = true;

        try
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            LoadSave loadSave = new LoadSave();
            loadSave.saveGame(ship);
        }
        catch(Exception ex)
        {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            ok = false;
        }
        finally
        {
            setCursor(Cursor.getDefaultCursor());
        }

        return ok;
    }

    public boolean loadGame()
    {
        boolean ok = true;
        
        try
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            LoadSave loadSave = new LoadSave();
            loadSave.loadGame(ship);
        } 
        catch(Exception ex)
        {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            ok = false;
        }
        finally
        {
            setCursor(Cursor.getDefaultCursor());
        }

        if(ok)
        {
            final long seed = ship.spaceBodySeed;
            
            loca = ship.loca;
            makeSystemWithHome(loca);

            if(ship.getState() == Ship.State.DOCKED ||
               ship.getState() == Ship.State.ORBIT) 
            {
                Solar station = system.findBodyBySeed(seed);
                ship.arrive(galaxy, station);
                showSpaceBody(station);
            } 
            else
            {
                switchToSystemView();
            }
        }
        
        repaint();
        
        return ok;
    }
}
