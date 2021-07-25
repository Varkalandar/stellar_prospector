package flyspace.ui;

/**
 *
 * @author Hj. Malthaner
 */
public class HTMLHelper 
{
    public static void displayHTMLMultiLine(PixFont font, String html, int color, int left, int top) 
    {
        String [] lines = html.split("<br>");
        
        for(String line : lines)
        {
            displayHTMLLine(font, line, color, left, top);
            top -= 18;
        }
    }
    
    public static void displayHTMLLine(PixFont font, String line, int color, int left, int top) 
    {
        StringBuilder buf = new StringBuilder(line);
        
        replace(buf, "<html>", "");
        replace(buf, "</html>", "");
        replace(buf, "&nbsp;", "");
        replace(buf, "&lt;", "<");
        replace(buf, "&gt;", ">");
        replace(buf, "<br>", "");
        
        line = buf.toString();
        
        int p1, p2;
        
        p1 = line.indexOf("<font color=");
        if(p1 < 0)
        {
            // Hajo: text all in one color
            font.drawString(line, color, left, top);
        }
        else
        {
            String part = line.substring(0, p1);

            int w = font.getStringWidth(part);
            font.drawString(part, color, left, top);

            p1 += 14;
            part = line.substring(p1, p1+6);
            int metalColor = Integer.parseInt(part, 16);

            p1 += 8;
            p2 = line.indexOf("</font>");
            part = line.substring(p1, p2);
            font.drawString(part, metalColor, left+w, top);
            w += font.getStringWidth(part);

            part = line.substring(p2+7, line.length());
            font.drawString(part, color, left+w, top);
        }
    }    

    private static void replace(StringBuilder buf, String from, String to) 
    {
        int p = 0;

        do
        {
            p = buf.indexOf(from, p);
            if(p >= 0)
            {
                buf.replace(p, p+from.length(), to);
            }
        } 
        while (p >= 0);
    }
}
