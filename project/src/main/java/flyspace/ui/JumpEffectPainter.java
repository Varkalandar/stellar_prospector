package flyspace.ui;

import flyspace.ogl.GlLifecycle;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Hj. Malthaner
 */
public class JumpEffectPainter 
{
    private final int [] colors;
    private int time;
    
    public JumpEffectPainter()
    {
        colors = new int [512];
        
        for(int i=0; i<512; i++) 
        {
            final float hue = 511.0f/i;
            int rgb = Color.HSBtoRGB(hue, 0.9f, 0.5f);
            colors[i] = 0x77000000 | rgb;
        }
        
        time = 1000;
    }
    
    public void paint(String message)
    {
        // glEnable(GL_BLEND);
        // glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        glClear(GL_COLOR_BUFFER_BIT);

        for(int i=0; i<512; i++)
        {
            final int dist = 1000 + i*200 - time * 4;

            if(dist > 0) 
            {
                int radius = 20000/dist;
                UiPanel.fillEllipse(width/2, height/2, radius+1, radius+1, colors[i]);
            }
        }
        
        GlLifecycle.pollAndSwap();
        
        time += 20;
        safeSleep(20);
    }
    
    protected void safeSleep(int millis)
    {
        try 
        {
            Thread.sleep(20);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(JumpEffectPainter.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }
}
