package solarex.system;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hj. Malthaner
 */
public class Biosphere 
{
    public static enum Lifeforms
    {
        REPLICATORS("Replicating Molecules"),
        PRIMITIVE_CELLS("Primitive Cells"),
        ADVANCED_CELLS("Advanced Cells"),
        MULTI_CELL("Multi Cell Organisms"),
        SIMPLE_LIFEFORMS("Simple Structured Lifeforms"),
        COMPLEX_LIFEFORMS("Complex Structured Lifeforms"),
        SIMPLE_CRYSTAL("Self Organizing Crystal Structures"),
        REPLICATING_CRYSTAL("Replicating Crystal Structures"),
        REPLICATING_GAS_SWIRLS("Replicating Gas Swirls"),
        INTELLIGENT("Mindful Lifeforms");
        
        public static int LAST_CELL = 5;
        public static int LAST_CRYSTAL = 7;
        
        private final String name;

        private Lifeforms(String name) 
        {
            this.name = name;
        }
        
        @Override
        public String toString()
        {
            return name;
        }
    };
    
    public static enum EnergySources
    {
        CHEMICAL("Chemical"),
        INFRARED_ABSORBER("Infrared Radiation"),
        OPTICAL_ABSORBER("Visible Light"),
        UV_ABSORBER("Ultraviolett Light"),
        XRAY_ABSORBER("X-Ray"),
        RADIOACTIVE("Radioactive Elements"),
        UNKNOWN("Unknown Sources")
        ;
        
        private final String name;

        private EnergySources(String name) 
        {
            this.name = name;
        }
        
        @Override
        public String toString()
        {
            return name;
        }
    }


    public final List<Lifeforms> lifeforms = new ArrayList <Lifeforms> ();
    public final List<EnergySources> energySources = new ArrayList <EnergySources> ();
    

}
