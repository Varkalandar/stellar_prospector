/*
 * Cargo.java
 *
 * Created on 09.02.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

/**
 * Ships, stations, settlements all deal with storage of goods. This class
 * is the commonly used container for all good storages.
 * 
 * @author Hj. Malthaner
 */
public class Cargo 
{

    /**
     * Current money balance. Not sure if cargo is the right place to store
     * it, but all entities that deal with money also seem to deal with
     * cargo structures.
     */
    public double money;

    /**
     * Max. space available for goods.
     * Measured in kg!
     */
    public int space;

    /**
     * Full array of all goods. If a good is not in cargo
     * it has units == 0
     */
    public Good [] goods = new Good [Good.Type.values().length];

    /**
     * Markers if a good is illegal (true) for the owner of the cargo
     * e.g. space stations and space ports will use this.
     */
    public boolean [] illegalGoods = new boolean [Good.Type.values().length];

    public Cargo()
    {
        for(int i=0; i<goods.length; i++) 
        {
            goods[i] = new Good();
            goods[i].type = Good.Type.values()[i];
        }
    }

    public int availableSpace()
    {
        return space - totalSpaceUsedByGoods();
    }

    /**
     * Space (rather mass) of the stored goods in kg.  
     */
    public int totalSpaceUsedByGoods()
    {
        int sum = 0;

        for(int i=0; i<goods.length; i++) 
        {
            sum += goods[i].units * goods[i].type.massPerUnit;
        }

        return sum;
    }

    /**
     * Save data for later re-loading
     * @param writer The writer to write the data to.
     */
    public void save(Writer writer) throws IOException
    {
        writer.write("<Cargo>\n");
        writer.write("<version>2</version>\n");
        writer.write("<mony>" + money + "</mony>\n");
        
        for(int i=0; i<goods.length; i++) {
            writer.write("<good>" + goods[i].units + "</good>\n");
        }
        for(int i=0; i<illegalGoods.length; i++) {
            writer.write("<good>" + illegalGoods[i] + "</good>\n");
        }
        
        writer.write("</Cargo>\n");
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
        money = Double.parseDouble(tmp.substring(6, tmp.length()-7));

        for(int i=0; i<goods.length; i++) {
            tmp = reader.readLine();
            goods[i].units = Integer.parseInt(tmp.substring(6, tmp.length()-7));
        }

        for(int i=0; i<illegalGoods.length; i++) {
            tmp = reader.readLine();
            illegalGoods[i] = Boolean.parseBoolean(tmp);
        }

        reader.readLine();
    }
}
