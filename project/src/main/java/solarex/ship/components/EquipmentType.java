/*
 * EquipmentType.java
 *
 * Created on 2012/11/05
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship.components;

/**
 * Instead of subclassing the ShipComponent class,
 * it seemed easier to add a type attribute, because
 * the behavior of the subclasses would be the same.
 * 
 * @author Hj. Malthaner
 */
public enum EquipmentType 
{
    MACHINE,
    DRONE,
    OTHER
}
