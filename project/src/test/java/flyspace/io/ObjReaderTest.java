package flyspace.io;

import flyspace.ogl.Mesh;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import junit.framework.TestCase;

/**
 *
 * @author hjm
 */
public class ObjReaderTest extends TestCase
{
    public void testObjReader() throws IOException
    {
        URL url = getClass().getResource("/flyspace/resources/3d/ship.obj");        
        assertNotNull(url);
        
        InputStream is = url.openStream();
        assertNotNull(is);
        
        ObjReader reader = new ObjReader();        
        Mesh mesh = reader.read(is);
        assertNotNull(mesh);        
    }
}
