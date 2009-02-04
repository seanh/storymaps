package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Utility class for loading resources (data files: xml, images, etc.) in a
 * location-independent manner.
 * 
 * @author seanh
 */
public class ResourceLoader {

    private static ResourceLoader instance = new ResourceLoader();

    public static Font bodyFont = new Font("SansSerif", Font.PLAIN, 16);        
    public static Font titleFont = new Font("SansSerif", Font.PLAIN, 18);
    
    // Prevent instantiation from outside of the class.
    private ResourceLoader() {}

    public static String loadString(String path) {
        InputStream textfile = instance.getClass().getResourceAsStream(path);
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        try {
            for (int n; (n = textfile.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException e) { System.out.println(e); }
        return out.toString();
    }
    
    public static PImage loadPImage(String path) {
        return new PImage(loadImage(path));
    }
    
    public static Image loadImage(String path) {
        return loadImageIcon(path).getImage();
    }
            
    public static ImageIcon loadImageIcon(String path) {
        InputStream imagefile = instance.getClass().getResourceAsStream(path);
        Image image = null;
        try {
            image = ImageIO.read(imagefile);
        } catch (IOException e) { System.out.println(e); }        
        return new ImageIcon(image);        
    }    
}
