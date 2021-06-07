/*
 * ShipComponent.java
 *
 * Created on 26.03.2012
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship.components;

/**
 * This interface describes all features of a ship component.
 * Currently, ship components are passive elements.
 * 
 * @author Hj. Malthaner
 */
public abstract class ShipComponent 
{
    public abstract EquipmentType getType();
    
    /**
     * The name of the component
     * @return The name
     */
    public abstract String getName();
    
    /**
     * A component has some mass (in tons)
     * @return the component mass
     */
    public abstract int getMass();

    /**
     * A feature may supply storage capacity for cargo
     * @return the cargo storage capacity
     */
    public abstract int getCargoCapacity();
    
    
    /**
     * A feature may supply capacity for passengers
     * @return the passenger capacity
     */
    public abstract int getPassengerCapacity();
    
    
    /**
     * A feature may add hyperjump range
     * @return the added hyperjump range
     */    
    public abstract int getHyperjumpRange();

    /**
     * The base price of this equipment item. May be modified 
     * by the trader.
     * @return the base price
     */    
    public abstract int getBasePrice();
    
    public int calculatePrice(int techLevel)
    {
        int factor = (12*100) / (techLevel + 6);
        
        int price = getBasePrice() * factor;

        // Hajo: round to 10
        return (price / 1000) * 10;
    }
    
    /**
     * The minimum operating temperature for this equipment
     * @return 
     */
    public abstract int getMinOperatingTemp();

    /**
     * The maximum operating temperature for this equipment
     * @return 
     */
    public abstract int getMaxOperatingTemp();
    
    /** 
     * A unique key for this component.
     * @return A unique key for this component.
     */
    public abstract String getKey();

    public abstract int getCurrentDurability();

    public abstract int getMaxDurability();

    public abstract void setCurrentDurability(int durability);
    
    public abstract double getEffectiveDriveRange(int mass);
    
}
