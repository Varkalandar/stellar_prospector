package flyspace;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * 
 * @author Hj. Malthaner
 */
public class View 
{
    private final Matrix4f transform;
    private final Matrix4f inverse;
    
    private Vector3f front;
    private Vector3f right;
    private Vector3f up;

    public View() 
    {
        this.front = new Vector3f(0, 0, 1);
        this.right = new Vector3f(1, 0, 0);
        this.up = new Vector3f(0, 1, 0);
        
        this.transform = new Matrix4f();
        this.inverse = new Matrix4f();
    }
    
    public void orient(Vector3f front3, Vector3f right3)
    {
        front.set(front3.x, front3.y, front3.z);
        front.normalise();
        right.set(right3.x, right3.y, right3.z);
        right.normalise();
        
        // Vector3f up3 = Vector3f.cross(right3, front3, null);
        Vector3f up3 = Vector3f.cross(front, right, null);
        up.set(up3.x, up3.y, up3.z);
        up.normalise();
        
        calculateTransform();
        calculateInverse();
    }


    public Matrix4f getTransform()
    {
        return transform;
        
        // return Matrix4f.load(transform, new Matrix4f());
        
        /*
        Matrix4f result = new Matrix4f();
        fillTransform(result);
        return result;
        */
    }
    
    public Matrix4f getInverse()
    {
        return inverse;
        
        // return Matrix4f.load(inverse, new Matrix4f());
        
        /*
        Matrix4f result = new Matrix4f();
        fillTransform(result);
        result.invert();
        return result;
        */
    }
    
    public void rotateAxis(float angleRad, Vector3f axis)
    {
        Matrix4f rot = new Matrix4f();
        
        Matrix4f.rotate(angleRad, axis, rot, rot);

        Vector4f front4 = new Vector4f(front.x, front.y, front.z, 0);
        Matrix4f.transform(rot, front4, front4);
        front.set(front4.x, front4.y, front4.z);
        
        Vector4f right4 = new Vector4f(right.x, right.y, right.z, 0);
        Matrix4f.transform(rot, right4, right4);
        right.set(right4.x, right4.y, right4.z);
        
        Vector4f up4 = new Vector4f(up.x, up.y, up.z, 0);
        Matrix4f.transform(rot, up4, up4);
        up.set(up4.x, up4.y, up4.z);
        
        front.normalise();
        right.normalise();
        up.normalise();
        
        calculateTransform();
        calculateInverse();
    }
    
    private void fillTransform(Matrix4f result)
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