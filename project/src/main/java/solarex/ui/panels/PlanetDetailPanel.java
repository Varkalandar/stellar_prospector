/*
 * PlanetViewPanel.java
 *
 * Created: 18-Nov-2009
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import solarex.ship.Ship;
import solarex.ship.components.EquipmentType;
import solarex.ship.components.ShipComponent;
import solarex.system.Biosphere;
import solarex.system.PlanetResources;
import solarex.system.Solar;
import solarex.ui.ImageCache;
import solarex.ui.components.BodyInfoLabel;
import solarex.ui.components.EquipmentWrapper;
import solarex.ui.components.PlanetDetailLabel;
import solarex.ui.ComponentFactory;
import solarex.ui.FontFactory;

/**
 * Planet explorer panel.
 * @author Hj. Malthaner
 */
public class PlanetDetailPanel extends javax.swing.JPanel {

    /**
     * The players ship.
     */
    private Ship ship;

    private Random rng;
    private ImageCache imageCache;
    private BodyInfoLabel bodyInfoLabel;
    private PlanetDetailLabel planetDetailLabel;
    private Font font = FontFactory.getNormal();

    private Solar planet;

    public final static String [] richness = new String [] 
    {
        "Trace deposits of <font color=\"%2\">%1</font> detected.",
        "Minor <font color=\"%2\">%1</font> deposits detected.",
        "Small deposits of <font color=\"%2\">%1</font> detected.",
        "Rich <font color=\"%2\">%1</font> deposits detected.",
        "Abundant <font color=\"%2\">%1</font> deposits detected.",
    };
    
    private MiningPanel miningPanel;

    /** Creates new form PlanetViewPanel */
    public PlanetDetailPanel(Ship ship, ImageCache imageCache)
    {
        initComponents();
        this.ship = ship;
        this.imageCache = imageCache;
        
        planetNameLabel.setText("My Planet");
        planetNameLabel.setFont(FontFactory.getPanelHeading());

        planetIconLabel.setIcon(imageCache.planets[Solar.PlanetType.EARTH.ordinal()]);
        // planetIconLabel.setText("      ");

        JPanel helper = new JPanel();
        helper.setLayout(null);
        helper.setOpaque(false);
        
        planetPanel.setBackground(ComponentFactory.boxBackground);
        // helper.setBorder(new LineBorder(Color.GRAY));
        
        bodyInfoLabel = new BodyInfoLabel();
        bodyInfoLabel.setLocation(28, 4);
        bodyInfoLabel.setSize(180, 150);
        bodyInfoLabel.setFont(FontFactory.getNormal());
        
        planetDetailLabel = new PlanetDetailLabel();
        planetDetailLabel.setLocation(170, 4);
        planetDetailLabel.setSize(180, 150);
        planetDetailLabel.setFont(FontFactory.getNormal());
        
        helper.add(bodyInfoLabel);
        helper.add(planetDetailLabel);
        helper.setSize(398, 135);
        helper.setLocation(1, 110);
        
        planetPanel.add(helper, BorderLayout.SOUTH);

        createTabs();
        jTabbedPane1.setUI(new ComponentFactory.MyTabbedPaneUi());
        
        ComponentFactory.customizeList(dockedDronesList, 2);
        ComponentFactory.customizeList(activeDronesList, 2);
        
        resourcesLabel.setFont(FontFactory.getLabelHeading());
        availableDronesLabel.setFont(FontFactory.getLabelHeading());
        activeDronesLabel.setFont(FontFactory.getLabelHeading());
        
        jTabbedPane1.setFont(FontFactory.getNormal());
        
        fillDronesLists(ship);
        calculateDeployButtonEnabled();
                
        ComponentFactory.customizeButton(deployButton);
        ComponentFactory.customizeButton(haulDroneButton);
    }

    private void createTabs()
    {
        JList list = createResourcesList(new String [] {"A", "B", "C"});
        JScrollPane scrolly = new JScrollPane(list);
        scrolly.setBorder(null);
        gasesPanel.add(scrolly);

        list = createResourcesList(new String [] {"Unprobed."});
        scrolly = new JScrollPane(list);
        scrolly.setBorder(null);
        fluidsPanel.add(scrolly);

        list = createResourcesList(new String [] {"Unprobed."});
        scrolly = new JScrollPane(list);
        scrolly.setBorder(null);
        mineralsPanel.add(scrolly);

        list = createResourcesList(new String [] {"Unprobed."});
        scrolly = new JScrollPane(list);
        scrolly.setBorder(null);
        metalsPanel.add(scrolly);

        list = createResourcesList(new String [] {"Unprobed."});
        scrolly = new JScrollPane(list);
        scrolly.setBorder(null);
        biospherePanel.add(scrolly);      

        list = createResourcesList(new String [] {"Unprobed."});
        scrolly = new JScrollPane(list);
        scrolly.setBorder(null);
        miscPanel.add(scrolly);                      
    }
    
    /**
     * Calculate number/amount of exploitable deposits
     * @param planet
     */
    public static String [] calculateMetals(Solar planet, Random rng, int [] deposits, long [] positions)
    {
        PlanetResources.calculateMetals(planet, rng, deposits, positions);    

        ArrayList <String> alist = new ArrayList();
        PlanetResources.Metals [] values = PlanetResources.Metals.values();

        for(int i=0; i<deposits.length; i++) {
            if(deposits[i] != 0) {

                String s;                
                s = richness[Math.min(deposits[i], richness.length-1)];
                s = s.replaceFirst("%1", values[i].toString().toLowerCase());
                s = s.replaceFirst("%2", values[i].color);
                
                // s = "" + deposits[i] + " " + values[i].toString().toLowerCase() + " deposits detected.";

                // Hajo: add "sort order" hint as first character
                char hint = (char)('z' - deposits[i]);
                alist.add("" + hint + s);
            }
        }

        Collections.sort(alist);

        String [] items;

        if(alist.isEmpty()) 
        {
            items = new String[1];
            items[0] = "No recoverable metal deposits found.";
        }
        else 
        {
            items = new String[alist.size()];
            for(int i=0; i<items.length; i++) 
            {
                // Hajo: remove "sort order" hint, first character
                items[i] = alist.get(i).substring(1);
            }
        }
        
        return items;
    }
        
    /**
     * Calculate number/amount of exploitable deposits
     * @param planet
     */
    private void calculateMetals(Solar planet)
    {
        int [] deposits = new int [PlanetResources.Metals.values().length];
        long [] positions = new long [PlanetResources.Metals.values().length];

        String [] items = calculateMetals(planet, rng, deposits, positions);
        
        JList list = createResourcesList(items);
        JScrollPane scrolly = (JScrollPane)metalsPanel.getComponent(0);

        scrolly.setViewportView(list);
    }

    /**
     * Calculate number/amount of exploitable deposits
     * @param planet
     */
    public static String [] calculateMinerals(Solar planet, int [] deposits)
    {
        ArrayList <String> alist = new ArrayList();
        PlanetResources.Minerals [] values = PlanetResources.Minerals.values();

        for(int i=0; i<deposits.length; i++) {
            if(deposits[i] != 0) {

                String s;
                s = richness[Math.min(deposits[i], richness.length-1)];
                s = s.replaceFirst("%1", values[i].toString().toLowerCase());
                s = s.replaceFirst("%2", values[i].color);

                // s = "" + deposits[i] + " " + values[i].toString().toLowerCase() + " deposits detected.";

                // Hajo: add "sort order" hint as first character
                char hint = (char)('z' - deposits[i]);
                alist.add("" + hint + s);
            }
        }

        Collections.sort(alist);

        String [] items;

        if(alist.isEmpty()) {
            items = new String[1];
            items[0] = "No recoverable mineral deposits found.";
        } else {
            items = new String[alist.size()];
            for(int i=0; i<items.length; i++) {
                // Hajo: remove "sort order" hint, first character
                items[i] = alist.get(i).substring(1);
            }
        }

        return items;
    }
    
    private void calculateMinerals(Solar planet)
    {
        int [] deposits = PlanetResources.calculateMinerals(planet, rng);

        String [] items = calculateMinerals(planet, deposits);

        JList list = createResourcesList(items);
        JScrollPane scrolly = (JScrollPane)mineralsPanel.getComponent(0);

        scrolly.setViewportView(list);
    }

    public static String [] calculateBiosphere(Solar planet, Random rng, int[] fluids)
    {
        PlanetResources.calculateBiosphere(planet, fluids, rng);
        Biosphere biosphere = planet.biosphere;
        
        String [] items;

        if(biosphere.lifeforms.isEmpty()) 
        {
            items = new String[1];
            items[0] = "No lifeforms detected.";
        }
        else 
        {
            items = new String[biosphere.lifeforms.size() + 
                               biosphere.energySources.size() + 5];
            
            int n = 0;
            items[n++] = "Detected Lifeforms:";
            items[n++] = "";
            
            for(Biosphere.Lifeforms lifeform : biosphere.lifeforms) 
            {
                items[n++] = "\u00B7 " + lifeform.toString();
            }

            items[n++] = "";
            items[n++] = "Likely Energy Sources:";
            items[n++] = "";

            for(Biosphere.EnergySources energySource : biosphere.energySources) 
            {
                items[n++] = "\u00B7 " + energySource.toString();
            }
        }

        return items;
    }
    
    private void calculateBiosphere(Solar planet, int[] fluids)
    {
        String [] items = calculateBiosphere(planet, rng, fluids);
        
        JList list = createResourcesList(items);
        JScrollPane scrolly = (JScrollPane)biospherePanel.getComponent(0);

        scrolly.setViewportView(list);
    }

    /**
     * Calculate other resources for a planet
     * @param planet
     */
    public static String [] calculateOtherResources(Solar planet, Random rng)
    {
        int [] otherResources = PlanetResources.calculateOtherResources(planet, rng);

        ArrayList <String> alist = new ArrayList();
        PlanetResources.OtherResource [] values = PlanetResources.OtherResource.values();

        for(int i=0; i<otherResources.length; i++) {
            if(otherResources[i] != 0) {

                String s;
                s = richness[Math.min(otherResources[i], richness.length-1)];
                s = s.replaceFirst("%1", values[i].toString().toLowerCase());
                s = s.replaceFirst("%2", values[i].color);

                // Hajo: add "sort order" hint as first character
                alist.add("" + (9-otherResources[i]) + s);
            }
        }

        Collections.sort(alist);

        String [] items;

        if(alist.isEmpty()) {
            items = new String[1];
            items[0] = "No other resources found.";
        } else {
            items = new String[alist.size()];
            for(int i=0; i<items.length; i++) {
                // Hajo: remove "sort order" hint, first character
                items[i] = alist.get(i).substring(1);
            }
        }

        return items;
    }
    /**
     * Calculate other resources for a planet
     * @param planet
     */
    private void calculateOtherResources(Solar planet)
    {
        String [] items = calculateOtherResources(planet, rng);
        
        JList list = createResourcesList(items);
        JScrollPane scrolly = (JScrollPane)miscPanel.getComponent(0);

        scrolly.setViewportView(list);
    }

    /**
     * Calculate atmosphere composition for a planet.
     * @param planet
     */
    public static String [] calculateAtmosphere(Solar planet, Random rng, int [] weights)
    {
        PlanetResources.calculateAtmosphere(planet, rng, weights);

        int sum = 0;
        for(int i=0; i<weights.length; i++) 
        {
            sum += weights[i];
        }

        PlanetResources.Gases [] values = PlanetResources.Gases.values();
        ArrayList <String> alist = new ArrayList();

        if(sum > 0) {

            for(int i=0; i<values.length; i++) {
                int percent = (weights[i]*100/sum);

                char sortOrder = (char)(200 - percent);

                if(percent > 0) {
                    alist.add("" + sortOrder +
                        "<font color=\"" + values[i].color + "\">" +
                        values[i].toString() + "</font> " +
                        percent + "%");
                } else {
                    alist.add("" + sortOrder +
                        "<font color=\"" + values[i].color + "\">" +
                        values[i].toString() + "</font> &lt;1%");
                }
            }
        }

        Collections.sort(alist);

        String [] items;

        if(sum == 0) {
            items = new String [] {"No significant atmosphere detected."};
        } else {
            items = new String [alist.size()];

            for(int i=0; i<items.length; i++) {
                // Hajo: remove "sort order" hint, first character
                items[i] = alist.get(i).substring(1);
            }
        }

        return items;
    }
    
    private int [] calculateAtmosphereList(Solar planet)
    {
        int [] weights = new int [PlanetResources.Gases.values().length];

        String [] items = calculateAtmosphere(planet, rng, weights);
        
        JList list = createResourcesList(items);
        JScrollPane scrolly = (JScrollPane)gasesPanel.getComponent(0);

        scrolly.setViewportView(list);
        
        return weights;
    }

    public static String [] calculateFluids(Solar planet, Random rng, int [] gases, int [] deposits, long [] positions)
    {
        PlanetResources.calculateFluids(planet, gases, rng, deposits, positions);

        ArrayList <String> alist = new ArrayList();
        PlanetResources.Fluids [] values = PlanetResources.Fluids.values();

        for(int i=0; i<deposits.length; i++) 
        {
            if(deposits[i] != 0) 
            {
                String s;
                s = richness[Math.min(deposits[i], richness.length-1)];
                s = s.replaceFirst("%1", values[i].toString().toLowerCase());
                s = s.replaceFirst("%2", values[i].color);

                // Hajo: add "sort order" hint as first character
                char hint = (char)('z' - deposits[i]);
                alist.add("" + hint + s);
            }
        }

        Collections.sort(alist);

        String [] items;

        if(alist.isEmpty()) 
        {
            items = new String[1];
            items[0] = "No recoverable fluids found.";
        }
        else 
        {
            items = new String[alist.size()];
            for(int i=0; i<items.length; i++) 
            {
                // Hajo: remove "sort order" hint, first character
                items[i] = alist.get(i).substring(1);
            }
        }
     
        return items;
    }
    
    private int [] calculateFluids(Solar planet, int [] gases)
    {
        int [] deposits = new int [PlanetResources.Fluids.values().length];
        long [] positions = new long [PlanetResources.Fluids.values().length];

        String [] items = calculateFluids(planet, rng, gases, deposits, positions);
        
        JList list = createResourcesList(items);
        JScrollPane scrolly = (JScrollPane)fluidsPanel.getComponent(0);

        scrolly.setViewportView(list);
        
        return deposits;
    }
    
    public void switchToPlanetView()
    {
        if(miningPanel != null)
        {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new Insets(12, 0, 16, 0);

            remove(miningPanel);
            add(innerPanel, gridBagConstraints);
            validate();

            miningPanel = null;
        }

        repaint(50);
    }
    
    /**
     * Refresh view with new planet data.
     * @param planet The new planet.
     */
    public void update(Solar planet)
    {
        this.planet = planet;
        
        // switch back from mining view if there is one open.
        switchToPlanetView();
        
        fillDronesLists(ship);
        
        rng = PlanetResources.getPlanetRng(planet);

        bodyInfoLabel.update(planet, ship.player.isExplored(planet));
        planetDetailLabel.update(planet);
        int [] gases = calculateAtmosphereList(planet);
        int [] fluids = calculateFluids(planet, gases);
        calculateMetals(planet);
        calculateMinerals(planet);
        calculateBiosphere(planet, fluids);
        calculateOtherResources(planet);
        
        planetNameLabel.setText(planet.name);        
        planetIconLabel.setIcon(imageCache.planets[planet.ptype.ordinal()]);
        
        calculateDeployButtonEnabled();
    }


    /**
     * Create a color-customized JList from the string array.
     *
     * @param items The list entries.
     * @return JList with DefaultListModel
     */
    private JList createResourcesList(String [] items)
    {
        DefaultListModel model = new DefaultListModel();

        for(int i=0; i<items.length; i++) 
        {
            model.add(i, "<html>&nbsp;" + items[i] + "</html>");
        }

        JList list = new JList(model);

        ComponentFactory.customizeList(list, 1);
        
        return list;
    }

    private void fillDronesLists(Ship ship)
    {
        DefaultListModel docked = new DefaultListModel();
        
        for(ShipComponent comp : ship.equipment.components)
        {
            if(comp.getType() == EquipmentType.DRONE)
            {
                docked.addElement(new EquipmentWrapper(comp));
            }
        }
        dockedDronesList.setModel(docked);

        DefaultListModel active = new DefaultListModel();
        // Todo: fill list
        activeDronesList.setModel(active);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        innerPanel = new javax.swing.JPanel();
        planetNameLabel = new javax.swing.JLabel();
        planetPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        planetIconLabel = new javax.swing.JLabel();
        resourcesPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        gasesPanel = new javax.swing.JPanel();
        fluidsPanel = new javax.swing.JPanel();
        mineralsPanel = new javax.swing.JPanel();
        metalsPanel = new javax.swing.JPanel();
        biospherePanel = new javax.swing.JPanel();
        miscPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        availableDronesLabel = new javax.swing.JLabel();
        dronesScroll = new javax.swing.JScrollPane();
        dockedDronesList = new javax.swing.JList();
        deployButton = new javax.swing.JButton();
        activeDronesLabel = new javax.swing.JLabel();
        activeDronesScroll = new javax.swing.JScrollPane();
        activeDronesList = new javax.swing.JList();
        haulDroneButton = new javax.swing.JButton();
        resourcesLabel = new javax.swing.JLabel();

        setBackground(java.awt.Color.black);
        setForeground(java.awt.Color.green);
        setLayout(new java.awt.GridBagLayout());

        innerPanel.setBackground(new java.awt.Color(15, 20, 37));
        innerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray));
        innerPanel.setForeground(java.awt.Color.green);
        innerPanel.setLayout(null);

        planetNameLabel.setBackground(java.awt.Color.black);
        planetNameLabel.setForeground(java.awt.Color.white);
        planetNameLabel.setText("Planet");
        innerPanel.add(planetNameLabel);
        planetNameLabel.setBounds(30, 10, 340, 30);

        planetPanel.setBackground(java.awt.Color.black);
        planetPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));
        planetPanel.setForeground(java.awt.Color.green);
        planetPanel.setLayout(null);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        planetIconLabel.setToolTipText("");
        planetIconLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        planetIconLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel2.add(planetIconLabel);
        planetIconLabel.setBounds(130, -10, 160, 160);

        planetPanel.add(jPanel2);
        jPanel2.setBounds(0, 7, 400, 150);

        innerPanel.add(planetPanel);
        planetPanel.setBounds(31, 46, 400, 230);

        resourcesPanel.setBackground(java.awt.Color.black);
        resourcesPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(64, 64, 64)));
        resourcesPanel.setForeground(java.awt.Color.green);
        resourcesPanel.setOpaque(false);
        resourcesPanel.setPreferredSize(new java.awt.Dimension(300, 250));
        resourcesPanel.setLayout(new java.awt.BorderLayout(0, 12));

        jTabbedPane1.setBackground(java.awt.Color.darkGray);
        jTabbedPane1.setForeground(java.awt.Color.green);

        gasesPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Gases", gasesPanel);

        fluidsPanel.setBackground(java.awt.Color.black);
        fluidsPanel.setForeground(java.awt.Color.green);
        fluidsPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Fluids", fluidsPanel);

        mineralsPanel.setBackground(java.awt.Color.black);
        mineralsPanel.setForeground(java.awt.Color.green);
        mineralsPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Minerals", mineralsPanel);

        metalsPanel.setBackground(java.awt.Color.black);
        metalsPanel.setForeground(java.awt.Color.green);
        metalsPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Metals", metalsPanel);

        biospherePanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Biosphere", biospherePanel);

        miscPanel.setBackground(java.awt.Color.black);
        miscPanel.setForeground(java.awt.Color.green);
        miscPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Misc.", miscPanel);

        resourcesPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        innerPanel.add(resourcesPanel);
        resourcesPanel.setBounds(468, 46, 440, 230);

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 270));
        jPanel1.setLayout(null);

        availableDronesLabel.setBackground(java.awt.Color.black);
        availableDronesLabel.setForeground(java.awt.Color.green);
        availableDronesLabel.setText("Docked Drones");
        jPanel1.add(availableDronesLabel);
        availableDronesLabel.setBounds(30, 0, 210, 20);

        dronesScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));

        dockedDronesList.setBackground(java.awt.Color.darkGray);
        dockedDronesList.setForeground(java.awt.Color.lightGray);
        dockedDronesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        dockedDronesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                dockedDronesListValueChanged(evt);
            }
        });
        dronesScroll.setViewportView(dockedDronesList);

        jPanel1.add(dronesScroll);
        dronesScroll.setBounds(30, 30, 400, 160);

        deployButton.setText("Deploy Drone");
        deployButton.setEnabled(false);
        deployButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deployButtonActionPerformed(evt);
            }
        });
        jPanel1.add(deployButton);
        deployButton.setBounds(140, 210, 180, 25);

        activeDronesLabel.setForeground(java.awt.Color.green);
        activeDronesLabel.setText("Active Drones");
        jPanel1.add(activeDronesLabel);
        activeDronesLabel.setBounds(470, 0, 250, 20);

        activeDronesScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));

        activeDronesList.setBackground(java.awt.Color.darkGray);
        activeDronesList.setForeground(java.awt.Color.lightGray);
        activeDronesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        activeDronesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                activeDronesListValueChanged(evt);
            }
        });
        activeDronesScroll.setViewportView(activeDronesList);

        jPanel1.add(activeDronesScroll);
        activeDronesScroll.setBounds(470, 30, 430, 160);

        haulDroneButton.setText("Haul Drone Inboard");
        haulDroneButton.setEnabled(false);
        haulDroneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                haulDroneButtonActionPerformed(evt);
            }
        });
        jPanel1.add(haulDroneButton);
        haulDroneButton.setBounds(600, 210, 170, 25);

        innerPanel.add(jPanel1);
        jPanel1.setBounds(1, 292, 939, 232);

        resourcesLabel.setBackground(java.awt.Color.black);
        resourcesLabel.setForeground(java.awt.Color.green);
        resourcesLabel.setText("Discovered Resources");
        innerPanel.add(resourcesLabel);
        resourcesLabel.setBounds(470, 20, 438, 16);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(32, 24, 24, 24);
        add(innerPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void deployButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deployButtonActionPerformed
    {//GEN-HEADEREND:event_deployButtonActionPerformed

        Solar station = null;
        
        if(planet.children.size() > 0 && 
           planet.children.get(0).btype == Solar.BodyType.STATION)
        {
            station = planet.children.get(0);
        }
        
        if(planet.society != null && planet.society.population != 0)
        {
            // Hajo: no minong on populated planets
            JOptionPane.showMessageDialog(this, 
                    "This planet is populated. The inhabitants do not allow you\n"
                    + "to mine or harvest resources here.");
            
        }
        else if(station != null)
        {
            String owners = station.society.race.toString().toLowerCase();
            
            // Hajo: no mining on owned planets
            JOptionPane.showMessageDialog(this, 
                    planet.name + " is property of the "
                    + owners
                    + ".\n"
                    + "The " + owners + " do not allow you to mine\n"
                    + "or harvest resources here.");
        }
        else if(ship.spaceBodySeed != planet.seed)
        {
            // Hajo: ship must be actually there
            JOptionPane.showMessageDialog(this, 
                    "Your ship is not in orbit of " + planet.name);
        }
        else
        {        
            Object o = dockedDronesList.getSelectedValue();

            if(o != null && o instanceof EquipmentWrapper)
            {
                ShipComponent drone = ((EquipmentWrapper)o).getComponent();

                DefaultListModel m = (DefaultListModel) activeDronesList.getModel();
                m.addElement(o);

                // activeDronesList.repaint();

                m = (DefaultListModel)dockedDronesList.getModel();
                m.removeElement(o);

                miningPanel = new MiningPanel(planet, ship, imageCache, drone, this);

                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new Insets(12, 0, 16, 0);

                remove(innerPanel);
                add(miningPanel, gridBagConstraints);
                validate();
            }        
        }
    }//GEN-LAST:event_deployButtonActionPerformed

    private void dockedDronesListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_dockedDronesListValueChanged
    {//GEN-HEADEREND:event_dockedDronesListValueChanged
        calculateDeployButtonEnabled();
    }//GEN-LAST:event_dockedDronesListValueChanged

    private void activeDronesListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_activeDronesListValueChanged
    {//GEN-HEADEREND:event_activeDronesListValueChanged
        haulDroneButton.setEnabled(activeDronesList.getSelectedValue() != null);
    }//GEN-LAST:event_activeDronesListValueChanged

    private void haulDroneButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_haulDroneButtonActionPerformed
    {//GEN-HEADEREND:event_haulDroneButtonActionPerformed
        Object o = activeDronesList.getSelectedValue();
        
        if(o != null && o instanceof EquipmentWrapper)
        {
            DefaultListModel m = (DefaultListModel) dockedDronesList.getModel();
            m.addElement(o);

            m = (DefaultListModel)activeDronesList.getModel();
            m.removeElement(o);
        }
    }//GEN-LAST:event_haulDroneButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activeDronesLabel;
    private javax.swing.JList activeDronesList;
    private javax.swing.JScrollPane activeDronesScroll;
    private javax.swing.JLabel availableDronesLabel;
    private javax.swing.JPanel biospherePanel;
    private javax.swing.JButton deployButton;
    private javax.swing.JList dockedDronesList;
    private javax.swing.JScrollPane dronesScroll;
    private javax.swing.JPanel fluidsPanel;
    private javax.swing.JPanel gasesPanel;
    private javax.swing.JButton haulDroneButton;
    private javax.swing.JPanel innerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel metalsPanel;
    private javax.swing.JPanel mineralsPanel;
    private javax.swing.JPanel miscPanel;
    private javax.swing.JLabel planetIconLabel;
    private javax.swing.JLabel planetNameLabel;
    private javax.swing.JPanel planetPanel;
    private javax.swing.JLabel resourcesLabel;
    private javax.swing.JPanel resourcesPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Check if we are there and in orbit
     */
    private void calculateDeployButtonEnabled() 
    {
        if(dockedDronesList.getSelectedValue() != null &&
           ship.getState() == Ship.State.ORBIT &&
           ship.spaceBodySeed == planet.seed)
        {
            deployButton.setEnabled(dockedDronesList.getSelectedValue() != null);
        }
    }


    
}
