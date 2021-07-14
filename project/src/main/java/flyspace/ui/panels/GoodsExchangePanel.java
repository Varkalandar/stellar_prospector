package flyspace.ui.panels;

import flyspace.FlySpace;
import flyspace.ui.Colors;
import flyspace.ui.DecoratedTrigger;
import flyspace.ui.Fonts;
import flyspace.ui.PixFont;
import flyspace.ui.Trigger;
import static flyspace.ui.UiPanel.fillBorder;
import static flyspace.ui.UiPanel.fillRect;
import java.text.NumberFormat;
import flyspace.ui.Mouse;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import solarex.ship.Cargo;
import solarex.ship.Good;
import solarex.ship.Ship;
import solarex.system.Solar;
import solarex.ui.panels.TradePanel;

/**
 *
 * @author Hj. Malthaner
 */
public class GoodsExchangePanel extends DecoratedUiPanel
{
    /**
     * Station good storage
     */
    private Cargo storage;
    private Solar station;
    private final FlySpace game;
    private final NumberFormat nf;
    private final Ship ship;
    
    private int selectedGood;
    private DecoratedTrigger buy;
    private DecoratedTrigger sell;
    private boolean clicked;
    private final DecoratedTrigger loungeTrigger;
    
    public GoodsExchangePanel(FlySpace game, Ship ship) 
    {
        this.game = game;
        this.ship = ship;
        this.nf = NumberFormat.getNumberInstance();
                
        buy = new DecoratedTrigger(Fonts.g17, "Buy", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        buy.setArea(65, 200, 120, 26);
        
        sell = new DecoratedTrigger(Fonts.g17, "Sell", Colors.TRIGGER, Colors.TRIGGER_TEXT);
        sell.setArea(250, 200, 120, 26);

        loungeTrigger = new DecoratedTrigger(Fonts.g17, "Return to Lounge", 0x33FF7700, 0xFFEE6600);
        loungeTrigger.setArea(934, 200, 185, 24);

        addTrigger(buy);
        addTrigger(sell);
        addTrigger(loungeTrigger);
    }

    public void setStation(Solar station)
    {
        this.station = station;
        
        storage = TradePanel.createCargo(station);
    }
    
    
    
    @Override
    public void activate() 
    {
        setupTextPanel(width, height);
    }

    @Override
    public void handleInput() 
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
                
                if(t == null)
                {
                    // Hajo: todo - max bounds
                    if(mx > 50 && my > 240)
                    {
                        // Hajo: check list
                        int lx = mx - 50;
                        int column = lx / 280;
                        int ly = my - 240;

                        int row = 15 - (ly / 24);

                        selectedGood = column * 15 + row;
                    }
                }
                else if(t == buy)
                {
                    if(storage.goods[selectedGood].salesPrice < ship.cargo.money)
                    {
                        storage.goods[selectedGood].units --;
                        ship.cargo.goods[selectedGood].units ++;
                        ship.cargo.money -= storage.goods[selectedGood].salesPrice;
                    }
                }
                else if(t == sell)
                {
                    if(ship.cargo.goods[selectedGood].units > 0 &&
                       storage.illegalGoods[selectedGood] == false)
                    {
                        storage.goods[selectedGood].units ++;
                        ship.cargo.goods[selectedGood].units --;
                        ship.cargo.money += storage.goods[selectedGood].salesPrice;
                    }
                }
                else if(t == loungeTrigger)
                {
                    game.showStationPanel();
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
        displayBackground(0xFF331100, 0xFF442211, 0xFF553311);

        PixFont font = Fonts.g12;
        
        displayTitle(station.name + " Goods Exchange");
        
        int leftColumn = 50;
        int middleColumn = 330;
        int rightColumn = 610;
        
        fillRect(leftColumn, 245, 830, 395, Colors.LIST_BG);
        fillBorder(leftColumn, 245, 830, 395, 1, Colors.LIST_BORDER);
        
        fillRect(910, 245, 224, 395, Colors.LIST_BG);
        fillBorder(910, 245, 224, 395, 1, Colors.LIST_BORDER);
        
        for(int i=0; i<15; i++)
        {
            displayGood(storage.goods[i], leftColumn, 590 - i*24);
        }
        
        for(int i=15; i<30; i++)
        {
            displayGood(storage.goods[i], middleColumn, 590 - (i-15)*24);
        }
        
        for(int i=30; i<Good.Type.values().length; i++)
        {
            displayGood(storage.goods[i], rightColumn, 590 - (i-30)*24);
        }
        
        font.drawString("Status:", Colors.TEXT, 919, 590);
        if(storage.illegalGoods[selectedGood])
        {
            font.drawString("Trade prohibited", Colors.RED, 965, 590);
        }
        else
        {
            font.drawString("Trade allowed", Colors.GREEN, 965, 590);
        }
            
        
        font.drawText(storage.goods[selectedGood].type.description, Colors.TEXT, 919, 550, 206, 1.0f);
  
        font.drawString("Cash:", Colors.LABEL, 480, 190);
        font.drawString(nf.format(ship.cargo.money) + "$", Colors.TEXT, 520, 190);
        
        font.drawString("Payload left:", Colors.LABEL, 610, 190);
        font.drawString("" + ship.cargo.availableSpace() + "kg", Colors.TEXT, 695, 190);

        
        displayTriggers();
        
    }

    private void displayGood(Good good, int x, int y) 
    {
        Cargo cargo = ship.cargo; 
        PixFont font = Fonts.g12;
        nf.setMaximumFractionDigits(2);
        
        
        int ord = good.type.ordinal();
        int color = Colors.fromString(good.type.color);
        
        if(selectedGood == ord)
        {
            fillRect(x+8, y+10, 260, 25, Colors.LIST_SELECT);
            fillBorder(x+8, y+10, 260, 25, 1, Colors.LIST_SELECT_BORDER);
        }
        
        
        font.drawString(good.type.toString(), color, x+15, y);

        String avail = "" + good.units;
        int aw = font.getStringWidth(avail);
        
        font.drawString(avail, color, x+172-aw, y);
        
        String have = "" + cargo.goods[ord].units;
        int hw = font.getStringWidth(have);
        font.drawString(have, color, x+204-hw, y);

        String price = nf.format(good.salesPrice) + " $";
        int pw = font.getStringWidth(price);
        
        font.drawString(price, color, x+260-pw, y);
    }

}
