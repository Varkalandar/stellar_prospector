/*
 * ComponentFactory.java
 *
 * Created: 04-Apr-2012
 *
 * Author: Hj. Malthaner
 * Email:  h_malthaner@users.sourceforge.net
 *
 * See license.txt for license details
 */
package solarex.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.TableCellRenderer;

/**
 * Creates customized swing components.
 * 
 * @author Hj. Malthaner
 */
public class ComponentFactory 
{
    // private static Color buttonLitColor = new Color(190, 130, 0);
    // private static Color buttonLitColor = new Color(50, 30, 0);
    private static Color buttonLitColor = new Color(50, 35, 0);
    private static Color buttonMetalColor = new Color(160, 162, 168);
    public static Color boxBackground = new Color(16, 32, 48);
    
    public static void customizeButton(JButton button)
    {
        // button.setUI(new TestButtonUI());
        button.setUI(new BlueButtonUI());
        button.setFont(FontFactory.getLabelHeading());
    }

    public static void customizeScrollpane(JScrollPane scrolly)
    {
        scrolly.getVerticalScrollBar().setUI(new ScrollBarUI());
        scrolly.getHorizontalScrollBar().setUI(new ScrollBarUI());
        scrolly.setBackground(Color.BLACK);
        scrolly.setBorder(new LineBorder(Color.GRAY));
        
        scrolly.getVerticalScrollBar().setUnitIncrement(12);        
        scrolly.getHorizontalScrollBar().setUnitIncrement(12);        
    }

    public static void customizeTable(JTable table)
    {
        table.setDefaultRenderer(String.class, new MyCellRenderer());
        table.setFont(FontFactory.getLarger());
        table.setBackground(ComponentFactory.boxBackground);
        table.setGridColor(ComponentFactory.boxBackground);
        table.setForeground(Color.LIGHT_GRAY);
        table.setFillsViewportHeight(true);
        table.setRowHeight(20);
        table.setTableHeader(null);
        table.getColumnModel().getColumn(0).setMaxWidth(200);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
    }

    public static void customizeButton(JButton button, String iconFile)
    {
        button.setUI(new IconButtonUI());
        ImageIcon icon = ImageCache.createImageIcon("/solarex/resources/ui/" + iconFile, "");
        button.setIcon(icon);
        button.setText("");
        button.setPreferredSize(new Dimension(icon.getIconWidth()+2, icon.getIconHeight()+2));
        button.setOpaque(false);
    }

    public static void customizeList(JList list, int linesPerEntry)
    {
        list.setBackground(boxBackground);
        list.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 2, 1));
        list.setFont(FontFactory.getLarger());
        list.setForeground(java.awt.Color.lightGray);
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(21*linesPerEntry);
        list.setSelectionBackground(new java.awt.Color(0, 0, 51));
    }
    
    public static void customizeArea(JTextArea area)
    {
        area.setBackground(boxBackground);
        area.setFont(FontFactory.getLarger());
        area.setForeground(java.awt.Color.lightGray);
    }

    private static class TestButtonUI extends BasicButtonUI
    {
        @Override
        public void installUI(JComponent component)
        {
            BasicBorders.RolloverButtonBorder rb = 
                    new BasicBorders.RolloverButtonBorder(Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK);

            BevelBorder bb = new BevelBorder(BevelBorder.RAISED,
                                             buttonMetalColor.brighter(), buttonMetalColor,
                                             buttonMetalColor.darker(), buttonMetalColor);
            
            LineBorder lb = new LineBorder(Color.BLACK, 1);
            
            CompoundBorder c1 = new CompoundBorder(lb, bb);
            CompoundBorder c2 = new CompoundBorder(c1, rb);
            
            super.installUI(component);
            component.setForeground(buttonLitColor);
            component.setBackground(buttonMetalColor);
            component.setBorder(new CompoundBorder(c2, new EmptyBorder(1, 12, 1, 12)));
        }
    }

    private static class BlueButtonUI extends BasicButtonUI
    {
        private Color dark = new Color(8, 16, 32);
        private Color mid = new Color(48, 96, 128);
        private Color light = new Color(255, 170, 100);
        // private Color roll = new Color(16, 64, 96);
        private Color roll = new Color(96, 192, 255);
        private Color lines = new Color(72, 136, 192, 96);
        
        @Override
        public void installUI(JComponent component)
        {
            BasicBorders.RolloverButtonBorder rb = 
                    new BasicBorders.RolloverButtonBorder(roll, roll, Color.WHITE, roll);

            LineBorder bb = new LineBorder(dark, 1);
            
            CompoundBorder c2 = new CompoundBorder(bb, rb);
            
            super.installUI(component);
            component.setForeground(light);
            component.setBackground(mid);
            component.setBorder(new CompoundBorder(c2, new EmptyBorder(1, 12, 1, 12)));
        }
        
        @Override
        public void paint(Graphics gr, JComponent c)
        {
            super.paint(gr, c);
            
            gr.setColor(lines);
            
            final int w = c.getWidth()-10;
            for(int y=4; y<c.getHeight()-3; y+=3)
            {
                gr.fillRect(5, y, w, 1);
            }
        }
    }

    private static class IconButtonUI extends BasicButtonUI
    {
        private Color mid = new Color(48, 96, 128);
        private Color light = new Color(255, 170, 100);
        private Color roll = new Color(96, 192, 255);
        
        @Override
        public void installUI(JComponent component)
        {
            BasicBorders.RolloverButtonBorder rb = 
                    new BasicBorders.RolloverButtonBorder(roll, roll, Color.WHITE, roll);
            
            super.installUI(component);
            component.setForeground(light);
            component.setBackground(mid);
            component.setBorder(rb);
        }
    }

    private static class ScrollBarUI extends BasicScrollBarUI
    {
        @Override
        public void installUI(JComponent c)
        {
            super.installUI(c);
            
            c.remove(incrButton);
            c.remove(decrButton);
            
            // c.setBackground(Color.BLACK);
            c.setOpaque(false);
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
        {
            // g.setColor(Color.GREEN);
            // g.fillRect(trackBounds.x-24, trackBounds.y + trackBounds.height/2 - 1, trackBounds.width+48, 2);
        }


        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
        {
            if(thumbBounds.isEmpty() || !scrollbar.isEnabled())
            {
                return;
            }

            int w = thumbBounds.width;
            int h = thumbBounds.height;

            g.translate(thumbBounds.x, thumbBounds.y);

            g.setColor(Color.GRAY);
            g.fillRect(1, 1, w-2, h-2);


            g.translate(-thumbBounds.x, -thumbBounds.y);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation)  
        {
            return new BasicArrowButton(orientation, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        }

        @Override
        protected JButton createIncreaseButton(int orientation)  
        {
            return new BasicArrowButton(orientation, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.DARK_GRAY);
        }
    }

    private static class MyCellRenderer extends JLabel implements TableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }



    public static class MyTabbedPaneUi extends BasicTabbedPaneUI
    {
        @Override
        protected void paintTab(Graphics gr, int tabPlacement,
                Rectangle[] rects, int tabIndex,
                Rectangle iconRect, Rectangle textRect) 
        {
            Rectangle tabRect = rects[tabIndex];
            int selectedIndex = tabPane.getSelectedIndex();
            boolean isSelected = selectedIndex == tabIndex;

            // hajo: adjust "hopping" tabs
            if(isSelected)
            {
                tabRect.x += 1;
                tabRect.width -= 1;
                gr.setColor(Color.GRAY);
            }
            else
            {
                tabRect.y -= 1;
                tabRect.height += 3;
                gr.setColor(Color.DARK_GRAY);
            }
            
            gr.fillRect(tabRect.x, tabRect.y, tabRect.width-2, tabRect.height);

            String title = tabPane.getTitleAt(tabIndex);

            if(isSelected)
            {
                gr.setColor(Color.ORANGE);
            }
            else
            {
                gr.setColor(Color.GRAY);
            }
            
            gr.drawString(title, tabRect.x + 10, 17);
        }

        @Override
        protected void paintContentBorder(Graphics gr, int tabPlacement, int selectedIndex) 
        {
            gr.setColor(ComponentFactory.boxBackground);
            
            // Hajo: hackity hack - all sizes hardcoded
            gr.fillRect(0, 26, 1000, 500);
        }
    
    }
}