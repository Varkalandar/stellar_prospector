package flyspace.ogl;

import flyspace.AbstractMesh;
import flyspace.MultiMesh;
import flyspace.Autopilot;
import flyspace.View;
import flyspace.FlySpace;
import flyspace.math.Math3D;
import flyspace.Space;
import flyspace.SpaceDebrisPainter;
import flyspace.ogl32.GL32MeshFactory;
import flyspace.ogl32.ShaderBank;
import flyspace.particles.ParticleDriver;
import flyspace.particles.ParticlePainter;
import flyspace.ui.Colors;
import flyspace.ui.Fonts;
import flyspace.ui.UiPanel;
import flyspace.ui.panels.CockpitPanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import flyspace.ui.Keyboard;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;
import org.lwjgl.opengl.GL20;
import solarex.ship.Ship;
import solarex.system.Matrix4;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.system.Vec4;

/**
 * Space view
 * 
 * @author Hj. Malthaner
 */
public class SpacePanel extends UiPanel
{
    public static final Logger logger = Logger.getLogger(SpacePanel.class.getName());

    private final float mouseSensitivity = 0.0009f;
    private float speed = 0f;
    private float accel = 0f;

    private final View view;
    
    private final FlySpace game;
    private final Ship ship;
    private final Space space;
    private final ArrayList<Solar> bodiesInMiningRange = new ArrayList<>(8);
    private final ArrayList<Solar> bodiesInDockingRange = new ArrayList<>(8);
    public int cursorI, cursorJ, cursorN;

    private final Texture nebula;
    private final AbstractMesh starsFar;
    private final AbstractMesh starsNear;

    private final ParticleDriver debrisDriver;
    
    
    float far = 100000;
    private Autopilot autopilot;
    
    // Hajo: for selection
    private int bestMatchDistance;
    private boolean clicked;
    
    // Hajo: view debuging
    /*
    private MultiMesh frontTester;
    private MultiMesh upTester;
    private MultiMesh rightTester;
    */
    
    public SpacePanel(FlySpace game, Ship ship, Space space) throws IOException
    {
        super(null);
        this.game = game;
        this.ship = ship;
        this.space = space;
        this.view = new View();
        this.debrisDriver = new ParticleDriver(10000);
        
        cursorI = -1;
        cursorJ = -1;
        cursorN = 9;

        nebula = TextureCache.loadTexture("/flyspace/resources/nebula.png");
        
        starsFar = GL32MeshFactory.createStars(1500, 900f);
        starsFar.bind();
        starsNear = GL32MeshFactory.createStars(1500, 700f);
        starsNear.bind();
        
        ShaderBank.setupShaders();

        ParticlePainter painter = new SpaceDebrisPainter();
        debrisDriver.setPainter(painter);

        logger.info("done.");
    }
    
    @Override
    public void activate()
    {
        ShaderBank.setupMatrices(width, height - CockpitPanel.HEIGHT);
        
        /*
        space.add(frontTester);
        space.add(upTester);
        space.add(rightTester);
        */
    }

    @Override
    public void handlePanelInput()
    {
        acceptInput(10.0f);
        
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        
        boolean keyAutopilot = Keyboard.isKeyDown(Keyboard.KEY_A);
        
        if(keyAutopilot)
        {
            autopilot = new Autopilot(10);
            speed = 0f;
            accel = 0f;
        }
        
        handleMiningList();
        handleDockingList();
        
        if(clicked && Mouse.isButtonDown(0) == false)
        {
            space.selectedMesh = null;
            bestMatchDistance = 99;
            
            for(MultiMesh mesh : space.meshes)
            {
                int distance = Math.abs(Mouse.getX() - mesh.lastScreenX) + Math.abs(Mouse.getY() - mesh.lastScreenY);

                if(distance < bestMatchDistance)
                {
                    bestMatchDistance = distance;
                    space.selectedMesh = mesh;
                }
            }

            if(space.selectedMesh != null)
            {
                setDestination();
            }
        }
        
        if(Mouse.isButtonDown(0) == false & clicked)
        {
            clicked = false;
        }        
        
        GlLifecycle.exitOnGLError("handleInput");
    }

    
    public void acceptInput(float delta) 
    {
        acceptInputRotate(delta);
        acceptInputGrab();
        acceptInputMove(delta);
    }

    
    private void acceptInputRotate(float delta) 
    {
        if(Mouse.isGrabbed()) 
	{
            int mouseDX = Mouse.getDX();
            int mouseDY = Mouse.getDY();
            
            
            view.rotateAxis(mouseDY * mouseSensitivity, new Vec3(1, 0, 0));
            view.rotateAxis(mouseDX * mouseSensitivity, new Vec3(0, 1, 0));
        }
    }

    
    private void acceptInputGrab() 
    {
        if(Mouse.isInsideWindow() && Mouse.isButtonDown(0)) 
	{
            Mouse.setGrabbed(true);
        }
        
        if(!Mouse.isButtonDown(0)) 
	{
            Mouse.setGrabbed(false);
        }
    }

    
    private void acceptInputMove(float delta) 
    {
        boolean keyAccell = Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP);
        boolean keyDecell = Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN);
        boolean keyBreak = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_LEFT_CONTROL);
        
        
        if(keyFast) 
	{
            speed += 2;
        }
        
        if(keyAccell) 
	{
            accel += 0.01f;
            
            if(accel > 10.0f)
            {
                accel = 10.0f;
            }
            
            speed += accel;
        }
        
        if(keyDecell)
	{
            accel -= 0.01f;
            
            if(accel < -10.0f)
            {
                accel = -10.0f;
            }
            
            speed += accel;
        }
        
        if(!keyAccell && !keyDecell)
        {
            accel = 0f;
        }
        
        if(keyBreak)
	{
            speed = 0;
            accel = 0;
            autopilot = null;
        }
        
        Matrix4 inverseView = view.getInverse();
        Vec3 front4 = new Vec3(0, 0, -1);
        Matrix4.transform(inverseView, front4, front4);
        front4.normalise();
        
        double mSpeed = speed * 1.0;
        
        Vec3 pos = ship.pos;
        pos.x += front4.x * mSpeed;
        pos.y += front4.y * mSpeed;
        pos.z += front4.z * mSpeed;
    }
    
    
    private void handleMiningList()
    {
        if(Mouse.isButtonDown(0) == false & clicked)
        {
            int y = 250;
            int left = width-200;

            y -= 20;

            int mx = Mouse.getX();
            int my = Mouse.getY();
            
            for(Solar body : bodiesInMiningRange)
            {
                if(mx >= left && mx < width && my > y+16 && my < y+34)
                {
                    System.err.println("Clicked for mining: " + body.name);
                    game.showPlanetMiningPanel(body);
                }
                y -= 18;
            }        
        }
    }
    
    
    private void handleDockingList()
    {
        if(Mouse.isButtonDown(0) == false & clicked)
        {
            int y = 350;
            int left = width-200;

            y -= 20;

            int mx = Mouse.getX();
            int my = Mouse.getY();
            
            for(Solar body : bodiesInDockingRange)
            {
                if(mx >= left && mx < width && my > y+16 && my < y+34)
                {
                    System.err.println("Clicked for docking: " + body.name);
                    game.dockAt(body);
                }
                y -= 18;
            }        
        }
    }

    
    /**
     * Called before a frame is displayed. All updates to game data should
     * happen here.
     * @param dt Time passed since last update call
     */
    @Override
    public void update(int dt)
    {
        // Hajo: collect bodies in mining distance
        
        bodiesInMiningRange.clear();
        bodiesInDockingRange.clear();
        double maxMining = 1.0E7;
        double maxDocking = 1.0E6;
        
        for(MultiMesh mesh : space.meshes)
        {
            Vec3 v = new Vec3(mesh.getPos());
            v.sub(ship.pos);
            
            double d2 = v.length2();
            
            // System.err.println("d2=" + d2);
            
            if(d2 < maxMining)
            {
                Solar body = mesh.getPeer();
                // System.err.println("  body=" + body.name);
                
                if(body.btype == Solar.BodyType.PLANET)
                {
                    // planet and in mining range

                    // should this list be sorted?
                    bodiesInMiningRange.add(body);
                }
            }
            
            if(d2 < maxDocking)
            {
                Solar body = mesh.getPeer();
                // System.err.println("  body=" + body.name);
                
                if(body.btype == Solar.BodyType.SPACEPORT ||
                   body.btype == Solar.BodyType.STATION)
                {
                    bodiesInDockingRange.add(body);
                }
            }
        }
    }
    
    
    @Override
    public void displayPanel()
    {
        if(autopilot != null) 
        {
            autopilot.drive(view, ship);
            if(autopilot.arrived) autopilot = null;
        }

        displaySpace(view);
    }
    
    
    private void displaySpace(View view)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, CockpitPanel.HEIGHT, width, height - CockpitPanel.HEIGHT);

        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_CLAMP);

        displayStars(starsFar);
        displayStars(starsNear);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_CLAMP);
        
        Matrix4 inverse = view.getInverse();
        Vec3 front4 = new Vec3(0, 0, -1);
        Vec3 up4 = new Vec3(0, 1, 0);
        Vec3 right4 = new Vec3(1, 0, 0);

        Matrix4.transform(inverse, front4, front4);
        Matrix4.transform(inverse, right4, right4);
        Matrix4.transform(inverse, up4, up4);

        // Hajo: Testing camera vectors
        /*
        Vec3 tPos = new Vec3(ship.pos);
        tPos.x += front4.x * 300;
        tPos.y += front4.y * 300;
        tPos.z += front4.z * 300;
        
        frontTester.setPos(tPos);
        
        tPos.x += up4.x * 30;
        tPos.y += up4.y * 30;
        tPos.z += up4.z * 30;
        upTester.setPos(tPos);
        
        tPos = new Vec3(ship.pos);
        tPos.x += front4.x * 300;
        tPos.y += front4.y * 300;
        tPos.z += front4.z * 300;
        tPos.x += right4.x * 30;
        tPos.y += right4.y * 30;
        tPos.z += right4.z * 30;

        rightTester.setPos(tPos);
        
        System.err.println("front=" + front4);
        System.err.println("up=" + up4);
        System.err.println("right=" + right4);
*/        
        
        // System.err.println("d1=" + Vec3.dot(up, right) + " d2=" + Vec3.dot(up, front) + "d3=" + Vec3.dot(front, right));
        
        
        // ----------
        

        // displayNebulae(camera);
        

        for(MultiMesh mesh : space.meshes)
        {
            Solar body = mesh.getPeer();
            Vec3 pos = new Vec3(mesh.getPos());
            pos.sub(ship.pos);
            
            double prod = front4.x * pos.x + front4.y * pos.y + front4.z * pos.z;
            double dist2 = pos.length2();

            // if(body != null) System.err.println(body.name + " at " + prod);
            
            // Hajo: show bodies only when in front of the camera
            if(prod > 0)
            {
                // Hajo: check for collisions ...

                if(body != null && dist2 < 20000*Space.DISPLAY_SCALE*Space.DISPLAY_SCALE)
                {
                    // Hajo: collision with a space station is considered docking ...
                    if(body.btype == Solar.BodyType.STATION || 
                       body.btype == Solar.BodyType.SPACEPORT)
                    {
                        speed = 0;
                        accel = 0;
                        // camera.setRotationX(0);
                        // camera.setRotationY(180);
                        game.dockAt(body);
                    }
                }
                                               
                // System.err.println(body.name + " at distance " + dist2);
                // if(body != null) System.err.println(body.name + " at " + prod);
                
                showBody(mesh);
            }
            else
            {
                // Hajo: don't show labels for bodies behind the camera
                mesh.lastScreenX = 999999;
                mesh.lastScreenY = 999999;
            }
        }

        GlLifecycle.exitOnGLError("displaySpace");
        
    
        displayDebris(view);
        
        glDisable(GL_DEPTH_TEST);

        glPushMatrix();
        glMatrixMode(GL_PROJECTION); 
        glLoadIdentity(); 
        
	glOrtho(0, width, 0, height, 100, -100);

        glMatrixMode(GL_MODELVIEW); 
        glLoadIdentity(); 
        
        glViewport(0, 0, width, height);

        
        for(MultiMesh mesh : space.meshes)
        {
            showLabel(mesh);
        }
        
        Fonts.g12.drawString("Speed: ", Colors.LABEL, 10, 168);
        
        if(autopilot != null)
        {
            Fonts.g12.drawString(autopilot.speedString, Colors.FIELD, 90, 168);
        }
        else
        {
            // Fonts.g12.drawString("" + speed + " (" + accel + ")", Colors.FIELD, 90, 168);
            Fonts.g12.drawString("" + speed, Colors.FIELD, 90, 168);
        }
        
        if(space.selectedMesh != null && space.selectedMesh.getPeer() != null)
        {
            Fonts.g12.drawString("Destination:", Colors.LABEL, 10, 152);
            Fonts.g12.drawString(space.selectedMesh.getPeer().name, Colors.FIELD, 90, 152);
        }
        
        Fonts.c9.drawString("Use cursor up/down for manual thrust. Press space to break hard.", Colors.CYAN, 10, 690);
        Fonts.c9.drawString("Drag view by mouse. Click to set autopilot destination.", Colors.CYAN, 10, 675);
        Fonts.c9.drawString("Press A to activate autopilot.", Colors.CYAN, 10, 660);
        
        
        displayMiningList();
        displayDockingList();
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
    
        glPopMatrix();
    
    }

    private void displayStars(AbstractMesh stars) 
    {
        glEnable(GL_POLYGON_SMOOTH);
        
        GL20.glUseProgram(ShaderBank.brightProgId);
        
        GlLifecycle.exitOnGLError("displayStars1");

        // Reset view and model matrices
        Matrix4 viewMatrix = view.getTransform();
        Matrix4 modelMatrix = new Matrix4();

        // Upload matrices to the uniform variables
        ShaderBank.updateViewMatrix(viewMatrix);
        ShaderBank.updateModelMatrix(modelMatrix);
                
        ShaderBank.uploadBrightMatrices();
        
        GlLifecycle.exitOnGLError("displayStars2");
         
        
        stars.display();
        
        GL20.glUseProgram(0);
        glDisable(GL_POLYGON_SMOOTH);
        
        GlLifecycle.exitOnGLError("displayStars3");
        
    }

    private void displayMiningList()
    {
        int y = 250;
        int left = width-200;

        Fonts.g12.drawString("In mining range:", Colors.LABEL, left, y);
        y -= 20;
        
        for(Solar body : bodiesInMiningRange)
        {
            Fonts.g12.drawString(body.name, Colors.FIELD, left, y);
            y -= 18;
        }        
    }

    
    private void displayDockingList()
    {
        int y = 350;
        int left = width-200;

        Fonts.g12.drawString("Automatic landing options:", Colors.LABEL, left, y);
        y -= 20;
        
        for(Solar body : bodiesInDockingRange)
        {
            Fonts.g12.drawString(body.name, Colors.FIELD, left, y);
            y -= 18;
        }        
    }
    
    /*
    private void displayNebulae(Camera camera) 
    {
        GL20.glUseProgram(0);
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, nebula.id);
        
        glMatrixMode(GL_MODELVIEW); 
        glLoadIdentity();

        Vec3 rotation = camera.getRotation();
        glRotatef(rotation.x, 1, 0, 0);
        glRotatef(rotation.y, 0, 1, 0);
        glRotatef(rotation.z, 0, 0, 1);
    
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        
        glBegin(GL_QUADS);

        float size = 300;
        float depth = 800;
        
        glColor4f(1.0f, 0.3f, 0.5f, 0.2f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(size, size, -depth);

        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(-size, size, -depth);

        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(-size, -size, -depth);

        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(size, -size, -depth);

        glEnd();
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);

        glPopMatrix();
    }
    */
    
    private void showBody(MultiMesh mesh) 
    {
        Solar body = mesh.getPeer();
        Vec3 meshPos = mesh.getPos();

        Vec3 relPos = new Vec3(meshPos);
        relPos.sub(ship.pos);
        
        Vec3 modelPos = toVec3(relPos);
        Vec3 modelAngle = new Vec3(mesh.getAngleX(), mesh.getAngleY(), 0);
        // modelScale = new Vec3(1, 1, 1);

        //-- Update matrices
        // Reset view and model matrices
        Matrix4 viewMatrix = view.getTransform();
        Matrix4 modelMatrix = new Matrix4();
        
        // Scale, translate and rotate model
        // Matrix4.scale(modelScale, modelMatrix, modelMatrix);
        Matrix4.translate(modelPos, modelMatrix, modelMatrix);
        Matrix4.rotate(Math3D.degToRad(modelAngle.z), new Vec3(0, 0, 1), 
                modelMatrix, modelMatrix);
        Matrix4.rotate(Math3D.degToRad(modelAngle.y), new Vec3(0, 1, 0), 
                modelMatrix, modelMatrix);
        Matrix4.rotate(Math3D.degToRad(modelAngle.x), new Vec3(1, 0, 0), 
                modelMatrix, modelMatrix);

        ShaderBank.updateViewMatrix(viewMatrix);
        ShaderBank.updateModelMatrix(modelMatrix);
        
        ShaderBank.updateLightPos((float)-ship.pos.x, (float)-ship.pos.y, (float)-ship.pos.z);
        
        if(body != null && body.btype == Solar.BodyType.SUN)
        {
            GL20.glUseProgram(ShaderBank.brightProgId);
            ShaderBank.uploadBrightMatrices();
        }
        else
        {
            GL20.glUseProgram(ShaderBank.shadedProgId);
            ShaderBank.uploadShadedMatrices();
        }
        
         
        mesh.display();
        
        GL20.glUseProgram(0);
        
        // Hajo: calculate name label position
        // projectionMatrix * viewMatrix * modelMatrix * in_Position;
        
        Matrix4 mvp = Matrix4.mul(viewMatrix, modelMatrix, null);
        Matrix4.mul(ShaderBank.projectionMatrix, mvp, mvp);

        Vec4 pos = new Vec4(0, 0, 0, 1);
        Vec4 result = Matrix4.transform(mvp, pos, null);        

        mesh.lastScreenX = (int)(result.x/result.w * width/2 + width/2);

        int viewHeight = height - CockpitPanel.HEIGHT;
        mesh.lastScreenY = (int)((result.y/result.w * viewHeight/2) + viewHeight/2) + CockpitPanel.HEIGHT;
    }


    private void showLabel(MultiMesh mesh)
    {
        Solar body = mesh.getPeer();

        if(body != null)
        {
            if(mesh == space.selectedMesh)
            {
                fillBorder(mesh.lastScreenX-5, mesh.lastScreenY-5, 10, 10, 1, Colors.CYAN);
            }
            
            Fonts.g12.drawStringScaled(body.name, Colors.CYAN,
                    mesh.lastScreenX+10, mesh.lastScreenY-16, 1.0f);
        
        }
    }

    
    private void setDestination() 
    {
        setDestination(ship, space.selectedMesh);
    }
    
    
    public static void setDestination(Ship ship, MultiMesh selectedMesh) 
    {
        Solar body = selectedMesh.getPeer();
        if(body != null)
        {
            // Vec3 bodyPos = body.getAbsolutePosition();
            Vec3 bodyPos = selectedMesh.getPos();
            Vec3 destination = new Vec3(bodyPos);

            // Hajo: take destination size into account - don't fly to the center of a planet

            logger.log(Level.INFO, "Setting {0} as destination.", body.name);

            Vec3 direction = new Vec3(ship.pos);
            direction.sub(destination);

            logger.log(Level.INFO, "Distance = {0}", direction.length());
            
            
            direction.normalise();
            
            if(body.btype == Solar.BodyType.STATION)
            {
                // Hajo: station sizes are wrong in scale
                direction.scale(30);
            }
            else
            {
                direction.scale(body.radius * 3 * Space.DISPLAY_SCALE);
            }
            
            destination.add(direction);

            ship.destination = destination;

            // logger.log(Level.INFO, "Body position: {0}", bodyPos.toString());
            logger.log(Level.INFO, "Body radius: {0}", body.radius);
            logger.log(Level.INFO, "Destination: {0}", ship.destination.toString());
        }
    }

    private void displayDebris(View camera) 
    {
        GL20.glUseProgram(ShaderBank.brightProgId);
        glDisable(GL_DEPTH_TEST);
        
        int zSource = 8000;
        int zSpeed = (int)(speed * 0.1 * 5);
        
        if(autopilot != null)
        {
            zSpeed = (int)(autopilot.currentSpeed * 0.01 * 5);
        }
        
        if(zSpeed > 1)
        {
            int count = (int)(Math.pow(zSpeed, 1.6) * 0.007);

            int lifetime = Math.min(2*zSource/zSpeed, 1000);
            
            // System.err.println("zSpeed=" + zSpeed + " count=" + count);
            
            for(int i=0; i < count; i++)
            {
                debrisDriver.addParticle(
                        (int)(Math.random() * zSource*2 - zSource),
                        (int)(Math.random() * zSource*2 - zSource),
                        -zSource,
                        0, 0, zSpeed, lifetime, 
                        (int)(Math.random() * 7),
                        0);
            }

            debrisDriver.driveParticles();
            
            Matrix4 viewMatrix = new Matrix4();
            Matrix4 modelMatrix = new Matrix4();

            ShaderBank.updateViewMatrix(viewMatrix);
            ShaderBank.updateModelMatrix(modelMatrix);
            // ShaderBank.uploadBrightMatrices();
            
            GlLifecycle.exitOnGLError("displayDebris");
            debrisDriver.drawParticles();
            GlLifecycle.exitOnGLError("displayDebris");
        }
        else
        {
            debrisDriver.clear();
        }

        glEnable(GL_DEPTH_TEST);
        GL20.glUseProgram(0);
    }

    public void lookAt(Vec3 lookAt)
    {
        Vec3 direction = new Vec3(lookAt);
        direction.sub(ship.pos);
        direction.normalise();
        
        Vec3 front = new Vec3((float)-direction.x, (float)-direction.y, (float)-direction.z);
        Vec3 up = new Vec3(0, 1, 0); // Hajo: pray ...
        Vec3 right = Vec3.cross(up, front, null);
        
        view.orient(front, right);
    }

    private static Vec3 toVec3(Vec3 pos)
    {
        return new Vec3((float)pos.x, (float)pos.y, (float)pos.z);
    }

}
