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
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.input.Mouse;
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
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import solarex.ship.Ship;
import solarex.ship.components.EquipmentType;
import solarex.ship.components.ShipComponent;
import solarex.system.Mining;
import solarex.system.PlanetResources;
import solarex.system.Solar;
import solarex.ui.panels.PlanetDetailPanel;
import solarex.util.RandomHelper;

/**
 *
 * @author Hj. Malthaner
 */
public class PlanetMiningPanel extends DecoratedUiPanel
{
    private Solar planet;
    private final FlySpace game;
    private final Ship ship;
    private final Mining mining;
    
    private MultiMesh planetMesh;
    private final View camera;
    
    private boolean clicked;

    private Space space;
    
    private int [] gases;
    private int [] fluids;
    private int [] minerals;
    private int [] metals;
    private int [] biosphere;
    private int [] misc;
    
    private final ArrayList<Deposit> allDepositsFound = new ArrayList<>();
    private final ArrayList<PlanetResources.Gases> gasesFound = new ArrayList<>();
    private final ArrayList<PlanetResources.Fluids> fluidsFound = new ArrayList<>();
    private final ArrayList<PlanetResources.Minerals> mineralsFound = new ArrayList<>();
    private final ArrayList<PlanetResources.Metals> metalsFound = new ArrayList<>();
    
    private final ArrayList<ShipComponent> availableDrones = new ArrayList<>();
    
    private int resourceSelected = -1;
    private int availableDronesSelected = -1;
    private int activeDronesSelected = -1;
    
    private final DecoratedTrigger scanTrigger;
    private final DecoratedTrigger launchTrigger;
    private final DecoratedTrigger recallTrigger;
            
    public PlanetMiningPanel(FlySpace game, Ship ship, Mining mining) 
    {
        this.game = game;
        this.ship = ship;
        this.mining = mining;
        
        this.camera = new View();
        
        scanTrigger = new DecoratedTrigger(Fonts.g17, "Start Resources Scan", 0x3377FF00, 0xFF66EE00);
        scanTrigger.setArea(450, 210, 300, 32);
        addTrigger(scanTrigger);

        launchTrigger = new DecoratedTrigger(Fonts.g17, "Launch Drone", 0x3377FF00, 0xFF66EE00);
        launchTrigger.setArea(800, 450, 300, 32);
        addTrigger(launchTrigger);

        recallTrigger = new DecoratedTrigger(Fonts.g17, "Recall Drone", 0x3377FF00, 0xFF66EE00);
        recallTrigger.setArea(800, 210, 300, 32);
        addTrigger(recallTrigger);
    }

    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
        
        ShaderBank.setupMatrices(250, 250);
        ShaderBank.updateViewMatrix(new Matrix4f());
        ShaderBank.updateLightPos(-10000.0f, 0.0f, 10000.0f);
        
        for(ShipComponent component : ship.equipment.components)
        {
            if(component.getType() == EquipmentType.DRONE)
            {
                availableDrones.add(component);
            }
        }
    }

    
    @Override
    public void handleInput() 
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
                
                if(t == scanTrigger)
                {
                    performResourcesScan();
                }
                else if(t == launchTrigger)
                {
                    launchDrone();
                }
                else if(t == recallTrigger)
                {
                    recallDrone();
                }
                
                handleResourceListSelection(mx, my);
                handleDroneListSelection(mx, my);
                handleActiveDroneListSelection(mx, my);
                
                clicked = false;
            }
        }
    }

    
    private void launchDrone()
    {
        if(availableDronesSelected > -1 && availableDronesSelected < availableDrones.size())
        {
            Mining.Operation operation = new Mining.Operation();
            operation.drone = availableDrones.get(availableDronesSelected);

            int s = resourceSelected;
            Deposit deposit = allDepositsFound.get(s);
            operation.depositWealth = deposit.wealth;

            if(s > 0 && s < gasesFound.size())
            {
                operation.gas = gasesFound.get(s);
            }

            s -= gasesFound.size();

            if(s > 0 && s < fluidsFound.size())
            {
                operation.fluid = fluidsFound.get(s);
            }

            s -= fluidsFound.size();

            if(s > 0 && s < mineralsFound.size())
            {
                operation.mineral = mineralsFound.get(s);
            }

            s -= mineralsFound.size();

            if(s > 0 && s < metalsFound.size())
            {
                operation.metal = metalsFound.get(s);        
            }

            mining.startOperation(operation);
            ship.equipment.removeComponent(operation.drone);
            availableDrones.remove(operation.drone);
        }
    }
    
    
    private void recallDrone()
    {
        if(activeDronesSelected > -1 && activeDronesSelected < mining.size())
        {
            Mining.Operation operation = mining.get(activeDronesSelected);

            int units = operation.amount / 100000;
            int goodType = -1;

            if(operation.gas != null)
            {
                goodType = PlanetResources.gasToGood(operation.gas.ordinal());
            }
            if(operation.fluid != null)
            {
                goodType = PlanetResources.fluidToGood(operation.fluid.ordinal());
            }
            if(operation.mineral != null)
            {
                goodType = PlanetResources.mineralToGood(operation.mineral.ordinal());
            }
            if(operation.metal != null)
            {
                goodType = PlanetResources.metalToGood(operation.metal.ordinal());
            }

            ship.cargo.goods[goodType].units += units;

            mining.stopOperation(operation);
            ship.equipment.addComponent(operation.drone);
            availableDrones.add(operation.drone);
            
        }
    }
    

    private void handleResourceListSelection(int mx, int my)
    {
        int left = 450;
        int top = 652;
        int bh = 390;
        int bw = 300;

        if(mx > left && mx < left + bw && my < top && my > top-bh)
        {
            resourceSelected = (top - my - 14) / 18;
        }
    }

    
    private void handleDroneListSelection(int mx, int my)
    {
        int left = 800;
        int top = 652;
        int bh = 150;
        int bw = 300;

        if(mx > left && mx < left + bw && my < top && my > top-bh)
        {
            availableDronesSelected = (top - my - 14) / 18;
        }
    }
    
    
    private void handleActiveDroneListSelection(int mx, int my)
    {
        int left = 800;
        int top = 410;
        int bh = 150;
        int bw = 300;

        if(mx > left && mx < left + bw && my < top && my > top-bh)
        {
            activeDronesSelected = (top - my - 14) / 40;
        }
    }
    
    
    @Override
    public void display() 
    {
        setupTextPanel(width, height);
        
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        displayBackground(0xFF000022, 0xFF001133, 0xFF112244);

        displayTitle(planet.name + " Mining Operations");
        
        displayPlanetInfo();
        
        displayResourceList();
        displayDronesList();
        displayActiveDronesList();
        
        displayTriggers();

        glViewport(50, 390, 250, 250);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);  
        glEnable(GL_BLEND);        
        
        rotatePlanet(planetMesh);
        
        Matrix4f modelMatrix = new Matrix4f();
        
        modelMatrix.translate(new Vector3f(0, 0, -planet.radius / 30));
        Matrix4f.rotate(Math3D.degToRad(planetMesh.getAngleZ()), new Vector3f(0, 0, 1), 
                modelMatrix, modelMatrix);
        Matrix4f.rotate(Math3D.degToRad(planetMesh.getAngleY()), new Vector3f(0, 1, 0), 
                modelMatrix, modelMatrix);
        Matrix4f.rotate(Math3D.degToRad(planetMesh.getAngleX()), new Vector3f(1, 0, 0), 
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
        
        Random rng = PlanetResources.getPlanetRng(planet);
        
        gases = new int [PlanetResources.Gases.values().length];
        PlanetDetailPanel.calculateAtmosphere(planet, rng, gases);

        fluids = new int [PlanetResources.Fluids.values().length];
        long [] fluidPositions = new long [PlanetResources.Fluids.values().length];
        PlanetDetailPanel.calculateFluids(planet, rng, gases, fluids, fluidPositions);
        
        minerals = PlanetResources.calculateMinerals(planet, rng);
        
        metals = new int [PlanetResources.Metals.values().length];
        long [] metalPositions = new long [PlanetResources.Metals.values().length];
        PlanetDetailPanel.calculateMetals(planet, rng, metals, metalPositions);
        
        // todo: check if biosphere allows mining?
        
        misc = PlanetResources.calculateOtherResources(planet, rng);
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
            font.drawString("Temperature:", Colors.LABEL, left, lineOffset);
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

    
    private void displayResourceList() 
    {
        int left = 450;
        int top = 652;
        int bh = 390;
        int bw = 300;
        
        Fonts.g17.drawString("Scan Results:", Colors.LABEL, left, top);
        
        fillRect(left, top-4-bh, bw, bh, Colors.LIST_BG);
        fillBorder(left, top-4-bh, bw, bh, 1, Colors.LIGHT_GRAY);
        
        int lineY = top - 42;
        
        if(resourceSelected > -1 && resourceSelected < allDepositsFound.size())
        {
            int selY = lineY - resourceSelected * 18 + 11;
            displayListSelectionBox(left+4, selY, bw-8, 21);
        }
        
        String [] size = {"Traces", "Poor", "Average", "Rich", "Abundant"};
        
        for(Deposit deposit : allDepositsFound)
        {            
            Fonts.g12.drawString(deposit.resource.toString(), 
                                 deposit.resource.getARGB(), left+8, lineY);
            
            Fonts.g12.drawString(size[deposit.wealth-1],
                                 Colors.FIELD, left+120, lineY);
            
            lineY -= 18;
        }
        
    }

    
    private void displayDronesList()
    {
        int left = 800;
        int top = 652;
        int bh = 150;
        int bw = 300;
        
        Fonts.g17.drawString("Available Drones:", Colors.LABEL, left, top);
        
        fillRect(left, top-4-bh, bw, bh, Colors.LIST_BG);
        fillBorder(left, top-4-bh, bw, bh, 1, Colors.LIGHT_GRAY);
        
        int lineY = top - 42;
        
        if(availableDronesSelected > -1 && availableDronesSelected < availableDrones.size())
        {
            int selY = lineY - availableDronesSelected * 18 + 11;
            displayListSelectionBox(left+4, selY, bw-8, 21);
        }
        
        for(ShipComponent drone : availableDrones)
        {
            // Fonts.g12.drawString(drone.getName(), Colors.FIELD, left+8, lineY);
            displayHTMLLine(Fonts.g12, drone.getName(), Colors.FIELD, left+8, lineY);
            lineY -= 18;
        }
    }
    
    
    private void displayActiveDronesList()
    {
        int left = 800;
        int top = 410;
        int bh = 150;
        int bw = 300;
        
        Fonts.g17.drawString("Active Drones:", Colors.LABEL, left, top);
        
        fillRect(left, top-4-bh, bw, bh, Colors.LIST_BG);
        fillBorder(left, top-4-bh, bw, bh, 1, Colors.LIGHT_GRAY);
        
        int lineY = top - 42;
        
        if(activeDronesSelected > -1 && activeDronesSelected < mining.size())
        {
            int selY = lineY - availableDronesSelected * 18 + 11 - 20;
            displayListSelectionBox(left+4, selY, bw-8, 43);
        }
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        
        for(Mining.Operation operation : mining.getOperations())
        {
            displayHTMLLine(Fonts.g12, operation.drone.getName(), Colors.FIELD, left+8, lineY);
            
            PlanetResources.Resource resource = PlanetResources.Gases.Hydrogen;
            if(operation.gas != null) resource = operation.gas;
            if(operation.fluid != null) resource = operation.fluid;
            if(operation.mineral != null) resource = operation.mineral;
            if(operation.metal != null) resource = operation.metal;

            String amount = nf.format(operation.amount * 0.00001);
            String label = resource.toString() + ":";
            int lw = Fonts.g12.getStringWidth(label);
            Fonts.g12.drawString(label, resource.getARGB(), left+20, lineY - 18);
            Fonts.g12.drawString(amount, Colors.FIELD, left+24 + lw, lineY - 18);
            
            lineY -= 40;
        }
    }
    
    
    public void displayListSelectionBox(int left, int top, int bw, int bh)
    {
        fillRect(left, top, bw, bh, Colors.LIST_SELECT);        
        fillBorder(left, top, bw, bh, 1, Colors.LIST_SELECT_BORDER);
    }

    
    private void displayHTMLLine(PixFont font, String line, int color, int left, int top) 
    {
        line = line.replace("&lt;", "<").replace("&gt;", ">").replace("<br>", " ");
        
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

    
    /**
     * Scan the planet for resources. Depending on the
     * quality of the ships scanners more or less resources
     * will be revealed. Resources which come in large quantities
     * have better chances to be discovered.
     */
    private void performResourcesScan() 
    {
        System.err.println("Starting resources scan.");
        
        Random rng = RandomHelper.createRNG(planet.seed + 17);
        gasesFound.clear();
        mineralsFound.clear();
        metalsFound.clear();
        
        
        int max = 1;
        
        for(int i = 0; i<gases.length; i++)
        {
            int wealth = gases[i];
            if(wealth > rng.nextInt(max))
            {
                PlanetResources.Gases gas = PlanetResources.Gases.values()[i];
                // System.err.println(gas.toString() + " " + w);
                gasesFound.add(gas);
                allDepositsFound.add(new Deposit(gas, wealth));
            }
        }
        
        for(int i = 0; i<fluids.length; i++)
        {
            int wealth = fluids[i];
            if(wealth > rng.nextInt(max))
            {
                PlanetResources.Fluids fluid = PlanetResources.Fluids.values()[i];
                // System.err.println(fluid.toString() + " " + w);
                fluidsFound.add(fluid);
                allDepositsFound.add(new Deposit(fluid, wealth));
            }
        }
        
        for(int i = 0; i<minerals.length; i++)
        {
            int wealth = minerals[i];
            if(wealth > rng.nextInt(max))
            {
                PlanetResources.Minerals mineral = PlanetResources.Minerals.values()[i];
                // System.err.println(mineral.toString() + " " + w);
                mineralsFound.add(mineral);
                allDepositsFound.add(new Deposit(mineral, wealth));
            }
        }
        
        for(int i = 0; i<metals.length; i++)
        {
            int wealth = metals[i];
            if(wealth > rng.nextInt(max))
            {
                PlanetResources.Metals metal = PlanetResources.Metals.values()[i];
                // System.err.println(metal.toString() + " " + w);
                metalsFound.add(metal);
                allDepositsFound.add(new Deposit(metal, wealth));
            }
        }
    }
    
    
    private class Deposit
    {
        PlanetResources.Resource resource;
        int wealth;

        private Deposit(PlanetResources.Resource resource, int wealth) 
        {
            this.resource = resource;
            this.wealth = wealth;
        }
    }
}
