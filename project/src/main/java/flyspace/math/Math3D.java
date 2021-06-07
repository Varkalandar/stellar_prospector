package flyspace.math;

import static java.lang.Math.*;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Hj. Malthaner
 */
public class Math3D 
{
    private static final Matrix3f rotX = new Matrix3f();
    private static final Matrix3f rotY = new Matrix3f();
    private static final Matrix3f rotZ = new Matrix3f();
    private static final Matrix3f rotAxis = new Matrix3f();
    
    public static Vector3f rotX(Vector3f point, double angle, Vector3f result)
    {
        double angleX = angle * 2.0 * PI / 360.0;
        float cosx = (float)cos(angleX);
        float sinx = (float)sin(angleX);

        rotX.m00 = 1.0f;
        rotX.m11 = cosx;
        rotX.m12 = -sinx;
        rotX.m21 = sinx;
        rotX.m22 = cosx;
        
        Matrix3f.transform(rotX, point, result);
        
        return result;
    }
    
    public static Vector3f rotY(Vector3f point, double angle, Vector3f result)
    {
        double angleY = angle * 2.0 * PI / 360.0;
        float cosx = (float)cos(angleY);
        float sinx = (float)sin(angleY);
        
        rotY.m00 = cosx;
        rotY.m02 = sinx;
        rotY.m11 = 1.0f;
        rotY.m20 = -sinx;
        rotY.m22 = cosx;
        
        Matrix3f.transform(rotY, point, result);

        return result;
    }
    
    public static Vector3f rotZ(Vector3f point, double angle, Vector3f result)
    {
        double angleZ = angle * 2.0 * PI / 360.0;
        
        rotZ.m00 = (float)cos(angleZ);
        rotZ.m01 = (float)-sin(angleZ);
        rotZ.m10 = (float)sin(angleZ);
        rotZ.m11 = (float)cos(angleZ);
        rotZ.m22 = 1.0f;
        
        Matrix3f.transform(rotZ, point, result);
        
        return result;
    }
    
    public static Vector3f rotAxis(Vector3f point, Vector3f axis, double angle, Vector3f result)
    {
        double angleRad = angle * 2.0 * PI / 360.0;
        
        float cosf = (float)cos(angleRad);
        float sinf = (float)sin(angleRad);
        
        
        rotAxis.m00 = cosf + axis.x * axis.x * (1 - cosf);
        rotAxis.m01 = axis.x * axis.y * (1 - cosf) - axis.z * sinf;
        rotAxis.m02 = axis.x * axis.y *(1 - cosf) + axis.y * sinf;
        
        rotAxis.m10 = axis.y * axis.x * (1 - cosf) + axis.z *sinf;
        rotAxis.m11 = cosf + axis.y * axis.y * (1 - cosf);
        rotAxis.m12 = axis.y * axis.z * (1-cosf) - axis.x * sinf;
        
        rotAxis.m20 = axis.z * axis.x * (1-cosf) - axis.y * sinf;
        rotAxis.m21 = axis.z * axis.y * (1-cosf) + axis.x *sinf;
        rotAxis.m22 = cosf + axis.z * axis.z * (1-cosf);
        
        Matrix3f.transform(rotAxis, point, result);
        
        return result;
    }

    public static Vector3f calcNormal(Vector3f left, Vector3f origin, Vector3f right) 
    {
        Vector3f normal = new Vector3f();
        Vector3f leftEdge = new Vector3f();
        Vector3f rightEdge = new Vector3f();
        
        Vector3f.sub(left, origin, leftEdge);
        Vector3f.sub(right, origin, rightEdge);
        
        Vector3f.cross(leftEdge, rightEdge, normal);
        normal.normalise();
        
        return normal;
    }

    public static float degToRad(float degrees) 
    {
        return degrees * (float)(Math.PI / 180);
    }
    
}
