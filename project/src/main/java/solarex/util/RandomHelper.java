/*
 * RandomHelper.java
 *
 * Created: 17-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.util;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Helper methods, using a random  number generator.
 * 
 * @author Hj. Malthaner
 */
public class RandomHelper
{
    
    public static Random createRNG()
    {
        Random rng;
        /*
        try 
        {
            rng = new SecureRandom();
        }
        catch(Exception ex) 
        {
            // ex.printStackTrace();
            System.err.println("Cannot get a high quality RNG, using a normal one.");
            rng = new Random();
        }
        */
        
        rng = new XORShiftRandom();
        
        return rng;
    }

    public static Random createRNG(long seed)
    {
        Random rng = createRNG();
        rng.setSeed(seed);

        return rng;        
    }
    
    /**
     * Gets a random element out of an array.
     * 
     * @return Chosen element.
     */
    public static <T> T oneOf(Random rng, T[] array)
    {
        return array[rng.nextInt(array.length)];
    }
    
    public static <T> T oneOf(List <T> list)
    {
        return list.get((int)(Math.random() * list.size()));
    }

    /**
     * Gets a random element out of an array.
     * 
     * @return Chosen element.
     */
    public static <T> T oneOf(T[] array)
    {
        return array[(int)(Math.random() * array.length)];
    }

    /**
     * Gets a random element out of a weighted list.
     * 
     * @return index of chosen interval.
     */
    public static int oneOfWeightedList(Random rng, final int [] weights)
    {
        int sum = 0;

        for(int i=0; i<weights.length; i++) {
            sum += weights[i];
        }

        final int weight = (int)(sum*rng.nextDouble());

        sum = 0;

        for(int i=0; i<weights.length; i++) {
            final int high = sum + weights[i];
            if(sum <= weight && weight < high) {
                return i;
            }
            sum = high;
        }

        // Hajo: empty lists or crippled weights
        return -1;
    }


    private static class XORShiftRandom extends Random 
    {
        private long seed = System.nanoTime();

        public XORShiftRandom() 
        {
        }

        @Override
        public void setSeed(long seed)
        {
            this.seed = seed;
        }
        
        /**
         * // Method is not thread-safe!
         */
        @Override
        protected int next(int nbits) 
        {
            long x = seed;
            x ^= (x << 21);
            x ^= (x >>> 35);
            x ^= (x << 4);
            seed = x;
            
            x &= ((1L << nbits) - 1);
            
            return (int) x;
        }
    }
}
