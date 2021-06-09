package flyspace.io;

import flyspace.ogl.Mesh;
import flyspace.ogl32.VertexData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

/**
 * Wavefront *.obj file reader.
 * 
 * @author hjm
 */
public class ObjReader 
{

    public Object read(InputStream is) throws IOException 
    {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        
        ArrayList <Vector3f> inVertices = new ArrayList<Vector3f>(1024);
        ArrayList <Vector3f> inNormals = new ArrayList<Vector3f>(1024);
        ArrayList <int []> inFaces = new ArrayList<int []>(512);
        
        String line;
        
        while((line = reader.readLine()) != null)
        {
            String [] parts = line.split(" ");
            Vector3f vec;
            
            if(parts[0].startsWith("vt"))
            {
                // Texture UV?
            }
            else if(parts[0].startsWith("vn"))
            {
                // a normal line
                vec = new Vector3f(Float.parseFloat(parts[1]),
                                   Float.parseFloat(parts[2]),
                                   Float.parseFloat(parts[3]));
                inNormals.add(vec);
            }
            else if(parts[0].startsWith("v"))
            {
                // a vertex line
                vec = new Vector3f(Float.parseFloat(parts[1]),
                                   Float.parseFloat(parts[2]),
                                   Float.parseFloat(parts[3]));
                inVertices.add(vec);
            }
            else if(parts[0].startsWith("f"))
            {
                // a face definition
                
                // each part is a triplet of indices
                // vertex/texture/normal
                
                int [] vtn = new int [9];
                readVtn(vtn, 0, parts[1]);
                readVtn(vtn, 3, parts[2]);
                readVtn(vtn, 6, parts[3]);
                
                inFaces.add(vtn);
            }            
        }
        
        reader.close();
        
        
        // create geometry buffers
        FloatBuffer cBuffer, vBuffer, nBuffer;    

        int faces = inFaces.size();
        vBuffer = BufferUtils.createFloatBuffer(faces * 9);
        nBuffer = BufferUtils.createFloatBuffer(faces * 9);
        cBuffer = BufferUtils.createFloatBuffer(faces * 9);
      
        for(int [] vtn : inFaces)
        {
            // triangles only
            for(int v = 0; v<3; v++)
            {
                Vector3f vec;
                
                vec = inVertices.get(vtn[v*3 + 0] - 1);
                vBuffer.put(vec.x).put(vec.y).put(vec.z);
                
                vec = inNormals.get(vtn[v*3 + 2] - 1);
                nBuffer.put(vec.x).put(vec.y).put(vec.z);
            }
        }

        for(int i=0; i<faces*3; i++)
        {
            cBuffer.put(0.4f).put(0.5f).put(0.6f);
        }

        vBuffer.flip();
        nBuffer.flip();
        cBuffer.flip();

        return new Object(cBuffer, vBuffer, nBuffer, faces);
    }

    
    public Faces readFaces(InputStream is) throws IOException 
    {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        
        ArrayList <Vector3f> inVertices = new ArrayList<Vector3f>(1024);
        ArrayList <Vector3f> inNormals = new ArrayList<Vector3f>(1024);
        ArrayList <int []> inFaces = new ArrayList<int []>(512);
        
        String line;
        
        while((line = reader.readLine()) != null)
        {
            String [] parts = line.split(" ");
            Vector3f vec;
            
            if(parts[0].startsWith("vt"))
            {
                // Texture UV?
            }
            else if(parts[0].startsWith("vn"))
            {
                // a normal line
                vec = new Vector3f(Float.parseFloat(parts[1]),
                                   Float.parseFloat(parts[2]),
                                   Float.parseFloat(parts[3]));
                inNormals.add(vec);
            }
            else if(parts[0].startsWith("v"))
            {
                // a vertex line
                vec = new Vector3f(Float.parseFloat(parts[1]),
                                   Float.parseFloat(parts[2]),
                                   Float.parseFloat(parts[3]));
                inVertices.add(vec);
            }
            else if(parts[0].startsWith("f"))
            {
                // a face definition
                
                // each part is a triplet of indices
                // vertex/texture/normal
                
                int [] vtn = new int [9];
                readVtn(vtn, 0, parts[1]);
                readVtn(vtn, 3, parts[2]);
                readVtn(vtn, 6, parts[3]);
                
                inFaces.add(vtn);
            }            
        }
        
        reader.close();
        
        
        // create geometry buffers
        FloatBuffer cBuffer, vBuffer, nBuffer;    

        int faces = inFaces.size();
        vBuffer = BufferUtils.createFloatBuffer(faces * 9);
        nBuffer = BufferUtils.createFloatBuffer(faces * 9);
        cBuffer = BufferUtils.createFloatBuffer(faces * 9);
      
        
        VertexData [] vertices = new VertexData[faces * 3];
        int [] indices = new int [faces * 3];
        int index = 0;
        
        for(int [] vtn : inFaces)
        {
            // triangles only
            for(int v = 0; v<3; v++)
            {
                Vector3f pos = inVertices.get(vtn[v*3 + 0] - 1);
                Vector3f normal = inNormals.get(vtn[v*3 + 2] - 1);
                
                VertexData vertex = new VertexData();
                
                vertex.setRGB(1, 1, 1);
                vertex.setXYZ(pos.x, pos.y, pos.z);
                vertex.setNormal(normal.x, normal.y, normal.z);
                
                vertices[index] = vertex;
                indices[index] = index;
                index++;
            }
        }

        return new Faces(vertices, indices);
    }

    
    private void readVtn(int [] vtn, int start, String triplet)
    {
        String [] indices;
               
        indices = triplet.split("/");
        
        vtn[start+0] = Integer.parseInt(indices[0]);
        vtn[start+1] = Integer.parseInt(indices[1]);
        vtn[start+2] = Integer.parseInt(indices[2]);        
    }

    public static class Object 
    {
        public final FloatBuffer cBuffer, vBuffer, nBuffer;    
        public final int faces;
        
        public Object(FloatBuffer cBuffer, FloatBuffer vBuffer, FloatBuffer nBuffer,
                      int faces) 
        {
            this.cBuffer = cBuffer;
            this.vBuffer = vBuffer;
            this.nBuffer = nBuffer;
            this.faces = faces;
        }
    }
    
    public static class Faces
    {
        public final VertexData [] vertexData;
        public final int [] indexData;
        
        public Faces(VertexData [] vertexData, int [] indexData) 
        {
            this.vertexData = vertexData;
            this.indexData = indexData;
        }
    }
}
