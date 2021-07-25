package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.HTMLHelper;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import static flyspace.ui.UiPanel.fillBorder;
import static flyspace.ui.UiPanel.fillRect;
import java.text.NumberFormat;
import java.util.ArrayList;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import solarex.ship.Ship;
import solarex.ship.components.EquipmentFactory;
import solarex.ship.components.ShipComponent;
import solarex.system.Solar;

/**
 *
 * @author Hj. Malthaner
 */
public class EquipmentShopPanel extends DecoratedUiPanel
{
    /**
     * Station good storage
     */
    private Solar station;
    private final FlySpace game;
    private final Ship ship;
    
    private boolean clicked;
    private final DecoratedTrigger buyTrigger;
    private final DecoratedTrigger sellTrigger;
    private final DecoratedTrigger loungeTrigger;
    
    private final ArrayList<ShipComponent> availableEquipmentList;
    
    private ShipComponent selectedComponent;
    private int selectedList;
    private final NumberFormat nf;
    
    public EquipmentShopPanel(FlySpace game, Ship ship) 
    {
        super(null);

        this.game = game;
        this.ship = ship;

        availableEquipmentList = new ArrayList<>();
        
        nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        
        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", 
                Colors.TRIGGER_HOT, Colors.TRIGGER_HOT_TEXT);
        loungeTrigger.setArea(945, 200, 185, 24);
        addTrigger(loungeTrigger);

        buyTrigger = new DecoratedTrigger(Fonts.g17, "Buy >>", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        buyTrigger.setArea(480, 320, 220, 24);
        addTrigger(buyTrigger);

        sellTrigger = new DecoratedTrigger(Fonts.g17, "<< Sell", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        sellTrigger.setArea(480, 290, 220, 24);
        addTrigger(sellTrigger);
    }

    public void setStation(Solar station)
    {
        this.station = station;
        
        buildAvailableEquipmentList();
        
        // selectedComponent = ship.equipment.components.get(0);
        // selectedList = 1;
        selectedComponent = availableEquipmentList.get(0);
        selectedList = 0;
    }
    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
    }

    @Override
    public void handlePanelInput() 
    {
        if(Mouse.isButtonDown(0))
        {
            clicked = true;
        }
        
        if(Mouse.isButtonDown(0) == false)
        {
            if(clicked)
            {
                int mx = Mouse.getX();
                int my = Mouse.getY();
                
                Trigger t = trigger(mx, my);
                
                if(t == loungeTrigger)
                {
                    game.showStationPanel();
                }
                if(t == buyTrigger)
                {
                    buy();
                }
                if(t == sellTrigger)
                {
                    sell();
                }
                else
                {
                    handleListSelection(mx, my);
                }
                clicked = false;
            }
        }
    }

    @Override
    public void displayPanel() 
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        displayBackground(0xFF331100, 0xFF442211, 0xFF553311);

        displayTitle(station.name + " Equipment Shop");

        PixFont font = Fonts.g12;
        
        int left = 50;
        int mid = 480;
        int right = 710;
        
        font.drawStringBold("Available Equipment", Colors.LABEL, left, 600, 1.0f);
        
        fillRect(left, 600-360, 420, 360, Colors.LIST_BG);
        fillBorder(left, 600-360, 420, 360, 1, Colors.LIST_BORDER);
        
        font.drawStringBold("Installed Equipment", Colors.LABEL, right, 600, 1.0f);

        fillRect(right, 600-360, 420, 360, Colors.LIST_BG);
        fillBorder(right, 600-360, 420, 360, 1, Colors.LIST_BORDER);
        
        font.drawStringBold("Details", Colors.LABEL, mid, 600, 1.0f);

        fillRect(mid, 600-240, 220, 240, Colors.LIST_BG);
        fillBorder(mid, 600-240, 220, 240, 1, Colors.LIST_BORDER);
        
        displayAvailableEquipment(left+10, 560);
        displayInstalledEquipment(right+10, 560);
        displaySelectedComponent(mid+10, 560);

        font.drawStringBold("Cash:", Colors.LABEL, left, 200, 1.0f);
        font.drawString(nf.format(ship.cargo.money) + "$", Colors.FIELD, left+47, 200);

        font.drawStringBold("Space used:", Colors.LABEL, right, 200, 1.0f);
        font.drawString("" + ship.equipment.getMass() + "/" + ship.type.equipmentSpace + "t", Colors.FIELD, right+95, 200);
        
        
        displayTriggers();
        
    }

    private void buildAvailableEquipmentList() 
    {
        availableEquipmentList.clear();
        
        EquipmentFactory factory = new EquipmentFactory();
        ShipComponent component;
        
        for(EquipmentFactory.Component componentType : EquipmentFactory.Component.values())
        {
            component = factory.create(componentType);
            availableEquipmentList.add(component);
        }
    }
    
    private void displayAvailableEquipment(int left, int top) 
    {        
        int entry = 0;
        
        for(ShipComponent component : availableEquipmentList)
        {
            if(selectedComponent == component && selectedList == 0)
            {
                fillRect(left-2, top + 11 - entry * 20, 404, 22, Colors.LIST_SELECT);
                fillBorder(left-2, top + 11 - entry * 20, 404, 22, 1, Colors.LIST_SELECT_BORDER);
            }

            displayComponent(left, top, entry++, component);
        }
    }

    private void displayInstalledEquipment(int left, int top) 
    {
        int entry = 0;
        
        for(ShipComponent component : ship.equipment.components)
        {
            if(selectedComponent == component && selectedList == 1)
            {
                fillRect(left-2, top + 11 - entry * 20, 404, 22, Colors.LIST_SELECT);
                fillBorder(left-2, top + 11 - entry * 20, 404, 22, 1, Colors.LIST_SELECT_BORDER);
            }
            
            displayComponent(left, top, entry++, component);
        }
    }

    private void displayComponent(int left, int top, int entry, ShipComponent component) 
    {
        PixFont font = Fonts.g12;
        
        String line = component.getName();
        String [] parts = line.split("<br> ");
        
        int y = top - entry * 20;
        
        HTMLHelper.displayHTMLLine(font, parts[0], Colors.TEXT, left, y);
        font.drawString(parts[1], Colors.TEXT, left+160, y);
        font.drawString("" + component.getBasePrice() + "$", Colors.TEXT, left+340, y);
    }
    
    
    private void displaySelectedComponent(int left, int top)
    {
        PixFont font = Fonts.g12;
        int row = 0;
        int col1 = left+100;

        String line = selectedComponent.getName();
        String [] parts = line.split("<br> ");
        
        HTMLHelper.displayHTMLLine(font, parts[0], Colors.TEXT, left, top - row * 20);
        row++;

        font.drawString(parts[1], Colors.TEXT, left, top - row * 20);
        row++;
        
        // Hajo: extra space
        top -= 9;
        
        if(selectedComponent.getPassengerCapacity() > 0)
        {
            font.drawString("Passenger Cap.:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + selectedComponent.getPassengerCapacity(),
                    Colors.FIELD, col1, top - row * 20);
            row++;
        }

        if(selectedComponent.getHyperjumpRange() > 0)
        {
            font.drawString("Jump range:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + selectedComponent.getHyperjumpRange() + "ly per 100t",
                    Colors.FIELD, col1, top - row * 20);
            row++;
        }

        if(selectedComponent.getMass() > 0)
        {
            font.drawString("Mass:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + selectedComponent.getMass() + "t",
                    Colors.FIELD, col1, top - row * 20);
            row++;
        }

        if(selectedComponent.getMinOperatingTemp() > 0)
        {
            font.drawString("Min. oper. temp:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + selectedComponent.getMinOperatingTemp() + "k",
                    Colors.FIELD, col1, top - row * 20);
            row++;
        }
        
        
        if(selectedComponent.getMaxOperatingTemp() > 0)
        {
            font.drawString("Max. oper. temp:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + selectedComponent.getMaxOperatingTemp() + "k",
                    Colors.FIELD, col1, top - row * 20);
            row++;
        }

        if(selectedComponent.getBasePrice() > 0)
        {
            font.drawString("Price:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + selectedComponent.calculatePrice(station.society.techLevel) + ".00$",
                    Colors.FIELD, col1, top - row * 20);
            row++;
        }
        
        if(selectedComponent.getMaxDurability()> 1)
        {
            font.drawString("Durability:", Colors.LABEL, left, top - row * 20);
            font.drawString("" + 
                    selectedComponent.getCurrentDurability() + "/" +
                    selectedComponent.getMaxDurability(), Colors.FIELD, col1, top - row * 20);
            row++;
        }
        
    }

    private void handleListSelection(int mx, int my) 
    {
        int top = 590;
        int entry = (top - my) / 20;
        
        if(mx < 600 && my > 200 && my < top && entry < availableEquipmentList.size())
        {
            selectedList = 0;
            selectedComponent = availableEquipmentList.get(entry);
        }
        
        if(mx > 700 && my > 200 && my < top && entry < ship.equipment.components.size())
        {
            selectedList = 1;
            selectedComponent = ship.equipment.components.get(entry);
        }
    }

    private void buy()
    {
        double price = selectedComponent.calculatePrice(station.society.techLevel);
        if(selectedList == 0 && ship.cargo.money >= price)
        {
            ship.cargo.money -= price;
            ship.equipment.addComponent(selectedComponent);
        }
    }

    private void sell()
    {
        if(selectedList == 1)
        {
            double price = selectedComponent.calculatePrice(station.society.techLevel);
            ship.cargo.money += price;
            ship.equipment.removeComponent(selectedComponent);
        }
    }
}
