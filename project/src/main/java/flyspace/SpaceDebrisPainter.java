package flyspace;

import flyspace.ogl.Mesh;
import flyspace.ogl32.GL32MeshFactory;
import flyspace.ogl32.ShaderBank;
import flyspace.particles.ParticlePainter;

import static flyspace.particles.ParticleDriver.LIFE;
import static flyspace.particles.ParticleDriver.MAXLIFE;
import static flyspace.particles.ParticleDriver.XPOS;
import static flyspace.particles.ParticleDriver.XSPEED;
import static flyspace.particles.ParticleDriver.YPOS;
import static flyspace.particles.ParticleDriver.YSPEED;
import static flyspace.particles.ParticleDriver.ZPOS;
import static flyspace.particles.ParticleDriver.ZSPEED;
import static flyspace.particles.ParticleDriver.COLOR;
import static flyspace.particles.ParticleDriver.ID;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import solarex.system.Matrix4;
import solarex.system.Vec3;

/**
 *
 * @author Hj. Malthaner
 */
public class SpaceDebrisPainter implements ParticlePainter
{
    private final AbstractMesh [] tetras;
    
    public SpaceDebrisPainter() 
    {
        tetras = new AbstractMesh[7];
        
        tetras[0] = GL32MeshFactory.createTetra(7, 1, 1, 1);
        tetras[1] = GL32MeshFactory.createTetra(7, 1, 1, 0.7f);
        tetras[2] = GL32MeshFactory.createTetra(7, 1, 0.7f, 0.2f);
        tetras[3] = GL32MeshFactory.createTetra(7, 1, 1, 0);
        tetras[4] = GL32MeshFactory.createTetra(7, 0.6f, 0.8f, 1.0f);
        tetras[5] = GL32MeshFactory.createTetra(7, 0.4f, 0.7f, 1.0f);
        tetras[6] = GL32MeshFactory.createTetra(7, 0.5f, 0.5f, 0.5f);
    
        for(AbstractMesh mesh : tetras)
        {
            mesh.bind();
        }
    }
    
    @Override
    public void paint(int[] particles, int base) 
    {
        int x = particles[base + XPOS];
        int y = particles[base + YPOS];
        int z = particles[base + ZPOS];
        int id = particles[base + ID];
        
        if(z < 0)
        {
            Matrix4 viewMatrix = new Matrix4();
            Matrix4 modelMatrix = new Matrix4();
         
            Matrix4.translate(new Vec3(x, y, z), modelMatrix, modelMatrix);
            ShaderBank.updateViewMatrix(viewMatrix);
            ShaderBank.updateModelMatrix(modelMatrix);
            ShaderBank.uploadBrightMatrices();

            tetras[id].display();
        }
        else
        {
            particles[base + LIFE] = 0;
        }
    }
}
