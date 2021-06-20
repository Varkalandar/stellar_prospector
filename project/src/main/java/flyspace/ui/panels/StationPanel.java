package flyspace.ui.panels;

import flyspace.AbstractMesh;
import flyspace.FlySpace;
import flyspace.Space;
import flyspace.ogl.GlPortraitPanel;
import flyspace.MultiMesh;
import flyspace.ogl32.GL32MeshFactory;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import static flyspace.ui.UiPanel.fillRect;
import java.net.URL;
import java.util.Random;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import solarex.galaxy.Galaxy;
import solarex.ship.Ship;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.system.Vec3;
import solarex.ui.ImageCache;
import solarex.util.RandomHelper;

/**
 *
 * @author hjm
 */
public class StationPanel extends DecoratedUiPanel
{
    private static final String welcomeTexts [] =
    {
        "Welcome to your last stop before nowhere. We have second hand " +
        "and repaired goods for excellent prices.",

        "Thank you for visiting. We have the best goods and services " +
        "within five lightyears, but quality has a price.",

        "You chose the right place to rest from your journey, traveller. " +
        "We have quality services for fair prices.",

        "Your eyes long for nature and something green after months in a " +
        "space ship? Visit our unique herbarium and feel well!",

        "Yawn ... oh, a visitor! At your service, just a minute, you " +
        "know it's rush hour, er week.",

        "Smuggler? Outlaw? Be warned, we do not tolerate lawbreakers over here. " +
        "Behave and you'll be welcome.",

        "This is the church of technogology! We have the newest and greatest " +
        "gadgets and everything else you want.",

        "Welcome to our abode in the sky. From low orbit you'll have " +
        "a great view over the planet.",

        "Damn technology ... where is this spare fuse? Hey! Oh, hello " +
        "traveller. Just feel home in our trusty metal jewel!",

        "Did you ever dream of walking on the ceiling? Then you are " +
        "just right here! At least until we have fixed the gravity generator.",

        "Hello stranger! Check our market deck for commodities to trade " +
        " and don't forget to visit our wideley known entertainment establishments.",
    };
    
    private static final String stationTexts [] =
    {
        "This station is more of a wreck than a place to stay. " +
        "Several decks have been left up, and even in the remaining space " +
        "electricity and artifical gravity suffer from freqent dropouts. " +
        "The air is thin and smells bad. " +
        "Chewing gum, band-aid and plugs seem to be common methods to " +
        "repair small breaches in the hull, larger plates of scrap metal welded " +
        "over bigger holes and cracks almost look professional in comparison.",

        "While the staff appears friendly and helpful, " +
        "this space station has definitely seen better days. " +
        "Corroded metal, puddles of oil in places, and opened wall covers " +
        "showing the dated electric wiring make you wonder how long this " +
        "tin can will stay air tight.\nMaybe it is better to keep the pressure " +
        "suit on all the time when leaving the ship, just in case." +
        "",
        
        "" +
        "An old but well maintained space station. Nothing special to " +
        "mention about this one, everything seems to be in place and " +
        "working as it should. Maybe a lack of comfort can be complained " +
        "about, but most space travelers have seen much worse." +
        "",

        "" +
        "Not the most modern station, but a well maintained one. " +
        "This should be a good place to stay for a while and rest from " +
        "a longer journey. They offer a nice lounge, a herbarium and even a " +
        "sauna for visitors. Not to mention two dark bars and a restaurant " +
        "which serves actually edible food." +
        "",

        "" +
        "A very modern space station. Polished metal, fresh paint " +
        "and the well known smell of new electronics mark this place." +
        "The staff is high nosed, well knowing how special their station " +
        "is, compared to the average station of the outer regions." +
        "",

        "" +
        "Run by a robotic civilization, this station is all open to space. " +
        "No atmosphere or shielding provided, but the usual commodities for " +
        "biological lifeforms like water and food are sold.\n" +
        "Service here is prompt and reliable, but you must obey their laws " +
        "absolutely, or you'll be recycled." +
        "",
    };

    private static final String spaceportTexts [] =
    {
        "" +
                "" +
                "Just an ordinary spaceport. A tower with somewhat up to " +
                "date equipment, a landing field without too many potholes, " +
                "some hangars to the side of the landing area " +
                "along with a few smaller " +
                "buildings for registration, import/export licenses and other " +
                "legal neccessities." +
                "Things look mostly clean and well maintained here. Security staff " +
                "can be seen now and then, and it seems to be a fairly safe place." +
                "" +
        "",

        "" +
                "" +
                "Dug into the ground under the landing field, the " +
                "facilities of this space port are safe from radiation " +
                "and occasional meteor hits. But having just artificial light " +
                "and metal plated walls " +
                "makes the stay here not much better than on board of " +
                "a space ship." +
                "" +
        "",

        "" +
                "" +
                "This space port sprawls widely over a large open plain. " +
                "Ground prices don't seem to be an issue here, underground " +
                "construction has been avoided and most buildings are just one " +
                "story tall. Which makes you wonder if this is a secret base " +
                "of a yet unseen gastropod race, who like things all in one flat space." +
                "" +
        "",

        "" +
                "" +
                "Twin towers? No, a full tower family! This spaceport must have " +
                "been designed by a tower fanatic. Well, also a basement fanatic," +
                "since most building seems to stretch as deep into the planets crust" +
                "as they tower above. And, antennas! Lots of antennas. " +
                "Did I mention antennans yet? " +
                "Each and every place here is plastered with antennas of all shapes and" +
                " sizes. " +
                "A civilzation of spies? A deep space phone company? SETI 2061? " +
                "Well, at least you can send hypergrams to almost all known " +
                "places from here in no time!" +
                "" +
        "",

        "" +
                "" +
                "This space port is the public part of a military fortification. " +
                "In a distance giant cannons and force field projectors are looming " +
                "in the sky, " +
                "some suspicios hills farther in the fields might well be disguised" +
                "hangars or bunkers. Vibrations of the ground tell of huge underground " +
                "power stations to feed the war machines." +
                "At least one can feel safe here, even a mid size alien invasion will " +
                "be stopped, at least until you ran up your ship and rocketed off " +
                "into space." +
                "" +
        "",

        "" +
                "" +
                "The crater marked landing field already gave a first impression, " +
                "but the buildings just confirm: This spaceport has seen much better " +
                "times. At least the age of the complex tells that it won't crumble in " +
                "the next few hours, some of the buildings and machines look rather ancient " +
                "and still do their jobs." +
                "" +
        "",

        "" +
                "" +
                "The inhabitants here are of noticeable smaller size than the average " +
                "human. Door frames have become dangerous passages, and in some rooms it's " +
                "even difficult to stand upright. But at least it's on a planet, with safe ground. " +
                "And the bar nearby is just terrific." +
        "",

        "" +
                "" +
                "Clonknik space ports are always an experience. Covers, shieldings, " +
                "and such are completely unknown to this robotic civilization. " +
                "Open high power conductors, microwave tunnels, " +
                "plasma fields and all kind of dangerous looking " +
                "machinery are placed just where they fit, " +
                "connected by metal girders, " +
                "cables and wires, towering into the sky. " +
                "Energy flares blind the eye, " +
                "radiation bursts flash the scenery. " +
                "The Clonkniks might know where it's safe, but for a human " +
                "this is a labyrinth of death traps. Luckily the Clonkniks offer " +
                "guide bots to lead visitors through the machine and energy jungle." +
                "" +
        "",
    };

    private static final String spaceportFloatingTexts [] =
    {
        "" +
                "" +
                "Build on a giant ice floe, floating on the surface of denser " +
                "layers of the atmosphere, this space port definitely is a sight " +
                "when arriving from space. Once landed, it is much like any other " +
                "spaceport, though, a big, fiberconcrete covered landing field, " +
                "a control tower and some additional buildings. A settlement " +
                "of traders, miners and scientists was founded nearby. " +
                "There are rumors that in the deeper layers of the planets atmosphere, " +
                "big riches can be found." +
                "" +
        "",
    };
    
    private Random rng;
    private Galaxy galaxy;
    private Solar station;
    private final FlySpace game;
    private final Ship ship;

    private String majorityLabel;
    private String governmentLabel;
    private String impressionLabel;
    private String advertisementLabel;
    private String techLabel;
    private String inhabitantsLabel;
    private String welcomeLabel;
    
    private final GlPortraitPanel portraitPanel;
    private final ImageCache imageCache;
    private final DecoratedTrigger launchTrigger;    
    private final DecoratedTrigger goodsExchangeTrigger;    
    private final DecoratedTrigger bulletinBoardTrigger;    
    private final DecoratedTrigger equipmentShopTrigger;    
    private boolean clicked;
    
    private final GoodsExchangePanel goodsExchangePanel;
    private final DecoratedTrigger prospectorsTrigger;
    private final ProspectorsAgencyPanel prospectorsPanel;
    private final BulletinBoardPanel bulletinBoardPanel;
    private final EquipmentShopPanel equipmentShopPanel;
    
    private Space space;
    
    private final QuestDialog questDialog;
    private boolean runQuestTest;
    
    
    public StationPanel(FlySpace game, Galaxy galaxy, Ship ship, ImageCache imageCache)
    {
        this.game = game;
        this.galaxy = galaxy;
        this.ship = ship;
        this.imageCache = imageCache;
        this.portraitPanel = new GlPortraitPanel();
        this.goodsExchangePanel = new GoodsExchangePanel(game, ship);
        this.prospectorsPanel = new ProspectorsAgencyPanel(game, ship);
        this.bulletinBoardPanel = new BulletinBoardPanel(game, galaxy, ship);
        this.equipmentShopPanel = new EquipmentShopPanel(game, ship);
        this.questDialog = new QuestDialog();
        
        DecoratedTrigger trigger;
        
        int yoff = 380;
        int left = 50;
        int bw = 300;
        
        prospectorsTrigger = new DecoratedTrigger(Fonts.g17, "Prospectors Agency", 0x3377FF00, 0xFF66EE00);
        prospectorsTrigger.setArea(left, yoff, bw, 32);
        addTrigger(prospectorsTrigger);
        yoff -= 40;
        
        goodsExchangeTrigger = new DecoratedTrigger(Fonts.g17, "Goods Exchange", 0x3377FF00, 0xFF66EE00);
        goodsExchangeTrigger.setArea(left, yoff, bw, 32);
        addTrigger(goodsExchangeTrigger);
        yoff -= 40;

        bulletinBoardTrigger = new DecoratedTrigger(Fonts.g17, "Bulletin Board", 0x3377FF00, 0xFF66EE00);
        bulletinBoardTrigger.setArea(left, yoff, bw, 32);
        addTrigger(bulletinBoardTrigger);
        yoff -= 40;

        equipmentShopTrigger = new DecoratedTrigger(Fonts.g17, "Equipment Shop", 0x3377FF00, 0xFF66EE00);
        equipmentShopTrigger.setArea(left, yoff, bw, 32);
        addTrigger(equipmentShopTrigger);
        yoff -= 40;

        trigger = new DecoratedTrigger(Fonts.g17, "Shipyard", 0x3377FF00, 0xFF66EE00);
        trigger.setArea(left, yoff, bw, 32);
        trigger.setEnabled(false);
        addTrigger(trigger);

    
        launchTrigger = new DecoratedTrigger(Fonts.g17, "Launch", 0x33FF7700, 0xFFEE6600);
        launchTrigger.setArea(620, yoff-16, bw, 32);
        addTrigger(launchTrigger);
    
    }
    
    @Override
    public void activate()
    {
        setupTextPanel(width, height);
        
        runQuestTest = true;
    }
    
    @Override
    public void handleInput()
    {
        if(runQuestTest)
        {
            ship.player.testQuests(galaxy, station, ship, questDialog);
            runQuestTest = false;
        }
        
        if(questDialog.isVisible())
        {
            questDialog.handleInput();
            return;
        }
        
        Trigger trigger = trigger(Mouse.getX(), Mouse.getY());
        
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        else
        {
            if(clicked)
            {
                if(trigger == launchTrigger)
                {
                    launch();
                } 
                else if(trigger == goodsExchangeTrigger)
                {
                    game.activatePanel(goodsExchangePanel);
                }
                else if(trigger == prospectorsTrigger)
                {
                    game.activatePanel(prospectorsPanel);
                }
                else if(trigger == bulletinBoardTrigger)
                {
                    bulletinBoardPanel.setStation(station);
                    game.activatePanel(bulletinBoardPanel);
                }
                else if(trigger == equipmentShopTrigger)
                {
                    equipmentShopPanel.setStation(station);
                    game.activatePanel(equipmentShopPanel);
                }

                clicked = false;
            }
        }
    }

    @Override
    public void display()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        displayBackground(0xFF111122, 0xFF111133, 0xFF222244);
        
        // PixFont font = Fonts.g12;
        PixFont font = Fonts.g17;
        
        int left = 50;

        displayTitle(welcomeLabel);
        
        font.drawText(advertisementLabel, Colors.TEXT, 500, 620, 300, 1.0f);
        
        font.drawText(impressionLabel, Colors.TEXT, 500, 475, 580, 1.0f);
    
        font.drawStringBold("Majority: ", Colors.LABEL, left, 580, 1.0f);
        font.drawStringBold("Government: ", Colors.LABEL, left, 550, 1.0f);
        font.drawStringBold("Population: ", Colors.LABEL, left, 520, 1.0f);
        font.drawStringBold("Technology: ", Colors.LABEL, left, 490, 1.0f);

        font.drawStringScaled(majorityLabel, Colors.FIELD, left+180, 580, 1.0f);
        font.drawStringScaled(governmentLabel, Colors.FIELD, left+180, 550, 1.0f);
        font.drawStringScaled(inhabitantsLabel, Colors.FIELD, left+180, 520, 1.0f);
        font.drawStringScaled(techLabel, Colors.FIELD, left+180, 490, 1.0f);

        font.drawStringBold("Our Services: ", Colors.FIELD, left, 440, 1.0f);
        
        fillRect(850, 520, 130, 140, 0xFF112288);
        fillRect(850+3, 520+3, 130-6, 140-6, 0xFF222222);
        portraitPanel.display(860, 530, 110, 120);
        
        displayTriggers();
        
        if(questDialog.isVisible())
        {
            questDialog.display(width, height);
        }
    }

    public void setStation(Solar station, Space space)
    {
        this.station = station;
        this.space = space;
        
        goodsExchangePanel.setStation(station);
        prospectorsPanel.setStation(station);
        
        rng = RandomHelper.createRNG(station.seed +
                                     (long)(station.radius*1000) +
                                     (long)(station.orbit) +
                                     station.eet +
                                     (station.name.hashCode() << 16));

        if(ship.getState() == Ship.State.DOCKED &&
           ship.spaceBodySeed == station.seed)
        {
            welcomeLabel = "Welcome to " + station.name + "!";
        }
        else
        {
            welcomeLabel = "Radio link to " + station.name + " was established ...";
        }
        
        inhabitantsLabel = "" + station.society.population;
        techLabel = "" + station.society.techLevel;

        advertisementLabel = welcomeTexts[(int)(rng.nextDouble() * welcomeTexts.length)];

        if(station.btype == Solar.BodyType.SPACEPORT)
        {
            if(station.society.race == Society.Race.Clonkniks)
            {
                impressionLabel = spaceportTexts[spaceportTexts.length-1];
            }
            else
            {

                if(station.getParent() != null &&
                   (station.getParent().ptype == Solar.PlanetType.BIG_GAS ||
                    station.getParent().ptype == Solar.PlanetType.RINGS))
                {
                    impressionLabel = spaceportFloatingTexts[(int)(rng.nextDouble() * spaceportFloatingTexts.length)];
                }
                else
                {
                    impressionLabel = spaceportTexts[(int)(rng.nextDouble() * spaceportTexts.length-1)];
                }
            }

        } 
        else
        {
            if(station.society.race == Society.Race.Clonkniks) 
            {
                impressionLabel = stationTexts[stationTexts.length-1];
            }
            else
            {
                impressionLabel = stationTexts[(int)(rng.nextDouble() * stationTexts.length-1)];
            }
        }

        governmentLabel = station.society.governmentType.toString();

        majorityLabel = station.society.race.toString();

        portraitPanel.setStation(station, rng, imageCache);
        
    }

    private void launch() 
    {
        Vec3 bodyPos, shipPos;
    
        if(station.btype == Solar.BodyType.SPACEPORT)
        {
            bodyPos = station.getAbsolutePosition();
            bodyPos.scale(Space.DISPLAY_SCALE);
            
            shipPos = new Vec3(bodyPos);
            shipPos.z -= station.orbit * 3 * Space.DISPLAY_SCALE;
            
            ship.destination.x = ship.pos.x;
            ship.destination.y = ship.pos.y;
            ship.destination.z = ship.pos.z;
            
            // testing
            /*
            try
            {
                URL url = getClass().getResource("/flyspace/resources/3d/ship.obj");        
                AbstractMesh mesh = GL32MeshFactory.createMesh(url);

                MultiMesh multiMesh = new MultiMesh(mesh);
                multiMesh.setPos(new Vec3(ship.pos.x, ship.pos.y, ship.pos.z-400));

                space.add(multiMesh);
            } catch(Exception e) { e.printStackTrace(); }
            */
        }
        else
        {
            MultiMesh stationMesh = space.findMeshForPeer(station);
            bodyPos = stationMesh.getPos();
            shipPos = new Vec3(bodyPos);
            shipPos.z += 10;
        }
        
        ship.pos.set(shipPos);
        ship.depart();
        game.showSpacePanel(bodyPos);
    }

    
}
