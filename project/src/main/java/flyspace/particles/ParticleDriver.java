package flyspace.particles;


/**
 * High performance particle driver.
 * 
 * @author Hj. Malthaner
 */
public class ParticleDriver 
{
    public static final int STRIDE = 10;
    
    public static final int LIFE = 0;
    public static final int MAXLIFE = 1;
    public static final int XPOS = 2;
    public static final int YPOS = 3;
    public static final int ZPOS = 4;
    public static final int XSPEED = 5;
    public static final int YSPEED = 6;
    public static final int ZSPEED = 7;
    public static final int ID = 8;
    public static final int COLOR = 9;
    
    private final int count;
    private int startSearchMark = 0;
    private int lastParticleMark = 0;
    
    /** We fake some sort of structs in this array */
    private int [] particles; 
    
    private ParticleMovement movement;
    private ParticlePainter painter;
    
    public ParticleDriver(int count)
    {
        this.particles = new int [STRIDE * count];
        this.count = count;
        this.startSearchMark = 0;
        this.lastParticleMark = 0;
        this.movement = new ParticleLinear();
//        this.painter = new ParticlePainter();
    }
    
    public void setMovement(ParticleMovement movement)
    {
        this.movement = movement;
    }

    public void setPainter(ParticlePainter painter)
    {
        this.painter = painter;
    }
    
    public boolean addParticle(int x, int y, int z, int xSpeed, int ySpeed, int zSpeed,
                               int lifetime, int id, int color)
    {
        for(int base=startSearchMark; base<count*STRIDE; base+=STRIDE)
        {
            if(particles[base] == 0)
            {
                // found a free entry
                particles[base + LIFE] = 1;               // now allocated
                particles[base + MAXLIFE] = lifetime + 1;  // max age
                particles[base + XPOS] = x;
                particles[base + YPOS] = y;
                particles[base + ZPOS] = z;
                particles[base + XSPEED] = xSpeed;
                particles[base + YSPEED] = ySpeed;
                particles[base + ZSPEED] = zSpeed;
                particles[base + ID] = id;
                particles[base + COLOR] = color;
                
                if(base > lastParticleMark) lastParticleMark = base;
                
                return true;
            }
        }
        
        return false;
    }
    
    public void driveParticles()
    {
        for(int base=0; base<lastParticleMark; base+=STRIDE)
        {
            if(particles[base + LIFE] > 0)
            {
                // found an active particle, drive it
                movement.drive(particles, base);
            }
            else
            {
                if(base < startSearchMark) startSearchMark = base;
            }
        }
    }    
    
    public void drawParticles()
    {
        for(int base=0; base<lastParticleMark; base+=STRIDE)
        {
            if(particles[base] > 0)
            {
                // found an active particle, draw it
            
                painter.paint(particles, base);
            }
        }
    }
    
    public void clear() 
    {
        for(int base=0; base<lastParticleMark; base+=STRIDE)
        {
            particles[base] = 0;
        }
        startSearchMark = 0;
        lastParticleMark = 0;
    }
}
