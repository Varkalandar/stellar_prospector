/*
 * Galaxy.java
 *
 * Created: 30-Apr-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.galaxy;

import flyspace.SystemBuilder;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import solarex.system.Society;
import solarex.system.Solar;

/**
 * Used to create all systems of a galaxy. Galaxies are divided into sectors
 * and systems are generated per sector.
 * 
 * @author Hj. Malthaner
 */
public class Galaxy
{

    private BufferedImage spiral;


    public Galaxy(Image spiralImg)
    {
        spiral = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
        spiral.getGraphics().drawImage(spiralImg, 0, 0, null);
    }

    
    public List<Solar> findSettlements(int range, SystemLocation here)
    {
        final ArrayList <SystemLocation> destinations = new ArrayList<SystemLocation>(64);
        
        for(int j=here.galacticSectorJ-range; j<=here.galacticSectorJ+range; j++)
        {
            for(int i=here.galacticSectorI-range; i<=here.galacticSectorI+range; i++)
            {
                final List <SystemLocation> sector = buildSector(i, j);
                destinations.addAll(sector);
            }
        }
    
        final List <Solar> settlements = new ArrayList<Solar>(256);
        for(SystemLocation location : destinations)
        {
            final Solar system = SystemBuilder.create(location, true);
            Society.populate(system);
            system.listSettlements(settlements);
        }
     
        return settlements;
    }
    

    /**
     * Create information about a galactical sector.
     * 
     * @param i Sector i coordinate
     * @param j Sector j coordinate
     * @return List of system information
     */
    public List <SystemLocation> buildSector(int i, int j)
    {
        ArrayList <SystemLocation> list = new ArrayList();

        final long seed = calcSectorSeed(i, j);
        final Random rng = new Random(seed);

        int xp = (i+80) & 0xFF;
        int yp = (j+80) & 0xFF;

        int anz = 0;
        
        if(xp >= 0 && xp<128 && yp>=0 && yp <128) {
            anz = (spiral.getRGB(xp, yp) & 0xFF) >>> 5;
        }
        
        // final int anz = rng.nextInt(6);

        for (int n = 0; n < anz; n++) 
        {
            SystemLocation loca = new SystemLocation();

            loca.galacticSectorI = i;
            loca.galacticSectorJ = j;

            loca.systemNumber = n;

            loca.ioff = rng.nextInt(128);
            loca.joff = rng.nextInt(128);

            loca.systemSeed = rng.nextLong();

            list.add(loca);
        }

        addSpecialSystems(list, i, j);
        
        return list;
    }

    private long calcSectorSeed(int i, int j)
    {
        final long GAMANUM = 1;
        return (Math.abs(i+4999) + Math.abs(j+5000)*10000) * 20 + GAMANUM;
    }

    private void addSpecialSystems(ArrayList<SystemLocation> list, int i, int j) 
    {
        if(i == 0 && j == 0)
        {
            SystemLocation loca = SystemBuilder.createSolLocation();
                    
            list.add(loca);
        }
    }


}
