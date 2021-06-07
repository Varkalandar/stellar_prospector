package flyspace.ui;

import flyspace.ogl.Texture;
import flyspace.ogl.TextureCache;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.lwjgl.opengl.GL11.*;

/**
 * A bitmap font class.
 *
 * @author Hj. Malthaner
 */
public class PixFont
{
    private final int letterWidths [] = new int [256];
    private final int letterHeights [] = new int [256];
    private final String slips [] = new String [256];

    private final int rasterX = 32;
    private final int rasterY = 32;
    
    /** suggested line spacing for this font */
    private int linespace;
    
    /** stores the height of the tallest letter in this font */
    private int letterHeight;

    private final Texture texture;
    
    /** 
     * Get the height of the tallest letter in this font.
     * @return the height of the tallest letter in this font
     */
    public int getLetterHeight()
    {
        return letterHeight;
    }
    
    public int getLinespace()
    {
        return linespace;
    }
    
    public void setLinespace(final int linespace)
    {
        this.linespace = linespace;
    }

    public PixFont(String filename) throws IOException
    {
        texture = TextureCache.loadTexture("/flyspace/resources/" + filename + ".png");
        
        scanDimensions(filename, texture.image);
        
        letterHeight = 0;
        
        for(int i=0; i<letterHeights.length; i++)
        {
            if(letterHeights[i] > letterHeight)
            {
                letterHeight = letterHeights[i];
            }
        }
        
        linespace = (int)((double)letterHeight*1.4 + 0.5);

        System.err.println("Letter height = " + letterHeight + " linespace = " + linespace);
    }

    public int [] getLetterWidths()
    {
        return letterWidths;
    }

    public void drawString(final String text, 
                           final int color, int x, final int y)
    {
        final int letters = text.length();
        
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);
        
        for(int p=0; p<letters; p++)
        {
            final char c = text.charAt(p);
            drawCharacter(x, y, color, c);

            x += letterWidths[c];
            
            if(p < letters-1 && slips[c] != null)
            {
                final char next = text.charAt(p+1);
                if(slips[c].indexOf(next) >= 0)
                {
                    x --;
                }
            }            
        }
        glEnd();
    }

    public void drawStringScaled(final String text, 
                                 final int color, final int x, final int y,
                                 final float factor)
    {
        drawStringScaled(text, color, x, y, factor, 0);
    }

    public void drawStringBold(final String text, 
                               final int color, final int x, final int y,
                               final float factor)
    {
        drawStringScaled(text, color, x, y, factor, 1);
        drawStringScaled(text, color, x+1, y, factor, 1);
    }

    public void drawStringScaled(final String text, 
                                 final int color, final int x, final int y,
                                 final float factor, int spacing)
    {
        final int letters = text.length();
        
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);
        float runx = 0;
        
        for(int p=0; p<letters; p++)
        {
            final char c = text.charAt(p);
            drawCharacterScaled(x + runx*factor, y, color, c, factor);

            runx += letterWidths[c] + spacing;
            
            if(p < letters-1 && slips[c] != null)
            {
                final char next = text.charAt(p+1);
                if(slips[c].indexOf(next) >= 0)
                {
                    runx --;
                }
            }
        }
        glEnd();
    }
    
    public void drawStringCentered(final String text, 
                                 final int color, final int x, final int y,
                                 final int w,
                                 final float factor)
    {
        int width = (int)(getStringWidth(text) * factor + 0.5);
        
        drawStringScaled(text, color, x + (w - width)/2, y, factor);
        
    }
    
    public void drawText(final String text,
                         final int color, final int left, int top, final int width,
                         float factor)
    {
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);
        
        // reverse scale the span
        int span = (int)(width/factor);
        
        final String breaks = " ";

        int start = 0;
        int x = 0;
        int y = 0;
        int wordWidth = 0;
        
        final int letters = text.length();
        
        // scan text, draw word by word
        for(int p=0; p<letters; p++)
        {
            final char c = text.charAt(p);
            wordWidth += letterWidths[c];

            if(p == letters-1 || breaks.indexOf(c) >= 0 || c == '\n')
            {
                // we found a word end

                if(x + wordWidth >= span)
                {
                    // we need a line break
                    x = 0;
                    y += linespace;
                }

                // draw word

                for(int i=start; i<=p; i++)
                {
                    final char cc = text.charAt(i);
                    drawCharacterScaled(left + (x * factor), 
                                        top - (y * factor), 
                                        color, cc, factor);
                    x += letterWidths[cc];
                }

                if(c == '\n')
                {
                    // hard line break
                    x = 0;
                    y += linespace;
                }
                         
                // next word;
                start = p+1;
                wordWidth = 0;
            }
        }
        glEnd();
    }

    private void drawCharacter(int x, int y, int color, int character)
    {
        float tw = 8f*32f;
        float th = 32f*32f;
        
        float tx = (character % 8) * 32.0f / tw;
        float ty = (character / 8) * 32.0f / th;

        float txx = tx + (31.0f / tw);
        float tyy = ty + (31.0f / th);

        glColor3f(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF) /255f, (color & 0xFF)/255f);
        
        glTexCoord2f(tx, tyy);
        glVertex2i(x, y);

        glTexCoord2f(txx, tyy);
        glVertex2i(x+31, y);

        glTexCoord2f(txx, ty);
        glVertex2i(x+31, y+31);

        glTexCoord2f(tx, ty);
        glVertex2i(x, y+31);
    }
    
    private void drawCharacterScaled(float x, float y, int color, int character, float factor)
    {
        float tw = 8f*32f;
        float th = 32f*32f;
        
        float tx = (character % 8) * 32.0f / tw;
        float ty = (character / 8) * 32.0f / th;

        float txx = tx + (31.0f / tw);
        float tyy = ty + (31.0f / th);

        glColor3f(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF) /255f, (color & 0xFF)/255f);
        
        glTexCoord2f(tx, tyy);
        glVertex2f(x, y);

        glTexCoord2f(txx, tyy);
        glVertex2f(x+31*factor, y);

        glTexCoord2f(txx, ty);
        glVertex2f(x+31*factor, y+31*factor);

        glTexCoord2f(tx, ty);
        glVertex2f(x, y+31*factor);
    }

    public int getStringWidth(String text)
    {
        final int letters = text.length();
        int w = 0;
        
        for(int p=0; p<letters; p++)
        {
            final char c = text.charAt(p);
            w += letterWidths[c];
        }
        
        return w;
    }

    private void scanDimensions(String filename, final BufferedImage tilesheet) throws IOException
    {
        for(int letter=0; letter<256; letter++)
        {
            final int sx = (letter & 7) * rasterX;
            final int sy = (letter >> 3) * rasterY;

            final BufferedImage tile = tilesheet.getSubimage(sx, sy, rasterX, rasterY);

            boolean ok;
            
            ok = true;
            for(int x=rasterX-1; x>=0 && ok; x--)
            {
                for(int y=0; y<rasterY && ok; y++)
                {
                    final int argb = tile.getRGB(x, y);
                    if((argb >>> 24) > 127)
                    {
                        // found a colored pixel
                        letterWidths[letter] = x+1;
                        // System.err.println("Width " + letter + " = " + letterWidths[letter]);
                        ok = false;
                    }
                }
            }
	    
            ok = true;
            for(int y=rasterY-1; y>=0 && ok; y--)
            {
                for(int x=0; x<rasterX && ok; x++)
                {
                    final int argb = tile.getRGB(x, y);
                    if((argb >>> 24) > 127)
                    {
                        // found a colored pixel
                        letterHeights[letter] = y+1;
                        // System.err.println("Height of " + letter + " is " + letterHeights[letter]);
                        ok = false;
                    }
                }
            }
        }

        InputStream in  = Class.class.getResourceAsStream("/flyspace/resources/" + filename + ".kern");
                
        if(in == null)
        {
            // no kerning adjustments ?
            letterWidths[32] = 5;
        }
        else
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            
            String line;
            
            while((line = reader.readLine()) != null)
            {
                if(line.length() > 2)
                {
                    int c = line.charAt(0);
                    if(line.charAt(1) == ' ')
                    {
                        int adjust = Integer.parseInt(line.substring(2));
                        letterWidths[c] += adjust;
                    }
                    else
                    {
                        slips[c] = line.substring(2);
                    }
                }
            }
            
            reader.close();
        }
    }
}
