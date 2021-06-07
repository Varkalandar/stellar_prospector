/*
 * Good.java
 *
 * Created on 03.02.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ship;

/**
 * Goods in Solarex, static list of good types, instanced describe a
 * certain amount of one type.
 * 
 * @author Hj. Malthaner
 */
public class Good 
{
    public enum Type
    {
        Hydrogen("Hydrogen", 
                "The lightest element, useful as a resource in many chemical and physical processes."
                + " It's needed to make low and high catalyst fusion fuel for the ship drives.",
                "#99aaff", 3, 100),
        NobleMetals("Noble metals", 
                    "Noble metals are resistant to corrosion and oxidation in most atmospheres, unlike many other metals."
                +   " They are expensive due to their rarity, and needed for machines and electronics as well as jewelry and ornamental purposes."
                +   " Noble metals include ruthenium, rhodium, palladium, silver, osmium, iridium, platinum, and gold.",
                    "#ffff77", 100, 10),
        IronMetals("Iron alloys", 
                   "Pure iron and mixtures of iron and other metals. Mainly used in construction and machine construction.",
                   "#ddccbb", 10, 100),
        NonIronMetals("Non-ferrous metals", 
                "This trade category includes any metal that is too heavy to be a light metal and not ferrous, including alloys, that does not contain iron"
                + " in appreciable amounts. Generally more expensive than ferrous metals, non-ferrous metals are used because"
                + " of desirable properties.",
                "#ffccbb", 20, 100),
        LightMetals("Light metals",  
                "Light metals are metals of low atomic weight. The cut-off between light metals and heavy metals varies."
                + " Light metals include lithium, beryllium, sodium, magnesium and aluminium.",
                "#ffffff", 20, 100),
        HeavyMetals("Heavy metals", 
                "A trade category of metals which are neither in the groups of light metals, iron alloys nor non-iron metals."
                + " Most of these metals have a high desnsity and most are toxic to some extend. This group "
                + "includes mainly the transition metals, some metalloids, lanthanides, and actinides.",
                "#cccccc", 12, 100),
        Radionuclides("Radionuclides", 
                "Radionuclides are atoms with an unstable nucleus, characterized by excess energy available to be imparted"
                + " either to a newly created radiation particle within the nucleus or via internal conversion."
                + " Radionuclides are mostly used for their chemical properties and as sources of radiation.",
                "#ddccbb", 80, 1),
        Transurans("Stable transuraniums",
                "These are a variety of very rare and exotic elements. They have a much bigger atomic weight than uranium "
                + "and are still stable substances."
                + "These may be among the most rare substances in the known universe.",
                "#ff99cc", 5000, 1),
        RareEarths("Rare earths", "#ffccee", 200, 100),
        CeramicMinerals("Ceramic minerals", "#eedddd", 2, 200),
        IndustrialCeramics("Industrial ceramics", "#ffeeee", 10, 200),
        IndustrialFibers("Industrial fibers", "#999999", 15, 100),
        Nanoparticles("Nanoparticles", "#BB99FF", 120, 10),
        MetalCompounds("Foam/fiber metals", 
                "Fiber fortified light weight metal foam parts. These make some"
                + " of the best armors against impact based weapons as well as"
                + " very stable construction parts."
                + " They even can withstand open fire for a while, because they conduct"
                + " heat only badly. Foam metal and heat resistent fibers need a high"
                + " manufacturing level to be produced, but are not particularly high"
                + " tech actually.",
                "#eeeeff", 35, 100),
        Plastics("Plastic compounds", 
                "Plastics compounds consist of a wide range of synthetic or semi-synthetic organic solids"
                + " that are moldable and mixed with other materials. The bases are typically organic polymers"
                + " of high molecular mass,"
                + " but they often contain other substances. They are usually synthetic.",
                "#997799", 22, 100),
        UltraPlastics("Ultraplasts", "#8877BB", 60, 100),
        
        Graphite("Graphite", 
                "Graphite is mostly consumed for refractories, batteries, steelmaking,"
                + " foundry facings and lubricants. Graphene, which occurs naturally in"
                + " graphite, has unique physical properties and is one of the"
                + " strongest substances known.",
                "#cccccc", 10, 200),
                
        Hydrocarbons("Hydrocarbons",
                "Hydrocarbons are compounds consisting mostly of hydrogen and carbon."
                + "This category includes a variety of fluid, ductile and solid materials,"
                + " which are found on an number of planet types.",
                "#bbbbaa", 9, 200),
        Silicones("Silicones", 
                "Silicones are typically heat-resistant and rubber-like. They"
                + " are used in sealants, adhesives, lubricants, medical"
                + " applications, cookware, and insulation. Silicones are"
                + " polymers that include silicon together with carbon,"
                + " hydrogen, oxygen, and sometimes other elements. Common"
                + " forms include silicone oil, silicone grease and silicone rubber.",
                "#ddeeff", 12, 200),
        Fertilizer("Fertilizer", 
                "A fertilizer is any material of natural or synthetic origin"
                + " that is applied to soils or to plant tissues to supply "
                + "one or more plant nutrients essential to the growth of plants.",
                "#ddee99", 3, 200),
        Medicine("Medical drugs", "#bbff88", 300, 10),
        Narcotics("Narcotics", "#ff8888", 300, 1),
        AtmoGases("Common atm. gases", "#ccffbb", 2, 100),
        InertGases("Inert gases", 
                "Inert gases don't undergo chemical reactions under a set of given conditions. This group"
                + " includes the noble gases and nitrogen which do not react with many substances under "
                + "most conditions. The ones who float like atmospheres with a high amount of"
                + " inert gases.", 
                "#ffffcc", 17, 100),
        Water("Water",
                "Between about 274 K and 374 K water is a clear fluid. It is"
                + " important to most live forms on earthlike planets, but very"
                + " dangerous for rockeaters since it dissolves their molecular"
                + " structures. Clonkniks consider water a dangerous substance, too.",
                "#bbddff", 3, 200),
        
        FoodRockeater("Rockeater food", "#778899", 2, 10),
        FoodTerranens("Terranean food", "#77DD99", 2, 10),
        FoodPoison("Poisonbreather food", "#BBBB99", 2, 10),
        
        MachineParts("Machine parts",
                "Simple mechanical and electrical components for low tech machines."
                + " These can be manufactured in most places.",
                "#eeeeee", 70, 100),
        Electronics("Electronic parts",
                "These are vacuum tube and semiconductor based elements and"
                + " compounds. Vacuum tubes are bigger and consume more power,"
                + " but there a few applications where they are still superior.",
                "#FFDD88", 190, 10),
        Crystalics("Crystalics parts", 
                "Superfast crystal based control and signaling structures."
                + " More potent than electronics, they depend on exotic crystals"
                + " and rare earths, which are often very expensive if available"
                + " at all.",
                "#66FF88", 250, 10),
        Hypertech("Warptronic parts", 
                "Warptronics are almost magic in their ability to process signals"
                + " at faster than light speeds. Inside these parts areas are"
                + " completely shielded from this universes influences so that"
                + " out-of-universe laws begin to apply to the reactions there."
                + " By clever tuning of the shielding these parts operate in a "
                + " sub space where speed of light is much higher than in our"
                + " universe. There are rumors about warptronic devices which"
                + " can detect fired laser beams before the beam's photons"
                + " actually reach the target."
                + " It is believed that the clonkniks are in possession of such"
                + " technology.",
                "#DD88DD", 500, 100),
        Biotech("Biotech components",
                "Biotech or biotechnology encompasses a wide range of procedures"
                + " for modifying living organisms."
                + " In general, biotech includes any technological application that uses biological"
                + " systems, living organisms, or derivatives thereof, to make or modify products"
                + " or processes for specific uses. It also includes technical systems which are made"
                + " to enhance the abilities of living beings.",
                "#ddbb88", 220, 100),
        Robots("Robots", 
                "Robots are artificial agents, usually electro-mechanical"
                + " machines that are guided by computer programs or"
                + " electronic circuitry. Robots can be autonomous,"
                + " semi-autonomous or remotely controlled. They range"
                + " from humanoid forms to nano robots, 'swarm' robots,"
                + " and industrial robots.",
                "#ddddff", 200, 150),
        Androids("Androids",
                "Androids are robotic or synthetic organisms designed to look"
                + " and act like a human, with a body having a"
                + " flesh-like resemblance. Although 'android' is used almost"
                + " universally to refer to both sexes, and those of no"
                + " particular sex, 'android' ('andro-', meaning masculine)"
                + " technically refers to the male form, while 'gynoid'"
                + " ('gyno-', meaning feminine) is the female form.",
                "#ffddaa", 200, 75),
        ArtificialIntelligence("Artificial intelligences", "#aaccff", 280, 1),
        Slaves("Slaves", "#cc9977", 320, 70),
        SpecialGoods("Special goods", "#3399FF", 999, 100);

        private String name;
        public final String color;
        public final String description;
        
        public final int price;
        public final int massPerUnit;
        
        Type(String name, String color, int price, int massPerUnit)
        {
            this.name = name;
            this.description = "";
            this.color = color;
            this.price = price;
            this.massPerUnit = massPerUnit;
        }

        Type(String name, String description, String color, int price, int massPerUnit)
        {
            this.name = name;
            this.description = description;
            this.color = color;
            this.price = price;
            this.massPerUnit = massPerUnit;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    /**
     * This many units are stored
     */
    public int units;

    /**
     * Average price this good was bought for
     */
    public double averagePrice;

    public double salesPrice;
    
    public Type type;
    
}
