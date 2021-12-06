package flyspace.ogl;

import flyspace.ui.Display;
import flyspace.ui.KeyboardFeeder;
import flyspace.ui.MouseFeeder;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;


/**
 *
 * @author Hj. Malthaner
 */
public class GlLifecycle
{
    private static long window;
    private static GLFWErrorCallback errorCallback;
    private static final MouseFeeder mouseFeeder = new MouseFeeder();
    private static final KeyboardFeeder keyboardFeeder = new KeyboardFeeder();
    private static Callback debugProc;
    
    public static void create(String title, boolean fullscreen) throws IOException
    {
        //Initialize GLFW.
        glfwInit();
        //Setup an error callback to print GLFW errors to the console.
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        
        //Set resizable
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);
        //Request an OpenGL 3.3 Core context.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        // glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); 
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE); 
        int windowWidth = Display.width;
        int windowHeight = Display.height;
        long monitor = 0;
        
        if(fullscreen) 
        {
            // Get the primary monitor.
            monitor = glfwGetPrimaryMonitor();
            // Retrieve the desktop resolution
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            windowWidth = vidMode.width();
            windowHeight = vidMode.height();
        }
        
        // Create the window with the specified title.
        window = glfwCreateWindow(windowWidth, windowHeight, title, monitor, 0);       

        if(window == 0) 
        {
            throw new RuntimeException("Failed to create window");
        }
        
        
        // Make this window's context the current on this thread.
        glfwMakeContextCurrent(window);

        GL.createCapabilities();
        // debugProc = GLUtil.setupDebugMessageCallback();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        glfwSetWindowPos(window, (screenSize.width - windowWidth)/2, (screenSize.height - windowHeight - 32) / 2);
        
        glfwSetCursorPosCallback(window, mouseFeeder.posCallback);
        glfwSetMouseButtonCallback(window, mouseFeeder.mouseButtonCallback);
        glfwSetScrollCallback(window, mouseFeeder.wheelScrollCallback);
        glfwSetKeyCallback(window, keyboardFeeder.keyCallback);
        glfwSetCharCallback(window, keyboardFeeder.charCallback);
    }


    public static void init()
    {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);   

        // glShadeModel(GL_SMOOTH);
        // glShadeModel(GL_FLAT);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        
        FloatBuffer lightModelAmbient = BufferUtils.createFloatBuffer(4);
        lightModelAmbient.put(0.08f).put(0.08f).put(0.08f).put(1.0f).position(0);
        GL11.glLightModelfv(GL11.GL_LIGHT_MODEL_AMBIENT, lightModelAmbient);
        
        
        FloatBuffer matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(0.4f).put(0.35f).put(0.3f).put(1.0f).position(0);
        
        GL11.glMaterialfv(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 10.0f);
        
        FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
        ambient.put(0f).put(0f).put(0f).put(1.0f).position(0);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ambient);

        FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
        diffuse.put(1.0f).put(1.0f).put(1.0f).put(1.0f).position(0);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffuse);

        FloatBuffer specular = BufferUtils.createFloatBuffer(4);
        specular.put(0.5f).put(0.5f).put(0.5f).put(1.0f).position(0);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_SPECULAR, specular);
        
        // glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    
    public static void destroy()
    {
        glfwDestroyWindow(window);   
        glfwTerminate();
    }

    
    public static void exitOnGLError(String errorMessage)
    {
        int errorValue = GL11.glGetError();

        if (errorValue != GL11.GL_NO_ERROR)
        {
            String errorString = "GL Error #" + Integer.toHexString(errorValue);  // gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
            Thread.dumpStack();
            System.exit(-1);
        }
    }
    

    public static boolean windowShouldClose() 
    {
        return glfwWindowShouldClose(window);
    }

    public static void pollAndSwap() 
    {
        //Polls input.
        glfwPollEvents();
        //Swaps framebuffers.
        glfwSwapBuffers(window);
    }

    public static void reactivateContext() 
    {
        glfwMakeContextCurrent(window);
    }
}
