package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.HTMLHelper;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import java.text.NumberFormat;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import solarex.quest.Quest;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.ship.components.ShipComponent;
import solarex.system.Solar;


/**
 *
 * @author Hj. Malthaner
 */
public class ShipInfoPanel extends DecoratedUiPanel
{
    /**
     * Station good storage
     */
    private Solar station;
    private final FlySpace game;
    private final Ship ship;
    
    private boolean clicked;
    private final DecoratedTrigger loungeTrigger;
    
    private final NumberFormat nf;
    
    public ShipInfoPanel(FlySpace game, Ship ship) 
    {
        super(null);

        this.game = game;
        this.ship = ship;

        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", 0x33FF7700, 0xFFEE6600);
        loungeTrigger.setArea(934, 200, 185, 24);
        addTrigger(loungeTrigger);
    
        nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
    
    }

    public void setStation(Solar station)
    {
        this.station = station;
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
                
                clicked = false;
            }
        }
    }

    @Override
    public void displayPanel() 
    {        
        glClear(GL_COLOR_BUFFER_BIT);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        displayBackground(0xFF332200, 0xFF443311, 0xFF554411);

        displayTitle("Ship Details");
        
        // Fonts.g32.drawStringScaled("The prospectors office is currently closed.", Colors.TEXT, 250, 500, 0.8f);

        PixFont font = Fonts.g12;
        
        int left = 50;
        int col1 = 400;
        int col2 = 740;
        
        font.drawStringBold(ship.type.shipName + ", a \"" + 
                ship.type.shipClass + "\" class Ship", Colors.ORANGE, left, 600, 1.0f);
        
        font.drawText(ship.type.shipDesc, Colors.TEXT, left, 560, 300, 1.0f);
        
        int yoff = 360;

        int leftFields = left + 150;
        font.drawStringBold("Ship mass:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString("" + nf.format(ship.getCurrentMass()) + " / " + nf.format(ship.type.totalMass) + " t",
                Colors.FIELD, leftFields, yoff);
        yoff -= 18;

        font.drawStringBold("Drive range:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString("" + nf.format(ship.equipment.getEffectiveDriveRange(ship.getCurrentMass())) + " ly",
                Colors.FIELD, leftFields, yoff);
        yoff -= 18;

        font.drawStringBold("Used Space:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString("" + 
                                 nf.format(ship.equipment.getMass() * 1000 + 
                                  ship.cargo.totalSpaceUsedByGoods()) +
                                 " / " + 
                                 nf.format(ship.type.equipmentSpace * 1000) + " kg",
                Colors.FIELD, leftFields, yoff);
        yoff -= 18;

        font.drawStringBold("Passenger Cap.:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString("" + ship.getFreePassengerCapacity() + " / " + 
                            ship.equipment.getPassengerCapacity() + " persons",
                Colors.FIELD, leftFields, yoff);
        yoff -= 18;

        font.drawStringBold("Sensors:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString("Standard Optical",
                Colors.FIELD, leftFields, yoff);
        yoff -= 18;

        font.drawStringBold("Shielding:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString("Low Radiation",
                Colors.FIELD, leftFields, yoff);
        yoff -= 18;
        yoff -= 9;

        font.drawStringBold("Cash:", Colors.LABEL, left, yoff, 1.0f);
        font.drawString(nf.format(ship.cargo.money) + " $",
                Colors.FIELD, leftFields, yoff);

        yoff = 600;
        
        font.drawStringBold("Installed Equipment", Colors.LABEL, col1, yoff, 1.0f);
        fillRect(col1, yoff-200, 300, 200, Colors.LIST_BG);
        fillBorder(col1, yoff-200, 300, 200, 1, Colors.LIST_BORDER);

        yoff -= 200;
        yoff -= 18;
        yoff -= 18;
        yoff -= 9;
        font.drawStringBold("Equipment Details", Colors.LABEL, col1, yoff, 1.0f);
        fillRect(col1, yoff-150, 300, 150, Colors.LIST_BG);
        fillBorder(col1, yoff-150, 300, 150, 1, Colors.LIST_BORDER);
        
        yoff = 600;
        
        font.drawStringBold("Cargo", Colors.LABEL, col2, yoff, 1.0f);
        fillRect(col2, yoff-170, 400, 170, Colors.LIST_BG);
        fillBorder(col2, yoff-170, 400, 170, 1, Colors.LIST_BORDER);

        yoff -= 170;
        yoff -= 18;
        yoff -= 18;
        yoff -= 9;
        font.drawStringBold("Quests and Jobs", Colors.LABEL, col2, yoff, 1.0f);
        fillRect(col2, yoff-140, 400, 140, Colors.LIST_BG);
        fillBorder(col2, yoff-140, 400, 140, 1, Colors.LIST_BORDER);
        
        displayEquipmentList(col1+10, 560);
        displayCargoList(col2+10, 560);
        displayQuestList(col2+10, 350);
        
        displayTriggers();
        
    }

    
    private void displayEquipmentList(int left, int top)
    {
        PixFont font = Fonts.g12;
        
        for(ShipComponent comp : ship.equipment.components)
        {
            // font.drawString(comp.getName(), Colors.TEXT, left, top);
            HTMLHelper.displayHTMLLine(font, comp.getName(), Colors.TEXT, left, top);
            top -= 18;
        }
    }
    
    
    private void displayCargoList(int left, int top) 
    {
        PixFont font = Fonts.g12;
        
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

                HTMLHelper.displayHTMLLine(font, "" + 
                            good.units + " unit" + plural + " of " + 
                            " <font color='" + good.type.color + "'>" +
                            good.type.toString().toLowerCase() +
                            "</font> (" + mass + " kg)", Colors.TEXT, left, top);
                top -= 18;
            }
        }
        
        if(!found)
        {
            font.drawString("No goods in cargo hold.", Colors.TEXT, left, top);
        }
    }
    
    private void displayQuestList(int left, int top) 
    {
        PixFont font = Fonts.g12;

        boolean found = false;
        for(Quest quest : ship.player.getQuests())
        {
            // font.drawString(quest.getQuestHeadline(), Colors.TEXT, left, top);
            HTMLHelper.displayHTMLLine(font, quest.getQuestHeadline(), Colors.TEXT, left, top);
            top -= 18;
            found = true;
        }
        
        if(!found)
        {
            font.drawString("No quests.", Colors.TEXT, left, top);
        }
    }
}
