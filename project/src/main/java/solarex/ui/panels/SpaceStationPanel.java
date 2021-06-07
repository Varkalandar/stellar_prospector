/*
 * SpaceStationPanel.java
 *
 * Created on 16.01.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import java.awt.Container;
import java.util.Random;
import javax.swing.JComponent;
import solarex.evolution.World;
import solarex.galaxy.Galaxy;
import solarex.ship.Ship;
import solarex.system.Society;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.components.PortraitPanel;
import solarex.ui.ComponentFactory;
import solarex.ui.FontFactory;
import solarex.util.RandomHelper;

/**
 * Space station general information panel.
 * 
 * @author Hj. Malthaner
 */
public class SpaceStationPanel extends javax.swing.JPanel 
{
    private Random rng;
    private Solar station;
    private final Galaxy galaxy;
    private final World world;

    /**
     * The players ship.
     */
    private final Ship ship;

    private final ImageCache imageCache;

    private static final String welcomeTexts [] =
    {
        "<html>Welcome to your last stop before nowhere. We have second hand " +
        "and repaired goods for excellent prices.</html>",

        "<html>Thank you for visiting. We have the best goods and services " +
        "within five lightyears, but quality has a price.</html>",

        "<html>You chose the right place to rest from your journey, traveller. " +
        "We have quality services for fair prices.</html>",

        "<html>Your eyes long for nature and something green after months in a " +
        "space ship? Visit our unique herbarium and feel well!</html>",

        "<html>Yawn ... oh, a visitor! At your service, just a minute, you " +
        "know it's rush hour, er week.</html>",

        "<html>Smuggler? Outlaw? Be warned, we do not tolerate lawbreakers over here. " +
        "Behave and you'll be welcome.</html>",

        "<html>This is the church of technogology! We have the newest and greatest " +
        "gadgets and everything else you want.</html>",

        "<html>Welcome to our abode in the sky. From low orbit you'll have " +
        "a great view over the planet.</html>",

        "<html>Damn technology ... where is this spare fuse? Hey! Oh, hello " +
        "traveller. Just feel home in our trusty metal jewel!</html>",

        "<html>Did you ever dream of walking on the ceiling? Then you are " +
        "just right here! At least until we have fixed the gravity generator.</html>",

        "<html>Hello stranger! Check our market deck for commodities to trade " +
        " and don't forget to visit our wideley known entertainment establishments.</html>",
    };
    
    private static final String stationTexts [] =
    {
        "<html>" +
        "<p>This station is more of a wreck than a place to stay. " +
        "Several decks have been left up, and even in the remaining space " +
        "electricity and artifial gravity suffer from freqent dropouts. " +
        "The air is thin and smells bad. " +
        "Chewing gum, band-aid and plugs seem to be common methods to " +
        "repair small breaches in the hull, larger plates of scrap metal welded " +
        "over bigger holes and cracks almost look professional in comparison.</p>" +
        "</html>",

        "<html><p>While the staff appears friendly and helpful, " +
        "this space station has definitely seen better days. " +
        "Corroded metal, puddles of oil in places, and opened wall covers " +
        "showing the dated electric wiring make you wonder how long this " +
        "tin can will stay air tight.</p><br><p>Maybe it is better to keep the pressure " +
        "suit on all the time when leaving the ship, just in case.</p>" +
        "</html>",
        
        "<html>" +
        "<p>An old but well maintained space station. Nothing special to " +
        "mention about this one, everything seems to be in place and " +
        "working as it should. Maybe a lack of comfort can be complained " +
        "about, but most space travelers have seen much worse.</p>" +
        "</html>",

        "<html>" +
        "<p>Not the most modern station, but a well maintained one. " +
        "This should be a good place to stay for a while and rest from " +
        "a longer journey. They offer a nice lounge, a herbarium and even a " +
        "sauna for visitors. Not to mention two dark bars and a restaurant " +
        "which serves actually edible food.</p>" +
        "</html>",

        "<html>" +
        "<p>A very modern space station. Polished metal, fresh paint " +
        "and the well known smell of new electronics mark this place." +
        "The staff is high nosed, well knowing how special their station " +
        "is, compared to the average station of the outer regions.</p>" +
        "</html>",

        "<html>" +
        "<p>Run by a robotic civilization, this station is all open to space. " +
        "No atmosphere or shielding provided, but the usual commodities for " +
        "biological lifeforms like water and food are sold.</p><br>" +
        "<p>Service here is prompt and reliable, but you must obey their laws " +
        "absolutely, or you'll be recycled.</p>" +
        "</html>",
    };

    private static final String spaceportTexts [] =
    {
        "<html>" +
                "<p>" +
                "Just an ordinary spaceport. A tower with somewhat up to " +
                "date equipment, a landing field without too many potholes, " +
                "some hangars to the side of the landing area " +
                "along with a few smaller " +
                "buildings for registration, import/export licenses and other " +
                "legal neccessities." +
                "Things look mostly clean and well maintained here. Security staff " +
                "can be seen now and then, and it seems to be a fairly safe place." +
                "</p>" +
        "</html>",

        "<html>" +
                "<p>" +
                "Dug into the ground under the landing field, the " +
                "facilities of this space port are safe from radiation " +
                "and occasional meteor hits. But having just artificial light " +
                "and metal plated walls " +
                "makes the stay here not much better than on board of " +
                "a space ship." +
                "</p>" +
        "</html>",

        "<html>" +
                "<p>" +
                "This space port sprawls widely over a large open plain. " +
                "Ground prices don't seem to be an issue here, underground " +
                "construction has been avoided and most buildings are just one " +
                "story tall. Which makes you wonder if this secretly is a base " +
                "of a yet unseen gastropod race, who like things all in one flat space." +
                "</p>" +
        "</html>",

        "<html>" +
                "<p>" +
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
                "</p>" +
        "</html>",

        "<html>" +
                "<p>" +
                "This space port is the public part of a military fortification. " +
                "In a distance giant cannons and force field projectors are looming " +
                "in the sky, " +
                "some suspicios hills farther in the fields might well be disguised" +
                "hangars or bunkers. Vibrations of the ground tell of huge underground " +
                "power stations to feed the war machines." +
                "At least one can feel safe here, even a mid size alien invasion will " +
                "be stopped, at least until you ran up your ship and rocketed off " +
                "into space." +
                "</p>" +
        "</html>",

        "<html>" +
                "<p>" +
                "The crater marked landing field already gave a first impression, " +
                "but the buildings just confirm: This spaceport has seen much better " +
                "times. At least the age of the complex tells that it won't crumble in " +
                "the next few hours, some of the buildings and machines look rather ancient " +
                "and still do their jobs." +
                "</p>" +
        "</html>",

        "<html>" +
                "<p>" +
                "The inhabitants here are of noticeable smaller size than the average " +
                "human. Door frames have become dangerous passages, and in some rooms it's " +
                "even difficult to stand upright. But at least it's on a planet, with safe ground. " +
                "And the bar nearby is just terrific.</p>" +
        "</html>",

        "<html>" +
                "<p>" +
                "Clonknik space ports are always an experience. Machine covers, shieldings, " +
                "walls and such are completely unknown to this robotic civilization. " +
                "Open high power conductors, microwave tunnels, " +
                "plasma fields and all kind of dangerous looking " +
                "machinery are placed where they seemed to fit, " +
                "connected by metal girders, " +
                "cables and wires, towering into the sky. " +
                "Energy flares blind the eyes, " +
                "radiation bursts flash the scenery. " +
                "The Clonknik might know where it's safe to be, but for a human " +
                "this is a labyrinth of death traps. Luckily the Clonknik offer " +
                "guide bots to lead visitors through the machine and energy jungle." +
                "</p>" +
        "</html>",
    };

    private static final String spaceportFloatingTexts [] =
    {
        "<html>" +
                "<p>" +
                "Build on a giant ice floe, floating on the surface of denser " +
                "layers of the atmosphere, this space port definitely is a sight " +
                "when arriving from space. Once landed, it is much like any other " +
                "spaceport, though, a big, fiberconcrete covered landing field, " +
                "a control tower and some additional buildings. A settlement " +
                "of traders, miners and scientists was founded nearby. " +
                "There are rumors that in the deeper layers of the planets atmosphere, " +
                "big riches can be found." +
                "</p>" +
        "</html>",
    };

    /** 
     * Creates new form SpaceStationPanel
     */
    public SpaceStationPanel(World world, Galaxy galaxy, Ship ship, ImageCache imageCache)
    {
        this.world = world;
        this.galaxy = galaxy;
        this.imageCache = imageCache;
        this.ship = ship;
        
        initComponents();

        welcomeLabel.setFont(FontFactory.getPanelHeading());
        advertisementLabel.setFont(FontFactory.getLarger());
        impressionLabel.setFont(FontFactory.getLarger());
        
        governmentLabel2.setFont(FontFactory.getLabelHeading());
        governmentField.setFont(FontFactory.getLarger());

        majorityLabel.setFont(FontFactory.getLabelHeading());
        majorityField.setFont(FontFactory.getLarger());

        techLabel.setFont(FontFactory.getLabelHeading());
        techField.setFont(FontFactory.getLarger());

        populationLabel.setFont(FontFactory.getLabelHeading());
        inhabitantsField.setFont(FontFactory.getLarger());
        
        servicesLabel.setFont(FontFactory.getLabelHeading());
        
        ComponentFactory.customizeButton(agencyButton);
        ComponentFactory.customizeButton(bulletinBoardButton);
        ComponentFactory.customizeButton(commodityButton);
        ComponentFactory.customizeButton(equipmentShopButton);
        ComponentFactory.customizeButton(shipyardButton);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        borderPanel = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        portraitContainer = new javax.swing.JPanel();
        portraitLabel = new javax.swing.JLabel();
        agencyButton = new javax.swing.JButton();
        shipyardButton = new javax.swing.JButton();
        bulletinBoardButton = new javax.swing.JButton();
        commodityButton = new javax.swing.JButton();
        majorityField = new javax.swing.JLabel();
        majorityLabel = new javax.swing.JLabel();
        techLabel = new javax.swing.JLabel();
        techField = new javax.swing.JLabel();
        populationLabel = new javax.swing.JLabel();
        inhabitantsField = new javax.swing.JLabel();
        impressionLabel = new javax.swing.JLabel();
        advertisementLabel = new javax.swing.JLabel();
        servicesLabel = new javax.swing.JLabel();
        governmentField = new javax.swing.JLabel();
        governmentLabel2 = new javax.swing.JLabel();
        equipmentShopButton = new javax.swing.JButton();

        setBackground(java.awt.Color.black);
        setForeground(java.awt.Color.green);
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        borderPanel.setBackground(new java.awt.Color(0, 51, 51));
        borderPanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray));
        borderPanel.setForeground(java.awt.Color.green);
        borderPanel.setMinimumSize(new java.awt.Dimension(600, 540));
        borderPanel.setPreferredSize(new java.awt.Dimension(800, 540));
        borderPanel.setLayout(null);

        welcomeLabel.setForeground(java.awt.Color.white);
        welcomeLabel.setText("Welcome to xyz station!");
        borderPanel.add(welcomeLabel);
        welcomeLabel.setBounds(20, 20, 660, 16);

        portraitContainer.setBackground(java.awt.Color.darkGray);
        portraitContainer.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray));
        portraitContainer.setPreferredSize(new java.awt.Dimension(127, 130));
        portraitContainer.setLayout(new java.awt.BorderLayout());

        portraitLabel.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        portraitLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        portraitLabel.setText("<html><center>Here<br>be<br>portrait.</center></html>");
        portraitContainer.add(portraitLabel, java.awt.BorderLayout.CENTER);

        borderPanel.add(portraitContainer);
        portraitContainer.setBounds(620, 60, 127, 130);

        agencyButton.setText("Prospectors Agency");
        agencyButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                agencyButtonActionPerformed(evt);
            }
        });
        borderPanel.add(agencyButton);
        agencyButton.setBounds(20, 250, 270, 30);

        shipyardButton.setText("Shipyard");
        shipyardButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                shipyardButtonActionPerformed(evt);
            }
        });
        borderPanel.add(shipyardButton);
        shipyardButton.setBounds(20, 410, 270, 30);

        bulletinBoardButton.setText("Bulletin Board");
        bulletinBoardButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bulletinBoardButtonActionPerformed(evt);
            }
        });
        borderPanel.add(bulletinBoardButton);
        bulletinBoardButton.setBounds(20, 330, 270, 30);

        commodityButton.setText("Commodity Exchange");
        commodityButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                commodityButtonActionPerformed(evt);
            }
        });
        borderPanel.add(commodityButton);
        commodityButton.setBounds(20, 290, 270, 30);

        majorityField.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        majorityField.setForeground(java.awt.Color.white);
        majorityField.setText("Democratic");
        borderPanel.add(majorityField);
        majorityField.setBounds(280, 70, 110, 16);

        majorityLabel.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        majorityLabel.setForeground(java.awt.Color.green);
        majorityLabel.setText("Majority:");
        borderPanel.add(majorityLabel);
        majorityLabel.setBounds(210, 70, 70, 18);

        techLabel.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        techLabel.setForeground(java.awt.Color.green);
        techLabel.setText("Tech. level:");
        borderPanel.add(techLabel);
        techLabel.setBounds(20, 130, 90, 18);

        techField.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        techField.setForeground(java.awt.Color.white);
        techField.setText("3");
        borderPanel.add(techField);
        techField.setBounds(120, 130, 80, 16);

        populationLabel.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        populationLabel.setForeground(java.awt.Color.green);
        populationLabel.setText("Population:");
        borderPanel.add(populationLabel);
        populationLabel.setBounds(20, 100, 100, 18);

        inhabitantsField.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        inhabitantsField.setForeground(java.awt.Color.white);
        inhabitantsField.setText("1712");
        borderPanel.add(inhabitantsField);
        inhabitantsField.setBounds(120, 100, 80, 16);

        impressionLabel.setForeground(java.awt.Color.lightGray);
        impressionLabel.setText("jLabel2");
        impressionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        borderPanel.add(impressionLabel);
        impressionLabel.setBounds(400, 240, 350, 230);

        advertisementLabel.setForeground(java.awt.Color.lightGray);
        advertisementLabel.setText("jLabel2");
        advertisementLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        borderPanel.add(advertisementLabel);
        advertisementLabel.setBounds(400, 70, 200, 140);

        servicesLabel.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        servicesLabel.setForeground(java.awt.Color.green);
        servicesLabel.setText("Our Services:");
        borderPanel.add(servicesLabel);
        servicesLabel.setBounds(20, 200, 180, 18);

        governmentField.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        governmentField.setForeground(java.awt.Color.white);
        governmentField.setText("Democratic");
        borderPanel.add(governmentField);
        governmentField.setBounds(120, 70, 80, 16);

        governmentLabel2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        governmentLabel2.setForeground(java.awt.Color.green);
        governmentLabel2.setText("Government:");
        borderPanel.add(governmentLabel2);
        governmentLabel2.setBounds(20, 70, 100, 18);

        equipmentShopButton.setText("Equipment Shop");
        equipmentShopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                equipmentShopButtonActionPerformed(evt);
            }
        });
        borderPanel.add(equipmentShopButton);
        equipmentShopButton.setBounds(20, 370, 270, 30);

        add(borderPanel, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void agencyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agencyButtonActionPerformed
        switchTo(new ProspectorsAgencyPanel(station, this));
    }//GEN-LAST:event_agencyButtonActionPerformed

    private void shipyardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shipyardButtonActionPerformed
        switchTo(new ShipyardPanel(ship, station, this));
    }//GEN-LAST:event_shipyardButtonActionPerformed

    private void bulletinBoardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bulletinBoardButtonActionPerformed
        switchTo(new BulletinBoardPanel(imageCache, world, galaxy, station, this, ship));
    }//GEN-LAST:event_bulletinBoardButtonActionPerformed

    private void commodityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commodityButtonActionPerformed
        switchTo(new TradePanel(station, ship, this));
}//GEN-LAST:event_commodityButtonActionPerformed

    private void equipmentShopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equipmentShopButtonActionPerformed
        switchTo(new ComponentTrade(station, ship, this));
    }//GEN-LAST:event_equipmentShopButtonActionPerformed

    /**
     * Display a new component instead of ourselves.
     * @param panel The component to show.
     */
    private void switchTo(JComponent panel)
    {
        Container c = getParent();
        c.remove(this);
        c.add(panel);
        c.validate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel advertisementLabel;
    private javax.swing.JButton agencyButton;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JButton bulletinBoardButton;
    private javax.swing.JButton commodityButton;
    private javax.swing.JButton equipmentShopButton;
    private javax.swing.JLabel governmentField;
    private javax.swing.JLabel governmentLabel2;
    private javax.swing.JLabel impressionLabel;
    private javax.swing.JLabel inhabitantsField;
    private javax.swing.JLabel majorityField;
    private javax.swing.JLabel majorityLabel;
    private javax.swing.JLabel populationLabel;
    private javax.swing.JPanel portraitContainer;
    private javax.swing.JLabel portraitLabel;
    private javax.swing.JLabel servicesLabel;
    private javax.swing.JButton shipyardButton;
    private javax.swing.JLabel techField;
    private javax.swing.JLabel techLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Customize station panel with texts appropriate for this type
     * of station or spaceport.
     *
     * @param station The station or spaceport to describe
     * @param ship The ship from where the connection is made
     */
    public void update(Solar station)
    {
        this.station = station;

        // System.err.println("Seed = " + station.seed);
        
        rng = RandomHelper.createRNG(station.seed +
                                     (long)(station.radius*1000) +
                                     (long)(station.orbit) +
                                     station.eet +
                                     (station.name.hashCode() << 16));

        if(ship.getState() == Ship.State.DOCKED &&
           ship.spaceBodySeed == station.seed)
        {
            welcomeLabel.setText("Welcome to " + station.name + "!");
        }
        else
        {
            welcomeLabel.setText("Radio link to " + station.name + " was established ...");
        }
        
        inhabitantsField.setText("" + station.society.population);
        techField.setText("" + station.society.techLevel);

        advertisementLabel.setText(welcomeTexts[(int)(rng.nextDouble() * welcomeTexts.length)]);

        if(station.btype == Solar.BodyType.SPACEPORT)
        {
            if(station.society.race == Society.Race.Clonkniks)
            {
                impressionLabel.setText(spaceportTexts[spaceportTexts.length-1]);
            }
            else
            {

                if(station.getParent() != null &&
                   (station.getParent().ptype == Solar.PlanetType.BIG_GAS ||
                    station.getParent().ptype == Solar.PlanetType.RINGS))
                {
                    impressionLabel.setText(spaceportFloatingTexts[(int)(rng.nextDouble() * spaceportFloatingTexts.length)]);
                }
                else
                {
                    impressionLabel.setText(spaceportTexts[(int)(rng.nextDouble() * spaceportTexts.length-1)]);
                }
            }

        } 
        else
        {
            if(station.society.race == Society.Race.Clonkniks) 
            {
                impressionLabel.setText(stationTexts[stationTexts.length-1]);
            }
            else
            {
                impressionLabel.setText(stationTexts[(int)(rng.nextDouble() * stationTexts.length-1)]);
            }
        }

        governmentField.setText(station.society.governmentType.toString());

        majorityField.setText(station.society.race.toString());

        calcPortrait(station);
    }


    private void calcPortrait(Solar station)
    {
        portraitContainer.removeAll();

        PortraitPanel portrait = new PortraitPanel();
        portrait.setStation(station, rng, imageCache);
        portraitContainer.add(portrait);
        portraitContainer.validate();
    }
}
