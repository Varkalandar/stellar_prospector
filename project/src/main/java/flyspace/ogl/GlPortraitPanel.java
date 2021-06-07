package flyspace.ogl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.components.PortraitPanel;

/**
 *
 * @author Hj. Malthaner
 */
public class GlPortraitPanel extends PortraitPanel
{
    private int portrait;
    private int background;
    
    public GlPortraitPanel()
    {
        portrait = glGenTextures();
        background = glGenTextures();
    }
    
    @Override
    public void setStation(final Solar station, Random rng, ImageCache imageCache)
    {
        super.setStation(station, rng, imageCache);
        
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
}
