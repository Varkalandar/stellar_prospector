package flyspace.ogl;

import flyspace.AbstractMesh;
import flyspace.FlySpace;
import static flyspace.FlySpace.logger;
import flyspace.LibraryPathExtender;
import flyspace.MeshFactory;
import flyspace.MultiMesh;
import flyspace.Space;
import flyspace.ogl32.GL32MeshFactory;
import flyspace.ogl32.ShaderBank;
import flyspace.ui.Fonts;
import flyspace.ui.UiPanel;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
 
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.glLight;
import org.lwjgl.util.glu.GLU;
import solarex.galaxy.SystemLocation;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.ui.ImageCache;
 
/**
 * Test for showcasing meshes
 * 
 * Based on LWJGL example from 
 * http://wiki.lwjgl.org/wiki/The_Quad_with_Projection,_View_and_Model_matrices
 */
public class MeshDisplayTest 
{
     
    private final Frame frame;
    private final Canvas canvas;
    
    public int displayHeight = 768;
    public int displayWidth = 1200;
    public int newDisplayHeight = displayHeight;
    public int newDisplayWidth = displayWidth;
    public boolean quitRequested;

    private SpacePanel spacePanel;
    
    
    private Space space;
    private Ship ship;
    
    // private AbstractMesh mesh;
    
    
    static
    {
        try 
        {
            LibraryPathExtender.addLibraryPath("lwjgl-2.9.1\\native\\windows");
            LibraryPathExtender.addLibraryPath("lwjgl-2.9.1/native/linux");
        }
        catch (Exception ex) 
        {
            Logger.getLogger(FlySpace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        MeshDisplayTest meshDisplayTest = new MeshDisplayTest();
        try
        {
            meshDisplayTest.createGL();
            meshDisplayTest.createPanels();
            meshDisplayTest.createMesh();
            meshDisplayTest.run();
        }
        catch (Exception ex) 
        {
            Logger.getLogger(FlySpace.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            meshDisplayTest.destroyGL();
        }
    }
    
    public void createGL() throws LWJGLException, IOException
    {
        GlLifecycle.create(canvas);
        GlLifecycle.init();
    }
    
    
    public void destroyGL()
    {
        GlLifecycle.destroy();
    }

    
    public MeshDisplayTest() 
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame = new Frame("Mesh Display Test");
        frame.setSize(displayWidth, displayHeight);
        frame.setLayout(new BorderLayout());
        // frame.setResizable(false);
        frame.setResizable(true);
        frame.setLocation((screenSize.width - displayWidth)/2, (screenSize.height - displayHeight - 32) / 2);
        frame.addWindowListener(new MeshDisplayTest.MyWindowListener());

        canvas = new Canvas();
        frame.add(canvas);
        frame.setVisible(true);

        ship = new Ship();
        
        space = new Space();
        
        try
        {
            BufferedImage icon = ImageIO.read(this.getClass().getResourceAsStream("/flyspace/resources/icon.png"));
            frame.setIconImage(icon);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        canvas.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                newDisplayWidth = canvas.getWidth();
                newDisplayHeight = canvas.getHeight();
            }
        });
    }
 
    
    public void createPanels() throws IOException
    {
        Fonts.init();
        ImageCache imageCache = new ImageCache();

        spacePanel = new SpacePanel(null, ship, space);
        activatePanel(spacePanel);
    }
    
    
    private void createMesh() throws IOException, InterruptedException
    {
        SystemLocation loca = new SystemLocation();
        loca.galacticSectorI = 0;
        loca.galacticSectorJ = 0;
        loca.ioff = 64;
        loca.joff = 64;
        loca.name = "Test";
        loca.systemNumber = 1;
        loca.systemSeed = 123456;
                
        Solar system = new Solar(loca, true);
        
        space.convertSystemParallelAndWait(system);


        // URL url = getClass().getResource("/flyspace/resources/3d/ship.obj");        
        // AbstractMesh mesh = GL32MeshFactory.createMesh(url);
        
        AbstractMesh mesh = GL32MeshFactory.createEarthTypePlanet(200, 123456);
        MultiMesh multiMesh = new MultiMesh(mesh);
        multiMesh.setPos(new Vec3(ship.pos.x, ship.pos.y, ship.pos.z-400));
        
        space.add(multiMesh);

    }
    
    
    public void activatePanel(UiPanel panel)
    {
        Insets insets = frame.getInsets();
        
        panel.setSize(displayWidth - insets.left - insets.right, 
                            displayHeight - insets.top - insets.bottom);
        panel.activate();
    }
    
    
    public void run()
    {
        long lastTime = 0;
        int frameCount = 0;
        
        while (!Display.isCloseRequested() && 
               !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) &&
               !quitRequested)
        {            
            if (Display.isVisible())
            {
                long currentTime = System.currentTimeMillis();
                if(lastTime < currentTime - 30000)
                {
                    lastTime = currentTime;
                    // Display.setTitle(title + " FPS: " + frameCount );
                    System.err.println("Average FPS last 30 seconds: " + (frameCount + 15)/30);
                    frameCount = 0;
                }


                spacePanel.handleInput();
                spacePanel.display();
                
                
                frameCount ++;
            } 
            else
            {
                if(Display.isDirty())
                {
                    spacePanel.display();
                }
                safeSleep(100);
            }
            
            Display.update();
            Display.sync(60);
        }
        
        logger.log(Level.FINE, "Exiting main loop.");
        
        frame.setVisible(false);
        frame.dispose();
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

    
    private void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
         
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
             
            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
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