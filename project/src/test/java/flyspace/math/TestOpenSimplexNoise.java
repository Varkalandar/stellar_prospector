package flyspace.math;

import junit.framework.TestCase;

/**
 *
 * @author Hj. Malthaner
 */
public class TestOpenSimplexNoise extends TestCase 
{
    public void testValueRange()
    {
        OpenSimplexNoise osm = new OpenSimplexNoise(123456);
        
        double minLevel = 99999;
        double maxLevel = - 99999;
        
        for(int i=0; i<10000000; i++)
        {
            double noise = osm.eval(Math.random() * 100000, Math.random() * 100000, Math.random() * 100000);
            
            if(noise > maxLevel) maxLevel = noise;
            if(noise < minLevel) minLevel = noise;
        }

        System.err.println("min=" + minLevel + " max=" + maxLevel);
        
        assertTrue("minLevel=" + minLevel, minLevel > -1.0);
        assertTrue("maxLevel=" + maxLevel, maxLevel < 1.0);
    }
}
