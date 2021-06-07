package flyspace.ogl;

import flyspace.AbstractMesh;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Hj. Malthaner
 */
public class Mesh extends AbstractMesh
{
    private final FloatBuffer cBuffer, vBuffer, nBuffer, tBuffer;
    private int textureId;
    private final int vertices;

    /** Texture data for late binding */
    private ByteBuffer tData;
    
    public Mesh(FloatBuffer vBuffer, FloatBuffer cBuffer, int vertices)
    {
        this.vBuffer = vBuffer;
        this.nBuffer = null;
        this.cBuffer = cBuffer;
        this.tBuffer = null;
        this.vertices = vertices;
    }

    public Mesh(FloatBuffer vBuffer, FloatBuffer nBuffer, 
                FloatBuffer cBuffer, FloatBuffer tBuffer, 
                int tId, int vertices)
    {
        this.vBuffer = vBuffer;
        this.nBuffer = nBuffer;
        this.cBuffer = cBuffer;
        this.tBuffer = tBuffer;
        this.textureId = tId;
        this.vertices = vertices;
    }

    public void setTextureData(ByteBuffer tData)
    {
        this.tData = tData;
    }
    
    @Override
    public void bind()
    {
        if(tData != null)
        {
            int remaining = tData.remaining();
            int tSteps = (int)Math.sqrt(remaining / 4);  // 4 bytes per pixel
            textureId = TextureCache.loadTexture(tData, tSteps, tSteps);
            tData = null;
        }
    }
    
    @Override
    public void unbind() 
    {
        if(tData == null)
        {
            if(textureId > 0)
            {
                glDeleteTextures(textureId);
                textureId = 0;
            }
        }
    }
    
    @Override
    public void display()
    {
        Vector3f pos = getPos();
        
        glBindTexture(GL_TEXTURE_2D, textureId);
        
        glTranslatef(pos.x, pos.y, pos.z);
        
        glRotatef(getAngleX(), 1, 0, 0);
        glRotatef(getAngleY(), 0, 1, 0);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        if(nBuffer != null) glEnableClientState(GL_NORMAL_ARRAY);
        if(tBuffer != null) glEnableClientState(GL_TEXTURE_COORD_ARRAY);
       
        glColorPointer(3, 0, cBuffer);
        glVertexPointer(3, 0, vBuffer);
        if(nBuffer != null) glNormalPointer(0, nBuffer);
        if(tBuffer != null) glTexCoordPointer(3, 0, tBuffer);
        
        glDrawArrays(GL_TRIANGLES, 0, vertices);

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);        
        glDisableClientState(GL_VERTEX_ARRAY);        
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
      
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
