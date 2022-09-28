package flyspace.math;

import static java.lang.Math.*;
import solarex.system.Matrix4;
import solarex.system.Vec3;

/**
 * 3D math functions, not thread safe.
 * 
 * @author Hj. Malthaner
 */
public class Math3D 
{
    private static final Matrix4 rotX = new Matrix4();
    private static final Matrix4 rotY = new Matrix4();
    private static final Matrix4 rotZ = new Matrix4();
    private static final Matrix4 rotAxis = new Matrix4();
    
    
    public static Vec3 rotX(Vec3 point, double angle, Vec3 result)
    {
        double angleX = angle * 2.0 * PI / 360.0;
        float cosx = (float)cos(angleX);
        float sinx = (float)sin(angleX);

        rotX.m00 = 1.0f;
        rotX.m11 = cosx;
        rotX.m12 = -sinx;
        rotX.m21 = sinx;
        rotX.m22 = cosx;
        
        Matrix4.transform(rotX, point, result);
        
        return result;
    }
    
    
    public static Vec3 rotY(Vec3 point, double angle, Vec3 result)
    {
        double angleY = angle * 2.0 * PI / 360.0;
        float cosx = (float)cos(angleY);
        float sinx = (float)sin(angleY);
        
        rotY.m00 = cosx;
        rotY.m02 = sinx;
        rotY.m11 = 1.0f;
        rotY.m20 = -sinx;
        rotY.m22 = cosx;
        
        Matrix4.transform(rotY, point, result);

        return result;
    }
    
    
    public static Vec3 rotZ(Vec3 point, double angle, Vec3 result)
    {
        double angleZ = angle * 2.0 * PI / 360.0;
        float cosf = (float)cos(angleZ);
        float sinf = (float)sin(angleZ);
        
        rotZ.m00 = cosf;
        rotZ.m01 = -sinf;
        rotZ.m10 = sinf;
        rotZ.m11 = cosf;
        rotZ.m22 = 1.0f;
        
        Matrix4.transform(rotZ, point, result);
        
        return result;
    }
    
    
    public static Vec3 rotAxis(Vec3 point, Vec3 axis, double angle, Vec3 result)
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
        
        Matrix4.transform(rotAxis, point, result);
        
        return result;
    }

    
    public static Vec3 calcNormal(Vec3 left, Vec3 origin, Vec3 right) 
    {
        Vec3 normal = new Vec3();
        Vec3 leftEdge = new Vec3(left);
        Vec3 rightEdge = new Vec3(right);
        
        leftEdge.sub(origin);
        rightEdge.sub(origin);
        
        Vec3.cross(leftEdge, rightEdge, normal);
        normal.normalise();
        
        return normal;
    }

    
    public static double degToRad(double degrees) 
    {
        return degrees * (Math.PI / 180);
    }   
}
