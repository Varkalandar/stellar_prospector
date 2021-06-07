/*
 * Mathlib.java
 *
 * Created: ???
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.util;

/**
 * More complex math functions get collected here.
 * 
 * @author Hj. Malthaner
 */
public class Mathlib
{
    private static final double sq2p = Math.sqrt(2 * Math.PI);

    public static double gauss(double x, double mu, double sigma)
    {
        final double t = ((x-mu)/sigma);
        return Math.exp(-0.5 * t * t) / (sigma * sq2p);
    }
}
