package solarex.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hj. Malthaner
 */
public class GameUiSettings 
{
    public static String fontName;

    public static void save()
    {
        File file = calculatePropsFileName();
        
        System.err.println("Saving settings to: " + file.getAbsolutePath());

        Properties props = new Properties();
        props.setProperty("font", fontName);
        try 
        {
            props.store(new FileWriter(file), "Solarex UI Settings");
        }
        catch (IOException ex) 
        {
            Logger.getLogger(GameUiSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void load() 
    {
        Properties props = new Properties();
        File file = calculatePropsFileName();
        
        if(file.exists())
        {
            System.err.println("Reading settings from: " + file.getAbsolutePath());

            try 
            {            
                props.load(new FileReader(file));
            }
            catch (IOException ex) 
            {
                Logger.getLogger(GameUiSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // fontName = "futuruka.ttf";
        fontName = "DidactGothic.ttf";
        
        if(props.getProperty("font") != null)
        {
            fontName = props.getProperty("font");
        }
    }

    private static File calculatePropsFileName()
    {
        final String userHome = System.getProperty("user.home") + File.separator;

        System.err.println("User home: " + userHome);

        File file = new File(userHome + ".solarex");
        file.mkdirs();
        file = new File(file.getAbsolutePath() + File.separator + "solarex.properties");

        return file;
    }
            
}
