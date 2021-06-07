/*
 * PortraitPanel.java
 *
 * Created: 02-Dec-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.components;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.ui.ImageCache;

/**
 * Portraits, e.g. for space station panels and space ports.
 * 
 * @author Hj. Malthaner
 */
public class PortraitPanel extends JPanel
{

    private BufferedImage upper;
    private BufferedImage mid;
    private BufferedImage lower;

    protected BufferedImage full;
    protected BufferedImage backdrop;

    public PortraitPanel()
    {
    }

    public void setStation(final Solar station, Random rng, ImageCache imageCache)
    {
        BufferedImage [] parts = null;
        BufferedImage [] backgrounds = imageCache.portraitBackgrounds;
        
        if(station.society.race == Society.Race.Poisonbreathers) 
        {
            parts = imageCache.poisonbreathers;
        }
        else if(station.society.race == Society.Race.Rockeaters) 
        {
            parts = imageCache.rockeaters;
            backgrounds = imageCache.rockeaterPortraitBackgrounds;
        }
        else if(station.society.race == Society.Race.Clonkniks) 
        {
            parts = imageCache.clonkniks;
            backgrounds = imageCache.clonknikPortraitBackgrounds;
        }
        else if(station.society.race == Society.Race.Floatees) 
        {
            parts = imageCache.floatees;
            backgrounds = imageCache.floateePortraitBackgrounds;
        }
        else if(station.society.race == Society.Race.Terraneans) 
        {
            if(rng.nextDouble() < 0.5) 
            {
                parts = imageCache.male;
            }
            else 
            {
                parts = imageCache.female;
            }
        }

        final int stride = parts.length / 3;

        upper = parts[0 + rng.nextInt(stride)];
        mid = parts[stride + rng.nextInt(stride)];
        lower = parts[stride*2 + rng.nextInt(stride)];

        if(rng.nextDouble() < 0.05) 
        {
            full = imageCache.transmissionError;
        } 
        else 
        {
            full = new BufferedImage(upper.getWidth(), 
                                     upper.getHeight() + mid.getHeight() + lower.getHeight(),
                                     BufferedImage.TYPE_INT_ARGB);
            
            Graphics gr = full.createGraphics();
            gr.drawImage(upper, 0, 0, this);
            gr.drawImage(mid, 0, upper.getHeight(), this);
            gr.drawImage(lower, 0, upper.getHeight() + mid.getHeight(), this);
            
        }
        
        backdrop = backgrounds[(int)(rng.nextDouble() * backgrounds.length)];
        
        setBackground(Color.GRAY);
    }

    @Override
    public void paint(Graphics gr)
    {
        super.paint(gr);

        final int w = getWidth();
        final int h = getHeight();

        ((Graphics2D)gr).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        gr.drawImage(backdrop, 0, 0, this);
        
        if(full.getWidth() >= w) 
        {
            gr.drawImage(full, 0, 0, this);
        }
        else 
        {
            gr.drawImage(full, 0, 0, w, h, this);
        }
    }
}
