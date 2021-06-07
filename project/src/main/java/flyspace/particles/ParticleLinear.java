package flyspace.particles;

import static flyspace.particles.ParticleDriver.LIFE;
import static flyspace.particles.ParticleDriver.MAXLIFE;
import static flyspace.particles.ParticleDriver.XPOS;
import static flyspace.particles.ParticleDriver.XSPEED;
import static flyspace.particles.ParticleDriver.YPOS;
import static flyspace.particles.ParticleDriver.YSPEED;
import static flyspace.particles.ParticleDriver.ZPOS;
import static flyspace.particles.ParticleDriver.ZSPEED;

/**
 *
 * @author Hj. Malthaner
 */
public class ParticleLinear implements ParticleMovement
{

    @Override
    public void drive(int[] particles, int base) 
    {
        // end of life reached?
        if(particles[base + LIFE] > particles[base+MAXLIFE])
        {
            particles[base + LIFE] = 0;
        }
        else
        {
            particles[base + LIFE] ++;
            particles[base + XPOS] += particles[base + XSPEED];
            particles[base + YPOS] += particles[base + YSPEED];
            particles[base + ZPOS] += particles[base + ZSPEED];
        }
    }
    
}
