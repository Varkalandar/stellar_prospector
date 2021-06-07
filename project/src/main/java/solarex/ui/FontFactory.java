/*
 * FontFactory.java
 *
 * Created: 04-Feb-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import solarex.io.GameUiSettings;

/**
 * Font factory and cache.
 *
 * @author Hj. Malthaner
 */
public class FontFactory
{
    private static Font smallest = null;
    private static Font smaller = null;
    private static Font normal = null;
    private static Font larger = null;
    private static Font labelHeading = null;
    private static Font panelHeading = null;

    private static Font baseFont;
    
    static
    {
        GameUiSettings.load();
        
        String fontName = GameUiSettings.fontName;
        
        InputStream fontStream = FontFactory.class.getResourceAsStream("/solarex/resources/" + fontName);
        try
        {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(baseFont);            
            
        } 
        catch (FontFormatException ex)
        {
            Logger.getLogger(FontFactory.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(FontFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static Font getSmallest()
    {
        if(smallest == null) {
            // smallest = new Font(fontName, Font.PLAIN, 10);
            smallest = baseFont.deriveFont(Font.PLAIN, 10);
        }

        return smallest;
    }

    public static Font getSmaller()
    {
        if(smaller == null) {
            // smaller = new Font(fontName, Font.PLAIN, 11);
            smaller = baseFont.deriveFont(Font.PLAIN, 12);
        }

        return smaller;
    }

    public static Font getNormal()
    {
        if(normal == null) {
            // normal = new Font(fontName, Font.PLAIN, 12);
            normal = baseFont.deriveFont(Font.PLAIN, 13);
        }

        return normal;
    }

    public static Font getLarger()
    {
        if(larger == null) 
        {
            larger = baseFont.deriveFont(Font.PLAIN, 14);
            // larger = baseFont.deriveFont(Font.BOLD, 13);
        }

        return larger;
    }

    public static Font getLabelHeading()
    {
        if(labelHeading == null) {
            // labelHeading = new Font(fontName, Font.BOLD, 13);
            labelHeading = baseFont.deriveFont(Font.BOLD, 14);
        }

        return labelHeading;
    }


    public static Font getPanelHeading()
    {
        if(panelHeading == null) {
            // panelHeading = new Font(fontName, Font.BOLD, 20);
            panelHeading = baseFont.deriveFont(Font.BOLD, 20);
        }

        return panelHeading;
    }
}
