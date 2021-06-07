package flyspace.particles;

/**
 *
 * @author Hj. Malthaner
 */
public interface ParticleMovement 
{
    /**
     * Move the particle.
     * @param particles The particle array
     * @param base The particle index
     */
    public void drive(int [] particles, int base);
}
