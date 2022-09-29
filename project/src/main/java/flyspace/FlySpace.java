package flyspace;

import flyspace.oal.SoundPlayer;
import flyspace.ui.JumpEffectPainter;
import flyspace.ogl.GlLifecycle;
import flyspace.ogl.SpacePanel;
import flyspace.ui.Display;
import flyspace.ui.Fonts;
import flyspace.ui.HyperjumpPainter;
import flyspace.ui.TextPainter;
import flyspace.ui.TitlePainter;
import flyspace.ui.UiPanel;
import flyspace.ui.panels.CockpitPanel;
import flyspace.ui.panels.GalacticMapPanel;
import flyspace.ui.panels.OptionsPanel;
import flyspace.ui.panels.PlanetInfoPanel;
import flyspace.ui.panels.PlanetMiningPanel;
import flyspace.ui.panels.ShipInfoPanel;
import flyspace.ui.panels.StationPanel;
import flyspace.ui.panels.SystemInfoPanel;
import flyspace.ui.panels.SystemMapPanel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import solarex.evolution.World;
import solarex.galaxy.Galaxy;
import solarex.galaxy.SystemLocation;
import solarex.io.LoadSave;
import solarex.ship.Ship;
import solarex.system.Mining;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.ui.ImageCache;
import solarex.util.ClockThread;


/**
 *
 * @author Hj. Malthaner
 */
public class FlySpace
{
    public static final Logger logger = Logger.getLogger(FlySpace.class.getName());
    
    private static final String TITLE_VERSION = "Stellar Prospector r008";
    
    /**
     * World master clock
     */
    private ClockThread clockThread;
    
    private ImageCache imageCache;
    private SoundPlayer soundPlayer;
    
    public boolean quitRequested;
    private SpacePanel spacePanel;
    
    /** Space for the current system */
    private Space localSpace;
    
    /** Space of a distant system that e.g. is being inspected */
    private Space distantSpace;
    
    private StationPanel stationPanel;
    private SystemInfoPanel systemInfoPanel;
    private SystemInfoPanel distantSystemInfoPanel;
    private PlanetInfoPanel planetInfoPanel;
    private PlanetMiningPanel planetMiningPanel;
    private SystemMapPanel systemMapPanel;
    private GalacticMapPanel galacticMapPanel;
    private CockpitPanel cockpitPanel;
    private OptionsPanel optionsPanel;
    private ShipInfoPanel shipPanel;
    
    private UiPanel activePanel;

    /**
     * The players ship
     */
    private Ship ship;

    /**
     * The currently visited system
     */
    private Solar system;
    
    /**
     * The current system's location
     */
    private SystemLocation loca = new SystemLocation();

    /**
     * If ship is docked, this is the place where.
     */
    Solar currentBody;

    private World world;
    private Galaxy galaxy;
    private final Mining mining;

        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        FlySpace flySpace = new FlySpace();
        try
        {
            flySpace.createGL();
            flySpace.createAL();
            flySpace.createPanels();
            flySpace.run();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(FlySpace.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            flySpace.destroyAL();
            flySpace.destroyGL();
        }
    }

    
    public World getWorld()
    {
        return world;
    }

    
    public ImageCache getImageCache()
    {
        return imageCache;
    }
    
    
    public FlySpace()
    {

        /*
        canvas.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                newDisplayWidth = canvas.getWidth();
                newDisplayHeight = canvas.getHeight();
            }
        });
*/
        
        mining = new Mining();
        initGameData();
    }

    
    public void createGL() throws IOException
    {
        GlLifecycle.create(TITLE_VERSION, false);
        GlLifecycle.init();

        String iconPath = "/flyspace/resources/icon.png";
        InputStream is = getClass().getResourceAsStream(iconPath);
        
        GlLifecycle.setWindowIcon(is);
    }
    
    
    public void destroyGL()
    {
        GlLifecycle.destroy();
    }
    
    
    public void createAL() throws IOException
    {
        String [] samples = new String []
        {
            "/flyspace/resources/sfx/click.wav"
        };
        
        soundPlayer = new SoundPlayer();
        soundPlayer.init();
        soundPlayer.loadSamples(samples);
    }
    
    
    public void destroyAL()
    {
        if(soundPlayer != null)
        {
            soundPlayer.destroy();
            soundPlayer = null;
        }
    }
    
    
    public void playSound(int number, float volume)
    {
        soundPlayer.play(number, volume);
    }        
    
    
    public void createPanels() throws IOException
    {
        Fonts.init();

        logger.info("Creating space panel");
        spacePanel = new SpacePanel(this, ship, localSpace);
        
        logger.info("Creating station panel");
        stationPanel = new StationPanel(this, galaxy, ship, imageCache);
        
        logger.info("Creating system info panel");
        systemInfoPanel = new SystemInfoPanel(this, ship, imageCache);
        
        distantSystemInfoPanel = new SystemInfoPanel(this, ship, imageCache);
        planetInfoPanel = new PlanetInfoPanel(this, ship);
        planetMiningPanel = new PlanetMiningPanel(this, ship, mining);
        systemMapPanel = new SystemMapPanel(this, localSpace, ship);
        galacticMapPanel = new GalacticMapPanel(this, galaxy, ship);
        
        cockpitPanel = new CockpitPanel(this, ship);
        optionsPanel = new OptionsPanel(this, ship);
        shipPanel = new ShipInfoPanel(this, ship);
        
        currentBody = makeSystemWithHome(ship.loca);
        
        activatePanel(cockpitPanel);
        activatePanel(spacePanel);
        
        showStationPanel();

        clockThread = new ClockThread();
        clockThread.start();
    }
    
    
    public void run()
    {
        long lastTime = System.currentTimeMillis();
        int frameCount = 0;
        
        while(GlLifecycle.windowShouldClose() == false &&
              !quitRequested)
        {            
            long currentTime = System.currentTimeMillis();
            int dt = (int)(currentTime - lastTime);

            int sleep = 20 - dt;
            if(sleep > 0)
            {
                safeSleep(sleep);
            }
            
            update(dt);
            display();

            lastTime = currentTime;
            frameCount ++;
            
            GlLifecycle.pollAndSwap();
            // Display.sync(60);
        }
        
        logger.log(Level.FINE, "Exiting main loop.");
    }

    
    /**
     * Called before a frame is displayed. All updates to game data should
     * happen here.
     * @param dt Time passed since last update call
     */
    private void update(int dt)
    {
        localSpace.update(dt);
        activePanel.update(dt);
        cockpitPanel.update(dt);
        mining.update(dt);
    }

    
    /**
     * Display the frame
     */
    private void display()
    {
        activePanel.handleInput();
        activePanel.display();

        cockpitPanel.handleInput();
        cockpitPanel.display();
    }

    
    private void safeSleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        } 
        catch (InterruptedException ex)
        {
        }
    }

    /** 
     * Show station panel for current station
     */
    public void showStationPanel()
    {
        stationPanel.setStation(currentBody, localSpace);
        activatePanel(stationPanel);
    }
    
    /** 
     * Show station panel for a distant station
     * @param station The station to show
     */
    public void showStationPanel(Solar station)
    {
        stationPanel.setStation(station, localSpace);
        activatePanel(stationPanel);
    }
    
    public void showSystemInfoPanel()
    {
        systemInfoPanel.setSystem(localSpace, system);
        activatePanel(systemInfoPanel);
    }
    
    public void showDistantSystemInfoPanel(SystemLocation loca)
    {
        Solar distantSystem = makeSystem(loca, distantSpace, new TextPainter());
        
        distantSystemInfoPanel.setSystem(distantSpace, distantSystem);
        activatePanel(distantSystemInfoPanel);
    }

    public void showLocalPlanetDetailPanel(Solar planet)
    {
        planetInfoPanel.setPlanet(planet, localSpace);
        activatePanel(planetInfoPanel);
    }
    
    public void showDistantPlanetDetailPanel(Solar planet)
    {
        planetInfoPanel.setPlanet(planet, distantSpace);
        activatePanel(planetInfoPanel);
    }
    
    public void showPlanetMiningPanel(Solar planet)
    {
        planetMiningPanel.setPlanet(planet, localSpace);
        activatePanel(planetMiningPanel);
    }
    
    public void showGalacticMapPanel()
    {
        activatePanel(galacticMapPanel);
    }
    
    public void showSystemMapPanel()
    {
        systemMapPanel.setSystem(system);
        activatePanel(systemMapPanel);
    }
    
    public void showSpacePanel()
    {
        activatePanel(spacePanel);
    }

    public void showSpacePanel(Vec3 lookAt)
    {
        spacePanel.lookAt(lookAt);
        activatePanel(spacePanel);
    }
    
    public void showOptionsPanel()
    {
        activatePanel(optionsPanel);
    }

    public void showShipPanel()
    {
        activatePanel(shipPanel);
    }

    
    public void activatePanel(UiPanel panel)
    {
        // Insets insets = frame.getInsets();
        
        activePanel = panel;
        // activePanel.setSize(displayWidth - insets.left - insets.right, 
        //                     displayHeight - insets.top - insets.bottom);
        
        activePanel.setSize(Display.width, Display.height);
        activePanel.activate();
    }

    
    /**
     * Create star system.
     * 
     * @param loca the system location
     * @return The newly created system
     */
    private static Solar makeSystem(SystemLocation loca, Space space, JumpEffectPainter painter)
    {
        Solar system = SystemBuilder.create(loca, true);
        Society.populate(system);
        try 
        {
            // space.convertSystemParallelAndWait(system);
            space.convertSystemParallelAndPlayEffect(system, painter);
        }
        catch (InterruptedException ex) 
        {
            Logger.getLogger(FlySpace.class.getName()).log(Level.SEVERE, null, ex);
        }
        return system;
    }
    
    
    /**
     * Change current system. Creates the new system and
     * sets it as current system.
     * 
     * @param loca The location of the new system
     * @return The new system
     */
    public Solar changeSystem(SystemLocation loca)
    {
        // system = makeSystem(loca, localSpace, new JumpEffectPainter());
        system = makeSystem(loca, localSpace, new HyperjumpPainter());
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
        system = makeSystem(loca, localSpace, new TitlePainter(TITLE_VERSION));
        Solar home = null;
        
        // Hajo: look for home planet ...
        // this is a bit of a hack since the galaxy creation
        // routine may change at times.
        if(system != null)
        {
            ArrayList <Solar> list = new ArrayList<>();
            system.listSettlements(list);
            for(Solar settlement : list)
            {
                // System.err.println(settlement.name + ", " + settlement.seed);
                
                if("Seyfert Transfer".equals(settlement.name))
                {
                    ship.arrive(galaxy, settlement);
                    home = settlement;
                    break;
                }
            }
        }
        
        return home;
    }
    
    
    private void initGameData()
    {
        ship = new Ship();
        ship.cargo.money = 10008.35;
        localSpace = new Space();
        distantSpace = new Space();
    
        world = new World();
        
        // Hajo: Init game UI
        imageCache = new ImageCache();
        
        galaxy = new Galaxy(imageCache.spiral.getImage());
        List <SystemLocation> list = galaxy.buildSector(-2, 0);
        loca = list.get(2);

        // Hajo: sync data
        ship.loca = loca;
    }

    
    public void dockAt(Solar body)
    {
        currentBody = body;
        ship.arrive(galaxy, body);
        showStationPanel();
    }

    
    public boolean loadGame() 
    {
        boolean ok = true;
        
        try
        {
            // frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            LoadSave loadSave = new LoadSave();
            loadSave.loadGame(ship);
        } 
        catch(IOException ex)
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ok = false;
        }
        finally
        {
            // frame.setCursor(Cursor.getDefaultCursor());
        }

        if(ok)
        {
            final long seed = ship.spaceBodySeed;
            
            loca = ship.loca;
            makeSystem(loca, localSpace, new TextPainter());

            if(ship.getState() == Ship.State.DOCKED ||
               ship.getState() == Ship.State.ORBIT) 
            {
                currentBody = system.findBodyBySeed(seed);
                ship.arrive(galaxy, currentBody);
                showStationPanel();
            } 
            else
            {
                showSpacePanel();
            }
        }
        
        return ok;
    }

    
    public boolean saveGame() 
    {
        boolean ok = true;

        try
        {
            // frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            LoadSave loadSave = new LoadSave();
            loadSave.saveGame(ship);
        }
        catch(IOException ex)
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ok = false;
        }
        finally
        {
            // frame.setCursor(Cursor.getDefaultCursor());
        }

        return ok;
    }

    
    private class MyWindowListener implements WindowListener
    {

        public MyWindowListener()
        {
        }

        @Override
        public void windowOpened(WindowEvent e)
        {
        }

        @Override
        public void windowClosing(WindowEvent e)
        {
            quitRequested = true;
        }

        @Override
        public void windowClosed(WindowEvent e)
        {
        }

        @Override
        public void windowIconified(WindowEvent e)
        {
        }

        @Override
        public void windowDeiconified(WindowEvent e)
        {
        }

        @Override
        public void windowActivated(WindowEvent e)
        {
        }

        @Override
        public void windowDeactivated(WindowEvent e)
        {
        }
    }
}
