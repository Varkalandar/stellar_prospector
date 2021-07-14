package flyspace.ogl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.ui.ImageCache;


/**
 *
 * @author Hj. Malthaner
 */
public class GlPortraitPanel
{
    public static final Logger logger = Logger.getLogger(GlPortraitPanel.class.getName());
    
    private BufferedImage upper;
    private BufferedImage mid;
    private BufferedImage lower;

    private BufferedImage full;
    private BufferedImage backdrop;
    
    private final int portrait;
    private final int background;
    
    public GlPortraitPanel()
    {
        logger.info("Creating ...");
        portrait = glGenTextures();
        background = glGenTextures();
    }
    
    public void setStation(final Solar station, Random rng, ImageCache imageCache)
    {
        loadPortraits(station, rng, imageCache);
        
        ByteBuffer buffer;

        buffer = TextureCache.convertTextureToRGBA(full);
        TextureCache.loadTexture(portrait, buffer, full.getWidth(), full.getHeight());
        buffer = TextureCache.convertTextureToRGBA(backdrop);
        TextureCache.loadTexture(background, buffer, backdrop.getWidth(), backdrop.getHeight());
    }    

    public void display(int x, int y, int w, int h)
    {
        glBindTexture(GL_TEXTURE_2D, background);
        displayQuad(x, y, w, h);
        glBindTexture(GL_TEXTURE_2D, portrait);
        displayQuad(x, y, w, h);
    }

    private void displayQuad(int x, int y, int w, int h)
    {
        glBegin(GL_QUADS);

        glColor3f(1, 1, 1);

        glTexCoord2f(0.0f, 1.0f);
        glVertex2i(x, y);

        glTexCoord2f(1.0f, 1.0f);
        glVertex2i(x+w, y);

        glTexCoord2f(1.0f, 0.0f);
        glVertex2i(x+w, y+h);

        glTexCoord2f(0.0f, 0.0f);
        glVertex2i(x, y+h);

        glEnd();
    }
    
    public void loadPortraits(Solar station, Random rng, ImageCache imageCache)
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
            gr.drawImage(upper, 0, 0, null);
            gr.drawImage(mid, 0, upper.getHeight(), null);
            gr.drawImage(lower, 0, upper.getHeight() + mid.getHeight(), null);
            
        }
        
        backdrop = backgrounds[(int)(rng.nextDouble() * backgrounds.length)];
    }    
}
