package flyspace.ogl32;

import flyspace.AbstractMesh;
import flyspace.ogl.TextureCache;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Mesh data container. Uses VBO/VAO for display.
 * 
 * @author Hj. Malthaner
 */
public class GL32Mesh extends AbstractMesh
{
    private static int WHITE_TEX;
    
    private final VertexData [] vertexData;
    private final int [] indexData;
    private ByteBuffer textureData;
    private int textureId;

    private int vaoId = 0;
    private int vboId = 0;
    private int vboiId = 0;
    private int indicesCount = 0;

    public GL32Mesh(VertexData [] vertexData, int [] indexData, ByteBuffer textureData)
    {
        this.textureId = 0;
        this.textureData = textureData;
        this.vertexData = vertexData;
        this.indexData = indexData;
    }

    @Override
    public void bind()
    {
        
        if(textureData != null)
        {
            int remaining = textureData.remaining();
            int tSteps = (int)Math.sqrt(remaining / 4);  // 4 bytes per pixel

            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            textureId = TextureCache.loadTexture(textureData, tSteps, tSteps);
            textureData = null;
        }
        else
        {
            if(WHITE_TEX == 0) makeWhiteTex();
            textureId = WHITE_TEX;
        }

        // Put each 'Vertex' in one FloatBuffer
        ByteBuffer verticesByteBuffer = 
                BufferUtils.createByteBuffer(vertexData.length * VertexData.stride);             
        
        FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
        for (int i = 0; i < vertexData.length; i++) 
        {
            // Add position, color and texture floats to the buffer
            verticesFloatBuffer.put(vertexData[i].getElements());
        }
        
        verticesFloatBuffer.flip();
         
         
        indicesCount = indexData.length;
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indicesCount);
        indicesBuffer.put(indexData);
        indicesBuffer.flip();
         
        // Create a new Vertex Array Object in memory and select it (bind)
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
         
        // Create a new Vertex Buffer Object in memory and select it (bind)
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        //GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STATIC_DRAW);
         
        // Put the position coordinates in attribute list 0
        GL20.glVertexAttribPointer(0, VertexData.positionElementCount, GL11.GL_FLOAT, 
                false, VertexData.stride, VertexData.positionByteOffset);
        // Put the color components in attribute list 1
        GL20.glVertexAttribPointer(1, VertexData.colorElementCount, GL11.GL_FLOAT, 
                false, VertexData.stride, VertexData.colorByteOffset);
        // Put the texture coordinates in attribute list 2
        GL20.glVertexAttribPointer(2, VertexData.textureElementCount, GL11.GL_FLOAT, 
                false, VertexData.stride, VertexData.textureByteOffset);
        // Normals
        GL20.glVertexAttribPointer(3, VertexData.normalElementCount, GL11.GL_FLOAT, 
                false, VertexData.stride, VertexData.normalByteOffset);
         
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
         
        // Create a new VBO for the indices and select it (bind) - INDICES
        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, 
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
    }
    
    @Override
    public void unbind() 
    {
        // Select the VAO
        GL30.glBindVertexArray(vaoId);
         
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
         
        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboId);
         
        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboiId);
         
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
    }
    
    @Override
    public void display()
    {
        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
         
        // Bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
         
        // Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
         
        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
         
        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    private static void makeWhiteTex()
    {
        int pixels = 64*64;
        ByteBuffer tData = ByteBuffer.allocateDirect(pixels*4);
        for(int i=0; i<pixels; i++)
        {
            tData.putInt(0xFFFFFFFF);
        }
        tData.flip();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        WHITE_TEX = TextureCache.loadTexture(tData, 64, 64);
    }
}
