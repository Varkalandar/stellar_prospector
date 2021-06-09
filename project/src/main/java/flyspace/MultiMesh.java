package flyspace;

import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import solarex.system.Solar;
import solarex.system.Vec3;

/**
 *
 * @author Hj. Malthaner
 */
public class MultiMesh
{
    public final ArrayList<AbstractMesh> meshes;
    private Solar peer;
    private Vec3 pos;
    private float angleX, angleY, angleZ;
    public int lastScreenX;
    public int lastScreenY;
    
    public MultiMesh()
    {
        meshes = new ArrayList<>(4);
        pos = new Vec3();
    }
    
    public MultiMesh(AbstractMesh mesh)
    {
        this();
        addMesh(mesh);
    }

    public final void addMesh(AbstractMesh mesh)
    {
        meshes.add(mesh);
    }
    
    public float getAngleX()
    {
        return angleX;
    }
    
    public void setAngleX(float angle)
    {
        angleX = angle;
        if(angleX > 360) angleX -= 360;
        if(angleX < -360) angleX += 360;
    }
    
    public float getAngleY()
    {
        return angleY;
    }
    
    public void setAngleY(float angle)
    {
        angleY = angle;
        if(angleY > 360) angleY -= 360;
        if(angleY < -360) angleY += 360;
    }
    
    public float getAngleZ()
    {
        return angleZ;
    }
    
    public void display()
    {
        for(AbstractMesh mesh : meshes)
        {
            mesh.display();
        }
    }    

    public Vec3 getPos() 
    {
        return pos;
    }

    public void setPos(Vec3 pos) 
    {
        this.pos.set(pos);
    }

    public void setPos(double x, double y, double z) 
    {
        pos.x = x;
        pos.y = y;
        pos.z = z;
    }
    
    public Solar getPeer() 
    {
        return peer;
    }

    public void setPeer(Solar system) 
    {
        this.peer = system;
    }



}
