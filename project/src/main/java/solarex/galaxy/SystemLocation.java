/*
 * SystemLocation.java
 *
 * Created: 30-Apr-2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.galaxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

/**
 * System location data.
 * 
 * @author Hj. Malthaner
 */
public class SystemLocation
{
    /** Sector I coordinate */
    public int galacticSectorI;

    /** Sector J coordinate */
    public int galacticSectorJ;

    /** System number within sector */
    public int systemNumber;

    /** X offset within sector (0..127) */
    public int ioff;

    /** Y offset within sector (0..127) */
    public int joff;

    /** Seed value for system rng */
    public long systemSeed;

    public String name = "unknown";


    public double distance(SystemLocation there)
    {
        int x1 = galacticSectorI * 128 + ioff;
        int y1 = galacticSectorJ * 128 + joff;

        int x2 = there.galacticSectorI * 128 + there.ioff;
        int y2 = there.galacticSectorJ * 128 + there.joff;

        double sectorDistance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));

        // Hajo: convert to ly
        sectorDistance = sectorDistance * 10.0 / 128.0;

        // System.err.println("distance=" + sectorDistance);

        return sectorDistance;
    }
    
    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    public void save(Writer writer) throws IOException
    {
        writer.write("<Location>\n");
        writer.write("<version>2</version>\n");
        writer.write("<gs_i>" + galacticSectorI + "</gs_i>\n");
        writer.write("<gs_j>" + galacticSectorJ + "</gs_j>\n");

        writer.write("<ioff>" + ioff + "</ioff>\n");
        writer.write("<joff>" + joff + "</joff>\n");

        writer.write("<sysn>" + systemNumber + "</sysn>\n");
        writer.write("<seed>" + systemSeed + "</seed>\n");
        
        writer.write("<name>" + name + "</name>\n");

        writer.write("</Location>\n");
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    public void load(BufferedReader reader) throws IOException
    {
        String tmp;

        reader.readLine();
        reader.readLine();

        tmp = reader.readLine();
        galacticSectorI = Integer.parseInt(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        galacticSectorJ = Integer.parseInt(tmp.substring(6, tmp.length()-7));

        tmp = reader.readLine();
        ioff = Integer.parseInt(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        joff = Integer.parseInt(tmp.substring(6, tmp.length()-7));

        tmp = reader.readLine();
        systemNumber = Integer.parseInt(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        systemSeed = Long.parseLong(tmp.substring(6, tmp.length()-7));
        tmp = reader.readLine();
        name = tmp.substring(6, tmp.length()-7);

        reader.readLine();
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof SystemLocation)
        {
            SystemLocation loca = (SystemLocation)other;

            return
                    loca.systemSeed == systemSeed &&
                    loca.systemNumber == systemNumber;
        }
        // Not comparable ...
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.galacticSectorI;
        hash = 59 * hash + this.galacticSectorJ;
        hash = 59 * hash + this.systemNumber;
        hash = 59 * hash + (int) (this.systemSeed ^ (this.systemSeed >>> 32));
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
