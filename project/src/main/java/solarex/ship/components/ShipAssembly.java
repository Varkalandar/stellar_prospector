/*
 * ShipAssembly.java
 *
 * Created on 26.03.2012
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Container for all components (equipment) of a ship.
 * 
 * @author Hj. Malthaner
 */
public class ShipAssembly extends ShipComponent
{
    public final ArrayList <ShipComponent> components;

    public ShipAssembly() 
    {
        components = new ArrayList<ShipComponent>();
    }

    @Override
    public EquipmentType getType()
    {
        return EquipmentType.OTHER;
    }

    @Override
    public String getName() 
    {
        return "Ship";
    }

    /**
     * A component weights something (in tons)
     * @return the component weight
     */
    @Override
    public int getMass()
    {
        int sum = 0;
        for(ShipComponent component : components)
        {
            sum += component.getMass();
        }
        
        return sum;
    }
    
    @Override
    public int getCargoCapacity() 
    {
        int sum = 0;
        for(ShipComponent component : components)
        {
            sum += component.getCargoCapacity();
        }
        
        return sum;
    }

    @Override
    public int getPassengerCapacity() 
    {
        int sum = 0;
        for(ShipComponent component : components)
        {
            sum += component.getPassengerCapacity();
        }
        
        return sum;
    }

    @Override
    public int getHyperjumpRange() 
    {
        int sum = 0;
        for(ShipComponent component : components)
        {
            sum += component.getHyperjumpRange();
        }
        
        return sum;
    }

    @Override
    public double getEffectiveDriveRange(int mass)
    {
        double range = getHyperjumpRange();
        return (range * 100.0) / (double)mass;
    }

    /** 
     * A unique key for this component.
     * @return A unique key for this component.
     */
    @Override
    public String getKey()
    {
        return "ShipAssembly";
    }
    
    
    /**
     * The base price of this equipment item. May be modified 
     * by the trader.
     * @return the base price
     */    
    @Override
    public int getBasePrice()
    {
        int sum = 0;
        for(ShipComponent component : components)
        {
            sum += component.getBasePrice();
        }
        
        return sum;        
    }

    /**
     * The minimum operating temperature for this equipment
     * @return 
     */
    @Override
    public int getMinOperatingTemp()
    {
        return 0;
    }

    /**
     * The maximum operating temperature for this equipment
     * @return 
     */
    @Override
    public int getMaxOperatingTemp()
    {
        return 0;
    }
    
    @Override
    public int getCurrentDurability()
    {
        return 0;
    }
    
    @Override
    public void setCurrentDurability(int durability)
    {
        // can't do this for the assembly
    }

    @Override
    public int getMaxDurability()
    {
        return 0;
    }

    @Override
    public int getUnitsPerTime()
    {
        return 0;
    }
    
    public void addComponent(ShipComponent comp)
    {
        components.add(comp);
    }

    public void removeComponent(ShipComponent comp) 
    {
        components.remove(comp);
    }

    public void save(Writer writer) throws IOException 
    {
        writer.write("<Equipment>\n");
        writer.write("<version>1</version>\n");
        writer.write("<itms>" + components.size() + "</itms>\n");
        
        for(ShipComponent comp : components) 
        {
            writer.write("<comp durability=\"" + comp.getCurrentDurability() + "\">" + 
                    comp.getKey() + "</comp>\n");
        }
        
        writer.write("</Equipment>\n");
    }

    /**
     * Read data to restore a former state
     * @param reader The reader to read from
     */
    public void load(BufferedReader reader) throws IOException
    {
        components.clear();
        
        String tmp;

        reader.readLine();
        reader.readLine();

        tmp = reader.readLine();
        int count = Integer.parseInt(tmp.substring(6, tmp.length()-7));

        EquipmentFactory fab = new EquipmentFactory();
        
        for(int i=0; i<count; i++) 
        {
            tmp = reader.readLine();
            final int bracketPos = tmp.indexOf('>') + 1;
            
            String key = tmp.substring(bracketPos, tmp.length()-7);
            EquipmentFactory.Component compKey = EquipmentFactory.Component.valueOf(key);
            
            ShipComponent comp = fab.create(compKey);

            final int durabilityStart = tmp.indexOf("durability=\"") + 12;
            final int durabilityEnd = tmp.indexOf("\"", durabilityStart);
            final String durabilityString = tmp.substring(durabilityStart, durabilityEnd);
            
            final int durability = Integer.parseInt(durabilityString);

            comp.setCurrentDurability(durability);
            
            components.add(comp);
        }

        reader.readLine();
    }

}
