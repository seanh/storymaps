package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
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
    
    // Prevent instantiation from outside of the class.
    private ResourceLoader() {}
    
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
        } catch (IOException e) { }        
        return new ImageIcon(image);        
    }    
    
}
