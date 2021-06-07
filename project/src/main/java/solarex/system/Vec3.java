/*
 * Vec3.java
 *
 * Created: 10-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
 
package solarex.system;

/**
 * Simple 3D vector type (the math one)
 * 
 * @author Hj. Malthaner
 */
public class Vec3
{

    public double x, y, z;

    /**
     * @returns length^2
     * @author Hj. Malthaner
     */
    public double length2() 
    {
        return x * x + y * y + z * z;
    }

    public void sub(Vec3 other)
    {
        x -= other.x;
        y -= other.y;
        z -= other.z;
    }

    public void add(Vec3 other)
    {
        x += other.x;
        y += other.y;
        z += other.z;
    }

    public Vec3()
    {
        x = y = z = 0.0;
    }

    public Vec3(Vec3 v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vec3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vec3 v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public double length() 
    {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public void scale(double d) 
    {
        x *= d;
        y *= d;
        z *= d;
    }

    public void normalise() 
    {
        scale(1.0 / length());
    }
    
    @Override
    public String toString()
    {
        return "x=" + x + " y=" + y + " z=" + z;
    }
}
