package solarex.ship;

/**
 *
 * @author Hj. Malthaner
 */
public enum ShipType 
{
    COURIER("Speedman Courier", "Courier", 45, 14, 35000,
            "Being one of the smallest and fastest ships available,"
            + " the Speedman Courier is often used for quick passenger"
            + " and mail services."),
    
    SPACE_BUG("Space Beetle", "Tin Can", 65, 20, 50000, 
            "Small but reliable, the Space Beetle is usually the first ship" +
            " of any space prospector. It is cheap to purchase and" +
            " easy to repair, even with limited repair abilities or" +
            " materials." +
            " The ship can be controlled by" +
            " a single person, but it doesn't provide room or" +
            " life support for a second person unless a passenger cabin " +
            " is installed."),
    
    LOCUST_HARVESTER("Locust Harvester", "Harvester", 100, 40, 100000, 
            "The Locust Harvester is a quite common vehicle for both"
            + " traders and prospectors. Not too large, so it can dock at"
            + " most stations and starports without problems, and still with"
            + " a nice storage capacity, this is a fine upgrade from the"
            + " Space Bug"),
    
    DEEP_SPACE_CLIPPER("Deep Space Clipper", "Clipper", 200, 100, 1000000, 
            "Deep Space Clippers are long range exploration vehicles, "
            + " with big storage capacities. They aren't cheap, but the"
            + " materials are all from the best, made to last even under"
            + " heavy conditions. Every aspiring prospector dreams of"
            + " owning a deep space clipper some day.");
    
    public final int totalMass;
    public final int equipmentSpace;
    public final String shipName;
    public final String shipClass;
    public final String shipDesc;
    public final int price;
    
    private ShipType(String shipName, String shipClass, 
                     int totalWeight, int equipmentSpace, int price,
                     String shipDesc)
    {
        this.shipName = shipName;
        this.shipClass = shipClass;
        this.shipDesc = shipDesc;
        this.totalMass = totalWeight;
        this.equipmentSpace = equipmentSpace;
        this.price = price;
    }
    
    @Override
    public String toString()
    {
        return "<html><font color='#FF9900'>" + shipName + "</font></html>";
    }
}
