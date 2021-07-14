package flyspace.ogl32;

import flyspace.io.ObjReader;
import flyspace.math.Math3D;
import flyspace.math.SimplexNoise;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Logger;
import solarex.system.Vec3;
import solarex.util.RandomHelper;

/**
 *
 * @author Hj. Malthaner
 */
public class GL32MeshFactory 
{
    private static final Logger logger = Logger.getLogger(GL32MeshFactory.class.getName());
    
    
    public static GL32Mesh createMesh(URL url) throws IOException 
    {
        InputStream is = url.openStream();
        ObjReader reader = new ObjReader();        
        ObjReader.Faces obj = reader.readFaces(is);
        
        GL32Mesh result = new GL32Mesh(obj.vertexData, obj.indexData, null);
        return result;
    }
    
    
    public static GL32Mesh createTetra(float radius, float r, float g, float b) 
    {
        float s = (float)(1.0 / Math.sqrt(2));
        
        Vec3 v1 = new Vec3(1f, 0f, -s);
        Vec3 v2 = new Vec3(-1f, 0f, -s);
        Vec3 v3 = new Vec3(0f, 1f, s);
        Vec3 v4 = new Vec3(0f, -1f, s);
        
        v1.scale(radius);
        v2.scale(radius);
        v3.scale(radius);
        v4.scale(radius);

        Vec3 normal;

        VertexData [] vertexData = new VertexData [4*3];
        VertexData vertex;
        int vertices = 0;
        
        // back
        /*
        vBuffer.put(v2.x).put(v2.y).put(v2.z);
        vBuffer.put(v3.x).put(v3.y).put(v3.z);
        vBuffer.put(v4.x).put(v4.y).put(v4.z);
*/
        
        normal = Math3D.calcNormal(v3, v2, v4);
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;

        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;

        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;

        // floor
        /*
        vBuffer.put(v2.x).put(v2.y).put(v2.z);
        vBuffer.put(v1.x).put(v1.y).put(v1.z);
        vBuffer.put(v3.x).put(v3.y).put(v3.z);
*/
        normal = Math3D.calcNormal(v2, v3, v1);
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;

        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        // left top
        /*
        vBuffer.put(v2.x).put(v2.y).put(v2.z);
        vBuffer.put(v4.x).put(v4.y).put(v4.z);
        vBuffer.put(v1.x).put(v1.y).put(v1.z);
*/
        normal = Math3D.calcNormal(v4, v2, v1);
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        /*
        vBuffer.put(v3.x).put(v3.y).put(v3.z);
        vBuffer.put(v1.x).put(v1.y).put(v1.z);
        vBuffer.put(v4.x).put(v4.y).put(v4.z);
        */
        normal = Math3D.calcNormal(v1, v3, v4);

        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        vertex = new VertexData();
        vertex.setRGB(r, g, b);
        vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
        vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        vertexData[vertices++] = vertex;
        
        int [] indices = new int [vertices];
        for(int i=0; i<vertices; i++)
        {
            indices[i] = i;
        }
        
        GL32Mesh result = new GL32Mesh(vertexData, indices, null);
        return result;
    }

    public static GL32Mesh createPrism(int sides, float radius, float height, float capPointHeight)
    {
        Vec3 v0 = new Vec3();
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 tmp = new Vec3();
        
        int vertices = 0;
        VertexData [] vertexData = new VertexData[sides * 4 * 3];
        
        for(int i=0; i<sides; i++)
        {
            VertexData vertex;
            Vec3 normal;
            
            tmp.set(0, 0, (float)radius);
            Math3D.rotY(tmp, i * 360.0 / sides, v1);
            
            tmp.set(0, 0, (float)radius);
            Math3D.rotY(tmp, (i + 1) * 360.0 / sides, v2);

            v0.y = height/2 + capPointHeight;
            v1.y = height/2;
            v2.y = height/2;

            // top lid

            normal = Math3D.calcNormal(v2, v0, v1);
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v0.x, (float)v0.y, (float)v0.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            

            Vec3 v3 = new Vec3(v2);
            v3.y = -height/2;
            Vec3 v4 = new Vec3(v1);
            v4.y = -height/2;
            
            // outer tri 1
                    
            normal = Math3D.calcNormal(v1, v3, v2);
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            // outer tri 2
            normal = Math3D.calcNormal(v1, v4, v2);

            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            
            // bottom lid
            
            v0.y = -height/2 - capPointHeight;
            v1.y = -height/2;
            v2.y = -height/2;

            normal = Math3D.calcNormal(v1, v0, v2);

            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v0.x, (float)v0.y, (float)v0.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;
            
            vertex = new VertexData();
            vertex.setRGB(1, 1, 1);
            vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
            vertex.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            vertexData[vertices++] = vertex;

        }

        int [] indices = new int [vertices];
        for(int i=0; i<vertices; i++)
        {
            indices[i] = i;
        }
        
        GL32Mesh result = new GL32Mesh(vertexData, indices, null);
        return result;
    }

    public static GL32Mesh createSphere(double radius, float r, float g, float b)
    {
        int vSteps = 80;
        
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 v3 = new Vec3();
        Vec3 v4 = new Vec3();
        Vec3 tmp = new Vec3();

        // Corners
        // 12
        // 43
        int vertices = 0;
        VertexData [] vertexData = new VertexData[vSteps * (vSteps-3) * 2 * 3];
        
        for(int j=1; j<vSteps-2; j++)
        {
            for(int i=0; i<vSteps; i++)
            {
                VertexData vertex;
                
                double angleX, angleY; 
                
                angleX = (j - vSteps/2.0) * 360.0 / vSteps;
                angleY = i * 360.0 / vSteps - 180; 

                v1.set(0, 0, (float)radius);
                Math3D.rotX(v1, angleX, tmp);
                Math3D.rotY(tmp, angleY, v1);
                
                angleX = (j - vSteps/2.0) * 360.0 / vSteps;
                angleY = (i + 1) * 360.0 / vSteps - 180; 
                
                v2.set(0, 0, (float)radius);
                Math3D.rotX(v2, angleX, tmp);
                Math3D.rotY(tmp, angleY, v2);
                
                angleX = ((j + 1) - vSteps/2.0) * 360.0 / vSteps;
                angleY = (i + 1) * 360.0 / vSteps - 180; 
                
                v3.set(0, 0, (float)radius);
                Math3D.rotX(v3, angleX, tmp);
                Math3D.rotY(tmp, angleY, v3);
                
                angleX = ((j + 1) - vSteps/2.0) * 360.0 / vSteps;
                angleY = i * 360.0 / vSteps - 180; 
                
                v4.set(0, 0, (float)radius);
                Math3D.rotX(v4, angleX, tmp);
                Math3D.rotY(tmp, angleY, v4);
                
                Vec3 n1 = new Vec3(v1);
                n1.normalise();
                
                Vec3 n2 = new Vec3(v2);
                n2.normalise();
                
                Vec3 n3 = new Vec3(v3);
                n3.normalise();
                
                Vec3 n4 = new Vec3(v4);
                n4.normalise();
                
                
                // assemble first triangle
                
                vertex = new VertexData();
                vertex.setRGB(r, g, b);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(r, g, b);
                vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
                vertex.setNormal((float)n4.x, (float)n4.y, (float)n4.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(r, g, b);
                vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
                vertex.setNormal((float)n3.x, (float)n3.y, (float)n3.z);
                vertexData[vertices++] = vertex;
                
/*
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v4.x).put(v4.y).put(v4.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
            
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n4.x).put(n4.y).put(n4.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
  */          
                // assemble second triangle
                vertex = new VertexData();
                vertex.setRGB(r, g, b);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(r, g, b);
                vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
                vertex.setNormal((float)n3.x, (float)n3.y, (float)n3.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(r, g, b);
                vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
                vertex.setNormal((float)n2.x, (float)n2.y, (float)n2.z);
                vertexData[vertices++] = vertex;
/*                
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
                vBuffer.put(v2.x).put(v2.y).put(v2.z);
                
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
                nBuffer.put(n2.x).put(n2.y).put(n2.z);
*/
            }
        }
        
        int [] indices = new int [vertices];
        for(int i=0; i<vertices; i++)
        {
            indices[i] = i;
        }
        
        GL32Mesh result = new GL32Mesh(vertexData, indices, null);
        return result;
    }
    
    public static GL32Mesh createStars(int count, float distance)
    {
        Vec3 [] sunColors = 
        {
            new Vec3(0.95f, 0.4f, 0.1f),     // red
            new Vec3(0.9f, 0.6f, 0.2f),     // orange
            new Vec3(0.8f, 0.8f, 0.2f),     // yellow
            new Vec3(0.75f, 0.75f, 0.75f),     // white
            new Vec3(0.6f, 0.7f, 0.95f),     // blue
        };

        int vertices = 0;
        VertexData [] vertexData = new VertexData[count * 2 * 3];

        for(int n=0; n<count; n++)
        {
            VertexData vertex;
            Vec3 sunColor = sunColors[(int)(Math.random() * sunColors.length)];
            float starSize = (float)(1.0 + Math.random() * 0.5f);
            double bright = 0.05 + Math.random() * 0.4 * starSize;
            
            float r = (float) (bright * (sunColor.x + Math.random() * 0.1));
            float g = (float) (bright * (sunColor.y + Math.random() * 0.1));
            float b = (float) (bright * (sunColor.z + Math.random() * 0.1));
            
            double angleX = Math.random();
            
            angleX = (angleX * angleX * angleX) * 180;
            
            if(Math.random() < 0.5) angleX = -angleX;
            
            double angleY = Math.random() * 360 - 180;
            
            Vec3 v1 = new Vec3(0, starSize*0.8f, -distance);
            Vec3 v2 = new Vec3(-starSize, -starSize, -distance);
            Vec3 v3 = new Vec3(starSize, -starSize, -distance);
            Vec3 tmp = new Vec3();

            Math3D.rotX(v1, angleX, tmp);
            Math3D.rotY(tmp, angleY, v1);

            Math3D.rotX(v2, angleX, tmp);
            Math3D.rotY(tmp, angleY, v2);

            Math3D.rotX(v3, angleX, tmp);
            Math3D.rotY(tmp, angleY, v3);

            vertex = new VertexData();
            vertex.setRGB(r, g, b);
            vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
            vertexData[vertices++] = vertex;

            vertex = new VertexData();
            vertex.setRGB(r, g, b);
            vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
            vertexData[vertices++] = vertex;

            vertex = new VertexData();
            vertex.setRGB(r, g, b);
            vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
            vertexData[vertices++] = vertex;

            v1 = new Vec3(0, -starSize*1.6f, -distance);
            v2 = new Vec3(starSize, 0, -distance);
            v3 = new Vec3(-starSize, 0, -distance);
            tmp = new Vec3();

            Math3D.rotX(v1, angleX, tmp);
            Math3D.rotY(tmp, angleY, v1);

            Math3D.rotX(v2, angleX, tmp);
            Math3D.rotY(tmp, angleY, v2);

            Math3D.rotX(v3, angleX, tmp);
            Math3D.rotY(tmp, angleY, v3);
            
            vertex = new VertexData();
            vertex.setRGB(r, g, b);
            vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
            vertexData[vertices++] = vertex;

            vertex = new VertexData();
            vertex.setRGB(r, g, b);
            vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
            vertexData[vertices++] = vertex;

            vertex = new VertexData();
            vertex.setRGB(r, g, b);
            vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
            vertexData[vertices++] = vertex;
        }
        
        int [] indices = new int [vertices];
        for(int i=0; i<vertices; i++)
        {
            indices[i] = i;
        }
        
        GL32Mesh result = new GL32Mesh(vertexData, indices, null);
        return result;
    }
    
    public static GL32Mesh createEarthTypePlanet(double radius, long seed)
    {
        Vec3 [] seaColors = 
        {
            new Vec3(0.03f, 0.07f, 0.20f),    // very deep sea
            new Vec3(0.07f, 0.13f, 0.30f),    // deep sea
            new Vec3(0.11f, 0.19f, 0.35f),    // sea
        };
                
        Vec3 [] landColors = 
        {
            new Vec3(0.3f, 0.3f, 0.0f),    // shore ??
            new Vec3(0.40f, 0.30f, 0.15f),    // desert
            new Vec3(0.3f, 0.30f, 0.1f),    // jungle
            new Vec3(0.32f, 0.28f, 0.15f),    // forest
            new Vec3(0.36f, 0.34f, 0.16f),    // barren
            new Vec3(0.46f, 0.35f, 0.20f),    // barren
            new Vec3(0.40f, 0.38f, 0.35f),    // highlands
            new Vec3(0.60f, 0.58f, 0.55f),
            new Vec3(0.90f, 0.91f, 0.93f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
        };
        
        Vec3 [] snowColors = 
        {
            new Vec3(0.60f, 0.58f, 0.55f),
            new Vec3(0.75f, 0.76f, 0.77f),    // snow
            new Vec3(0.85f, 0.86f, 0.87f),    // snow
            new Vec3(0.90f, 0.91f, 0.93f),    // snow
            new Vec3(0.98f, 0.99f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
        };
        
        SimplexNoise surfaceGen = new SimplexNoise(seed);
        surfaceGen.setFrequency(0.08);
        surfaceGen.setPersistence(0.75);
        surfaceGen.setOctaves(7);
        
        SimplexNoise atmosphereGen = new SimplexNoise(seed*23);
        atmosphereGen.setFrequency(0.1);
        atmosphereGen.setPersistence(0.8);
        atmosphereGen.setOctaves(7);
        atmosphereGen.setYScale(2);
        
        SurfaceParameters surface = new SurfaceParameters();
        
        surface.simnoise = surfaceGen;
        surface.mountainSteepness = 1.3;
        surface.polarCapLimit = 0.65;
        surface.waterLevel = 0.32;
        surface.roughness = 0.02;
        surface.seaColors = randomizeColors(seaColors, seed);
        surface.landColors = randomizeColors(landColors, seed);
        surface.polarSnowColors = randomizeColors(snowColors, seed);
        
        AtmosphereParameters atmosphere = null;
        
        atmosphere = new AtmosphereParameters();
        atmosphere.simnoise = atmosphereGen;
        atmosphere.threshholdClearSky = 0.22;
        atmosphere.threshholdFullCloud = 0.50;
        
        
        return createPlanet(surface, atmosphere, radius);
    }
    
    public static GL32Mesh createMarsTypePlanet(double radius, long seed)
    {
        Vec3 [] seaColors = 
        {
            new Vec3(0.20f, 0.20f, 0.32f),
            new Vec3(0.20f, 0.20f, 0.32f),
        };
        
        Vec3 [] landColors = 
        {
            new Vec3(0.32f, 0.28f, 0.24f),     // depressions
            new Vec3(0.35f, 0.28f, 0.25f),     // plains middle
            new Vec3(0.41f, 0.28f, 0.25f),
            new Vec3(0.45f, 0.29f, 0.25f),
            new Vec3(0.50f, 0.30f, 0.25f),
            new Vec3(0.53f, 0.31f, 0.25f),
            new Vec3(0.56f, 0.36f, 0.30f),
            new Vec3(0.56f, 0.48f, 0.34f),
            new Vec3(0.56f, 0.52f, 0.44f),
            new Vec3(0.95f, 0.96f, 0.97f),
            new Vec3(0.97f, 0.98f, 0.99f),
            new Vec3(1.00f, 1.00f, 1.00f),
        };
        
        Vec3 [] snowColors = 
        {
            new Vec3(0.60f, 0.58f, 0.55f),
            new Vec3(0.75f, 0.76f, 0.77f),    // snow
            new Vec3(0.85f, 0.86f, 0.87f),    // snow
            new Vec3(0.90f, 0.91f, 0.93f),    // snow
            new Vec3(0.98f, 0.99f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
        };
        
        SimplexNoise surfaceGen = new SimplexNoise(seed);
        surfaceGen.setFrequency(0.3);
        surfaceGen.setPersistence(0.85);
        surfaceGen.setOctaves(6);
        
        SimplexNoise atmosphereGen = new SimplexNoise(seed*23);
        atmosphereGen.setFrequency(0.1);
        atmosphereGen.setPersistence(0.8);
        atmosphereGen.setOctaves(7);
        atmosphereGen.setYScale(2);
        
        SurfaceParameters surface = new SurfaceParameters();
        
        surface.simnoise = surfaceGen;
        surface.mountainSteepness = 1.9;
        surface.polarCapLimit = 0.70;
        surface.waterLevel = 0;
        surface.roughness = 0.02;
        surface.seaColors = seaColors;
        surface.landColors = randomizeColors(landColors, seed);
        surface.polarSnowColors = snowColors;
        
        AtmosphereParameters atmosphere = null;
        
        atmosphere = new AtmosphereParameters();
        atmosphere.simnoise = atmosphereGen;
        atmosphere.threshholdClearSky = 0.32;
        atmosphere.threshholdFullCloud = 0.60;
        
        
        return createPlanet(surface, atmosphere, radius);
    }
    
    public static GL32Mesh createRockTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.23f, 0.23f, 0.23f),
            new Vec3(0.30f, 0.27f, 0.25f),
            new Vec3(0.35f, 0.32f, 0.3f),
            new Vec3(0.45f, 0.40f, 0.32f),
            new Vec3(0.52f, 0.50f, 0.46f),
            new Vec3(0.60f, 0.56f, 0.5f),
            new Vec3(0.62f, 0.56f, 0.55f),
        };

        Vec3 [] randomPlanetColors = randomizeColors(planetColors, seed);
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setOctaves(8);
        simnoise.setPersistence(0.75);

        simnoise.setFrequency(0.17);
        return createPlanet(simnoise, 1.5, null, randomPlanetColors, radius, 0.01, 1.0, 0);

        // simnoise.setFrequency(0.0005);
        // return createPlanetField(simnoise, planetColors, radius*10, radius/10, 1.0, 0);
    }
    
    public static GL32Mesh createCarbonRichPlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(1.0f, 1.0f, 1.0f),
            new Vec3(0.30f, 0.27f, 0.25f),
            new Vec3(0.35f, 0.32f, 0.3f),
            new Vec3(0f, 0f, 0f),
            new Vec3(0.45f, 0.40f, 0.32f),
            new Vec3(0.52f, 0.50f, 0.46f),
            new Vec3(0.45f, 0.40f, 0.32f),
            new Vec3(0f, 0f, 0f),
            new Vec3(0.35f, 0.32f, 0.3f),
            new Vec3(0.30f, 0.27f, 0.25f),
            new Vec3(1.0f, 1.0f, 1.0f),
        };

        Vec3 [] randomPlanetColors = randomizeColors(planetColors, seed);
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setOctaves(8);
        simnoise.setPersistence(0.9);

        simnoise.setFrequency(0.2);
        return createPlanet(simnoise, 1.2, null, randomPlanetColors, radius, 0.01, 1.0, 0);
    }

    public static GL32Mesh createCloudTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.80f, 0.81f, 0.83f),
            new Vec3(0.80f, 0.81f, 0.83f),
            new Vec3(0.90f, 0.91f, 0.93f),
            new Vec3(0.80f, 0.81f, 0.83f),
            new Vec3(0.70f, 0.71f, 0.73f),
        };

        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setFrequency(0.2);
        simnoise.setPersistence(0.5);
        simnoise.setOctaves(5);
        simnoise.setYScale(3);
        
        return createPlanet(simnoise, 1.0, null, planetColors, radius, 0.0, 1.0, 0);
    }
    
    public static GL32Mesh createIceTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.70f, 0.81f, 0.83f),
            new Vec3(0.80f, 0.91f, 0.93f),
            new Vec3(0.80f, 0.91f, 0.93f),
            new Vec3(0.70f, 0.81f, 0.83f),
            new Vec3(0.40f, 0.51f, 0.53f),
            new Vec3(0.80f, 0.91f, 0.93f),
        };

        Vec3 [] randomPlanetColors = randomizeColors(planetColors, seed);
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setFrequency(0.2);
        simnoise.setPersistence(0.8);
        simnoise.setOctaves(8);
        simnoise.setYScale(2);
        
        return createPlanet(simnoise, 1.0, null, randomPlanetColors, radius, 0.0, 1.0, 0);
    }

    public static GL32Mesh createBrownGasGiantTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.41f, 0.35f, 0.25f),
            new Vec3(0.46f, 0.40f, 0.30f),
            new Vec3(0.36f, 0.25f, 0.15f),
            new Vec3(0.46f, 0.36f, 0.25f),
            new Vec3(0.41f, 0.35f, 0.25f),
            new Vec3(0.46f, 0.40f, 0.30f),
        };

        Vec3 [] randomPlanetColors = randomizeColors(planetColors, seed);
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setFrequency(0.1);
        simnoise.setPersistence(0.1);
        simnoise.setOctaves(3);
        simnoise.setYScale(10);
        
        return createPlanet(simnoise, 1.0, null, randomPlanetColors, radius, 0.0, 1.0, 0);
    }
    
    public static GL32Mesh createBlueGasGiantTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.60f, 0.81f, 0.93f),
            new Vec3(0.50f, 0.61f, 0.83f),
            new Vec3(0.50f, 0.71f, 0.93f),
            new Vec3(0.50f, 0.71f, 0.83f),
        };

        Vec3 [] randomPlanetColors = randomizeColors(planetColors, seed);
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setFrequency(0.3);
        simnoise.setPersistence(0.5);
        simnoise.setOctaves(3);
        simnoise.setYScale(8);
        
        return createPlanet(simnoise, 1.0, null, randomPlanetColors, radius, 0.0, 1.0, 0);
    }
    
    public static GL32Mesh createPlanet(SimplexNoise surfaceGen,
                                    double mountainSteepness,
                                    SimplexNoise atmosphereGen,
                                    Vec3 [] planetColors,
                                    double radius,
                                    double roughness,
                                    double polarCapLimit, 
                                    double waterLevel)
    {
        SurfaceParameters surface = new SurfaceParameters();
        
        surface.simnoise = surfaceGen;
        surface.mountainSteepness = mountainSteepness;
        surface.polarCapLimit = polarCapLimit;
        surface.waterLevel = waterLevel;
        surface.roughness = roughness;
        surface.seaColors = null;
        surface.landColors = planetColors;
        surface.polarSnowColors = null;
        
        AtmosphereParameters atmosphere = null;
        
        if(atmosphereGen != null)
        {
            atmosphere = new AtmosphereParameters();
            atmosphere.simnoise = atmosphereGen;
            atmosphere.threshholdClearSky = 0.22;
            atmosphere.threshholdFullCloud = 0.50;
        }
        
        
        return createPlanet(surface, atmosphere, radius);
    }
    
    private static GL32Mesh createPlanetOld(SurfaceParameters surface,
                                         AtmosphereParameters atmosphere,
                                         double radius)
    {
        // a grid in polar coordinates
        
        int vSteps = 80;
        // int tSteps = vSteps * 32;
        int tSteps = vSteps * 8;
        // int tSteps = vSteps;

        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 v3 = new Vec3();
        Vec3 v4 = new Vec3();
        Vec3 tmp = new Vec3();

        Vec3 c1 = new Vec3();
        
        // Corners
        // 12
        // 43

        // Hajo: create a higher res texture for the planet
        // ByteBuffer tData = BufferUtils.createByteBuffer(tSteps*tSteps*4);
        ByteBuffer tData = ByteBuffer.allocateDirect(tSteps*tSteps*4);
        
        for(int j=0; j<tSteps; j++)
        {
            for(int i=0; i<tSteps; i++)
            {
                double angleX = (j - tSteps/2.0) * 360.0 / tSteps;
                double angleY = i * 360.0 / tSteps - 180; 

                v1.set(0, 0, 10);
                Math3D.rotX(v1, angleX, tmp);
                Math3D.rotY(tmp, angleY, v1);
                
                if(atmosphere != null)
                {
                    float alpha = calcCloudColor(atmosphere, v1, c1);
                    
                    if(alpha < 1) // Hajo: translucent clouds?
                    {
                        calcPlanetColor(surface, v1, tmp);
                        
                        c1.scale(alpha);
                        tmp.scale(1.0f - alpha);
                        c1.x += tmp.x;
                        c1.y += tmp.y;
                        c1.z += tmp.z;
                    }
                }
                else
                {
                    calcPlanetColor(surface, v1, c1);
                }
                
                int R = (int)(c1.x * 0xFF);
                int G = (int)(c1.y * 0xFF);
                int B = (int)(c1.z * 0xFF);
                
                int rgba = (R << 24) | (G << 16) | (B << 8) | 0xFF;
                
                tData.putInt(rgba);
                
                
                // if(i==0) System.err.println("v1=" + v1);
                // if(i==0) System.err.println("c1=" + c1);
            }
        }
        tData.flip();
        
        /*
        {
            Graphics gr = tImage.createGraphics();
            gr.setColor(Color.RED);
            gr.fillRect(0, 0, tSteps/2, tSteps/2);
            gr.setColor(Color.YELLOW);
            gr.fillRect(tSteps/2, tSteps/2, tSteps/2, tSteps/2);
        }
        */
        
        /*
        try {
            ImageIO.write(tImage, "png", new FileOutputStream("./planet_texture.png"));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        */
        
        int vertices = 0;
        VertexData [] vertexData = new VertexData[vSteps * (vSteps-2) * 6];
        
        for(int j=1; j<vSteps-1; j++)
        {
            for(int i=0; i<vSteps; i++)
            {
                double angleX, angleY; 
                
                angleX = (j - vSteps/2.0) * 360.0 / vSteps;
                angleY = i * 360.0 / vSteps - 180; 

                v1.set(0, 0, (float)radius);
                Math3D.rotX(v1, angleX, tmp);
                Math3D.rotY(tmp, angleY, v1);
                
                angleX = (j - vSteps/2.0) * 360.0 / vSteps;
                angleY = (i + 1) * 360.0 / vSteps - 180; 
                
                v2.set(0, 0, (float)radius);
                Math3D.rotX(v2, angleX, tmp);
                Math3D.rotY(tmp, angleY, v2);
                
                angleX = ((j + 1) - vSteps/2.0) * 360.0 / vSteps;
                angleY = (i + 1) * 360.0 / vSteps - 180; 
                
                v3.set(0, 0, (float)radius);
                Math3D.rotX(v3, angleX, tmp);
                Math3D.rotY(tmp, angleY, v3);
                
                angleX = ((j + 1) - vSteps/2.0) * 360.0 / vSteps;
                angleY = i * 360.0 / vSteps - 180; 
                
                v4.set(0, 0, (float)radius);
                Math3D.rotX(v4, angleX, tmp);
                Math3D.rotY(tmp, angleY, v4);
                
                Vec3 n1 = new Vec3(v1);
                n1.normalise();
                
                Vec3 n2 = new Vec3(v2);
                n2.normalise();
                
                Vec3 n3 = new Vec3(v3);
                n3.normalise();
                
                Vec3 n4 = new Vec3(v4);
                n4.normalise();
                
                // noise -> heise and colors
                
                v1.scale((float)(1 + surface.roughness * calcPlanetHeight(surface, v1)));
                v2.scale((float)(1 + surface.roughness * calcPlanetHeight(surface, v2)));
                v3.scale((float)(1 + surface.roughness * calcPlanetHeight(surface, v3)));
                v4.scale((float)(1 + surface.roughness * calcPlanetHeight(surface, v4)));
                
                float tStep = 1.0f / vSteps;
                float ti = (float)i / vSteps;
                float tj = (float)j / vSteps;
                
                VertexData vertex;
                
                // assemble first triangle

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj+tStep);
                vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
                vertex.setNormal((float)n4.x, (float)n4.y, (float)n4.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti+tStep, tj+tStep);
                vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
                vertex.setNormal((float)n3.x, (float)n3.y, (float)n3.z);
                vertexData[vertices++] = vertex;
                
                
                // assemble second triangle

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti+tStep, tj+tStep);
                vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
                vertex.setNormal((float)n3.x, (float)n3.y, (float)n3.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti+tStep, tj);
                vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
                vertex.setNormal((float)n2.x, (float)n2.y, (float)n2.z);
                vertexData[vertices++] = vertex;
            }
        }
        
        int [] indices = new int [vertices];
        for(int i=0; i<vertices; i++)
        {
            indices[i] = i;
        }
        
        GL32Mesh result = new GL32Mesh(vertexData, indices, tData);
        return result;
    }

    private static GL32Mesh createPlanet(SurfaceParameters surface,
                                         AtmosphereParameters atmosphere,
                                         double radius)
    {
        // a grid in polar coordinates
        
        int vSteps = 80;
        // int tSteps = vSteps * 32;
        int tSteps = vSteps * 8;
        // int tSteps = vSteps;

        Vec3 v1 = new Vec3();
        Vec3 c1 = new Vec3();
        Vec3 tmp = new Vec3();
        
        // Corners
        // 12
        // 43

        // Hajo: create a higher res texture for the planet
        // ByteBuffer tData = BufferUtils.createByteBuffer(tSteps*tSteps*4);
        ByteBuffer tData = ByteBuffer.allocateDirect(tSteps*tSteps*4);
        
        for(int j=0; j<tSteps; j++)
        {
            for(int i=0; i<tSteps; i++)
            {
                double angleX = (j - tSteps/2.0) * 360.0 / tSteps;
                double angleY = i * 360.0 / tSteps - 180; 

                v1.set(0, 0, 10);
                Math3D.rotX(v1, angleX, tmp);
                Math3D.rotY(tmp, angleY, v1);
                
                if(atmosphere != null)
                {
                    float alpha = calcCloudColor(atmosphere, v1, c1);
                    
                    if(alpha < 1) // Hajo: process translucent clouds
                    {
                        calcPlanetColor(surface, v1, tmp);
                        
                        c1.scale(alpha);
                        tmp.scale(1.0f - alpha);
                        c1.x += tmp.x;
                        c1.y += tmp.y;
                        c1.z += tmp.z;
                    }
                }
                else
                {
                    calcPlanetColor(surface, v1, c1);
                }
                
                int R = (int)(c1.x * 0xFF);
                int G = (int)(c1.y * 0xFF);
                int B = (int)(c1.z * 0xFF);
                
                int rgba = (R << 24) | (G << 16) | (B << 8) | 0xFF;
                
                tData.putInt(rgba);
                
                // if(i==0) System.err.println("v1=" + v1);
                // if(i==0) System.err.println("c1=" + c1);
            }
        }
        tData.flip();
        
        int vertices = 0;
        // Hajo: we need an extra column of vertices to close the mesh
        VertexData [] vertexData = new VertexData[(vSteps + 1) * (vSteps-2)];
        
        for(int j=1; j<vSteps-1; j++)
        {
            for(int i=0; i<=vSteps; i++)
            {
                double angleX, angleY; 
                
                angleX = (j - vSteps/2.0) * 360.0 / vSteps;
                angleY = i * 360.0 / vSteps - 180; 

                v1.set(0, 0, (float)radius);
                Math3D.rotX(v1, angleX, tmp);
                Math3D.rotY(tmp, angleY, v1);
                
                Vec3 n1 = new Vec3(v1);
                n1.normalise();
                
                // noise -> heise and colors
                
                v1.scale((float)(1 + surface.roughness * calcPlanetHeight(surface, v1)));
                
                float ti = (float)i / vSteps;
                float tj = (float)j / vSteps;
                
                VertexData vertex;

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;
            }
        }
        
        int [] indices = new int [vertices * 6];
        int iCount = 0;
        for(int j=1; j<vSteps-1; j++)
        {
            for(int i=0; i<vSteps; i++)
            {
                int base = (j-1) * (vSteps + 1) + i;
                
                // assemble first triangle
                indices[iCount++] = base;
                indices[iCount++] = base + (vSteps + 1);
                indices[iCount++] = base + 1 + (vSteps + 1);
                
                /*

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj+tStep);
                vertex.setXYZ((float)v4.x, (float)v4.y, (float)v4.z);
                vertex.setNormal(n4.x, n4.y, n4.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti+tStep, tj+tStep);
                vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
                vertex.setNormal((float)n3.x, (float)n3.y, (float)n3.z);
                vertexData[vertices++] = vertex;
                */
                
                // assemble second triangle
                indices[iCount++] = base;
                indices[iCount++] = base + 1 + (vSteps + 1);
                indices[iCount++] = base + 1;

                /*
                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti, tj);
                vertex.setXYZ((float)v1.x, (float)v1.y, (float)v1.z);
                vertex.setNormal((float)n1.x, (float)n1.y, (float)n1.z);
                vertexData[vertices++] = vertex;

                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti+tStep, tj+tStep);
                vertex.setXYZ((float)v3.x, (float)v3.y, (float)v3.z);
                vertex.setNormal((float)n3.x, (float)n3.y, (float)n3.z);
                vertexData[vertices++] = vertex;
                
                vertex = new VertexData();
                vertex.setRGB(1, 1, 1);
                vertex.setST(ti+tStep, tj);
                vertex.setXYZ((float)v2.x, (float)v2.y, (float)v2.z);
                vertex.setNormal((float)n2.x, (float)n2.y, (float)n2.z);
                vertexData[vertices++] = vertex;
                */
            }
        }
        
        GL32Mesh result = new GL32Mesh(vertexData, indices, tData);
        return result;
    }

    private static Vec3 calcPlanetColor(SurfaceParameters surface,
                                            Vec3 v,
                                            Vec3 result) 
    {
        // Hajo: legacy support
        if(surface.seaColors == null)
        {
            return calcPlanetColorOld(surface, v, result);
        }
        
        double noise = surface.simnoise.noise(v);
        noise = Math.pow(noise, surface.mountainSteepness);

        
        double polarCapheight = surface.polarCapLimit * 10;
        
        // if(Math.abs(v.x) < 0.000000001) System.err.println("noise=" + noise);

        // Hajo: simulate polar caps
        double absY = Math.abs(v.y);
        double polar = absY - polarCapheight;

        // fake more snow towards the poles
        noise = Math.min(1.0, noise + (absY * 0.025));
        
        if(polar + noise*3 > 2)
        {
            interpolateColor(noise, surface.polarSnowColors, result);
            return result;
        }
        
        // Hajo: test for sea areas
        if(noise < surface.waterLevel)
        {
            // Hajo: normalize level
            double level = noise / surface.waterLevel;
            interpolateColor(level, surface.seaColors, result); 
            return result;
        }
        
        // Hajo: finally, the land areas

        // normalize
        double level = (noise - surface.waterLevel) / (1 - surface.waterLevel);
        
        interpolateColor(level, surface.landColors, result);
        
        return result;
    }
    
    private static Vec3 calcPlanetColorOld(SurfaceParameters surface,
                                            Vec3 v,
                                            Vec3 result) 
    {
        double noise = surface.simnoise.noise(v);
        noise = Math.pow(noise, surface.mountainSteepness);

        double polarCapheight = surface.polarCapLimit * 10;
        double level;
        
        
        // if(Math.abs(v.x) < 0.000000001) System.err.println("noise=" + noise);
        
        // Hajo: test for sea areas

        if(noise < surface.waterLevel)
        {
            // Hajo: there are 3 water colors
            level = noise / surface.waterLevel * 3;
        }
        else
        {
            level = 0.5 + (noise * (surface.landColors.length - 1));
        }
        
        // Hajo: simulate polar caps by raising level into snowy heights
        double polar = Math.abs(v.y) - polarCapheight;
        
        if(polar + noise*3 > 2)
        {
            level += polar * 5;
            level = Math.max(level, surface.landColors.length * 0.45);
            level = Math.min(level, surface.landColors.length - 1);
        }
        
        
        int index = Math.min((int)level, surface.landColors.length - 1);
        
        
        if(index == 0)
        {
            result.set(surface.landColors[index]);
        }
        else
        {
            // Hajo: mix two colors by fraction
            float f = (float)level - (float)index;
            
            result.set(surface.landColors[index-1]);
            Vec3 next = surface.landColors[index];
            
            result.scale(1.0f - f);
            
            result.x += next.x * f;
            result.y += next.y * f;
            result.z += next.z * f;
            
        }
        
        // result.set(0.5f, 0.5f, 0.5f);
        
        return result;
    }
    
    private static float calcCloudColor(AtmosphereParameters atmosphere, 
                                        Vec3 v,
                                        Vec3 result) 
            
    {
        double noise = atmosphere.simnoise.noise(v);
        float alpha;
        
        if(noise < atmosphere.threshholdClearSky)
        {
            result.set(1, 1, 1);
            alpha = 0;
        }
        else if(noise > atmosphere.threshholdFullCloud)
        {
            result.set(1, 1, 1);
            alpha = 1;
        }
        else
        {
            result.set(1, 1, 1);
            double actual = noise - atmosphere.threshholdClearSky;
            double most = atmosphere.threshholdFullCloud - atmosphere.threshholdClearSky;
            
            alpha = (float)(actual / most);
        }
        
        return alpha;
    }

    private static double calcPlanetHeight(SurfaceParameters surface, Vec3 v) 
    {
        double level = surface.simnoise.noise(v);
        
        level = Math.max(level, surface.waterLevel);
            
        // System.err.println("level=" + level);
        
        return level;
    }

    private static Vec3[] randomizeColors(Vec3[] planetColors, long seed) 
    {
        Random rng = RandomHelper.createRNG(seed);
        
        Vec3 [] result = new Vec3 [planetColors.length];
        
        for(int i=0; i<planetColors.length; i++)
        {
            result[i] = new Vec3(planetColors[i]);
            randomizeColor(rng, result[i]);
        }
        
        return result;
    }

    private static void randomizeColor(Random rng, Vec3 color)
    {
        color.x = randomizeColorComponent(rng, color.x);
        color.y = randomizeColorComponent(rng, color.y);
        color.z = randomizeColorComponent(rng, color.z);
        
    }

    private static double randomizeColorComponent(Random rng, double c) 
    {        
        c += (rng.nextDouble() - 0.5) * 0.04;
        
        if(c < 0) c = 0;
        if(c > 1) c = 1;
        
        return c;
    }

    public static Vec3 interpolateColor(double noise, Vec3[] colors, Vec3 result) 
    {
        // scale
        float level = (float)(noise * (colors.length - 1));
        
        // floor
        int index = (int)level;
        
        // Hajo: mix two colors by fraction
        float fraction = level - (float)index;
            
        Vec3 next = colors[index+1];
        result.set(colors[index]);
            
        result.scale(1.0f - fraction);
            
        result.x += next.x * fraction;
        result.y += next.y * fraction;
        result.z += next.z * fraction;
        
        return result;
    }

    private static class SurfaceParameters
    {
        SimplexNoise simnoise;
        Vec3 [] seaColors;
        Vec3 [] landColors;
        Vec3 [] polarSnowColors;
        double mountainSteepness;
        /** actual sphere deformations for mountains*/
        double roughness;
        /** 0 .. 1 */
        double polarCapLimit;
        /** 0 .. 1 */
        double waterLevel;
    }
    
    private static class AtmosphereParameters
    {
        SimplexNoise simnoise;
        Vec3 [] cloudColors;
        double threshholdClearSky = 0.22f;
        double threshholdFullCloud = 0.50f;
    }
}
