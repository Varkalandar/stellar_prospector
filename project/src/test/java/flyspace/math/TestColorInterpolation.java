package flyspace.math;

import flyspace.ogl32.GL32MeshFactory;
import junit.framework.TestCase;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Hj. Malthaner
 */
public class TestColorInterpolation extends TestCase
{
    public void testColorInterpolation()
    {
        Vector3f [] colors = 
        {
            new Vector3f(0, 0, 0),
            new Vector3f(1.00f, 1.00f, 1.00f),
        };
        
        Vector3f result = new Vector3f();
        for(int i=0; i<1000; i++)
        {
            double level = i/1000.0;
            GL32MeshFactory.interpolateColor(level, colors, result);
        
            double diff = Math.abs(result.x - level);
            assertTrue("diff=" + diff, diff < 0.0001);
        }
    }
}
