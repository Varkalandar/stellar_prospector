package flyspace.ui;

import flyspace.ogl.GlLifecycle;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

/**
 *
 * @author Hj. Malthaner
 */
public class HyperjumpPainter extends JumpEffectPainter
{
    private static class Framebuffer 
    {
        int fb;
        int texture;
    }
    
    private final int [] colors;
    private Framebuffer fb1, fb2;
    private int frame = 0;
    
    public HyperjumpPainter()
    {
        fb1 = createFramebuffer();
        fb2 = createFramebuffer();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fb1.fb);
        glClear(GL_COLOR_BUFFER_BIT);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fb2.fb);
        glClear(GL_COLOR_BUFFER_BIT);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        
        colors = new int [128];
        
        for(int i=0; i<128; i++) 
        {
            final float hue = i/128f;
            int rgb = Color.HSBtoRGB(hue, 0.9f, 0.6f);
            colors[i] = rgb;
        }
    }
    
    
    @Override
    public void paint(String message)
    {

        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        // draw a new shape to the first buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fb1.fb);
        UiPanel.fillEllipse(width/2, height/2, 60, 60, colors[(frame*2) & 127]);
        
        checkGlError();
        
        // zoom the first buffer, paint to the second
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fb2.fb);
        checkGlError();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fb1.texture);
        checkGlError();
        
        GL11.glColor3f(1f, 1f, 1f);
        checkGlError();

        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0.03f, 0.03f);
        GL11.glVertex2i(0, 0);

        GL11.glTexCoord2f(0.97f, 0.03f);
        GL11.glVertex2i(width, 0);

        GL11.glTexCoord2f(0.97f, 0.97f);
        GL11.glVertex2i(width, height);

        GL11.glTexCoord2f(0.03f, 0.97f);
        GL11.glVertex2i(0, height);
        
        GL11.glEnd();
        checkGlError();

        // now draw the zoomed image
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        checkGlError();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fb1.texture);
        checkGlError();


        GL11.glColor3f(1f, 1f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(0, 0);

        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(width-1, 0);

        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(width-1, height-1);

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(0, height-1);
        
        GL11.glEnd();
        
        checkGlError();
        
        GlLifecycle.pollAndSwap();

        checkGlError();

        // switch the buffers
        Framebuffer t = fb2;
        fb2 = fb1;
        fb1 = t;
        
        
        safeSleep(20);
        frame ++;
    }


    private Framebuffer createFramebuffer()
    {
        int width = Display.width;
        int height = Display.height;
        
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        checkGlError();

        int [] framebuffers = new int [1];        
        GL30.glGenFramebuffers(framebuffers);

        checkGlError();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffers[0]);

        checkGlError();

        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);

        checkGlError();
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        
        System.err.println("fb=" + framebuffers[0] + " tex=" + texture);
        System.err.println("status=" + GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER));
        
        Framebuffer fb = new Framebuffer();
        fb.fb = framebuffers[0];
        fb.texture = texture;
        
        return fb;
    }
    
    private void checkGlError()
    {
        int error = GL11.glGetError();
        
        if(error != GL11.GL_NO_ERROR)
        {
            System.err.println("GL Error: " + error);
        }
    }
}
