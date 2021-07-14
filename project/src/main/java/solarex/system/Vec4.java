package solarex.system;

/**
 * Simple 4D vector type (the math one)
 * 
 * @author Hj. Malthaner
 */
public class Vec4 
{
    public double x, y, z, w;

    public Vec4()
    {
        
    }
    
    public Vec4(int x, int y, int z, int w) 
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }    
}
