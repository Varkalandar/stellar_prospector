package flyspace.ui;

import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Hj. Malthaner
 */
public abstract class UiPanel
{
    private final ArrayList<Trigger> triggers;
    protected int width, height;

    
    public UiPanel()
    {
        triggers = new ArrayList<>();
    }
    
    
    public final void  addTrigger(Trigger trigger)
    {
        triggers.add(trigger);
    }
    
    public Trigger trigger(int x, int y)
    {
        for(Trigger trigger : triggers)
        {
            if(trigger.area.contains(x, y) && trigger.isEnabled())
            {
                return trigger;
            }
        }
        
        return null;
    }
            
    public final void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    
    abstract public void activate();
    
    abstract public void handleInput();
    
    abstract public void display();

    public void displayTriggers()
    {
        for(Trigger trigger : triggers)
        {
            trigger.display();
        }
    }
    
    
    public static void setupTextPanel(int width, int height)
    {
        glMatrixMode(GL_PROJECTION); 
        glLoadIdentity(); 

	glOrtho(0, width, 0, height, 1, -1);
        
        glMatrixMode(GL_MODELVIEW); 
        glLoadIdentity(); 
        glViewport(0, 0, width, height);
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    
    public static void fillRect(int x, int y, int w, int h, int argb)
    {
        glBindTexture(GL_TEXTURE_2D, 0);

        glBegin(GL_QUADS);

        glColor4f(((argb >> 16) & 0xFF)/255f, ((argb >> 8) & 0xFF) /255f, (argb & 0xFF)/255f, 
                  ((argb >> 24) & 0xFF)/255f);

        glVertex2i(x, y);
        glVertex2i(x+w, y);
        glVertex2i(x+w, y+h);
        glVertex2i(x, y+h);

        glEnd();
    }

    public static void fillBorder(int x, int y, int w, int h, int d, int argb)
    {
        glBindTexture(GL_TEXTURE_2D, 0);

        glBegin(GL_QUADS);

        glColor4f(((argb >> 16) & 0xFF)/255f, ((argb >> 8) & 0xFF) /255f, (argb & 0xFF)/255f, 
                  ((argb >> 24) & 0xFF)/255f);

        // top
        glVertex2i(x, y);
        glVertex2i(x+w, y);
        glVertex2i(x+w, y+d);
        glVertex2i(x, y+d);

        // bottom
        glVertex2i(x, y+h-d);
        glVertex2i(x+w, y+h-d);
        glVertex2i(x+w, y+h);
        glVertex2i(x, y+h);

        // left
        glVertex2i(x, y+d);
        glVertex2i(x+d, y+d);
        glVertex2i(x+d, y+h-d);
        glVertex2i(x, y+h-d);

        // right
        glVertex2i(x+w-d, y+d);
        glVertex2i(x+w, y+d);
        glVertex2i(x+w, y+h-d);
        glVertex2i(x+w-d, y+h-d);

        glEnd();
    }
    
    public static void fillCircle(int x, int y, int radius, int argb)
    {
        int corners = Math.max(12, radius/4);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBegin(GL_POLYGON);
        
        glColor4f(((argb >> 16) & 0xFF)/255f, ((argb >> 8) & 0xFF) /255f, (argb & 0xFF)/255f, 
                  ((argb >> 24) & 0xFF)/255f);
        
        for(int i=0; i<corners; i++)
        {
            double angle = 2 * Math.PI * i / corners;
            glVertex3d(x + Math.cos(angle) * radius, y + Math.sin(angle) * radius, 0.0);
        }
        glEnd();
    }

    public static void fillEllipse(int x, int y, int xRad, int yRad, int argb)
    {
        int corners = Math.max(12, Math.max(xRad, yRad)/4);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBegin(GL_POLYGON);
        
        glColor4f(((argb >> 16) & 0xFF)/255f, ((argb >> 8) & 0xFF) /255f, (argb & 0xFF)/255f, 
                  ((argb >> 24) & 0xFF)/255f);
        
        for(int i=0; i<corners; i++)
        {
            double angle = 2 * Math.PI * i / corners;
            glVertex3d(x + Math.cos(angle) * xRad, y + Math.sin(angle) * yRad, 0.0);
        }
        glEnd();
    }

    public static void drawCircle(int x, int y, int radius, int argb)
    {
        int corners = Math.max(12, radius);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBegin(GL_LINE_LOOP);
        
        glColor4f(((argb >> 16) & 0xFF)/255f, ((argb >> 8) & 0xFF) /255f, (argb & 0xFF)/255f, 
                  ((argb >> 24) & 0xFF)/255f);
        
        for(int i=0; i<corners; i++)
        {
            double angle = 2 * Math.PI * i / corners;
            glVertex3d(x + Math.cos(angle) * radius, y + Math.sin(angle) * radius, 0.0);
        }
        glEnd();
    }

    public static void drawRoundRect(int x, int y, int w, int h, int argb)
    {
        int d = 1;
        glBindTexture(GL_TEXTURE_2D, 0);

        glBegin(GL_QUADS);

        glColor4f(((argb >> 16) & 0xFF)/255f, ((argb >> 8) & 0xFF) /255f, (argb & 0xFF)/255f, 
                  ((argb >> 24) & 0xFF)/255f);

        // top
        glVertex2i(x+1, y);
        glVertex2i(x+w-2, y);
        glVertex2i(x+w-2, y+d);
        glVertex2i(x+1, y+d);

        // bottom
        glVertex2i(x+1, y+h-d);
        glVertex2i(x+w-2, y+h-d);
        glVertex2i(x+w-2, y+h);
        glVertex2i(x+1, y+h);

        // left
        glVertex2i(x, y+d);
        glVertex2i(x+d, y+d);
        glVertex2i(x+d, y+h-d);
        glVertex2i(x, y+h-d);

        // right
        glVertex2i(x+w-d, y+d);
        glVertex2i(x+w, y+d);
        glVertex2i(x+w, y+h-d);
        glVertex2i(x+w-d, y+h-d);

        glEnd();
    }

}
