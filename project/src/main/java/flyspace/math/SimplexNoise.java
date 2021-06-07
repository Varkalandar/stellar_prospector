package flyspace.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * Simplex noise octaves integrator.
 * 
 * @author Hj. Malthaner
 */
public class SimplexNoise 
{
    // private final SimplexNoise_octave smn;
    private final OpenSimplexNoise osmn;
    private double frequency;
    private double persistence;
    private int octaves;
    private double seed;
    
    private double yScale;
    
    public SimplexNoise(long seed)
    {
        // this.smn = new SimplexNoise_octave(seed);
        this.osmn = new OpenSimplexNoise(seed);
        
        this.seed = ((seed & 0x7FFFFFFF) + (seed >> 32)) * 0.01;
        this.frequency = 1.0;
        this.persistence = 0.5; 
        this.octaves = 3;
        this.yScale = 1.0;
    }
    
    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }
    
    public void setPersistence(double persistence)
    {
        this.persistence = persistence;
    }
    
    public void setOctaves(int octaves)
    {
        this.octaves = octaves;
    }
    
    
    double maxn = -99999; 
    double minn = +99999;
    
    
    /**
     * Simplex noise over several octaves. Beware, there are two factors in
     * this method to scale the result into (0 .. 1), but these were found
     * experimentally!
     * 
     * @param v location
     * @return Value in (0 .. 1)
     */
    public double noise(Vector3f v)
    {
        double value = 0;
        double amplitude = 1.0f;
        double f = frequency;
        double div = 0;
        double x = v.x + seed;
        double y = v.y * yScale + seed;
        double z = v.z + seed;
        
        for(int octave = 0; octave < octaves; octave ++)
        {
            // double noise = smn.noise(x * f, y * f, z * f);
            double noise = osmn.eval(x * f, y * f, z * f);
            
            value += noise * amplitude;
            div += amplitude;
            amplitude *= persistence;
            f += f;
        }
        
        // Hajo: noise scale experimentally evaluated!
        value = (value + 1.545) * 0.640 / div;
        
        /*
        if(value > maxn)
        {
            maxn = value;
            System.err.println("maxn=" + maxn);
        }
        if(value < minn)
        {
            minn = value;
            System.err.println("minn=" + minn);
        }
        */
        
        // Hajo: just in case, make sure to be in [0 ... 1]
        return Math.min(Math.max(value, 0.0), 1.0);
    }

    public void setYScale(double scale) 
    {
        yScale = scale;
    }
}
