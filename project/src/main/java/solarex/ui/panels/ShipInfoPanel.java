/*
 * ShipInfoPanel.java
 *
 * Created: 07.05.2010
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */

package solarex.ui.panels;

import java.text.NumberFormat;
import javax.swing.DefaultListModel;
import solarex.quest.Quest;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.ship.components.ShipComponent;
import solarex.ui.components.EquipmentWrapper;
import solarex.ui.components.QuestWrapper;
import solarex.ui.ComponentFactory;
import solarex.ui.FontFactory;

/**
 *
 * @author Hj. Malthaner
 */
public class ShipInfoPanel extends javax.swing.JPanel {

    /** Creates new form ShipInfoPanel */
    public ShipInfoPanel(Ship ship) 
    {
        ship.recalculateCargoSpace();
        
        initComponents();

        ComponentFactory.customizeScrollpane(jScrollPane1);
        ComponentFactory.customizeScrollpane(jScrollPane2);
        ComponentFactory.customizeScrollpane(jScrollPane3);
        ComponentFactory.customizeScrollpane(jScrollPane4);
        
        ComponentFactory.customizeList(equipmentList, 2);
        ComponentFactory.customizeList(cargoList, 1);
        ComponentFactory.customizeList(questsList, 1);

        equipmentDetailArea.setBackground(ComponentFactory.boxBackground);
        
        titleLabel.setFont(FontFactory.getPanelHeading());
        shipNameField.setFont(FontFactory.getLabelHeading());

        cargoModuleLabel.setFont(FontFactory.getLabelHeading());
        massLabel.setFont(FontFactory.getLabelHeading());
        driveLabel1.setFont(FontFactory.getLabelHeading());
        cabinsLabel.setFont(FontFactory.getLabelHeading());
        sensorsLabel.setFont(FontFactory.getLabelHeading());
        cargoLabel.setFont(FontFactory.getLabelHeading());
        questsLabel.setFont(FontFactory.getLabelHeading());
        equipmentDetailsLabel.setFont(FontFactory.getLabelHeading());
        shieldingLabel.setFont(FontFactory.getLabelHeading());
        cashLabel.setFont(FontFactory.getLabelHeading());
        installedEquipmentLabel1.setFont(FontFactory.getLabelHeading());

        cargoModuleField.setFont(FontFactory.getLarger());
        massField.setFont(FontFactory.getLarger());
        driveField1.setFont(FontFactory.getLarger());
        cabinsField.setFont(FontFactory.getLarger());
        sensorsField.setFont(FontFactory.getLarger());
        shieldingField1.setFont(FontFactory.getLarger());
        cashField.setFont(FontFactory.getLarger());

        descriptionField.setFont(FontFactory.getLarger());
        
        equipmentDetailArea.setFont(FontFactory.getLarger());

        shipNameField.setText(ship.type.shipName +
                ", a \"" + ship.type.shipClass + "\" class ship.");
        descriptionField.setText("<html>" + ship.type.shipDesc + "</html>");
        
        fillEquipmentList(ship);
        fillCargoList(ship);
        fillQuestsList(ship);
    }

    private void fillEquipmentList(Ship ship)
    {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        final DefaultListModel shipModel = new DefaultListModel();

        for(final ShipComponent comp : ship.equipment.components)
        {
            shipModel.addElement(new EquipmentWrapper(comp));
        }
        
        equipmentList.setModel(shipModel);
        
        massField.setText("" + nf.format(ship.getCurrentMass()) + " / " + nf.format(ship.type.totalMass) + " t");
        driveField1.setText("" + nf.format(ship.equipment.getEffectiveDriveRange(ship.getCurrentMass())) + " ly");
        cabinsField.setText("" + ship.getFreePassengerCapacity() + " / " + 
                            ship.equipment.getPassengerCapacity() + " persons");
                

        cashField.setText(nf.format(ship.cargo.money) + " Cr");
        
        nf.setMaximumFractionDigits(0);
        cargoModuleField.setText("" + 
                                 nf.format(ship.equipment.getMass() * 1000 + 
                                  ship.cargo.totalSpaceUsedByGoods()) +
                                 " / " + 
                                 nf.format(ship.type.equipmentSpace * 1000) + " kg");
    }
    
    private void fillCargoList(Ship ship)
    {
        DefaultListModel model = new DefaultListModel();
        boolean found = false;
        for(Good good : ship.cargo.goods)
        {
            if(good.units > 0)
            {
                found = true;

                final int mass = good.units * good.type.massPerUnit;
                String plural = "";
                if(good.units > 1)
                {
                    plural = "s";
                }

                model.addElement("<html><font color='#CCCCCC'>&nbsp;" + 
                            good.units + " unit" + plural + " of " + 
                            " <font color='" + good.type.color + "'>" +
                            good.type.toString().toLowerCase() +
                            "</font> (" + mass + " kg)</font></html>");
            }
        }
        
        if(!found)
        {
            model.addElement("<html>&nbsp;No goods.</html>");
        }
        
        cargoList.setModel(model);
    }
    
    private void fillQuestsList(Ship ship)
    {
        DefaultListModel model = new DefaultListModel();
        boolean found = false;
        for(Quest quest : ship.player.getQuests())
        {
            QuestWrapper w = new QuestWrapper(quest);
            model.addElement(w);
            found = true;
        }
        
        if(!found)
        {
            model.addElement(" No quests.");
        }
        
        questsList.setModel(model);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contenPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        cashField = new javax.swing.JLabel();
        shipNameField = new javax.swing.JLabel();
        cashLabel = new javax.swing.JLabel();
        massLabel = new javax.swing.JLabel();
        massField = new javax.swing.JLabel();
        cargoModuleLabel = new javax.swing.JLabel();
        cargoModuleField = new javax.swing.JLabel();
        cabinsLabel = new javax.swing.JLabel();
        cabinsField = new javax.swing.JLabel();
        descriptionField = new javax.swing.JLabel();
        sensorsLabel = new javax.swing.JLabel();
        sensorsField = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cargoList = new javax.swing.JList();
        cargoLabel = new javax.swing.JLabel();
        shieldingField1 = new javax.swing.JLabel();
        shieldingLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        equipmentList = new javax.swing.JList();
        equipmentDetailsLabel = new javax.swing.JLabel();
        questsLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        questsList = new javax.swing.JList();
        installedEquipmentLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        equipmentDetailArea = new javax.swing.JLabel();
        driveLabel1 = new javax.swing.JLabel();
        driveField1 = new javax.swing.JLabel();

        setBackground(java.awt.Color.black);
        setForeground(java.awt.Color.green);
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        contenPanel.setBackground(new java.awt.Color(33, 33, 0));
        contenPanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray));
        contenPanel.setMinimumSize(new java.awt.Dimension(880, 586));
        contenPanel.setPreferredSize(new java.awt.Dimension(880, 586));
        contenPanel.setLayout(null);

        titleLabel.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        titleLabel.setForeground(java.awt.Color.white);
        titleLabel.setText("Ship Information");
        contenPanel.add(titleLabel);
        titleLabel.setBounds(20, 10, 420, 40);

        cashField.setForeground(java.awt.Color.white);
        cashField.setText("0");
        contenPanel.add(cashField);
        cashField.setBounds(150, 220, 240, 30);

        shipNameField.setForeground(java.awt.Color.orange);
        shipNameField.setText("Space Bug, a \"Tin Can\" class ship. ");
        contenPanel.add(shipNameField);
        shipNameField.setBounds(20, 50, 270, 30);

        cashLabel.setForeground(java.awt.Color.green);
        cashLabel.setText("Cash:");
        contenPanel.add(cashLabel);
        cashLabel.setBounds(20, 220, 80, 30);

        massLabel.setForeground(java.awt.Color.green);
        massLabel.setText("Ship mass:");
        contenPanel.add(massLabel);
        massLabel.setBounds(20, 90, 100, 30);

        massField.setForeground(java.awt.Color.white);
        massField.setText("65");
        contenPanel.add(massField);
        massField.setBounds(150, 90, 290, 30);

        cargoModuleLabel.setForeground(java.awt.Color.green);
        cargoModuleLabel.setText("Used Space:");
        contenPanel.add(cargoModuleLabel);
        cargoModuleLabel.setBounds(20, 130, 100, 30);

        cargoModuleField.setForeground(java.awt.Color.white);
        cargoModuleField.setText("Standard cargo module (5t)");
        contenPanel.add(cargoModuleField);
        cargoModuleField.setBounds(150, 130, 290, 30);

        cabinsLabel.setForeground(java.awt.Color.green);
        cabinsLabel.setText("Passenger Cap.:");
        contenPanel.add(cabinsLabel);
        cabinsLabel.setBounds(20, 150, 130, 30);

        cabinsField.setForeground(java.awt.Color.white);
        cabinsField.setText("None (1 max.)");
        contenPanel.add(cabinsField);
        cabinsField.setBounds(150, 150, 290, 30);

        descriptionField.setForeground(java.awt.Color.lightGray);
        descriptionField.setText("Ship description.");
        descriptionField.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        contenPanel.add(descriptionField);
        descriptionField.setBounds(450, 20, 370, 170);

        sensorsLabel.setForeground(java.awt.Color.green);
        sensorsLabel.setText("Sensors:");
        contenPanel.add(sensorsLabel);
        sensorsLabel.setBounds(20, 170, 100, 30);

        sensorsField.setForeground(java.awt.Color.white);
        sensorsField.setText("Standard optical sensors");
        contenPanel.add(sensorsField);
        sensorsField.setBounds(150, 170, 240, 30);

        cargoList.setBackground(java.awt.Color.darkGray);
        cargoList.setForeground(java.awt.Color.lightGray);
        cargoList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(cargoList);

        contenPanel.add(jScrollPane1);
        jScrollPane1.setBounds(450, 210, 370, 190);

        cargoLabel.setForeground(java.awt.Color.green);
        cargoLabel.setText("Cargo");
        contenPanel.add(cargoLabel);
        cargoLabel.setBounds(450, 180, 250, 30);

        shieldingField1.setForeground(java.awt.Color.white);
        shieldingField1.setText("Low radiation shielding");
        contenPanel.add(shieldingField1);
        shieldingField1.setBounds(150, 190, 290, 30);

        shieldingLabel.setForeground(java.awt.Color.green);
        shieldingLabel.setText("Shielding:");
        contenPanel.add(shieldingLabel);
        shieldingLabel.setBounds(20, 190, 100, 30);

        equipmentList.setBackground(java.awt.Color.darkGray);
        equipmentList.setForeground(java.awt.Color.lightGray);
        equipmentList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        equipmentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                equipmentListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(equipmentList);

        contenPanel.add(jScrollPane2);
        jScrollPane2.setBounds(20, 280, 400, 150);

        equipmentDetailsLabel.setForeground(java.awt.Color.green);
        equipmentDetailsLabel.setText("Equipment Details");
        contenPanel.add(equipmentDetailsLabel);
        equipmentDetailsLabel.setBounds(20, 440, 250, 30);

        questsLabel.setForeground(java.awt.Color.green);
        questsLabel.setText("Quests and jobs");
        contenPanel.add(questsLabel);
        questsLabel.setBounds(450, 410, 250, 30);

        questsList.setBackground(java.awt.Color.darkGray);
        questsList.setForeground(java.awt.Color.lightGray);
        questsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(questsList);

        contenPanel.add(jScrollPane3);
        jScrollPane3.setBounds(450, 440, 370, 130);

        installedEquipmentLabel1.setForeground(java.awt.Color.green);
        installedEquipmentLabel1.setText("Installed Equipment");
        contenPanel.add(installedEquipmentLabel1);
        installedEquipmentLabel1.setBounds(20, 250, 250, 30);

        equipmentDetailArea.setBackground(new java.awt.Color(64, 64, 64));
        equipmentDetailArea.setForeground(new java.awt.Color(192, 192, 192));
        equipmentDetailArea.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        equipmentDetailArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        equipmentDetailArea.setOpaque(true);
        jScrollPane4.setViewportView(equipmentDetailArea);

        contenPanel.add(jScrollPane4);
        jScrollPane4.setBounds(20, 470, 400, 100);

        driveLabel1.setForeground(java.awt.Color.green);
        driveLabel1.setText("Drive Range:");
        contenPanel.add(driveLabel1);
        driveLabel1.setBounds(20, 110, 100, 30);

        driveField1.setForeground(java.awt.Color.white);
        driveField1.setText("Short range interstellar jump drive");
        contenPanel.add(driveField1);
        driveField1.setBounds(150, 110, 290, 30);

        add(contenPanel, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void equipmentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_equipmentListValueChanged
        Object o = equipmentList.getSelectedValue();
        if(o != null)
        {
            EquipmentWrapper wrap = (EquipmentWrapper)o;
            showEquipmentDetails(wrap);
        }

    }//GEN-LAST:event_equipmentListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cabinsField;
    private javax.swing.JLabel cabinsLabel;
    private javax.swing.JLabel cargoLabel;
    private javax.swing.JList cargoList;
    private javax.swing.JLabel cargoModuleField;
    private javax.swing.JLabel cargoModuleLabel;
    private javax.swing.JLabel cashField;
    private javax.swing.JLabel cashLabel;
    private javax.swing.JPanel contenPanel;
    private javax.swing.JLabel descriptionField;
    private javax.swing.JLabel driveField1;
    private javax.swing.JLabel driveLabel1;
    private javax.swing.JLabel equipmentDetailArea;
    private javax.swing.JLabel equipmentDetailsLabel;
    private javax.swing.JList equipmentList;
    private javax.swing.JLabel installedEquipmentLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel massField;
    private javax.swing.JLabel massLabel;
    private javax.swing.JLabel questsLabel;
    private javax.swing.JList questsList;
    private javax.swing.JLabel sensorsField;
    private javax.swing.JLabel sensorsLabel;
    private javax.swing.JLabel shieldingField1;
    private javax.swing.JLabel shieldingLabel;
    private javax.swing.JLabel shipNameField;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables


    private void showEquipmentDetails(EquipmentWrapper wrapper) 
    {
        String info = wrapper.getDetailsString();
        equipmentDetailArea.setText(info);
    }
    
}
