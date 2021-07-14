package flyspace;

import flyspace.io.ObjReader;
import flyspace.math.Math3D;
import flyspace.math.SimplexNoise;
import flyspace.ogl.Mesh;
import flyspace.ogl.TextureCache;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import solarex.system.Vec3;
import solarex.util.RandomHelper;

/**
 *
 * @author Hj. Malthaner
 */
public class MeshFactory 
{
    public static Mesh createMesh(URL url) throws IOException 
    {
        InputStream is = url.openStream();
        ObjReader reader = new ObjReader();        
        ObjReader.Object obj = reader.read(is);
        
        Mesh result = new Mesh(obj.vBuffer, obj.nBuffer, obj.cBuffer, null, 0, obj.faces*3);
        return result;
    }
    
    /*
    public static Mesh createShipOld() 
    {
        FloatBuffer cBuffer, vBuffer, nBuffer;    

        Vec3 tip = new Vec3(0f, -1.0f, 4.0f);
        Vec3 left = new Vec3(-2.5f, -1.0f, -1f);
        Vec3 right = new Vec3(+2.5f, -1.0f, -1f);
        Vec3 top = new Vec3(0f, 1f, -1f);
        
        // create geometry buffers
        cBuffer = BufferUtils.createFloatBuffer(36);

        for(int i=0; i<12; i++)
        {
            cBuffer.put(0.3f).put(0.35f).put(0.4f);
        }

        cBuffer.flip();

        vBuffer = BufferUtils.createFloatBuffer(36);
      
        // back
        vBuffer.put(left.x).put(left.y).put(left.z);
        vBuffer.put(top.x).put(top.y).put(top.z);
        vBuffer.put(right.x).put(right.y).put(right.z);

        // floor
        vBuffer.put(left.x).put(left.y).put(left.z);
        vBuffer.put(right.x).put(right.y).put(right.z);
        vBuffer.put(tip.x).put(tip.y).put(tip.z);

        // left top
        vBuffer.put(left.x).put(left.y).put(left.z);
        vBuffer.put(tip.x).put(tip.y).put(tip.z);
        vBuffer.put(top.x).put(top.y).put(top.z);

        // right top
        vBuffer.put(right.x).put(right.y).put(right.z);
        vBuffer.put(top.x).put(top.y).put(top.z);
        vBuffer.put(tip.x).put(tip.y).put(tip.z);

        vBuffer.flip();
        
        nBuffer = BufferUtils.createFloatBuffer(36);

        // todo - calculate correct normals

        Vec3 normal;
        
        // back
        normal = Math3D.calcNormal(top, left, right);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);

        // floor
        normal = Math3D.calcNormal(tip, right, left);
        
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        
        
        normal = Math3D.calcNormal(tip, left, top);
        
        // left top
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);

        normal = Math3D.calcNormal(top, right, tip);
        
        // right top
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);
        nBuffer.put(normal.x).put(normal.y).put(normal.z);

        nBuffer.flip();


        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, null, 0, 12);
        
        return result;
    }
    */

    public static Mesh createTetra(float radius, int color) 
    {
        FloatBuffer cBuffer, vBuffer, nBuffer;    

        float s = (float)(1.0 / Math.sqrt(2));
        
        Vec3 v1 = new Vec3(1f, 0f, -s);
        Vec3 v2 = new Vec3(-1f, 0f, -s);
        Vec3 v3 = new Vec3(0f, 1f, s);
        Vec3 v4 = new Vec3(0f, -1f, s);
        
        v1.scale(radius);
        v2.scale(radius);
        v3.scale(radius);
        v4.scale(radius);
        
        // create geometry buffers
        cBuffer = BufferUtils.createFloatBuffer(36);
        
        for(int i=0; i<12; i++)
        {
            cBuffer.put(((color >> 16) & 0xFF)/255f).put(((color >> 8) & 0xFF)/255f).put((color & 0xFF)/255f);
            // cBuffer.put(1f).put(1f).put(1f);
        }

        /*
        cBuffer.put(1f).put(0f).put(0f);
        cBuffer.put(1f).put(0f).put(0f);
        cBuffer.put(1f).put(0f).put(0f);

        cBuffer.put(0f).put(1f).put(0f);
        cBuffer.put(0f).put(1f).put(0f);
        cBuffer.put(0f).put(1f).put(0f);
        
        cBuffer.put(0f).put(0f).put(1f);
        cBuffer.put(0f).put(0f).put(1f);
        cBuffer.put(0f).put(0f).put(1f);

        cBuffer.put(1f).put(0f).put(1f);
        cBuffer.put(1f).put(0f).put(1f);
        cBuffer.put(1f).put(0f).put(1f);
        */
        
        cBuffer.flip();

        vBuffer = BufferUtils.createFloatBuffer(36);
      
        // back
        vBuffer.put((float)v2.x).put((float)v2.y).put((float)v2.z);
        vBuffer.put((float)v3.x).put((float)v3.y).put((float)v3.z);
        vBuffer.put((float)v4.x).put((float)v4.y).put((float)v4.z);

        // floor
        vBuffer.put((float)v2.x).put((float)v2.y).put((float)v2.z);
        vBuffer.put((float)v1.x).put((float)v1.y).put((float)v1.z);
        vBuffer.put((float)v3.x).put((float)v3.y).put((float)v3.z);

        // left top
        vBuffer.put((float)v2.x).put((float)v2.y).put((float)v2.z);
        vBuffer.put((float)v4.x).put((float)v4.y).put((float)v4.z);
        vBuffer.put((float)v1.x).put((float)v1.y).put((float)v1.z);

        vBuffer.put((float)v3.x).put((float)v3.y).put((float)v3.z);
        vBuffer.put((float)v1.x).put((float)v1.y).put((float)v1.z);
        vBuffer.put((float)v4.x).put((float)v4.y).put((float)v4.z);
        
        vBuffer.flip();
        
        nBuffer = BufferUtils.createFloatBuffer(36);

        // todo - calculate correct normals

        Vec3 normal;
        
        normal = Math3D.calcNormal(v3, v2, v4);

        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);

        normal = Math3D.calcNormal(v2, v3, v1);
        
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        
        normal = Math3D.calcNormal(v4, v2, v1);

        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);

        normal = Math3D.calcNormal(v1, v3, v4);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        nBuffer.put((float)normal.x).put((float)normal.y).put((float)normal.z);
        
        nBuffer.flip();

        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, null, 0, 12);
        
        return result;
    }

    /*
    public static Mesh createPrism(int sides, float radius, float height, float capPointHeight)
    {
        FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9 * sides * 8);
        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9 * sides * 8);
        FloatBuffer nBuffer = BufferUtils.createFloatBuffer(9 * sides * 8);

        Vec3 v0 = new Vec3();
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 tmp = new Vec3();
        
        int vertices = 0;
        
        for(int i=0; i<sides; i++)
        {
            Vec3 n;
            
            tmp.set(0, 0, (float)radius);
            Math3D.rotY(tmp, i * 360.0 / sides, v1);
            
            tmp.set(0, 0, (float)radius);
            Math3D.rotY(tmp, (i + 1) * 360.0 / sides, v2);

            v0.y = height/2 + capPointHeight;
            v1.y = height/2;
            v2.y = height/2;
            
            // top lid
            vBuffer.put(v0.x).put(v0.y).put(v0.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            vBuffer.put(v1.x).put(v1.y).put(v1.z);

            n = Math3D.calcNormal(v2, v0, v1);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);

            Vec3 v3 = new Vec3(v2);
            v3.y = -height/2;
            Vec3 v4 = new Vec3(v1);
            v4.y = -height/2;
            
            // outer tri 1
                    
            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            vBuffer.put(v3.x).put(v3.y).put(v3.z);

            n = Math3D.calcNormal(v1, v3, v2);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            
            // outer tri 2
            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v3.x).put(v3.y).put(v3.z);
            vBuffer.put(v4.x).put(v4.y).put(v4.z);
            
            n = Math3D.calcNormal(v1, v4, v2);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            
            v0.y = -height/2 - capPointHeight;
            v1.y = -height/2;
            v2.y = -height/2;

            // bottom lid
            vBuffer.put(v0.x).put(v0.y).put(v0.z);
            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            
            n = Math3D.calcNormal(v1, v0, v2);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            nBuffer.put(n.x).put(n.y).put(n.z);
            
            vertices += 12;
        }

        for(int i=0; i<vertices; i++)
        {
            cBuffer.put(1).put(1).put(1);
        }
        
        vBuffer.flip();
        cBuffer.flip();
        nBuffer.flip();
        // tBuffer.flip();
        
        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, null, 0, vertices);
        return result;
        
    }
    */
    
    /*
    public static Mesh createSmoothPrism(int sides, float radius, float height, float capPointHeight)
    {
        FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9 * sides * 8);
        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9 * sides * 8);
        FloatBuffer nBuffer = BufferUtils.createFloatBuffer(9 * sides * 8);

        Vec3 v0 = new Vec3();
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 tmp = new Vec3();
        Vec3 zero = new Vec3();
        
        int vertices = 0;
        
        for(int i=0; i<sides; i++)
        {
            Vec3 n0, n1, n2;
            
            tmp.set(0, 0, (float)radius);
            Math3D.rotY(tmp, i * 360.0 / sides, v1);
            
            tmp.set(0, 0, (float)radius);
            Math3D.rotY(tmp, (i + 1) * 360.0 / sides, v2);

            v0.y = height/2 + capPointHeight;
            v1.y = height/2;
            v2.y = height/2;
            
            // top lid
            vBuffer.put(v0.x).put(v0.y).put(v0.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            vBuffer.put(v1.x).put(v1.y).put(v1.z);

            n0 = Math3D.calcNormal(v2, v0, v1);
            nBuffer.put(n0.x).put(n0.y).put(n0.z);
            nBuffer.put(n0.x).put(n0.y).put(n0.z);
            nBuffer.put(n0.x).put(n0.y).put(n0.z);
            
            // bottom lid

            v0.y = -height/2 - capPointHeight;
            v1.y = -height/2;
            v2.y = -height/2;
            
            vBuffer.put(v0.x).put(v0.y).put(v0.z);
            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);

            n0 = Math3D.calcNormal(v1, v0, v2);
            
            nBuffer.put(n0.x).put(n0.y).put(n0.z);
            nBuffer.put(n0.x).put(n0.y).put(n0.z);
            nBuffer.put(n0.x).put(n0.y).put(n0.z);
            
            
            Vec3 v3 = new Vec3(v2);
            v2.y = height/2;
            v3.y = -height/2;
            Vec3 v4 = new Vec3(v1);
            v1.y = height/2;
            v4.y = -height/2;
            
            n1 = new Vec3(v1);
            n1.y = 0;
            n1.normalise();
            n2 = new Vec3(v2);
            n2.y = 0;
            n2.normalise();
            
            // outer tri 1
                    
            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            vBuffer.put(v3.x).put(v3.y).put(v3.z);

            nBuffer.put(n1.x).put(n1.y).put(n1.z);
            nBuffer.put(n2.x).put(n2.y).put(n2.z);
            nBuffer.put(n2.x).put(n2.y).put(n2.z);
            
            // outer tri 2
            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v3.x).put(v3.y).put(v3.z);
            vBuffer.put(v4.x).put(v4.y).put(v4.z);
            
            nBuffer.put(n1.x).put(n1.y).put(n1.z);
            nBuffer.put(n2.x).put(n2.y).put(n2.z);
            nBuffer.put(n1.x).put(n1.y).put(n1.z);
            
            
            vertices += 12;
        }

        for(int i=0; i<vertices; i++)
        {
            cBuffer.put(1).put(1).put(1);
        }
        
        vBuffer.flip();
        cBuffer.flip();
        nBuffer.flip();
        // tBuffer.flip();
        
        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, null, 0, vertices);
        return result;
        
    }
    */
    
    /*
    public static Mesh createSphere(double radius, float r, float g, float b)
    {
        int vSteps = 80;

        FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer nBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 v3 = new Vec3();
        Vec3 v4 = new Vec3();
        Vec3 tmp = new Vec3();

        // Corners
        // 12
        // 43
        int vertices = 0;
        
        for(int j=1; j<vSteps-2; j++)
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
                
                
                // assemble first triangle
                cBuffer.put(r).put(g).put(b);
                cBuffer.put(r).put(g).put(b);
                cBuffer.put(r).put(g).put(b);

                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v4.x).put(v4.y).put(v4.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
            
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n4.x).put(n4.y).put(n4.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
            
                // assemble second triangle
                cBuffer.put(r).put(g).put(b);
                cBuffer.put(r).put(g).put(b);
                cBuffer.put(r).put(g).put(b);
                
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
                vBuffer.put(v2.x).put(v2.y).put(v2.z);
                
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
                nBuffer.put(n2.x).put(n2.y).put(n2.z);
                
                vertices += 6;
            }
        }
        
        vBuffer.flip();
        cBuffer.flip();
        nBuffer.flip();
        
        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, null, 0, vertices);
        return result;
    }
    */
    
    /*
    public static Mesh createStars(int count, float distance)
    {
        Vec3 [] sunColors = 
        {
            new Vec3(0.9f, 0.4f, 0.4f),     // red
            new Vec3(0.9f, 0.7f, 0.4f),     // orange
            new Vec3(0.9f, 0.9f, 0.4f),     // yellow
            new Vec3(0.7f, 0.7f, 0.7f),     // white
            new Vec3(0.6f, 0.7f, 0.9f),     // blue
        };
        
        
        FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9 * count * 2);
        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9 * count * 2);

        for(int n=0; n<count; n++)
        {
            Vec3 sunColor = sunColors[(int)(Math.random() * sunColors.length)];
            double bright = Math.random()*0.8;
            
            float r = (float) (bright * (sunColor.x + Math.random() * 0.1));
            float g = (float) (bright * (sunColor.y + Math.random() * 0.1));
            float b = (float) (bright * (sunColor.z + Math.random() * 0.1));
            
            cBuffer.put(r).put(g).put(b);
            cBuffer.put(r).put(g).put(b);
            cBuffer.put(r).put(g).put(b);
            cBuffer.put(r).put(g).put(b);
            cBuffer.put(r).put(g).put(b);
            cBuffer.put(r).put(g).put(b);
            
            double angleX = Math.random();
            
            angleX = (angleX * angleX * angleX) * 180;
            
            if(Math.random() < 0.5) angleX = -angleX;
            
            double angleY = Math.random() * 360 - 180;
            
            float starSize = (float)(0.5 + Math.random()) * 0.5f;
            
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


            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            vBuffer.put(v3.x).put(v3.y).put(v3.z);

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


            vBuffer.put(v1.x).put(v1.y).put(v1.z);
            vBuffer.put(v2.x).put(v2.y).put(v2.z);
            vBuffer.put(v3.x).put(v3.y).put(v3.z);
        }
        
        cBuffer.flip();
        vBuffer.flip();
            
        Mesh result = new Mesh(vBuffer, cBuffer, count * 3 * 2);
        
        return result;
    }
    */
    
    
    /*
    public static Mesh createEarthTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.08f, 0.15f, 0.45f),    // very deep sea
            new Vec3(0.13f, 0.18f, 0.50f),    // deep sea
            new Vec3(0.16f, 0.21f, 0.51f),    // sea
            new Vec3(0.3f, 0.3f, 0.0f),    // shore ??
            new Vec3(0.0f, 0.3f, 0.3f),    // shore
            new Vec3(0.4f, 0.5f, 0.1f),    // jungle
            new Vec3(0.35f, 0.4f, 0.15f),    // jungle
            new Vec3(0.35f, 0.35f, 0.18f),    // forest
            new Vec3(0.40f, 0.38f, 0.35f),    // highlands
            new Vec3(0.95f, 0.96f, 0.97f),    // snow
            new Vec3(0.97f, 0.98f, 0.99f),    // snow
            new Vec3(0.98f, 0.99f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
            new Vec3(1.00f, 1.00f, 1.00f),    // snow
        };
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setFrequency(0.08);
        simnoise.setPersistence(0.75);
        simnoise.setOctaves(7);
        
        SimplexNoise atmos = new SimplexNoise(seed*23);
        atmos.setFrequency(0.1);
        atmos.setPersistence(0.8);
        atmos.setOctaves(7);
        atmos.setYScale(2);
        
        return createPlanet(simnoise, atmos, planetColors, radius, 0.01, 0.5, 0.3);
    }
    */
    
    /*
    public static Mesh createMarsTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.20f, 0.20f, 0.32f),     // depressions
            new Vec3(0.3f, 0.27f, 0.25f),     // plains middle
            new Vec3(0.36f, 0.28f, 0.25f),    // shore
            new Vec3(0.40f, 0.28f, 0.25f),    // jungle
            new Vec3(0.45f, 0.28f, 0.25f),    // jungle
            new Vec3(0.50f, 0.28f, 0.25f),    // forest
            new Vec3(0.55f, 0.32f, 0.28f),    // forest
            new Vec3(0.6f, 0.45f, 0.3f),    // highlands
            new Vec3(0.6f, 0.5f, 0.4f),    // highlands
            new Vec3(0.95f, 0.96f, 0.97f),    // snow
            new Vec3(0.97f, 0.98f, 0.99f),    // snow
        };
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setFrequency(0.5);
        simnoise.setPersistence(0.85);
        simnoise.setOctaves(6);
        
        return createPlanet(simnoise, null, planetColors, radius, 0.01, 0.95, 0);
    }
    */
    
    /*
    public static Mesh createRockTypePlanet(double radius, long seed)
    {
        Vec3 [] planetColors = 
        {
            new Vec3(0.25f, 0.23f, 0.21f),
            new Vec3(0.30f, 0.27f, 0.25f),
            new Vec3(0.35f, 0.32f, 0.3f),
            new Vec3(0.45f, 0.40f, 0.32f),
            new Vec3(0.52f, 0.50f, 0.46f),
            new Vec3(0.60f, 0.56f, 0.5f),
        };

        Vec3 [] randomPlanetColors = randomizeColors(planetColors, seed);
        
        SimplexNoise simnoise = new SimplexNoise(seed);
        simnoise.setOctaves(8);
        simnoise.setPersistence(0.7);

        simnoise.setFrequency(0.15);
        return createPlanet(simnoise, null, randomPlanetColors, radius, 0.01, 1.0, 0);

        // simnoise.setFrequency(0.0005);
        // return createPlanetField(simnoise, planetColors, radius*10, radius/10, 1.0, 0);
    }
    */
    
    /*
    public static Mesh createCarbonRichPlanet(double radius, long seed)
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
        return createPlanet(simnoise, null, randomPlanetColors, radius, 0.01, 1.0, 0);
    }
    */
    
    /*
    public static Mesh createCloudTypePlanet(double radius, long seed)
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
        
        return createPlanet(simnoise, null, planetColors, radius, 0.0, 1.0, 0);
    }
    */
    
    /*
    public static Mesh createIceTypePlanet(double radius, long seed)
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
        
        return createPlanet(simnoise, null, randomPlanetColors, radius, 0.0, 1.0, 0);
    }
    */
    
    /*
    public static Mesh createBrownGasGiantTypePlanet(double radius, long seed)
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
        
        return createPlanet(simnoise, null, randomPlanetColors, radius, 0.0, 1.0, 0);
    }
    */
    
    /*
    public static Mesh createBlueGasGiantTypePlanet(double radius, long seed)
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
        
        return createPlanet(simnoise, null, randomPlanetColors, radius, 0.0, 1.0, 0);
    }
    */
    
    /*
    public static Mesh createPlanet(SimplexNoise surface,
                                    SimplexNoise atmosphere,
                                    Vec3 [] planetColors, 
                                    double radius,
                                    double roughness,
                                    double polarCapLimit, 
                                    double waterLevel)
    {
        double polarCapHeight = 10 * polarCapLimit;
        
        // a grid in polar coordinates
        
        int vSteps = 80;
        // int tSteps = vSteps * 32;
        int tSteps = vSteps * 8;
        // int tSteps = vSteps * 4;

        FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer nBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer tBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        
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
        BufferedImage tImage = new BufferedImage(tSteps, tSteps, BufferedImage.TYPE_INT_ARGB);
        
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
                        calcPlanetColor(surface, planetColors, v1, polarCapHeight, waterLevel, tmp);
                        
                        c1.scale(alpha);
                        tmp.scale(1.0f - alpha);
                        c1.x += tmp.x;
                        c1.y += tmp.y;
                        c1.z += tmp.z;
                    }
                }
                else
                {
                    calcPlanetColor(surface, planetColors, v1, polarCapHeight, waterLevel, c1);
                }
                
                int argb = 
                        0xFF000000 |
                        (((int)(c1.x * 0xFF)) << 16) |
                        ((((int)(c1.y * 0xFF)) << 8)|
                        (int)(c1.z * 0xFF));
                
                tImage.setRGB(i, j, argb);
            }
        }
        
        
        ByteBuffer tData = TextureCache.convertTextureToRGBA(tImage);
        
        int vertices = 0;
        
        for(int j=1; j<vSteps-2; j++)
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
                
                v1.scale((float)(1 + roughness * calcPlanetHeight(surface, v1, waterLevel)));
                v2.scale((float)(1 + roughness * calcPlanetHeight(surface, v2, waterLevel)));
                v3.scale((float)(1 + roughness * calcPlanetHeight(surface, v3, waterLevel)));
                v4.scale((float)(1 + roughness * calcPlanetHeight(surface, v4, waterLevel)));
                
                
                float tStep = 1.0f / vSteps;
                float ti = (float)i / vSteps;
                float tj = (float)j / vSteps;
                
                
                // assemble first triangle
                
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);

                tBuffer.put(ti).put(tj).put(0f);
                tBuffer.put(ti).put(tj+tStep).put(0f);
                tBuffer.put(ti+tStep).put(tj+tStep).put(0f);
                
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v4.x).put(v4.y).put(v4.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
            
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n4.x).put(n4.y).put(n4.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
            
                // assemble second triangle
                
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);

                
                tBuffer.put(ti).put(tj).put(0f);
                tBuffer.put(ti+tStep).put(tj+tStep).put(0f);
                tBuffer.put(ti+tStep).put(tj).put(0f);
                
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
                vBuffer.put(v2.x).put(v2.y).put(v2.z);
                
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
                nBuffer.put(n2.x).put(n2.y).put(n2.z);
                
                vertices += 6;
            }
        }
        
        vBuffer.flip();
        cBuffer.flip();
        nBuffer.flip();
        tBuffer.flip();
        
        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, tBuffer, 0, vertices);
        result.setTextureData(tData);
        return result;
    }
    */


    /*
    public static Mesh createPlanetField(SimplexNoise simnoise, 
                                    Vec3 [] planetColors, 
                                    double radius,
                                    double roughness,
                                    double polarCapLimit, 
                                    double waterLevel)
    {
        double polarCapHeight = radius * polarCapLimit;
        
        int vSteps = 300;
        int tSteps = vSteps * 6;

        FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer nBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        FloatBuffer tBuffer = BufferUtils.createFloatBuffer(9 * vSteps * vSteps * 2);
        
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3 v3 = new Vec3();
        Vec3 v4 = new Vec3();

        Vec3 c1 = new Vec3();
        
        float meshScale = (float)(radius / vSteps);
        float scale = meshScale * vSteps / tSteps;
        
        double [] heights = new double [vSteps * vSteps]; 
        
        for(int j=0; j<vSteps; j++)
        {
            for(int i=0; i<vSteps; i++)
            {
                v1.set(i*meshScale, j*meshScale, 0);
                double height = calcPlanetHeight(simnoise, v1, waterLevel);
                
                if(height > 0.66) 
                {
                    // height = Math.pow(height, 0.8);
                }
                else if(height > 0.33) 
                {
                    height = Math.pow(height, 2);
                } 
                else
                {
                    height = Math.pow(height, 4);
                }
                
                heights[j*vSteps + i] = roughness * height;
            }
        }
        
        
        
        // Corners
        // 12
        // 43

        // Hajo: create a higher res texture for the planet
        BufferedImage tImage = new BufferedImage(tSteps, tSteps, BufferedImage.TYPE_INT_ARGB);
        
        
        for(int j=0; j<tSteps; j++)
        {
            for(int i=0; i<tSteps; i++)
            {
                v1.set(i*scale, j*scale, 0);
                
                calcPlanetColor(simnoise, planetColors, v1, polarCapHeight, waterLevel, c1);

                int argb = 
                        0xFF000000 |
                        (((int)(c1.x * 0xFF)) << 16) |
                        ((((int)(c1.y * 0xFF)) << 8)|
                        (int)(c1.z * 0xFF));
                
                tImage.setRGB(i, j, argb);
            }
        }
        
        
        ByteBuffer buf = TextureCache.convertTextureToRGBA(tImage);
        int tId = TextureCache.loadTexture(buf, tSteps, tSteps);
        
        int vertices = 0;
        
        for(int j=1; j<vSteps-2; j++)
        {
            for(int i=0; i<vSteps; i++)
            {

                v1.set(i*meshScale, j*meshScale, 0);
                
                v2.set((i+1)*meshScale, j*meshScale, 0);
                
                v3.set((i+1)*meshScale, (j+1)*meshScale, 0);

                v4.set(i*meshScale, (j+1)*meshScale, 0);

                v1.z = -(float)heights[j * vSteps + i];
                v2.z = -(float)heights[j * vSteps + (i+1)];
                v3.z = -(float)heights[(j+1) * vSteps + (i+1)];
                v4.z = -(float)heights[(j+1) * vSteps + i];
                
                Vec3 n1 = new Vec3(0, 0, -1);
                n1.normalise();
                
                Vec3 n2 = new Vec3(0, 0, -1);
                n2.normalise();
                
                Vec3 n3 = new Vec3(0, 0, -1);
                n3.normalise();
                
                Vec3 n4 = new Vec3(0, 0, -1);
                n4.normalise();
                
                // noise -> heise and colors
                
                                
                float tStep = 1.0f / vSteps;
                float ti = (float)i / vSteps;
                float tj = (float)j / vSteps;
                
                
                // assemble first triangle
                
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);

                tBuffer.put(ti).put(tj).put(0f);
                tBuffer.put(ti).put(tj+tStep).put(0f);
                tBuffer.put(ti+tStep).put(tj+tStep).put(0f);
                
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v4.x).put(v4.y).put(v4.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
            
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n4.x).put(n4.y).put(n4.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
            
                // assemble second triangle
                
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);
                cBuffer.put(1).put(1).put(1);

                
                tBuffer.put(ti).put(tj).put(0f);
                tBuffer.put(ti+tStep).put(tj+tStep).put(0f);
                tBuffer.put(ti+tStep).put(tj).put(0f);
                
                
                vBuffer.put(v1.x).put(v1.y).put(v1.z);
                vBuffer.put(v3.x).put(v3.y).put(v3.z);
                vBuffer.put(v2.x).put(v2.y).put(v2.z);
                
                nBuffer.put(n1.x).put(n1.y).put(n1.z);
                nBuffer.put(n3.x).put(n3.y).put(n3.z);
                nBuffer.put(n2.x).put(n2.y).put(n2.z);
                
                vertices += 6;
            }
        }
        
        vBuffer.flip();
        cBuffer.flip();
        nBuffer.flip();
        tBuffer.flip();
        
        Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, tBuffer, tId, vertices);
        // Mesh result = new Mesh(vBuffer, nBuffer, cBuffer, null, 0, vertices);
        return result;
    }
    */


    private static Vec3 calcPlanetColor(SimplexNoise simnoise, 
                                            Vec3[] planetColors, 
                                            Vec3 v,
                                            double polarCapLimit,
                                            double waterLevel,
                                            Vec3 result) 
    {
        double noise = simnoise.noise(v);
        double level;
        
        // Hajo: test for sea areas

        if(noise < waterLevel)
        {
            // Hajo: there are 3 water colors
            level = noise / waterLevel * 3;
        }
        else
        {
            level = 0.5 + (noise * (planetColors.length - 1));
        }
        
        // Hajo: simulate polar caps by raising level into snowy heights
        double polar = Math.abs(v.y) - polarCapLimit;
        
        if(polar > 0)
        {
            level = Math.max(level, planetColors.length - 1);
        }
        
        
        int index = Math.min((int)level, planetColors.length - 1);
        
        
        if(index == 0)
        {
            result.set(planetColors[index]);
        }
        else
        {
            // Hajo: mix two colors by fraction
            float f = (float)level - (float)index;
            
            result.set(planetColors[index-1]);
            Vec3 next = planetColors[index];
            
            result.scale(1.0f - f);
            
            result.x += next.x * f;
            result.y += next.y * f;
            result.z += next.z * f;
            
        }
        
        return result;
    }
    
    private static float calcCloudColor(SimplexNoise simnoise, 
                                        Vec3 v,
                                        Vec3 result) 
            
    {
        double noise = simnoise.noise(v);
        float alpha;
        float threshholdClearSky = 0.25f;
        float threshholdFullCloud = 0.55f;
        
        if(noise < threshholdClearSky)
        {
            result.set(1, 1, 1);
            alpha = 0;
        }
        else if(noise > threshholdFullCloud)
        {
            result.set(1, 1, 1);
            alpha = 1;
        }
        else
        {
            result.set(1, 1, 1);
            alpha = ((float)noise - threshholdClearSky) * 1f / (threshholdFullCloud - threshholdClearSky);
        }
        
        return alpha;
    }

    private static double calcPlanetHeight(SimplexNoise simnoise, Vec3 v, double waterLevel) 
    {
        double level = simnoise.noise(v);
        
        level = Math.max(level, waterLevel);
            
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
}
