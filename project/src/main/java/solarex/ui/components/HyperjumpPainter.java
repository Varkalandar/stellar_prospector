/*
 * HyperjumpPainter.java
 *
 * Created: 10-Oct-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import solarex.ui.ImageCache;

/**
 *
 * @author Hj. Malthaner
 */
public class HyperjumpPainter
{
    private Color [] colors;
    private boolean active = false;

    private int time;
    private final ImageCache imageCache;

    public boolean isActive()
    {
        return active;
    }

    public boolean isDone()
    {
        return time > 3900;
    }

    public void start()
    {
        time = 0; // System.currentTimeMillis();
        active = true;
    }

    public void stop()
    {
        active = false;
    }

    public void moveJump(int deltaT)
    {
        time += deltaT;
    }

    public HyperjumpPainter(ImageCache imageCache)
    {
        this.imageCache = imageCache;
        
        colors = new Color[256];

        /*
        int rs = 0;
        int rd = 255;

        int gs = 0;
        int gd = 255;

        int bs = 255;
        int bd = 0;

        int rv = rd - rs;
        int gv = gd - gs;
        int bv = bd - bs;


        for(int i=0; i<256; i++) 
        {
            colors[i] = new Color(
                    rs + rv * i / 256,
                    gs + gv * i / 256,
                    bs + bv * i / 256,
                    128
            );
        }
        
        */
        
        for(int i=0; i<256; i++) 
        {
            final float hue = 255.0f/i;
            int rgb = Color.HSBtoRGB(hue, 1.0f, 0.95f);
            colors[i] = new Color(
                    (rgb >> 16) & 0xFF,
                    (rgb >> 8) & 0xFF,
                    (rgb >> 16) & 0xFF,
                    144
            );
            
        }
        
    }


    public void paint(final Graphics gr, final int width, final int height)
    {
        // gr.setColor(Color.BLACK);
        // gr.fillRect(0, 0, width, height);
        gr.drawImage(imageCache.hyperspace.getImage(), 0, 0, width, height, null);

        for(int i=0; i<256; i++)
        {
            gr.setColor(colors[i]);
            
            final int dist = 1000 + i*50 - time*4;

            if(dist > 0) 
            {
                final int radius = 10000/dist;

                gr.drawOval(width/2 - (radius+10), height/2 - (radius+10), radius*2+20, radius*2+20);
                gr.drawOval(width/2 - (radius+5), height/2 - (radius+5), radius*2+10, radius*2+10);
                gr.drawOval(width/2 - radius, height/2 - radius, radius*2, radius*2);
            }
        }
    }
}
