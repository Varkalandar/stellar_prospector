package flyspace.ui;

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
    private int [] colors;
    private int time;
    
    public JumpEffectPainter()
    {
        colors = new int [256];
        
        for(int i=0; i<256; i++) 
        {
            final float hue = 255.0f/i;
            int rgb = Color.HSBtoRGB(hue, 1.0f, 0.95f);
            colors[i] = 0x77000000 | rgb;
        }
        
        time = 1000;
    }
    
    public void paint(int countdown)
    {
        // glEnable(GL_BLEND);
        // glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int width = Display.getWidth();
        int height = Display.getHeight();
        UiPanel.setupTextPanel(width, height);

        glClear(GL_COLOR_BUFFER_BIT);

        for(int i=0; i<256; i++)
        {
            final int dist = 1000 + i*50 - time;

            if(dist > 0) 
            {
                final int radius = 10000/dist;

                UiPanel.fillEllipse(width/2, height/2, radius+20, radius+20, colors[i]);
                // gr.drawOval(width/2 - (radius+5), height/2 - (radius+5), radius*2+10, radius*2+10);
                // gr.drawOval(width/2 - radius, height/2 - radius, radius*2, radius*2);
            }
        }
        
        time += 20;
        // todo ?
        // Display.update();
        
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(JumpEffectPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
