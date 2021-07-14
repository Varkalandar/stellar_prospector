package flyspace.ogl;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import solarex.util.ResourceLoader;

/**
 * Texture loading and texture management.
 * 
 * @author Hj. Malthaner
 */
public class TextureCache 
{
    private static final Logger logger = Logger.getLogger(TextureCache.class.getName());

    public final static Texture [] grounds = new Texture [300];
    public final static Texture [] species = new Texture [400];
    public final static Texture [] textures = new Texture [2048];
    
    private static ResourceLoader resourceLoader;
    
    public static void initialize(LoaderCallback callback)
    {        
        if(callback != null) callback.update("Loading ground textures ...");
        loadTextures("/jewelhunt/resources/", "grounds/", "catalog.xml", grounds, 0);
        
        if(callback != null) callback.update("Loading creature textures ...");
        loadTextures("/jewelhunt/resources/", "mobs/", "catalog.xml", species, 0);
        
        if(callback != null) callback.update("Loading item textures ...");
        loadTextures("/jewelhunt/resources/", "ts/", "catalog.xml", textures, 0);
    }
    
    public static void mergeTilesFromFile(String path, String folder, Texture [] tex, int idOffset)
    {
        loadTextures(path, folder, "catalog.xml", tex, idOffset);
    }
    
    public static Texture loadTexture(String filename) throws IOException
    {
        if(resourceLoader == null) resourceLoader = new ResourceLoader();

        
        // InputStream in = Class.class.getResourceAsStream(filename);
        InputStream in = resourceLoader.getResourceAsStream(filename);
        
        
        BufferedImage img = ImageIO.read(in);
        in.close();
        ByteBuffer buf = convertTextureToRGBA(img);
        
        int texId = loadTexture(buf, img.getWidth(), img.getHeight());
        
        return new Texture(texId, img);
    }
        
    public static int loadTexture(ByteBuffer buf, int tWidth, int tHeight)
    {    
        // Create a new texture object in memory and bind it
        int texId = glGenTextures();

        loadTexture(texId, buf, tWidth, tHeight);
        
        return texId;
    }
    
    public static void loadTexture(int texId, ByteBuffer buf, int tWidth, int tHeight)
    {    
        glBindTexture(GL_TEXTURE_2D, texId);

        // All RGB bytes are aligned to each other and each component is 1 byte
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        
        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tWidth, tHeight, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, buf);
        

        GlLifecycle.exitOnGLError("Texture loading");

        // Setup the ST coordinate system
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        // Setup what to do when the texture has to be scaled
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        
        GlLifecycle.exitOnGLError("loadPNGTexture");
    }

    public static ByteBuffer convertTextureToRGB(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        
        ByteBuffer buf = ByteBuffer.allocateDirect(Math.max(width*height*4, 65536));

        int [] pixels = img.getRGB(0, 0, width, height, null, 0, width);
        
        for(int i=0; i<pixels.length; i++)
        {
            int argb = pixels[i];
            buf.put((byte)(argb >> 16));
            buf.put((byte)(argb >> 8));
            buf.put((byte)(argb));
        }
        
        buf.position(0);

        return buf;
    }

    public static ByteBuffer convertTextureToRGBA(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        
        ByteBuffer buf = ByteBuffer.allocateDirect(Math.max(width*height*4, 65536));

        int [] pixels = img.getRGB(0, 0, width, height, null, 0, width);
        
        for(int i=0; i<pixels.length; i++)
        {
            int argb = pixels[i];
            int rgba = (argb << 8) | (argb >>> 24);
            buf.putInt(rgba);
        }
        
        buf.flip();

        return buf;
    }
    

    private static int rgbDiff(int a, int b)
    {
        int diff;
        
        diff = Math.abs(((a >> 16) & 0xFF) - ((b >> 16) & 0xFF))
               + Math.abs(((a >> 8) & 0xFF) - ((b >> 8) & 0xFF))
               + Math.abs(((a) & 0xFF) - ((b) & 0xFF));
        
        return diff;
    }

    private static void loadTextures(String path, String folder, String fileName, Texture[] textures, int idOffset)
    {
        try 
        {
            InputStream in  = Class.class.getResourceAsStream(path + folder + fileName);
            
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(in);

            NodeList fileTypes = doc.getElementsByTagName("Tile");

            int tileCount = fileTypes.getLength();

            System.err.println("Tiles in set: " + tileCount);
            
            for (int tileIndex = 0; tileIndex < tileCount; tileIndex++) 
            {
                int tileId = -1;
                String tileName = null;
                boolean rgba = false;
                String tags = "";
                Rectangle area = new Rectangle();
                int footX = 0;
                int footY = 0;
                int alpha = 0xFF;
                
                
                Node tileNode = fileTypes.item(tileIndex);
            
                // System.err.println("- " + tileNode.getNodeValue());
                
                NodeList cnlist = tileNode.getChildNodes();
                for (int n = 0; n < cnlist.getLength(); n++) 
                {
                    Node cnNode = cnlist.item(n);
                    String childName = cnNode.getNodeName();
                    // System.err.println("-- " + name);
                    
                    if("Description".equals(childName))
                    {
                        NodeList descList = cnNode.getChildNodes();
                        
                        for (int i = 0; i < descList.getLength(); i++) 
                        {
                            Node descNode = descList.item(i);
                            String name = descNode.getNodeName();
                            // System.err.println("--- " + name);
                            
                            if("id".equals(name))
                            {
                                tileId = Integer.parseInt(descNode.getTextContent());
                                tileId += idOffset;

                                // System.err.println("tileId=" + tileId);
                            }
                            if("footX".equals(name))
                            {
                                footX = Integer.parseInt(descNode.getTextContent());
                            }
                            if("footY".equals(name))
                            {
                                footY = Integer.parseInt(descNode.getTextContent());
                            }
                        }
                    }
                    if("Metadata".equals(childName))
                    {
                        NodeList metaList = cnNode.getChildNodes();
                        int stringIndex = 0;
                        
                        for (int i = 0; i < metaList.getLength(); i++) 
                        {
                            Node metaNode = metaList.item(i);
                            String name = metaNode.getNodeName();
                            
                            // System.err.println("--- " + name + " children: " + metaNode.getChildNodes().getLength());
                            
                            if("string".equals(name) && stringIndex == 0)
                            {
                                // the tile name is the first string attribute
                                tileName = metaNode.getTextContent();
                                //System.err.println("id=" + tileId + " name='" + tileName + "'");
                                stringIndex ++;
                            }
                            else if("string".equals(name) && stringIndex == 1)
                            {
                                // the tags follow in the second string attribute
                                tags = metaNode.getTextContent();
                                //System.err.println("id=" + tileId + " tags='" + tags + "'");
                                
                                rgba = tags.contains("rgba");
                                stringIndex ++;
                            }
                            else if("string".equals(name) && stringIndex == 2)
                            {
                                // area is third string attribute
                                String areaString = metaNode.getTextContent();
                                
                                String [] parts = areaString.split("x");
                                
                                if(parts.length == 4)
                                {
                                    area.x = Integer.parseInt(parts[0]);
                                    area.y = Integer.parseInt(parts[1]);
                                    area.width = Integer.parseInt(parts[2]);
                                    area.height = Integer.parseInt(parts[3]);
                                }
                                else
                                {
                                    if(parts.length > 1)
                                    {
                                        logger.log(Level.INFO, "id={0}: Expected 4 area paramaters, got {1}", new Object[]{tileId, parts.length});
                                    }
                                }
                                // System.err.println("id=" + tileId + " area=" + area);
                                stringIndex ++;
                            }
                        }
                    }
                }

                if(tileName != null && tileName.length() > 0)
                {
                    String filename = "" + tileIndex + "," + tileId + "," + tileName + ".png";

                    
                    int stackRun = 1;
                    int p = tags.indexOf("n=");
                    if(p != -1)
                    {
                        stackRun = Integer.parseInt(tags.substring(p+2, p+3));
                    }    
                    
                    System.err.println("loading: " + filename + " rgba=" + rgba + " tileId=" + tileId + 
                                       " area=" + area + " stackRun=" + stackRun);

                    Texture tex = loadTexture(path + folder + filename);

                    // Hajo: legacy/unset?
                    if(footX == 0 && footY == 0)
                    {
                        footX = tex.image.getWidth() / 2;
                    }
                    
                    textures[tileId] = new Texture(tex.id, tex.image, tags, area, footX, footY, 
                                                   stackRun, tileIndex, tileId);
                }
            }
            
        }
        catch (ParserConfigurationException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (SAXException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (IOException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    static Texture getStackTexture(int n, int count)
    {
        // are there several stack size images available?

        Texture tex = textures[n];

        if(count > tex.stackRun) count = tex.stackRun;

        // show proper stack size
        tex = textures[n + count - 1];
        
        return tex;
    }

    public static interface LoaderCallback
    {
        public void update(String what);
    }
}
