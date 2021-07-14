package flyspace;

import solarex.system.Vec3;

/**
 * Technology independent mesh data.
 * 
 * @author Hj. Malthaner
 */
public abstract class AbstractMesh
{
    private final Vec3 pos;
    private float angleX, angleY;
    
    public AbstractMesh()
    {
        pos = new Vec3();
    }
    
    protected Vec3 getPos()
    {
        return pos;
    }
    
    public void setPos(Vec3 pos)
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
