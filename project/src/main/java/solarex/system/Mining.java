package solarex.system;

import java.util.ArrayList;
import solarex.ship.components.ShipComponent;

/**
 * Container and processor for all ongoing mining operations.
 * @author hjm
 */
public class Mining 
{
    
    private final ArrayList <Operation> operations;
    
    
    public Mining()
    {
        this.operations = new ArrayList<>(1024);
    }
    
    
    public void startOperation(Operation operation)
    {
        operations.add(operation);
    }
            

    /**
     * Called before a frame is displayed. All updates to game data should
     * happen here.
     * @param dt Time passed since last update call
     */
    public void update(int dt)
    {
        for(Operation operation : operations)
        {
            ShipComponent drone = operation.drone;
            
            // time is in ms, so this should be 1/1000 of a unit?
            int amount = drone.getUnitsPerTime() * dt;
            operation.amount += amount;
        }
    }

    
    public static class Operation
    {
        public ShipComponent drone;
        public PlanetResources.Fluids fluid;
        public PlanetResources.Gases gas;
        public PlanetResources.Metals metal;
        public PlanetResources.Minerals minaral;
        
        public int amount;
    }
}
