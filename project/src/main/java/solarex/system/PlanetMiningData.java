/*
 * PlanetMiningData.java
 *
 * Created on 2012/11/06
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.system;

/**
 * Data (hardcoded) for planet mining operations. Made to
 * match  the visual appearance of planet images.
 * 
 * @author Hj. Malthaner
 */
public class PlanetMiningData
{
    public enum Surface
    {
        FLUID,
        FLUID_ROCKS,
        FLUID_ICE,
        FLUID_ICE_ROCKS,
        ICE,
        ICE_ROCKS,
        ROCKS,
        
        NONE
    };
    
    private Surface [] earth = new Surface []
    {
        Surface.FLUID_ICE, Surface.FLUID_ICE, Surface.FLUID_ICE, Surface.FLUID_ICE, Surface.FLUID_ICE, Surface.FLUID_ICE,
        Surface.FLUID_ROCKS, Surface.FLUID_ROCKS, Surface.FLUID, Surface.FLUID_ROCKS, Surface.FLUID_ROCKS, Surface.FLUID,
        Surface.ROCKS, Surface.FLUID_ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.FLUID_ROCKS,
        Surface.FLUID, Surface.FLUID, Surface.FLUID, Surface.FLUID_ROCKS, Surface.FLUID, Surface.FLUID,
        Surface.FLUID, Surface.FLUID, Surface.FLUID_ROCKS, Surface.FLUID_ROCKS, Surface.FLUID, Surface.FLUID,
        Surface.FLUID_ICE, Surface.FLUID_ICE_ROCKS, Surface.FLUID_ICE_ROCKS, Surface.FLUID_ICE, Surface.FLUID_ICE, Surface.FLUID_ICE,
    };
    
    private Surface [] cloud = new Surface []
    {
        Surface.ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS,
        Surface.ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.ROCKS,
        Surface.ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS,
        Surface.ROCKS, Surface.FLUID, Surface.FLUID_ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.ROCKS,
        Surface.ROCKS, Surface.FLUID_ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.ROCKS,        
        Surface.ROCKS, Surface.ROCKS, Surface.FLUID_ROCKS, Surface.ROCKS, Surface.ROCKS, Surface.ROCKS,
    };
    
    public Surface getSurfaceData(Solar planet, int gx, int gy)
    {
        Surface result = Surface.NONE;
        
        if(gx >= 0 && gx < 6 && gy >= 0 & gy < 6)
        {
            switch(planet.ptype)
            {

                case ATM_ROCK:
                    if(gy < 1 || gy > 4)
                    {
                        result = Surface.ICE_ROCKS;
                    }
                    else
                    {
                        result = Surface.ROCKS;
                    }
                    break;

                case BARE_ROCK:
                    result = Surface.ROCKS;
                    break;

                case CARBON_RICH:
                    result = Surface.ROCKS;
                    break;

                case CLOUD:
                    result = cloud[gy*6 + gx];
                    break;

                case EARTH:
                    result = earth[gy*6 + gx];
                    break;

                case ICE:
                    result = Surface.ICE;
                    break;

                case SMALL_GAS:
                    if(gy < 1 || gy > 4)
                    {
                        result = Surface.FLUID_ICE;
                    }
                    else
                    {
                        result = Surface.FLUID;
                    }
                    break;

                default:
                    result = Surface.NONE;
                    break;
            }
        }
        
        return result;
    }
}
