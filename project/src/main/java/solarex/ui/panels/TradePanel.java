/*
 * TradePanel.java
 *
 * Created: 03.02.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import solarex.ship.Cargo;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.system.PlanetResources;
import solarex.system.Solar;
import solarex.util.ClockThread;
import solarex.ui.ComponentFactory;
import solarex.ui.FontFactory;
import solarex.util.RandomHelper;


/**
 * Space station trade panel.
 * @author Hj. Malthaner
 */
public class TradePanel extends javax.swing.JPanel
{

    public static Cargo createCargo(Solar station)
    {
        Cargo cargo = new Cargo();
        Random grng = RandomHelper.createRNG(station.seed + ClockThread.getDayOfGame());
        
        // Hajo: calculation order is important!
        // cross check with planet view panel calculation order
        final Random rng = PlanetResources.getPlanetRng(station.getParent());
        
        // gases
        int [] gases = new int [PlanetResources.Gases.values().length];
        PlanetResources.calculateAtmosphere(station.getParent(), rng, gases);

        // fluids
        int [] fluidDeposits = new int [PlanetResources.Fluids.values().length];
        long [] fluidPositions = new long [PlanetResources.Fluids.values().length];
        PlanetResources.calculateFluids(station.getParent(), gases, rng, 
                                        fluidDeposits, fluidPositions);

        // metals
        int [] metalDeposits = new int [PlanetResources.Metals.values().length];
        long [] metalPositions = new long [PlanetResources.Metals.values().length];
        PlanetResources.calculateMetals(station.getParent(), rng, 
                                        metalDeposits, metalPositions);    
        
        int [] minerals = PlanetResources.calculateMinerals(station.getParent(), rng);
        int [] otherResources = PlanetResources.calculateOtherResources(station.getParent(), rng);

        for(int i=0; i<Good.Type.values().length; i++)
        {
            cargo.goods[i].units = (int)(10 + grng.nextDouble()*20);

            cargo.goods[i].salesPrice =
                calculatePrice(station, metalDeposits, minerals, cargo.goods[i]);

            // System.err.println(Good.Type.values()[i].toString() + " units=" + cargo.goods[i].units);
        }

        // make some goods illegal by government type
        switch(station.society.governmentType)
        {
            case Communism:
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Robots.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Socialism:
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                break;
            case Theocracy:
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Biotech.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Medicine.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Utopia:
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                break;
        }
            
        // make some goods illegal by race
        switch(station.society.race)
        {
            case Clonkniks:
                cargo.illegalGoods[Good.Type.ArtificialIntelligence.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Floatees:
                cargo.illegalGoods[Good.Type.Biotech.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                break;
            case Poisonbreathers:
                cargo.illegalGoods[Good.Type.Narcotics.ordinal()] = true;
                break;                
            case Rockeaters:
                cargo.illegalGoods[Good.Type.Nanoparticles.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Androids.ordinal()] = true;
                break;
            case Terraneans:
                cargo.illegalGoods[Good.Type.Slaves.ordinal()] = true;
                cargo.illegalGoods[Good.Type.Narcotics.ordinal()] = true;
                break;                
        }

        // government can also override race defaults ....
        switch(station.society.governmentType)
        {
            case Anarchy:
                for(int i=0; i<cargo.illegalGoods.length; i++)
                {
                    cargo.illegalGoods[i] = false;
                }
                break;
                
        }

        // illegal goods are not available officially.
        for(int i=0; i<cargo.illegalGoods.length; i++)
        {
            if(cargo.illegalGoods[i])
            {
                cargo.goods[i].units = 0;
            }
        }
        
        
        return cargo;
    }

    private static double calculatePrice(Solar station,
                                         int [] metals,
                                         int [] minerals,
                                         final Good good)
    {
        double price = good.type.price;
        // Hajo: randomize and modify price.

        switch(station.society.governmentType)
        {
            case Anarchy:
                price *= 2.0;
                break;
            case Dictatorship:
                price *= 1.5;
                break;
        }

        if(Good.Type.IronMetals == good.type ||
           Good.Type.LightMetals == good.type ||
           Good.Type.HeavyMetals == good.type ||
           Good.Type.MetalCompounds == good.type ||
           Good.Type.NobleMetals == good.type ||
           Good.Type.NonIronMetals == good.type
                )
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++) {
                metalWealth += metals[i];
            }
            price = (price * 0.5) + (price * 2.0/(metalWealth+1));
            good.units = (int)((good.units * 0.2) + (good.units * metalWealth));
        }

        if(good.type == Good.Type.NobleMetals)
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isNobleMetal(i))
                {
                    metalWealth += metals[i];
                }
            }
            price = (price * 0.5) + (price * 1.0/(metalWealth+1));
            good.units = (int)((good.units * 0.3) + (good.units * metalWealth));
        }

        if(good.type == Good.Type.NonIronMetals)
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isNonIronMetal(i))
                {
                    metalWealth += metals[i];
                }
            }
            price = (price * 0.5) + (price * 1.0/(metalWealth+1));
            good.units = (int)((good.units * 0.3) + (good.units * metalWealth));
        }

        if(good.type == Good.Type.HeavyMetals)
        {
            int metalWealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isHeavyMetal(i))
                {
                    metalWealth += metals[i];
                }
            }
            price = (price * 0.5) + (price * 2.0/(metalWealth+1));
            good.units = (int)((good.units * 0.2) + (good.units * metalWealth));
        }


        if(good.type == Good.Type.RareEarths ||
           good.type == Good.Type.Crystalics ||
           good.type == Good.Type.Electronics ||
           good.type == Good.Type.CeramicMinerals
                )
        {
            int wealth = 0;
            for(int i=0; i<metals.length; i++)
            {
                if(PlanetResources.isRareEarth(i))
                {
                    wealth += minerals[i];
                }
            }
            price = (price * 0.5) + (price * 1.0/(wealth+1));
            good.units = (int)((good.units * 0.2) + (good.units * wealth * 2));
        }

        // Hajo: Tech level modifiers - some good will just not be available
        // below a certain tech level. Prices generally go down with raising
        // tech levels due to better manufacturing capabilities.

        price = 0.5 * price  + 0.5 * price * (17/(12+station.society.techLevel));
        good.units = (int)(0.5 * good.units  + 0.5 * good.units * station.society.techLevel);

        if(good.type == Good.Type.Hypertech)
        {
            if(station.society.techLevel < 21)
            {
                good.units /= 110;
            }
        }

        if(good.type == Good.Type.ArtificialIntelligence)
        {
            if(station.society.techLevel < 20)
            {
                good.units /= 90;
            }
        }

        if(good.type == Good.Type.Transurans)
        {
            if(station.society.techLevel < 16)
            {
                good.units /= 200;
            }
            else
            {
                good.units /= 100;
            }
        }

        if(good.type == Good.Type.Robots)
        {
            if(station.society.techLevel < 15)
            {
                good.units /= 15;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.Biotech)
        {
            if(station.society.techLevel < 14)
            {
                good.units /= 25;
            }
        }

        if(good.type == Good.Type.Crystalics)
        {
            if(station.society.techLevel < 13)
            {
                good.units /= 15;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.Nanoparticles)
        {
            if(station.society.techLevel < 12)
            {
                good.units /= 12;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.Electronics)
        {
            if(station.society.techLevel < 11)
            {
                good.units /= 10;
            }
            else
            {
                good.units *= 2;
            }
        }

        if(good.type == Good.Type.MetalCompounds)
        {
            if(station.society.techLevel < 10)
            {
                good.units /= 8;
            }
            else
            {
                good.units *= 3;
            }
        }

        if(good.type == Good.Type.Medicine)
        {
            if(station.society.techLevel < 9)
            {
                good.units /= 5 - 1;
            }
            else
            {
                good.units *= 2;
            }
        }

        // Hajo: Scarse goods become more expensive
        price = price + price * (10.0 / (10 + good.units));

        // Hajo: also, take care of daily fluctuations.
        int day = ClockThread.getDayOfMonth() + ClockThread.getMonthOfYear() * 30;
        Random arng = RandomHelper.createRNG(station.seed);
        for(int i=0; i<day; i++)
        {
            arng.nextDouble();
        }
        final double luckyDay = arng.nextDouble();

        // Hajo: up to 10% fluctuation between days
        price = price + 0.1 * (price * (luckyDay - 0.5));

        return price;
    }

    /**
     * Station good storage
     */
    private Cargo storage;


    private SpaceStationPanel spaceStationPanel;

    /** Creates new form TradePanel */
    public TradePanel(Solar station, Ship ship, SpaceStationPanel spaceStationPanel)
    {
        this.spaceStationPanel = spaceStationPanel;
        initComponents();

        ComponentFactory.customizeScrollpane(goodsScrollPane);
        
        storage = createCargo(station);

        titleLabel.setFont(FontFactory.getPanelHeading());
        titleLabel.setText(station.name + " Commodity Exchange");

        tradePanel.setBackground(ComponentFactory.boxBackground);

        cargoSpaceLabel.setFont(FontFactory.getLabelHeading());
        cashLabel.setFont(FontFactory.getLabelHeading());
        cargoSpaceField.setFont(FontFactory.getLarger());
        cashField.setFont(FontFactory.getLarger());

        descriptionLabel.setFont(FontFactory.getLabelHeading());
        descriptionField.setFont(FontFactory.getLarger());
        descriptionField.setBackground(ComponentFactory.boxBackground);
        

        jLabel1.setFont(FontFactory.getLabelHeading());
        jLabel2.setFont(FontFactory.getLabelHeading());
        jLabel3.setFont(FontFactory.getLabelHeading());
        jLabel4.setFont(FontFactory.getLabelHeading());
        jLabel5.setFont(FontFactory.getLabelHeading());
        
        for(int i=0; i<Good.Type.values().length; i++)
        {
            GoodLine line =
                    new GoodLine(station, ship, storage.goods[i]);
            tradePanel.add(line);
        }

        updateGoodDesc(0);
        
        ComponentFactory.customizeButton(loungeButton);
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

        borderPanel = new javax.swing.JPanel();
        goodsScrollPane = new javax.swing.JScrollPane();
        tradePanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cargoSpaceLabel = new javax.swing.JLabel();
        cargoSpaceField = new javax.swing.JLabel();
        cashLabel = new javax.swing.JLabel();
        cashField = new javax.swing.JLabel();
        loungeButton = new javax.swing.JButton();
        descriptionLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        descriptionField = new javax.swing.JLabel();

        setBackground(java.awt.Color.black);
        setForeground(java.awt.Color.green);
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        borderPanel.setBackground(new java.awt.Color(0, 51, 51));
        borderPanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray));
        borderPanel.setForeground(java.awt.Color.green);
        borderPanel.setMinimumSize(new java.awt.Dimension(800, 540));
        borderPanel.setPreferredSize(new java.awt.Dimension(800, 540));
        borderPanel.setLayout(null);

        goodsScrollPane.setBackground(java.awt.Color.darkGray);
        goodsScrollPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        tradePanel.setBackground(java.awt.Color.darkGray);
        tradePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tradePanelMouseClicked(evt);
            }
        });
        tradePanel.setLayout(new javax.swing.BoxLayout(tradePanel, javax.swing.BoxLayout.Y_AXIS));
        goodsScrollPane.setViewportView(tradePanel);

        borderPanel.add(goodsScrollPane);
        goodsScrollPane.setBounds(20, 80, 510, 400);

        titleLabel.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        titleLabel.setForeground(java.awt.Color.white);
        titleLabel.setText("XYZ Commodity Exchange");
        borderPanel.add(titleLabel);
        titleLabel.setBounds(20, 10, 420, 40);

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel1.setForeground(java.awt.Color.green);
        jLabel1.setText("Commodity");
        borderPanel.add(jLabel1);
        jLabel1.setBounds(20, 60, 170, 18);

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel2.setForeground(java.awt.Color.green);
        jLabel2.setText(" Price");
        borderPanel.add(jLabel2);
        jLabel2.setBounds(220, 60, 50, 18);

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel3.setForeground(java.awt.Color.green);
        jLabel3.setText(" Cargo   Avg. Price");
        borderPanel.add(jLabel3);
        jLabel3.setBounds(390, 60, 140, 18);

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel4.setForeground(java.awt.Color.green);
        jLabel4.setText(" Stock");
        borderPanel.add(jLabel4);
        jLabel4.setBounds(270, 60, 50, 18);

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel5.setForeground(java.awt.Color.green);
        jLabel5.setText("    Sell Buy");
        borderPanel.add(jLabel5);
        jLabel5.setBounds(310, 60, 90, 18);

        cargoSpaceLabel.setForeground(java.awt.Color.green);
        cargoSpaceLabel.setText("Cargo space left:");
        borderPanel.add(cargoSpaceLabel);
        cargoSpaceLabel.setBounds(210, 490, 140, 40);

        cargoSpaceField.setForeground(java.awt.Color.white);
        cargoSpaceField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cargoSpaceField.setText("0");
        borderPanel.add(cargoSpaceField);
        cargoSpaceField.setBounds(300, 490, 110, 40);

        cashLabel.setForeground(java.awt.Color.green);
        cashLabel.setText("Cash:");
        borderPanel.add(cashLabel);
        cashLabel.setBounds(440, 490, 70, 40);

        cashField.setForeground(java.awt.Color.white);
        cashField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cashField.setText("0");
        borderPanel.add(cashField);
        cashField.setBounds(480, 490, 110, 40);

        loungeButton.setText("Return To Lounge");
        loungeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loungeButtonActionPerformed(evt);
            }
        });
        borderPanel.add(loungeButton);
        loungeButton.setBounds(20, 500, 170, 25);

        descriptionLabel.setForeground(java.awt.Color.green);
        descriptionLabel.setText("Description");
        borderPanel.add(descriptionLabel);
        descriptionLabel.setBounds(550, 60, 130, 20);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray));
        jPanel1.setLayout(new java.awt.BorderLayout());

        descriptionField.setBackground(java.awt.Color.darkGray);
        descriptionField.setForeground(java.awt.Color.white);
        descriptionField.setText("jLabel6");
        descriptionField.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        descriptionField.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 1, 1));
        descriptionField.setOpaque(true);
        jPanel1.add(descriptionField, java.awt.BorderLayout.CENTER);

        borderPanel.add(jPanel1);
        jPanel1.setBounds(550, 80, 210, 400);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(borderPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void loungeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loungeButtonActionPerformed
        Container c = getParent();
        c.remove(this);
        c.add(spaceStationPanel);
        c.validate();
        c.repaint();
    }//GEN-LAST:event_loungeButtonActionPerformed

    private void tradePanelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tradePanelMouseClicked
    {//GEN-HEADEREND:event_tradePanelMouseClicked
        int y = evt.getY();
        
        y /= 24;
        
        updateGoodDesc(y);
    }
    
    private void updateGoodDesc(int goodIndex)
    {
        Good.Type type = Good.Type.values()[goodIndex];
        
        String text = 
                "<html>"
                + "<font color=" + type.color + "><b>" + type.toString() + "</b></font>"
                + "<br><br>Unit mass: "
                + type.massPerUnit + " kg"
                + "<br><br>Acceptance: "
                + (storage.illegalGoods[goodIndex] ? "<font color=#FF4411>Prohibited</font>" : "<font color=#33CC00>Good</font>")
                + "<br><br><p>" + type.description + "</p"
                + "</html>";
        
        descriptionField.setText(text);
    }//GEN-LAST:event_tradePanelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borderPanel;
    private javax.swing.JLabel cargoSpaceField;
    private javax.swing.JLabel cargoSpaceLabel;
    private javax.swing.JLabel cashField;
    private javax.swing.JLabel cashLabel;
    private javax.swing.JLabel descriptionField;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JScrollPane goodsScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loungeButton;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel tradePanel;
    // End of variables declaration//GEN-END:variables


    private class GoodLine extends JPanel
    {
        public JLabel nameLabel;
        public JLabel priceLabel;
        public JLabel cargoLabel;
        public JLabel stockLabel;
        public JLabel buyLabel;
        public final double price;
        public int cargo;

        private final NumberFormat nf = NumberFormat.getInstance();

        /**
         * Constructs a line (panel) for the storage list display
         * @param index Indexes into Good.Type
         * @see Good.Type
         */
        public GoodLine(Solar station,
                        final Ship ship,
                        final Good good)
        {
            setLayout(null);
            setOpaque(false);

            this.price = good.salesPrice;
            
            final String name = good.type.toString();

            nameLabel = new JLabel("<html><font color=" + good.type.color + "><b>" + name + "</b></font></html>");
            nameLabel.setSize(180, 24);
            nameLabel.setLocation(8, 0);
            nameLabel.setFont(FontFactory.getNormal());
            add(nameLabel);

            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
        
            priceLabel = new JLabel(nf.format(price));
            priceLabel.setSize(56, 24);
            priceLabel.setLocation(180, 0);
            priceLabel.setForeground(Color.WHITE);
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            priceLabel.setFont(FontFactory.getNormal());
            add(priceLabel);

            stockLabel = new JLabel("" + good.units);
            stockLabel.setSize(42, 24);
            stockLabel.setLocation(244, 0);
            stockLabel.setForeground(Color.WHITE);
            stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            stockLabel.setFont(FontFactory.getNormal());
            add(stockLabel);

            JButton minus = new JButton("<");
            minus.setBackground(Color.GRAY);
            minus.setSize(22, 22);
            minus.setLocation(310, 1);
            minus.setMargin(new Insets(0,0,0,0));
            add(minus);

            JButton plus = new JButton(">");
            plus.setBackground(Color.GRAY);
            plus.setSize(22, 22);
            plus.setLocation(338, 1);
            plus.setMargin(new Insets(0,0,0,0));
            add(plus);

            if(ship.getState() != Ship.State.DOCKED ||
               ship.spaceBodySeed != station.seed)
            {
                plus.setEnabled(false);
                minus.setEnabled(false);
            }

            cargoLabel = new JLabel("" + ship.cargo.goods[good.type.ordinal()].units);
            cargoLabel.setSize(42, 24);
            cargoLabel.setLocation(366, 0);
            cargoLabel.setForeground(Color.WHITE);
            cargoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cargoLabel.setFont(FontFactory.getNormal());
            add(cargoLabel);

            buyLabel = new JLabel();
            buyLabel.setSize(48, 24);
            buyLabel.setLocation(420, 0);
            buyLabel.setForeground(Color.LIGHT_GRAY);
            buyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            buyLabel.setFont(FontFactory.getNormal());
            add(buyLabel);

            setSize(460, 24);
            setPreferredSize(getSize());

            // update labels
            transfer(ship, good.type.ordinal(), 0);

            plus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(good.units > 0 && 
                       ship.cargo.availableSpace() > 0 &&
                       ship.cargo.money >= price) {
                        transfer(ship, good.type.ordinal(), 1);
                    }
                }
            });

            minus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(ship.cargo.goods[good.type.ordinal()].units > 0) {
                        transfer(ship, good.type.ordinal(), -1);
                    }
                }
            });
        }


        private String getUnitColor(int units)
        {
            // String color = "<html><font color=#33ff33>";
            String color = "<html><font color=#ffffff>";
            
            if(units == 0) {
                color = "<html><font color=#ff5555>";
            }
            else if(units <= 1) {
                color = "<html><font color=#ff9933>";
            }
            else if(units <= 3) {
                color = "<html><font color=#ffff00>";
            }

            return color;
        }

        private void transfer(Ship ship, int index, int amount)
        {
            if(amount + ship.cargo.goods[index].units == 0) 
            {
                buyLabel.setText("");
            }
            else 
            {
                if(amount > 0)
                {
                    double avg = ship.cargo.goods[index].averagePrice;
                    avg = (avg * ship.cargo.goods[index].units + price * amount) /
                        (ship.cargo.goods[index].units + amount);
                    buyLabel.setText(nf.format(avg));
                    ship.cargo.goods[index].averagePrice = avg;
                }
            }

            String color;

            ship.cargo.goods[index].units += amount;
            color = getUnitColor(ship.cargo.goods[index].units);
            cargoLabel.setText(color + ship.cargo.goods[index].units + "</font></html>");

            storage.goods[index].units -= amount;
            color = getUnitColor(storage.goods[index].units);
            stockLabel.setText(color + storage.goods[index].units + "</font></html>");

            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);

            ship.cargo.money -= price * amount;

            cashField.setText(nf.format(ship.cargo.money) + " Cr");

            int space = ship.cargo.availableSpace();
            cargoSpaceField.setText("" + space + " kg");
        }
    }
}
