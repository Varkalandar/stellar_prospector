package flyspace;

import org.lwjgl.util.vector.Vector3f;

/**
 * Technology independent mesh data.
 * 
 * @author Hj. Malthaner
 */
public abstract class AbstractMesh
{
    private final Vector3f pos;
    private float angleX, angleY;
    
    public AbstractMesh()
    {
        pos = new Vector3f();
    }
    
    protected Vector3f getPos()
    {
        return pos;
    }
    
    public void setPos(Vector3f pos)
    {
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        this.pos.z = pos.z;
    } 

    public void setPos(float x, float y, float z)
    {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    } 
    
    public void demoRot()
    {
        angleX += 0.1;
        if(angleX > 360) angleX -= 360;
        
        angleY += 1.0;
        if(angleY > 360) angleY -= 360;
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
    
    public abstract void display();
    
    public abstract void bind();
    
    public abstract void unbind();
    
}
