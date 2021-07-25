package flyspace.ui.panels;

import flyspace.View;
import flyspace.FlySpace;
import flyspace.Space;
import flyspace.MultiMesh;
import flyspace.math.Math3D;
import flyspace.ogl32.ShaderBank;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.LabeledTrigger;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import java.text.NumberFormat;
import java.util.Random;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.opengl.GL20;
import solarex.ship.Ship;
import solarex.system.Matrix4;
import solarex.system.PlanetResources;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.ui.panels.PlanetDetailPanel;

/**
 *
 * @author Hj. Malthaner
 */
public class PlanetInfoPanel extends DecoratedUiPanel
{
    private Solar planet;
    private final FlySpace game;
    private final Ship ship;
    
    private MultiMesh planetMesh;
    private View camera;
    
    private boolean clicked;
    private final DecoratedTrigger loungeTrigger;
    private Space space;
    private final LabeledTrigger gasesTrigger;
    private final LabeledTrigger fluidsTrigger;
    private final LabeledTrigger mineralsTrigger;
    private final LabeledTrigger metalsTrigger;
    private final LabeledTrigger biosphereTrigger;
    private final LabeledTrigger miscTrigger;
    
    private String [] gases;
    private String [] fluids;
    private String [] minerals;
    private String [] metals;
    private String [] biosphere;
    private String [] misc;
    
    private Trigger activeTrigger;
    
    public PlanetInfoPanel(FlySpace game, Ship ship) 
    {
        super(null);

        this.game = game;
        this.ship = ship;
        this.camera = new View();
        
        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", 
                Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        loungeTrigger.setArea(934, 200, 185, 24);
        addTrigger(loungeTrigger);
        
        int tabX = 351;
        int tabY = 656;
        
        gasesTrigger = new LabeledTrigger(Fonts.g12, "Gases", Colors.TRIGGER_TEXT, Colors.TRIGGER_SELECTED);
        gasesTrigger.setArea(tabX, tabY, 100, 24);
        addTrigger(gasesTrigger);
        tabY -= 24;
        
        fluidsTrigger = new LabeledTrigger(Fonts.g12, "Fluids", Colors.TRIGGER_TEXT, Colors.TRIGGER_SELECTED);
        fluidsTrigger.setArea(tabX, tabY, 100, 24);
        addTrigger(fluidsTrigger);
        tabY -= 24;
        
        mineralsTrigger = new LabeledTrigger(Fonts.g12, "Minerals", Colors.TRIGGER_TEXT, Colors.TRIGGER_SELECTED);
        mineralsTrigger.setArea(tabX, tabY, 100, 24);
        addTrigger(mineralsTrigger);
        tabY -= 24;

        metalsTrigger = new LabeledTrigger(Fonts.g12, "Metals", Colors.TRIGGER_TEXT, Colors.TRIGGER_SELECTED);
        metalsTrigger.setArea(tabX, tabY, 100, 24);
        addTrigger(metalsTrigger);
        tabY -= 24;

        biosphereTrigger = new LabeledTrigger(Fonts.g12, "Biosphere", Colors.TRIGGER_TEXT, Colors.TRIGGER_SELECTED);
        biosphereTrigger.setArea(tabX, tabY, 100, 24);
        addTrigger(biosphereTrigger);
        tabY -= 24;
        
        miscTrigger = new LabeledTrigger(Fonts.g12, "Misc.", Colors.TRIGGER_TEXT, Colors.TRIGGER_SELECTED);
        miscTrigger.setArea(tabX, tabY, 100, 24);
        addTrigger(miscTrigger);
        tabY -= 24;
        
        activateTrigger(gasesTrigger);
    }
    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
        
        ShaderBank.setupMatrices(250, 250);
        ShaderBank.updateViewMatrix(new Matrix4());
        ShaderBank.updateLightPos(-10000.0f, 0.0f, 10000.0f);
    }

    @Override
    public void handlePanelInput() 
    {
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        
        if(Mouse.isButtonDown(0) == false)
        {
            if(clicked)
            {
                int mx = Mouse.getX();
                int my = Mouse.getY();
                
                Trigger t = trigger(mx, my);
                
                if(t == loungeTrigger)
                {
                    game.showStationPanel();
                }
                else if(t == gasesTrigger)
                {
                    activateTrigger(t);
                }
                else if(t == fluidsTrigger)
                {
                    activateTrigger(t);
                }
                else if(t == mineralsTrigger)
                {
                    activateTrigger(t);
                }
                else if(t == metalsTrigger)
                {
                    activateTrigger(t);
                }
                else if(t == biosphereTrigger)
                {
                    activateTrigger(t);
                }
                else if(t == miscTrigger)
                {
                    activateTrigger(t);
                }
                
                clicked = false;
            }
        }
    }

    @Override
    public void displayPanel() 
    {
        setupTextPanel(width, height);
        
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        displayBackground(0xFF000022, 0xFF001133, 0xFF112244);

        displayTitle(planet.name + " Details");
        
        displayPlanetInfo();
        
        displayListSpace();
        
        displayTriggers();

        glViewport(50, 390, 250, 250);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);  
        glEnable(GL_BLEND);        
        
        rotatePlanet(planetMesh);
        
        Matrix4 modelMatrix = new Matrix4();
        
        Matrix4.translate(new Vec3(0, 0, -planet.radius / 30), modelMatrix, modelMatrix);
        Matrix4.rotate(Math3D.degToRad(planetMesh.getAngleZ()), new Vec3(0, 0, 1), 
                modelMatrix, modelMatrix);
        Matrix4.rotate(Math3D.degToRad(planetMesh.getAngleY()), new Vec3(0, 1, 0), 
                modelMatrix, modelMatrix);
        Matrix4.rotate(Math3D.degToRad(planetMesh.getAngleX()), new Vec3(1, 0, 0), 
                modelMatrix, modelMatrix);
        
        GL20.glUseProgram(ShaderBank.shadedProgId);
        
        ShaderBank.updateModelMatrix(modelMatrix);
        ShaderBank.uploadShadedMatrices();
        
        planetMesh.display();

        GL20.glUseProgram(0);
    }

    
    public void rotatePlanet(MultiMesh mesh)
    {
        float angleY = mesh.getAngleY();
        angleY += 0.02;
        if(angleY > 360) angleY -= 360;
        
        mesh.setAngleY(angleY);
    }
    
    
    public void setPlanet(Solar planet, Space space) 
    {
        this.space = space;
        this.planet = planet;
        
        planetMesh = space.findMeshForPeer(planet);

        /*
        camera.setPos(planetMesh.getPos());
        camera.addToZ(planet.radius * 4 * Space.DISPLAY_SCALE);
        
        camera.lookDirection(new Vec3(0, 0, -1));
        */
        
        Random rng = PlanetResources.getPlanetRng(planet);
        
        int [] weights = new int [PlanetResources.Gases.values().length];
        gases = PlanetDetailPanel.calculateAtmosphere(planet, rng, weights);

        int [] fluidDeposits = new int [PlanetResources.Fluids.values().length];
        long [] positions = new long [PlanetResources.Fluids.values().length];
        fluids = PlanetDetailPanel.calculateFluids(planet, rng, weights, fluidDeposits, positions);
        
        int [] deposits = PlanetResources.calculateMinerals(planet, rng);
        minerals = PlanetDetailPanel.calculateMinerals(planet, deposits);
        
        deposits = new int [PlanetResources.Metals.values().length];
        positions = new long [PlanetResources.Metals.values().length];
        metals = PlanetDetailPanel.calculateMetals(planet, rng, deposits, positions);
        
        biosphere = PlanetDetailPanel.calculateBiosphere(planet, rng, fluidDeposits);
        
        misc = PlanetDetailPanel.calculateOtherResources(planet, rng);
    }

    private void displayPlanetInfo() 
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        PixFont font = Fonts.g12;
        int lineOffset = 360;
        int lineSpace = 16;
        int columnOffset = 110;
        int left = 50;
        
        
        if(planet.society.population > 0) 
        {
            font.drawStringBold(planet.name + " - " + planet.society.race, Colors.LABEL, left, lineOffset, 1.0f);
            lineOffset -= lineSpace + lineSpace/2;
            
            nf.setMaximumFractionDigits(0);

            long pop = planet.society.population;

            if(pop < 1000) 
            {
                font.drawString("Population:", Colors.LABEL, left, lineOffset);
                font.drawString("< 1000", Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
            else if(pop < 10000) 
            {
                font.drawString("Population:", Colors.LABEL, left, lineOffset);
                font.drawString("< 10000", Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
            else 
            {
                font.drawString("Population:", Colors.LABEL, left, lineOffset);
                font.drawString(nf.format((pop/1000)*1000), Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
        }
        else 
        {
            font.drawStringBold(planet.name, Colors.LABEL, left, lineOffset, 1.0f);
            lineOffset -= lineSpace + lineSpace/2;
        }

        nf.setMaximumFractionDigits(2);

        
        if(planet.btype == Solar.BodyType.PLANET) 
        {
            font.drawString("Type:", Colors.LABEL, left, lineOffset);
            font.drawString(Solar.planetDescription[planet.ptype.ordinal()], Colors.FIELD, left+columnOffset, lineOffset);
            lineOffset -= lineSpace;

            double m = Solar.massToEarthMasses(planet.mass);

            if(m < 0.0001) 
            {
                font.drawString("Mass:", Colors.LABEL, left, lineOffset);
                font.drawString(nf.format(planet.mass/1000000) + " mt", Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
            else 
            {
                font.drawString("Mass:", Colors.LABEL, left, lineOffset);
                font.drawString(nf.format(m) + " earth masses", Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
        } 
        else if(planet.btype == Solar.BodyType.SUN) 
        {
            double m = Solar.massToSunMasses(planet.mass);
            font.drawString("Mass:", Colors.LABEL, left, lineOffset);
            font.drawString(nf.format(m) + " sun masses", Colors.FIELD, left+columnOffset, lineOffset);
            lineOffset -= lineSpace;
        }

        font.drawString("Radius:", Colors.LABEL, left, lineOffset);
        font.drawString((planet.btype == Solar.BodyType.STATION) ? "< 1 km" : nf.format(planet.radius) + " km", Colors.FIELD, left+columnOffset, lineOffset);
        lineOffset -= lineSpace;

        if(planet.btype != Solar.BodyType.SUN &&
           planet.btype != Solar.BodyType.SPACEPORT)
        {
            double au = Solar.distanceToAU(planet.orbit);

            if(au < 0.01) 
            {
                font.drawString("Orbit:", Colors.LABEL, left, lineOffset);
                font.drawString(nf.format(planet.orbit) + " km", Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
            else 
            {
                font.drawString("Orbit:", Colors.LABEL, left, lineOffset);
                font.drawString(nf.format(au) + " au", Colors.FIELD, left+columnOffset, lineOffset);
                lineOffset -= lineSpace;
            }
        }

        if(planet.btype == Solar.BodyType.PLANET)
        {
            font.drawString("Temp.:", Colors.LABEL, left, lineOffset);
            font.drawString(planet.eet + " Kelvin", Colors.FIELD, left+columnOffset, lineOffset);
            lineOffset -= lineSpace;
                
            
            double gravity = planet.calcSurfaceGravity();
            
            font.drawString("Gravity:", Colors.LABEL, left, lineOffset);
            font.drawString(nf.format(gravity * 0.1) + " g", Colors.FIELD, left+columnOffset, lineOffset);
            lineOffset -= lineSpace;
            
            final Solar parent = planet.getParent();

            final double parentMass = parent.mass * 1000; // kg

            // orbit radius in meters
            final double a = planet.orbit * 1000.0;
            final double G = 6.67384E-11;

            final double time = 2 * Math.PI * Math.sqrt(a*a*a / (G * (parentMass + planet.mass*1000)));

            final double days = time / (60*60*24);

            final String orbitString;

            if(days < 300)
            {
                orbitString = nf.format(days) + " days";
            }
            else
            {
                orbitString = nf.format(days/365) + " years";
            }

            font.drawString("Orbital period:", Colors.LABEL, left, lineOffset);
            font.drawString(orbitString, Colors.FIELD, left+columnOffset, lineOffset);
            lineOffset -= lineSpace;            

            font.drawString("Rotation period:", Colors.LABEL, left, lineOffset);
            font.drawString(nf.format(planet.rotationPeriod / (60.0*60.0)) + " hours", Colors.FIELD, left+columnOffset, lineOffset);
            lineOffset -= lineSpace;            
        }
    }

    private void displayListSpace() 
    {
        int left = 450;
        int top = 640;
        
        fillRect(left, 280, 320, 400, Colors.LIST_BG);
        fillBorder(left, 280, 320, 400, 1, Colors.LIGHT_GRAY);
        
        fillRect(left+340, 490, 320, 190, Colors.LIST_BG);
        fillBorder(left+340, 490, 320, 190, 1, Colors.LIGHT_GRAY);
        
        fillRect(left+340, 280, 320, 190, Colors.LIST_BG);
        fillBorder(left+340, 280, 320, 190, 1, Colors.LIGHT_GRAY);
        
        PixFont font = Fonts.g12;
        
        if(activeTrigger == gasesTrigger)
        {
            showList(gases, font, left, top);
        }
        else if(activeTrigger == fluidsTrigger)
        {
            showList(fluids, font, left, top);
        }
        else if(activeTrigger == mineralsTrigger)
        {
            showList(minerals, font, left, top);
        }
        else if(activeTrigger == metalsTrigger)
        {
            showList(metals, font, left, top);
        }
        else if(activeTrigger == biosphereTrigger)
        {
            showList(biosphere, font, left, top);
        }
        else if(activeTrigger == miscTrigger)
        {
            showList(misc, font, left, top);
        }
    }
    
    private void displayHTMLLine(PixFont font, String line, int color, int left, int top) 
    {
        line = line.replace("&lt;", "<");
        
        
        int p1, p2;
        
        p1 = line.indexOf("<font color=");
        if(p1 < 0)
        {
            // Hajo: text all in one color
            font.drawString(line, color, left, top);
        }
        else
        {
            String part = line.substring(0, p1);

            int w = font.getStringWidth(part);
            font.drawString(part, color, left, top);

            p1 += 14;
            part = line.substring(p1, p1+6);
            int metalColor = Integer.parseInt(part, 16);

            p1 += 8;
            p2 = line.indexOf("</font>");
            part = line.substring(p1, p2);
            font.drawString(part, metalColor, left+w, top);
            w += font.getStringWidth(part);

            part = line.substring(p2+7, line.length());
            font.drawString(part, color, left+w, top);
        }
    }

    private void activateTrigger(Trigger t) 
    {
        activeTrigger = t;

        gasesTrigger.setSelected(t == gasesTrigger);
        fluidsTrigger.setSelected(t == fluidsTrigger);
        mineralsTrigger.setSelected(t == mineralsTrigger);
        metalsTrigger.setSelected(t == metalsTrigger);
        biosphereTrigger.setSelected(t == biosphereTrigger);
        miscTrigger.setSelected(t == miscTrigger);
    }

    private void showList(String[] items, PixFont font, int left, int top) 
    {
        for(String gas : items)
        {
            displayHTMLLine(font, gas, 0xFFFFFFFF, left + 16, top);
            top -= 18;
        }
    }
}
