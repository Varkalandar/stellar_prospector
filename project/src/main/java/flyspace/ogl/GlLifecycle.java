package flyspace.ogl;

import java.awt.Canvas;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.util.glu.GLU.gluErrorString;

/**
 *
 * @author Hj. Malthaner
 */
public class GlLifecycle
{
    public static void init()
    {
        glEnable(GL11.GL_TEXTURE_2D);
        glEnable(GL11.GL_DEPTH_TEST);
        glDepthFunc(GL11.GL_LEQUAL);
        glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,GL11.GL_NICEST);   

        // glShadeModel(GL_SMOOTH);
        // glShadeModel(GL_FLAT);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        
        FloatBuffer lightModelAmbient = BufferUtils.createFloatBuffer(4);
        lightModelAmbient.put(0.08f).put(0.08f).put(0.08f).put(1.0f).position(0);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, lightModelAmbient);
        
        
        FloatBuffer matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(0.4f).put(0.35f).put(0.3f).put(1.0f).position(0);
        
        glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);
        glMaterialf(GL_FRONT, GL_SHININESS, 10.0f);
        
        FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
        ambient.put(0f).put(0f).put(0f).put(1.0f).position(0);
        glLight(GL_LIGHT0, GL_AMBIENT, ambient);

        FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
        diffuse.put(1.0f).put(1.0f).put(1.0f).put(1.0f).position(0);
        glLight(GL_LIGHT0, GL_DIFFUSE, diffuse);

        FloatBuffer specular = BufferUtils.createFloatBuffer(4);
        specular.put(0.5f).put(0.5f).put(0.5f).put(1.0f).position(0);
        glLight(GL_LIGHT0, GL_SPECULAR, specular);
        
        // glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);

        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void create(Canvas canvas) throws LWJGLException, IOException
    {
        Display.setParent(canvas);
        
        // setTitle(title);
        Display.setVSyncEnabled(true);
        Display.create();
        
        //Keyboard
        Keyboard.create();

        //Mouse
        Mouse.setGrabbed(false);
        Mouse.create();
    }
    
    public static void destroy()
    {
        // Methods already check if created before destroying.
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public static void exitOnGLError(String errorMessage)
    {
        int errorValue = glGetError();

        if (errorValue != GL_NO_ERROR)
        {
            String errorString = gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);

            if (Display.isCreated())
            {
                Display.destroy();
            }
            
            System.exit(-1);
        }
    }
    
}
