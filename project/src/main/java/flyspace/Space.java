package flyspace;

import flyspace.math.Math3D;
import flyspace.ogl32.GL32MeshFactory;
import flyspace.ui.JumpEffectPainter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;
import solarex.system.Solar;
import solarex.system.Vec3;

/**
 * 
 * @author Hj. Malthaner
 */
public class Space 
{
    private static final Logger logger = Logger.getLogger(Space.class.getName());

    public final ArrayList<MultiMesh> meshes;
    private volatile int convertersRunning;
    public static final float DISPLAY_SCALE = 0.01f;
    
    public MultiMesh selectedMesh;

    public Space()
    {
        this.meshes = new ArrayList<>(1024);
    }
    
    
    public synchronized void convertSystemParallelAndWait(Solar system) throws InterruptedException
    {
        long T0 =  System.currentTimeMillis();
    
        clear();
        
        convertersRunning = 0;
        convertSystem(system);
        
        
        while(convertersRunning > 0)
        {
            logger.log(Level.INFO, "Waiting for {0} converters.", convertersRunning);
            wait();
        }

        logger.log(Level.INFO, "Binding textures.");
        
        for(MultiMesh multiMesh : meshes)
        {
            for(AbstractMesh mesh: multiMesh.meshes)
            {
                mesh.bind();
            }
        }
        
        long T1 =  System.currentTimeMillis();
        logger.log(Level.INFO, "System fully converted in {0} ms", (T1 - T0));
    }
    
    public void convertSystemParallelAndPlayEffect(Solar system, JumpEffectPainter painter) throws InterruptedException
    {
        long T0 =  System.currentTimeMillis();
    
        meshes.clear();
        
        convertersRunning = 0;
        convertSystem(system);
        
        
        while(convertersRunning > 0)
        {
            painter.paint(convertersRunning);    
            // System.err.println("converters left: " + convertersRunning);
        }

        logger.log(Level.INFO, "Binding textures.");
        
        for(MultiMesh multiMesh : meshes)
        {
            for(AbstractMesh mesh: multiMesh.meshes)
            {
                mesh.bind();
            }
        }
        
        long T1 =  System.currentTimeMillis();
        logger.log(Level.INFO, "System fully converted in {0} ms", (T1 - T0));
    }

    private void convertSystem(Solar system)
    {
        convertBodyAsync(system);
        
        for(Solar body : system.children)
        {
            convertSystem(body);
        }
    }
    
    private void convertBodyAsync(final Solar system)
    {
        Thread t = new Thread() 
        {
            @Override
            public void run()
            {
                convertBody(system);
                notifyConverterDone();
            }
        };
        
        convertersRunning ++;
        t.start();
        // t.run();
    }
    
    private synchronized void notifyConverterDone()
    {
        convertersRunning --;
        notify();
    }
    
    public void convertBody(Solar system)
    {
        System.err.println("Converting: " + system.name);
        
        MultiMesh mesh = null;
        
        switch(system.btype)
        {
            case PLANET:
                mesh = convertPlanet(system);
                // body = new MultiMesh(GL32MeshFactory.createTetra(system.radius));
                break;
            case SUN:
                mesh = convertSun(system);
                break;
            case STATION:
                mesh = convertStation(system);
                break;
            case SPACEPORT:
                mesh = convertSpaceport(system);
                break;
        }
        
        if(mesh != null)
        {
            mesh.setPeer(system);
            
            Vec3 pos = system.getAbsolutePosition();
            
            if(system.btype == Solar.BodyType.SPACEPORT)
            {
                mesh.setPos(pos.x*DISPLAY_SCALE, 
                            pos.y*DISPLAY_SCALE,
                            (pos.z + system.radius)*DISPLAY_SCALE);
            }
            else
            {
                // System.err.println("  x=" + pos.x + " y=" + pos.y + " z=" + pos.z);
                mesh.setPos(pos.x*DISPLAY_SCALE, 
                            pos.y*DISPLAY_SCALE,
                            pos.z*DISPLAY_SCALE);
            }
            
            add(mesh);
        }
        else
        {
            System.err.println("  " + system.name + " has no conversion.");
        }
    }

    
    public MultiMesh convertSun(Solar system)
    {
        MultiMesh sun = new MultiMesh();
        AbstractMesh mesh;
        switch(system.stype)
        {
            case S_YELLOW:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 1.0f, 0.9f, 0.4f);
                break;
            case S_ORANGE:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 1.0f, 0.6f, 0.1f);
                break;
            case S_RED_GIANT:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 0.7f, 0.2f, 0.1f);
                break;
            case S_BROWN_DWARF:
                // Hajo: Wikipedia says, these are actually violet
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 0.5f, 0.3f, 0.3f);
                break;
            case S_WHITE_DWARF:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 0.9f, 0.9f, 0.9f);
                break;
            case S_NEUTRON:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 0.8f, 0.9f, 1.0f);
                break;
            case S_BLACK_HOLE:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 0.1f, 0.1f, 0.1f);
                break;
            default:
                mesh = GL32MeshFactory.createSphere(system.radius*DISPLAY_SCALE, 1.0f, 0.9f, 0.0f);
        }
        
        sun.addMesh(mesh);
        
        return sun;
    }    
    
    public MultiMesh convertPlanet(Solar system)
    {
        MultiMesh planet = new MultiMesh();
        AbstractMesh mesh;
        
        switch(system.ptype)
        {
            case CARBON_RICH:
                mesh = GL32MeshFactory.createCarbonRichPlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            case BARE_ROCK:
                mesh = GL32MeshFactory.createRockTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                // mesh = GL32MeshFactory.createEarthTypePlanet(system.radius, system.seed);
                break;
            case CLOUD:
                mesh = GL32MeshFactory.createCloudTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            case ATM_ROCK:
                mesh = GL32MeshFactory.createMarsTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            case EARTH:
                mesh = GL32MeshFactory.createEarthTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            case ICE:
                mesh = GL32MeshFactory.createIceTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            case SMALL_GAS:
                mesh = GL32MeshFactory.createBlueGasGiantTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            case BIG_GAS:
                mesh = GL32MeshFactory.createBrownGasGiantTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
            default:
                mesh = GL32MeshFactory.createBlueGasGiantTypePlanet(system.radius*DISPLAY_SCALE, system.seed);
                break;
        }
        
        planet.addMesh(mesh);
        
        return planet;
    }    

    public MultiMesh convertStation(Solar system)
    {
        MultiMesh station = new MultiMesh();
        AbstractMesh mesh;
        
        float r = (float)(system.radius * DISPLAY_SCALE * 0.1);
        
        mesh = GL32MeshFactory.createPrism(6, 4*r, 0.5f * r, 0);
        station.addMesh(mesh);
        // mesh = GL32MeshFactory.createSmoothPrism(25, 0.5f * r, 2*r, 3*r);
        mesh = GL32MeshFactory.createPrism(6, 0.5f * r, 2*r, 3*r);
        station.addMesh(mesh);
        
        return station;
    }
    
    public MultiMesh convertSpaceport(Solar system)
    {
        MultiMesh station = new MultiMesh();
        AbstractMesh mesh;
        
        float r = (float)(system.radius * DISPLAY_SCALE * 0.1);
        
        // mesh = MeshFactory.createSphere(r, 0.8f, 1.0f, 0.2f);
        mesh = GL32MeshFactory.createSphere(r, 0.8f, 1.0f, 0.2f);
        station.addMesh(mesh);
        
        return station;
    }
    
    public void createSampleMeshes()
    {
        AbstractMesh mesh;
        // mesh = new Mesh();
        // mesh = GL32MeshFactory.createEarthTypePlanet();
        // mesh.setPos(new Vector3f(-20, 0, 0));
        // meshes.add(mesh);
        
        // mesh = GL32MeshFactory.createEarthTypePlanet();
        // mesh = GL32MeshFactory.createMarsTypePlanet();
        // mesh = GL32MeshFactory.createIceTypePlanet();
        // mesh = GL32MeshFactory.createBrownGasGiantTypePlanet();
        mesh = GL32MeshFactory.createBlueGasGiantTypePlanet(100, 123456);
        

        MultiMesh planet = new MultiMesh(mesh);
        planet.setPos(new Vec3(0, 0, -200));
        meshes.add(planet);

        /*
        MultiMesh station = new MultiMesh();
        mesh = GL32MeshFactory.createPrism(6, 4, 0.5f, 0);
        station.addMesh(mesh);
        mesh = GL32MeshFactory.createSmoothPrism(25, 0.5f, 2, 3);
        station.addMesh(mesh);
        station.setPos(new Vec3(-20, 0, 0));
        meshes.add(station);
        */
        
        MultiMesh mm = new MultiMesh();
        mesh = MeshFactory.createTetra(100, 0xFFFFFFFF);
        mm.addMesh(mesh);
        mm.setPos(new Vec3(0, 0, 0));
        meshes.add(mm);
        
    }

    synchronized public void add(MultiMesh body) 
    {
        if(body == null) throw new IllegalArgumentException("Body is null!");
            
        meshes.add(body);
    }

    public MultiMesh findMeshForPeer(Solar peer) 
    {
        for(MultiMesh mesh : meshes)
        {
            if(mesh.getPeer() == peer) return mesh;
        }
        
        return null;
    }

    private void clear() 
    {
        for(MultiMesh multiMesh : meshes)
        {
            for(AbstractMesh mesh : multiMesh.meshes)
            {
                mesh.unbind();
            }
        }
        
        meshes.clear();
    }

    
    /**
     * Update positions and rotations of all bodies in this space.
     * @param dt Time passed since last update
     */
    void update(int dt) 
    {
        for(MultiMesh multiMesh : meshes)
        {
            updateBody(multiMesh, dt);
        }
    }

            
    private void updateBody(MultiMesh mesh, int dt) 
    {
        Solar body = mesh.getPeer();
        Vec3 meshPos = mesh.getPos();
        
        // Hajo: suns are fully illuminated
        if(body == null || body.btype == Solar.BodyType.STATION)
        {
            rotateStation(mesh, dt);
        }
        else if(body.btype == Solar.BodyType.PLANET)
        {
            rotatePlanet(mesh, dt);
            if(body.children.size() > 0)
            {
                Solar maybeSpaceport = body.children.get(0);
                if(maybeSpaceport.btype == Solar.BodyType.SPACEPORT)
                {
                   MultiMesh spaceportMesh = findMeshForPeer(maybeSpaceport);
                   
                   // Hajo: this is relative to the planet center
                   Vec3 input = 
                           new Vec3(0, 
                                    0, 
                                    (body.radius * -Space.DISPLAY_SCALE));
                   
                   Vec3 result = new Vec3();
                   Math3D.rotY(input, -mesh.getAngleY(), result);
                   
                   // Hajo: spaceport mesh needs absolute position
                   spaceportMesh.setPos(result.x + meshPos.x, 
                                        result.y + meshPos.y,
                                        result.z + meshPos.z);
                   
                   // fspaceportMesh.setAngleX(90);
                   spaceportMesh.setAngleY(-mesh.getAngleY());
                }
            }
        }
    }
    
    
    public void rotateStation(MultiMesh mesh, int dt)
    {      
        float angleX = mesh.getAngleX();
        angleX += 0.00625f * dt;
        if(angleX > 360) angleX -= 360;
        
        mesh.setAngleY(angleX);
        
        
        float angleY = mesh.getAngleY();
        angleY += 0.00625 * dt;
        if(angleY > 360) angleY -= 360;
        
        mesh.setAngleY(angleY);
    }
    
    
    public void rotatePlanet(MultiMesh mesh, int dt)
    {
        float angleY = mesh.getAngleY();
        angleY += 0.00125f * dt;
        if(angleY > 360) angleY -= 360;
        
        mesh.setAngleY(angleY);
    }
    
}
