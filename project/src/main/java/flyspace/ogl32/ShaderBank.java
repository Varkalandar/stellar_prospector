package flyspace.ogl32;

import flyspace.ogl.GlLifecycle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Hj. Malthaner
 */
public class ShaderBank 
{
    // Shader program IDs
    public static int shadedProgId = 0;
    public static int brightProgId = 0;

    // Matrix locations
    private static int projectionMatrixLocation = 0;
    private static int viewMatrixLocation = 0;
    private static int modelMatrixLocation = 0;
    private static int lightPosLocation = 0;
    
    private static int brightProjectionMatrixLocation = 0;
    private static int brightViewMatrixLocation = 0;
    private static int brightModelMatrixLocation = 0;
    private static int brightLightPosLocation = 0;
    
    /** Read only! */
    public static Matrix4f projectionMatrix = null;

    private static Matrix4f viewMatrix = null;
    private static Matrix4f modelMatrix = null;
    private static Vector4f lightPos = null;

    private static final FloatBuffer matrix44Buffer;
    
    static
    {
        // Create a FloatBuffer with the proper size to store our matrices later
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
        
        lightPos = new Vector4f();
    }
    
    public static void updateProjectionMatrix(Matrix4f projection)
    {
        projectionMatrix = projection;
    }
    
    public static void updateModelMatrix(Matrix4f model)
    {
        modelMatrix = model;
    }
    
    public static void updateViewMatrix(Matrix4f view)
    {
        viewMatrix = view;
    }

    public static void updateLightPos(float x, float y, float z)
    {
        lightPos.set(x, y, z);
    }
    
    public static void uploadBrightMatrices()
    {
        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(brightProjectionMatrixLocation, false, matrix44Buffer);
        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(brightViewMatrixLocation, false, matrix44Buffer);
        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(brightModelMatrixLocation, false, matrix44Buffer);
        
        GL20.glUniform4f(brightLightPosLocation, lightPos.x, lightPos.y, lightPos.z, 0f);
        
        GlLifecycle.exitOnGLError("uploadBrightMatrices");
    }
    
        
    public static void uploadShadedMatrices()
    {
        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
        
        GL20.glUniform4f(lightPosLocation, lightPos.x, lightPos.y, lightPos.z, 0f);

        GlLifecycle.exitOnGLError("uploadShadedMatrices");
    }
    
    public static void setupMatrices(int width, int height) 
    {
        // Setup projection matrix
        projectionMatrix = new Matrix4f();
        float fieldOfView = 45f;
        float aspectRatio = (float)width / (float)(height);
        float nearPlane = 1f;
        float farPlane = 100000f;
         
        float yScale = coTangent(degreesToRadians(fieldOfView / 2f));
        float xScale = yScale / aspectRatio;
        float frustum_length = farPlane - nearPlane;
         
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
        projectionMatrix.m33 = 0;
        
        // projectionMatrix.m30 = -CockpitPanel.HEIGHT;
        // projectionMatrix.m31 = CockpitPanel.HEIGHT/4;
         
        // Setup view matrix
        viewMatrix = new Matrix4f();
         
        // Setup model matrix
        modelMatrix = new Matrix4f();         
    }

    public static void setupShaders()
    {
        // Load the vertex shader
        int vsId = loadShader("/flyspace/ogl32/vertex.glsl", GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        int fsId = loadShader("/flyspace/ogl32/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
         
        int brightFsId = loadShader("/flyspace/ogl32/fragment_bright.glsl", GL20.GL_FRAGMENT_SHADER);
        
        // Create a new shader program that links both shaders
        shadedProgId = GL20.glCreateProgram();
        GL20.glAttachShader(shadedProgId, vsId);
        GL20.glAttachShader(shadedProgId, fsId);
 
        // Position information will be attribute 0
        GL20.glBindAttribLocation(shadedProgId, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(shadedProgId, 1, "in_Color");
        // Texture information will be attribute 2
        GL20.glBindAttribLocation(shadedProgId, 2, "in_TexCoord");
        // Normal information will be attribute 3
        GL20.glBindAttribLocation(shadedProgId, 3, "in_Normal");
 
        GL20.glLinkProgram(shadedProgId);
        GL20.glValidateProgram(shadedProgId);

        // Get matrices uniform locations
        projectionMatrixLocation = GL20.glGetUniformLocation(shadedProgId, "projectionMatrix");
        viewMatrixLocation = GL20.glGetUniformLocation(shadedProgId, "viewMatrix");
        modelMatrixLocation = GL20.glGetUniformLocation(shadedProgId, "modelMatrix");
        lightPosLocation = GL20.glGetUniformLocation(shadedProgId, "light_pos");
        
        GlLifecycle.exitOnGLError("setupShaders1");
 
        // Create a new shader program that links both shaders
        brightProgId = GL20.glCreateProgram();
        GL20.glAttachShader(brightProgId, vsId);
        GL20.glAttachShader(brightProgId, brightFsId);
 
        // Position information will be attribute 0
        GL20.glBindAttribLocation(brightProgId, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(brightProgId, 1, "in_Color");
        // Texture information will be attribute 2
        GL20.glBindAttribLocation(brightProgId, 2, "in_TexCoord");
        // Normal information will be attribute 3
        GL20.glBindAttribLocation(brightProgId, 3, "in_Normal");
 
        GL20.glLinkProgram(brightProgId);
        GL20.glValidateProgram(brightProgId);
        
 
        brightProjectionMatrixLocation = GL20.glGetUniformLocation(brightProgId, "projectionMatrix");
        brightViewMatrixLocation = GL20.glGetUniformLocation(brightProgId, "viewMatrix");
        brightModelMatrixLocation = GL20.glGetUniformLocation(brightProgId, "modelMatrix");
        brightLightPosLocation = GL20.glGetUniformLocation(brightProgId, "light_pos");
 
        GlLifecycle.exitOnGLError("setupShaders2");
    }
    
    private static int loadShader(String filename, int type) 
    {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID;
         
        try 
        {
            InputStream in = Class.class.getResourceAsStream(filename);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
         
        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
         
        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL_FALSE) 
        {
            System.err.println("Could not compile shader: " + filename + ":\n");
            
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 65536));
            
            System.exit(-1);
        }
         
        GlLifecycle.exitOnGLError("loadShader");
         
        return shaderID;
    }
    
    private static float coTangent(float angle) 
    {
        return (float)(1f / Math.tan(angle));
    }
     
    private static float degreesToRadians(float degrees) 
    {
        return degrees * (float)(Math.PI / 180);
    }
}
