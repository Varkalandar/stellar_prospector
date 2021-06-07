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

package solarex.ui.components;

import solarex.ship.components.ShipComponent;

/**
 *
 * @author Hj. Malthaner
 */
public class EquipmentWrapper
{
    private final ShipComponent comp;

    public EquipmentWrapper(ShipComponent comp)
    {
        this.comp = comp;
    }

    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder("<html><font color='#CCCCCC'> ");
        buf.append(comp.getName());
        buf.append(", Durability ");
        buf.append(comp.getCurrentDurability());
        buf.append(" / ");
        buf.append(comp.getMaxDurability());
        buf.append("</font></html>");

        return buf.toString();
    }

    public ShipComponent getComponent() 
    {
        return comp;
    }
    
    public String getDetailsString()
    {
        StringBuilder buf = new StringBuilder("<html>");
        
        if(comp.getPassengerCapacity() > 0)
        {
            buf.append("Passenger capacity: <font color=#FFFFFF>")
                    .append(comp.getPassengerCapacity()).append("</font><br>");
        }

        if(comp.getHyperjumpRange() > 0)
        {
            buf.append("Hyperjump range:    <font color=#FFFFFF>")
                    .append(comp.getHyperjumpRange()).append(" ly per 100t</font><br>");
        }

        if(comp.getMass() > 0)
        {
            buf.append("Mass: <font color=#FFFFFF>")
                    .append(comp.getMass()).append(" t</font><br>");
        }

        if(comp.getMinOperatingTemp() > 0)
        {
            buf.append("Min. oper. temp:    <font color=#FFFFFF>")
                    .append(comp.getMinOperatingTemp()).append(" k</font><br>");
        }
        if(comp.getMaxOperatingTemp() > 0)
        {
            buf.append("Max. oper. temp:    <font color=#FFFFFF>")
                    .append(comp.getMaxOperatingTemp()).append(" k</font><br>");
        }

        if(comp.getBasePrice() > 0)
        {
            buf.append("Price: <font color=#FFFFFF>")
                    .append(comp.getBasePrice()).append(".00 Cr</font><br>");
        }
    
        if(comp.getMaxDurability()> 1)
        {
            buf.append("Durability: <font color=#FFFFFF>")
                    .append(comp.getCurrentDurability()).append("/")
                    .append(comp.getMaxDurability()).append("</font><br>");
        }

        buf.append("</html>");
        
        return buf.toString();
    }
}
