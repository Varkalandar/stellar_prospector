/*
 * ImageCache.java
 *
 * Created: ???
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import solarex.system.Solar;

/**
 * Image cache and proxy.
 * 
 * @author Hj. Malthaner
 */
public class ImageCache
{
    public final ImageIcon [] planets = new ImageIcon [Solar.PlanetType.values().length];
    public final ImageIcon [] suns = new ImageIcon [Solar.SunType.values().length];
    public final ImageIcon station;
    public final ImageIcon spaceport;
    public final ImageIcon spiral;
    public final ImageIcon hyperspace;
    public final ImageIcon metalBand;
    public final ImageIcon newspaper;

    /* Portrait arrays */
    public final BufferedImage [] rockeaters = new BufferedImage[9];
    public final BufferedImage [] poisonbreathers = new BufferedImage[9];
    public final BufferedImage [] clonkniks = new BufferedImage[9];
    public final BufferedImage [] floatees = new BufferedImage[9];
    public final BufferedImage [] male = new BufferedImage[9];
    public final BufferedImage [] female = new BufferedImage[9];
    public final BufferedImage transmissionError;

    public final BufferedImage [] portraitBackgrounds = new BufferedImage[5];
    public final BufferedImage [] floateePortraitBackgrounds = new BufferedImage[4];
    public final BufferedImage [] clonknikPortraitBackgrounds = new BufferedImage[4];
    public final BufferedImage [] rockeaterPortraitBackgrounds = new BufferedImage[4];

    public final ImageIcon [] backdrops = new ImageIcon[5];

    private static final String PATH = "/solarex/resources/";
    
    /** 
     * @return An ImageIcon, or null if the path was invalid.
     */
    public static ImageIcon createImageIcon(final String path,
                                            final String description) 
    {
        ImageIcon result = null;
        URL imgURL = Class.class.getResource(path);
        
        if (imgURL != null) 
        {
            result = new ImageIcon(imgURL, description);
        }
        else 
        {
            System.err.println("Couldn't find file: " + path);
        }
        
        return result;
    }
    
    /** 
     * @return An ImageIcon, or null if the path was invalid.
     */
    public static BufferedImage createImage(final String path,
                                            final String description) 
    {
        BufferedImage result = null;
        InputStream in = Class.class.getResourceAsStream(path);
        
        if (in != null) 
        {
            try
            {
                result = ImageIO.read(in);
            } catch (IOException ex)
            {
                Logger.getLogger(ImageCache.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else 
        {
            System.err.println("Couldn't find file: " + path);
        }
        
        return result;
    }

    public ImageCache()
    {
        station = createImageIcon(PATH + "station.png", "Station");
        spaceport = createImageIcon(PATH + "city.png", "City");
        spiral = createImageIcon(PATH + "spiral.jpg", "Spiral");
        hyperspace = createImageIcon(PATH + "backdrop/hyperspace.jpg", "Hyperspace");
        metalBand = createImageIcon(PATH + "backdrop/metal_band.png", "Metal band");
        newspaper = createImageIcon(PATH + "backdrop/newspaper_foil.jpg", "Newspaper");

        planets[Solar.PlanetType.BARE_ROCK.ordinal()] =
                createImageIcon(PATH + "rock_planet.png", "Rocky planet");
        planets[Solar.PlanetType.ATM_ROCK.ordinal()] =
                createImageIcon(PATH + "sand_atmos_planet.png", "Rocky planet");
        planets[Solar.PlanetType.ICE.ordinal()] =
                createImageIcon(PATH + "ice_planet.png", "Rocky planet");
        planets[Solar.PlanetType.BIG_GAS.ordinal()] =
                createImageIcon(PATH + "gas_planet.png", "Rocky planet");
        planets[Solar.PlanetType.SMALL_GAS.ordinal()] =
                createImageIcon(PATH + "small_gas_planet.png", "Rocky planet");
        planets[Solar.PlanetType.RINGS.ordinal()] =
                createImageIcon(PATH + "ring_planet.png", "Rocky planet");
        planets[Solar.PlanetType.CLOUD.ordinal()] =
                createImageIcon(PATH + "cloud_planet.png", "Rocky planet");
        planets[Solar.PlanetType.EARTH.ordinal()] =
                createImageIcon(PATH + "earth_planet.png", "Rocky planet");
        planets[Solar.PlanetType.CARBON_RICH.ordinal()] =
                createImageIcon(PATH + "graphite_planet.png", "Rock carbon planet");
        planets[Solar.PlanetType.STATION_1.ordinal()] = station;

        suns[Solar.SunType.S_YELLOW.ordinal()] =
                createImageIcon(PATH + "yellow_sun.png", "Rocky planet");
        suns[Solar.SunType.S_ORANGE.ordinal()] =
                createImageIcon(PATH + "orange_sun.png", "Rocky planet");
        suns[Solar.SunType.S_RED_GIANT.ordinal()] =
                createImageIcon(PATH + "red_giant.png", "Rocky planet");
        suns[Solar.SunType.S_WHITE_DWARF.ordinal()] =
                createImageIcon(PATH + "white_dwarf.png", "Rocky planet");
        suns[Solar.SunType.S_BLUE_GIANT.ordinal()] =
                createImageIcon(PATH + "blue_giant.png", "Rocky planet");
        suns[Solar.SunType.S_NEUTRON.ordinal()] =
                createImageIcon(PATH + "neutron_star.png", "Neutron star");
        suns[Solar.SunType.S_BLACK_HOLE.ordinal()] =
                createImageIcon(PATH + "black_hole.png", "Black hole");
        suns[Solar.SunType.S_BROWN_DWARF.ordinal()] =
                createImageIcon(PATH + "brown_dwarf.png", "Brown dwarf");

        loadPortraits(rockeaters, "rockeater");
        loadPortraits(poisonbreathers, "poisonbreather");
        loadPortraits(clonkniks, "clonknik");
        loadPortraits(floatees, "floatee");
        loadPortraits(male, "terranean/male");
        loadPortraits(female, "terranean/female");
        transmissionError =
                createImage(PATH + "portrait_generic.png", "Transmission error");

        backdrops[0] = createImageIcon(PATH + "backdrop/space_bg_1.jpg", "Backdrop 1");
        backdrops[1] = createImageIcon(PATH + "backdrop/space_bg_2.jpg", "Backdrop 2");
        backdrops[4] = createImageIcon(PATH + "backdrop/space_bg_5.jpg", "Backdrop 5");
        
        for(int i=0; i<portraitBackgrounds.length; i++)
        {
            portraitBackgrounds[i] = 
                    createImage(PATH + "backdrop/portrait_bg_" + i + ".png", "");
        }
        for(int i=0; i<floateePortraitBackgrounds.length; i++)
        {
            floateePortraitBackgrounds[i] = 
                    createImage(PATH + "backdrop/floatee_portraits/" + i + ".png", "");
        }
        for(int i=0; i<clonknikPortraitBackgrounds.length; i++)
        {
            clonknikPortraitBackgrounds[i] = 
                    createImage(PATH + "backdrop/clonknik_portraits/" + i + ".png", "");
        }
        for(int i=0; i<rockeaterPortraitBackgrounds.length; i++)
        {
            rockeaterPortraitBackgrounds[i] = 
                    createImage(PATH + "backdrop/rock_portraits/" + i + ".png", "");
        }
    }


    private void loadPortraits(final BufferedImage [] ports, final String name)
    {
        final int stride = ports.length / 3;

        for(int n=0; n<stride; n++) 
        {
            ports[0 + n] =
                    loadPortraitPart(PATH + "" + name + "/up_" + n + ".png",
                    name);
            ports[stride + n] =
                    loadPortraitPart(PATH + "" + name + "/mid_" + n + ".png",
                    name);
            ports[stride*2 + n] =
                    loadPortraitPart(PATH + "" + name + "/low_" + n + ".png",
                    name);
        }
    }
    
    private BufferedImage loadPortraitPart(final String path, final String name)
    {
        final ImageIcon orig = createImageIcon(path, name);
        
        final Image img = orig.getImage();
        final int w = img.getWidth(null);
        final int h = img.getHeight(null);
        
        final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics gr = result.getGraphics();
        
        gr.drawImage(img, 0, 0, null);
        
        for(int y=0; y<h; y++)
        {
            for(int x=0; x<w; x++)
            {
                int argb = result.getRGB(x, y);
                if((argb & 0xFFFFFF) == 0x575E5F)
                {
                    // make area transparent
                    result.setRGB(x, y, 0);
                }
            }
            
        }
        
        return result;
    }
}
