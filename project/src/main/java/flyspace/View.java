package flyspace;

import solarex.system.Matrix4;
import solarex.system.Vec3;

/**
 * 
 * @author Hj. Malthaner
 */
public class View 
{
    private final Matrix4 transform;
    private final Matrix4 inverse;
    
    private Vec3 front;
    private Vec3 right;
    private Vec3 up;

    public View() 
    {
        this.front = new Vec3(0, 0, 1);
        this.right = new Vec3(1, 0, 0);
        this.up = new Vec3(0, 1, 0);
        
        this.transform = new Matrix4();
        this.inverse = new Matrix4();
    }
    
    public void orient(Vec3 front3, Vec3 right3)
    {
        front.set(front3);
        front.normalise();
        right.set(right3);
        right.normalise();
        
        // Vector3f up3 = Vector3f.cross(right3, front3, null);
        Vec3 up3 = Vec3.cross(front, right, null);
        up.set(up3);
        up.normalise();
        
        calculateTransform();
        calculateInverse();
    }


    public Matrix4 getTransform()
    {
        return transform;
    }
    
    public Matrix4 getInverse()
    {
        return inverse;
    }
    
    public void rotateAxis(float angleRad, Vec3 axis)
    {
        Matrix4 rot = new Matrix4();
        
        Matrix4.rotate(angleRad, axis, rot, rot);

        Matrix4.transform(rot, front, front);
        
        Matrix4.transform(rot, right, right);
        
        Matrix4.transform(rot, up, up);
        
        front.normalise();
        right.normalise();
        up.normalise();
        
        calculateTransform();
        calculateInverse();
    }
    
    private void fillTransform(Matrix4 result)
    {
        result.m00 = right.x;
        result.m01 = right.y;
        result.m02 = right.z; 
        result.m03 = 0;
        
        result.m10 = up.x;
        result.m11 = up.y;
        result.m12 = up.z;
        result.m13 = 0;
        
        result.m20 = front.x;
        result.m21 = front.y; 
        result.m22 = front.z;
        result.m23 = 0;
        
        result.m30 = 0.0f; 
        result.m31 = 0.0f;
        result.m32 = 0.0f;
        result.m33 = 1.0f;        
    }
    
    private void calculateTransform()
    {
        fillTransform(transform);
    }
    
    private void calculateInverse()
    {
        fillTransform(inverse);
        inverse.invert();
    }
}