/*
 * EquipmentAspect.java
 *
 * Created on 29.03.2012
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */package solarex.ship.components;

/**
 *
 * @author Hj. Malthaner
 */
public enum EquipmentAspect 
{
    /**
     * The name of the component
     */
    NAME,
    
    /**
     * A component weights something (in tons)
     */
    WEIGHT,

    /**
     * A feature may supply storage capacity for cargo
     */
    CARGO_CAPACITY,
    
    /**
     * A feature may supply capacity for passengers
     */
    PASSENGER_CAPACITY,
    
    /**
     * A feature may add hyperjump range
     */    
    HYPERJUMP_RANGE,
    
    MIN_OPERATING_TEMP,
    MAX_OPERATING_TEMP,
   
    CURRENT_DURABILITY,
    MAX_DURABILITY,
    
    /**
     * The base price of this equipment item. May be modified 
     * by the trader.
     */    
    BASE_PRICE,
    
    TYPE,
    
    KEY,
}
