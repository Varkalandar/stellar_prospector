package flyspace.ogl32;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
 
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
 
/**
 * Test how to display planets with OGL 32 programmable pipeline.
 * 
 * Based on LWJGL example from 
 * http://wiki.lwjgl.org/wiki/The_Quad_with_Projection,_View_and_Model_matrices
 */
public class PlanetOrthoDisplayTest 
{
    // Entry point for the application
    public static void main(String[] args) 
    {
        new PlanetOrthoDisplayTest();
    }
     
    // Setup variables
    private final String WINDOW_TITLE = "OpenGL32Test";
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;
    private final double PI = 3.14159265358979323846;
    
    
    // Shader variables
    private int pId = 0;

    // Moving variables
    private int projectionMatrixLocation = 0;
    private int viewMatrixLocation = 0;
    private int modelMatrixLocation = 0;
    private Matrix4f projectionMatrix = null;
    private Matrix4f viewMatrix = null;
    private Matrix4f modelMatrix = null;
    private Vector3f modelPos = null;
    private Vector3f modelAngle = null;
    private Vector3f modelScale = null;
    private Vector3f cameraPos = null;
    private FloatBuffer matrix44Buffer = null;
     
    private GL32Mesh mesh;
    
    public PlanetOrthoDisplayTest() 
    {
        // Initialize OpenGL (Display)
        this.setupOpenGL();
         
        this.setupMesh();
        this.setupShaders();
        this.setupMatrices();
         
        while (!Display.isCloseRequested()) 
        {
            // Do a single loop (logic/render)
            this.loopCycle();
             
            // Force a maximum FPS of about 60
            Display.sync(60);
            // Let the CPU synchronize with the GPU if GPU is tagging behind
            Display.update();
        }
         
        // Destroy OpenGL (Display)
        this.destroyOpenGL();
    }
 
    private void setupMatrices() {
        // Setup projection matrix
        projectionMatrix = new Matrix4f();
        
        /*
        projectionMatrix.m00 = 1;
        projectionMatrix.m11 = 1;
        projectionMatrix.m22 = 1;
        projectionMatrix.m33 = 1;
        */ 
        
        float far = 10000;
        float near = 1;
        
        projectionMatrix.m00 = 1.0f / WIDTH;;
        projectionMatrix.m11 = 1.0f / HEIGHT;
        projectionMatrix.m22 = -2 / (far - near);
        //projectionMatrix.m23 = - (far + near) / (far - near);
        projectionMatrix.m33 = 1;
        
        // Setup view matrix
        viewMatrix = new Matrix4f();
         
        // Setup model matrix
        modelMatrix = new Matrix4f();
         
        // Create a FloatBuffer with the proper size to store our matrices later
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
    }
  
    private void setupOpenGL() 
    {
        // Setup an OpenGL context with API version 3.2
        try 
        {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
             
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create(pixelFormat, contextAtrributes);
             
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
            
        } catch (LWJGLException e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
         
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0f);
         
        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
         
        this.exitOnGLError("setupOpenGL");
    }
     
    private void setupMesh() 
    {
        float radius = 1000;
    
        mesh = GL32MeshFactory.createEarthTypePlanet(radius, 123456);
        // mesh = GL32MeshFactory.createMarsTypePlanet(1.0, 123456);
        // mesh = MeshFactory.createRockTypePlanet(1.0, 1234567891);
        mesh.bind();
        
        // Set the default quad rotation, scale and position values
        modelPos = new Vector3f(0, 0, 0); // -radius);
        modelAngle = new Vector3f(0, 0, 0);
        modelScale = new Vector3f(1, 1, 1);
        cameraPos = new Vector3f(0, 0, 0);
         
        this.exitOnGLError("setupMesh");
    }
     
    private void setupShaders() {       
        // Load the vertex shader
        int vsId = this.loadShader("/flyspace/ogl32/vertex.glsl", GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        int fsId = this.loadShader("/flyspace/ogl32/fragment_bright.glsl", GL20.GL_FRAGMENT_SHADER);
         
        // Create a new shader program that links both shaders
        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);
 
        // Position information will be attribute 0
        GL20.glBindAttribLocation(pId, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(pId, 1, "in_Color");
        // Texture information will be attribute 2
        GL20.glBindAttribLocation(pId, 2, "in_TexCoord");
        // Normal information will be attribute 3
        GL20.glBindAttribLocation(pId, 3, "in_Normal");
 
        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);
 
        // Get matrices uniform locations
        projectionMatrixLocation = GL20.glGetUniformLocation(pId,"projectionMatrix");
        viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
        modelMatrixLocation = GL20.glGetUniformLocation(pId, "modelMatrix");
 
        this.exitOnGLError("setupShaders");
    }
     
    private void logicCycle() 
    {
        //-- Input processing
        float rotationDelta = 15f;
        float scaleDelta = 0.1f;
        float posDelta = 0.1f;
        Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta, scaleDelta);
        Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta, -scaleDelta);
         
        while(Keyboard.next()) {            
            // Only listen to events where the key was pressed (down event)
            if (!Keyboard.getEventKeyState()) continue;
             
             
            // Change model scale, rotation and translation values
            switch (Keyboard.getEventKey()) {
            // Move
            case Keyboard.KEY_UP:
                modelPos.y += posDelta;
                break;
            case Keyboard.KEY_DOWN:
                modelPos.y -= posDelta;
                break;
            case Keyboard.KEY_RBRACKET:
                modelPos.z += posDelta;
                break;
            case Keyboard.KEY_MINUS:
                modelPos.z -= posDelta;
                break;
            // Scale
            case Keyboard.KEY_P:
                Vector3f.add(modelScale, scaleAddResolution, modelScale);
                break;
            case Keyboard.KEY_M:
                Vector3f.add(modelScale, scaleMinusResolution, modelScale);
                break;
            // Rotation
            case Keyboard.KEY_LEFT:
                modelAngle.y += rotationDelta;
                break;
            case Keyboard.KEY_RIGHT:
                modelAngle.y -= rotationDelta;
                break;

            case Keyboard.KEY_PRIOR:
                modelAngle.x += rotationDelta;
                break;
            case Keyboard.KEY_NEXT:
                modelAngle.x -= rotationDelta;
                break;
            }
        }
         
        //-- Update matrices
        // Reset view and model matrices
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
         
        // Translate camera
        // Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);
         
        // Scale, translate and rotate model

        Vector3f planetScale = new Vector3f(modelScale);
        // planetScale.scale(1/radius);
        
        Matrix4f.scale(planetScale, modelMatrix, modelMatrix);
        
        Matrix4f.translate(modelPos, modelMatrix, modelMatrix);        
        Matrix4f.rotate(this.degreesToRadians(modelAngle.z), new Vector3f(0, 0, 1), 
                modelMatrix, modelMatrix);
        Matrix4f.rotate(this.degreesToRadians(modelAngle.y), new Vector3f(0, 1, 0), 
                modelMatrix, modelMatrix);
        Matrix4f.rotate(this.degreesToRadians(modelAngle.x), new Vector3f(1, 0, 0), 
                modelMatrix, modelMatrix);
         
        // Upload matrices to the uniform variables
        GL20.glUseProgram(pId);
         
        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
         
        GL20.glUseProgram(0);
         
        this.exitOnGLError("logicCycle");
    }
     
    private void renderCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        
        GL20.glUseProgram(pId);
         
        mesh.display();
        GL20.glUseProgram(0);
         
        this.exitOnGLError("renderCycle");
    }
     
    private void loopCycle() {
        // Update logic
        this.logicCycle();
        // Update rendered frame
        this.renderCycle();
         
        this.exitOnGLError("loopCycle");
    }
     
    private void destroyOpenGL() 
    {  
         
        // Delete the shaders
        GL20.glUseProgram(0);
        GL20.glDeleteProgram(pId);
         
        mesh.unbind();
        this.exitOnGLError("destroyOpenGL");
         
        Display.destroy();
    }
     
    private int loadShader(String filename, int type) 
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
        
        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) 
        {
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 65536));
            
            System.err.println("Could not compile shader: " + filename);
            System.exit(-1);
        }
         
        this.exitOnGLError("loadShader");
         
        return shaderID;
    }
     
    private float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }
     
    private float degreesToRadians(float degrees) {
        return degrees * (float)(PI / 180d);
    }
     
    private void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
         
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
             
            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }
}