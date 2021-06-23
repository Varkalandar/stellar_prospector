package solarex.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hjm
 */
public class ResourceLoader 
{
    private static final Logger LOGGER = Logger.getLogger(ResourceLoader.class.getName());
    private final Class me;
            
    public ResourceLoader()
    {
        me = getClass();
    }
    
    public URL getResource(String path)
    {
        URL result = me.getResource(path);
        
        if(result == null)
        {
            LOGGER.log(Level.SEVERE, "Cannot open resource: {0}", path);
        }
        
        return result;
    }
    
    public InputStream getResourceAsStream(String path)
    {
        URL resource = getResource(path);
        InputStream in = null;
        
        try 
        {
            in = resource.openStream();
        }
        catch (IOException ex) 
        {
            LOGGER.log(Level.SEVERE, "Cannot open stream for resource: {0}", path);
        }
        
        return in;
    }
}
