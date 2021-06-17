/*
 * EquipmentFactory.java
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
 *
 * @author Hj. Malthaner
 */
public class EquipmentFactory 
{
    public enum Component
    {
        SINGLE_PASSENGER_CABIN,
        DOUBLE_PASSENGER_CABIN,
        LUXURY_PASSENGER_CABIN,
        STANDARD_DRIVE,
        FUEL_TANK,
        FANFOSTAR,
        TROMPETECHOS,
        SUPERSCOOPER,
        DRILLDOWN_DYNAMOS,
        INO_SLUURP,
    }
    
    public ShipComponent create(Component what)
    {
        Equipment equipment = new Equipment();
        
        switch(what)
        {
            case SINGLE_PASSENGER_CABIN:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Boxworks B-1&gt;</font><br> Single Passenger Cabin");
                equipment.setAspect(EquipmentAspect.WEIGHT, 2);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 1);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 1000);          
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.OTHER);
                break;
            case DOUBLE_PASSENGER_CABIN:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Boxworks B-2&gt;</font><br> Double Passenger Cabin");
                equipment.setAspect(EquipmentAspect.WEIGHT, 3);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 2);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 1500);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.OTHER);
                break;
            case LUXURY_PASSENGER_CABIN:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Boxworks Eden-2&gt;</font><br> Luxurous Passenger Cabin");
                equipment.setAspect(EquipmentAspect.WEIGHT, 3);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 1);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 2000);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 80);
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 80);          
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.OTHER);
                break;
            case STANDARD_DRIVE:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Tenclon Motors X-1&gt;</font><br> Jump Drive");
                equipment.setAspect(EquipmentAspect.WEIGHT, 9);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 7);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 93000);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 1000);
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 1000);          
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.MACHINE);
                break;
            case FUEL_TANK:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Tenclon Motors FT-1&gt;</font><br> Fuel Tank");
                equipment.setAspect(EquipmentAspect.WEIGHT, 1);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 1000);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.MACHINE);
                break;
            case TROMPETECHOS:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Trompetechos Dual&gt;</font><br> Gas Filtration Drone");
                equipment.setAspect(EquipmentAspect.WEIGHT, 5);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 8000);
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.DRONE);
                equipment.setAspect(EquipmentAspect.MIN_OPERATING_TEMP, 100);
                equipment.setAspect(EquipmentAspect.MAX_OPERATING_TEMP, 800);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.UNITS_PER_TIME, 1);          
                break;
            case FANFOSTAR:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Fanfostar Chorus-12&gt;</font><br> Gas Filtration Drone");
                equipment.setAspect(EquipmentAspect.WEIGHT, 5);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 10000);
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.DRONE);
                equipment.setAspect(EquipmentAspect.MIN_OPERATING_TEMP, 150);
                equipment.setAspect(EquipmentAspect.MAX_OPERATING_TEMP, 1000);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.UNITS_PER_TIME, 1);          
                break;
            case SUPERSCOOPER:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Super Scooper&gt;</font><br> Mining Drone");
                equipment.setAspect(EquipmentAspect.WEIGHT, 8);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 12000);
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.DRONE);
                equipment.setAspect(EquipmentAspect.MIN_OPERATING_TEMP, 80);
                equipment.setAspect(EquipmentAspect.MAX_OPERATING_TEMP, 400);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.UNITS_PER_TIME, 1);          
                break;
            case DRILLDOWN_DYNAMOS:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Drilldown Dynamos&gt;</font><br> Mining Drone");
                equipment.setAspect(EquipmentAspect.WEIGHT, 9);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 15000);
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.DRONE);
                equipment.setAspect(EquipmentAspect.MIN_OPERATING_TEMP, 120);
                equipment.setAspect(EquipmentAspect.MAX_OPERATING_TEMP, 1200);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 150);          
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 150);          
                equipment.setAspect(EquipmentAspect.UNITS_PER_TIME, 1);          
                break;
            case INO_SLUURP:
                equipment.setAspect(EquipmentAspect.NAME, "<font color='#FF9900'>&lt;Ino SLUURP&gt;</font><br> Fluid Distillation Drone");
                equipment.setAspect(EquipmentAspect.WEIGHT, 2);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 1500);
                equipment.setAspect(EquipmentAspect.TYPE, EquipmentType.DRONE);
                equipment.setAspect(EquipmentAspect.MIN_OPERATING_TEMP, 180);
                equipment.setAspect(EquipmentAspect.MAX_OPERATING_TEMP, 300);
                equipment.setAspect(EquipmentAspect.CURRENT_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.MAX_DURABILITY, 100);          
                equipment.setAspect(EquipmentAspect.UNITS_PER_TIME, 1);          
                break;
            default:
                equipment.setAspect(EquipmentAspect.NAME, "Unset");
                equipment.setAspect(EquipmentAspect.WEIGHT, 1);
                equipment.setAspect(EquipmentAspect.CARGO_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.PASSENGER_CAPACITY, 0);
                equipment.setAspect(EquipmentAspect.HYPERJUMP_RANGE, 0);
                equipment.setAspect(EquipmentAspect.BASE_PRICE, 0);
                break;
        }
        
        equipment.setAspect(EquipmentAspect.KEY, what.name());
        
        return equipment;
    }
}
