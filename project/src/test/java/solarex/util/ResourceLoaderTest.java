package solarex.util;

import java.net.URL;
import junit.framework.TestCase;

/**
 *
 * @author hjm
 */
public class ResourceLoaderTest extends TestCase
{
    public void testURLResult()
    {
        ResourceLoader loader = new ResourceLoader();
        
        URL result = loader.getResource("/solarex/resources/station.png");
        
        assertNotNull(result);
        
        result = loader.getResource("/solarex/resources/portrait_generic.png");
        assertNotNull(result);
    }
}
