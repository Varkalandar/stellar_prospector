/*
 * Equipment.java
 *
 * Created on 29.03.2012
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ship.components;

/**
 * Generic ship equipment data class.
 * 
 * @author Hj. Malthaner
 */
public class Equipment extends ShipComponent
{

    private Object [] values;
    
    Equipment()
    {
        values = new Object [EquipmentAspect.values().length];
    }
    
    @Override
    public EquipmentType getType()
    {
        int n = EquipmentAspect.TYPE.ordinal();
        Object o = values[n];

        return (EquipmentType)o;
    }
    
    @Override
    public String getName() 
    {
        return getString(EquipmentAspect.NAME);
    }

    @Override
    public int getMass() 
    {
        return getInt(EquipmentAspect.WEIGHT);
    }

    @Override
    public int getCargoCapacity() 
    {
        return getInt(EquipmentAspect.CARGO_CAPACITY);
    }

    @Override
    public int getPassengerCapacity() 
    {
        return getInt(EquipmentAspect.PASSENGER_CAPACITY);
    }

    @Override
    public int getHyperjumpRange() 
    {
        return getInt(EquipmentAspect.HYPERJUMP_RANGE);
    }
    
    /**
     * The base price of this equipment item. May be modified 
     * by the trader.
     * @return the base price
     */    
    @Override
    public int getBasePrice()
    {
        return getInt(EquipmentAspect.BASE_PRICE);        
    }
    
    /**
     * The minimum operating temperature for this equipment
     * @return 
     */
    @Override
    public int getMinOperatingTemp()
    {
        return getInt(EquipmentAspect.MIN_OPERATING_TEMP);
    }

    /**
     * The maximum operating temperature for this equipment
     * @return 
     */
    @Override
    public int getMaxOperatingTemp()
    {
        return getInt(EquipmentAspect.MAX_OPERATING_TEMP);
    }

    @Override
    public int getCurrentDurability()
    {
        return getInt(EquipmentAspect.CURRENT_DURABILITY);
    }

    @Override
    public void setCurrentDurability(int durability)
    {
        setAspect(EquipmentAspect.CURRENT_DURABILITY, durability);
    }
    
    @Override
    public int getMaxDurability()
    {
        return getInt(EquipmentAspect.MAX_DURABILITY);
    }
    
    @Override
    public double getEffectiveDriveRange(int mass)
    {
        double range = getHyperjumpRange();
        return (range * 100.0) / mass;
    }
    
    /** 
     * A unique key for this component.
     * @return A unique key for this component.
     */
    @Override
    public String getKey()
    {
        return getString(EquipmentAspect.KEY);
    }

    void setAspect(EquipmentAspect as, Object value)
    {
        int n = as.ordinal();
        values[n] = value;
    }
    
    private int getInt(EquipmentAspect as)
    {
        int n = as.ordinal();
        Object o = values[n];
        Integer i = (Integer)o;
        return i != null ? i.intValue() : 0;
    }
    
    private String getString(EquipmentAspect as)
    {
        int n = as.ordinal();
        Object o = values[n];
        
        return o.toString();
    }
}
