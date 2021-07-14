package flyspace;

import solarex.ship.Ship;
import solarex.system.Vec3;

/**
 *
 * @author Hj. Malthaner
 */
public class Autopilot 
{
    public boolean arrived;
    public String speedString;
    public double currentSpeed;
    private final double accel;

    public Autopilot(double accel)
    {
        this.accel = accel;
        arrived = false;
        speedString = "Autopilot activated";
    }
    
    public void drive(View view, Ship ship)
    {
        double dx = ship.destination.x - ship.pos.x;
        double dy = ship.destination.y - ship.pos.y;
        double dz = ship.destination.z - ship.pos.z;

        double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);


        if(dist > 0) 
        {
            // Hajo: Normalize vector
            dx /= dist;
            dy /= dist;
            dz /= dist;

            Vec3 front = new Vec3((float)-dx, (float)-dy, (float)-dz);
            Vec3 up = new Vec3(0, 1, 0); // Hajo: pray ...
            Vec3 right = Vec3.cross(front, up, null);
            
            view.orient(front, right);
            
            // Hajo: find breaking distance
            final double breakingDistance = 
                    (currentSpeed * currentSpeed) / (2*accel);
            
            // Hajo: Do we need to break yet?
            // We add a bit of a safety margin for soft approaches.
            if(breakingDistance*1.4 < dist) 
            {
                currentSpeed += accel;
                speedString = "" + (currentSpeed/10.0) + " (accelerating)";
            }
            else if(breakingDistance*1.2 > dist)
            {
                currentSpeed -= accel;                
                speedString = "" + (currentSpeed/10.0) + " (decelerating)";
            }
            else 
            {
                speedString = "" + (currentSpeed/10.0) + " (floating)";
            }

            // Hajo: min. speed to be sure we arrive
            if(currentSpeed < 1000) 
            {
                currentSpeed = 1000;
                speedString = "" + (currentSpeed/10.0) + " (approaching)";
            }

            ship.pos.x += dx*currentSpeed;
            ship.pos.y += dy*currentSpeed;
            ship.pos.z += dz*currentSpeed;

            // Hajo: check if we reached the destination
            double dxLeft = ship.destination.x - ship.pos.x;
            double dyLeft = ship.destination.y - ship.pos.y;
            double dzLeft = ship.destination.z - ship.pos.z;

            if(Math.abs(dxLeft) < currentSpeed &&
               Math.abs(dyLeft) < currentSpeed &&
               Math.abs(dzLeft) < currentSpeed)
            {

                // Hajo: clean up remaining distance
                ship.pos.x = ship.destination.x;
                ship.pos.y = ship.destination.y;
                ship.pos.z = ship.destination.z;
                
                currentSpeed = 0.0;
                speedString = "" + currentSpeed + " (arrived)";
                
                arrived = true;
            }
        }
    }
}
