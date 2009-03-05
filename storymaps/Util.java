package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * Miscellaneous static utility methods.
 * 
 * FIXME: this and some other files should probably move to a Util package.
 * 
 * @author seanh
 */
public class Util {

    /**
     * Report an exception: print it to stderr and append it to errors.log.
     * @param detail
     * @param e
     */
    public static void reportException(String detail, Exception e) {
        System.err.println(detail);
        System.err.println(e);
        // TODO: append detail and e to an errors.log file.
    }
    
    /**
     * Return a string representing the current time.
     */
    public static String nowStr() {
        DateFormat longTimeStamp =
                DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL);
        return longTimeStamp.format(new Date());
    }
    
    /**
     * Given a relative path (relative to the classpath, e.g.
     * /storymaps/data/functions.xml) read in a file and return the contents as
     * a string.
     */
    public static String readTextFromFileRelative(String relativePath) throws IOException {
        String absolutePath = Util.class.getResource(relativePath).getPath();
        if (absolutePath == null) {
            String detail = "IOException when trying to read text from file at path: "+absolutePath;
            IOException e = new IOException(detail);
            reportException(detail, e);
            throw e;
        }
        return readTextFromFileAbsolute(absolutePath);
    }

    /**
     * Given an absolute path read in a file and return the contents as a
     * string.
     */
    public static String readTextFromFileAbsolute(String absolutePath) throws IOException {
        String contents = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(absolutePath));
            String line;            
            while ((line = in.readLine()) != null) {
                contents = contents + line;
            }
            in.close();
        } catch (IOException e) {
            String detail = "IOException when reading-in text file from path: "+absolutePath;
            reportException(detail,e);
            throw new IOException(detail,e);
        }
        return contents;
    }

    /**
     * Write a string out to a text file.
     * 
     * @param s The string to be written.
     * @param absolutePath The absolute path to the file to write to.
     */
    public static void writeTextToFile(String s, String absolutePath) throws IOException {        
        try {
            PrintWriter out = new PrintWriter(new FileWriter(absolutePath));
            out.print(s);
            out.close();
        } catch (IOException e) {
            String detail = "IOException when writing text file to path: "+absolutePath;
            reportException(detail, e);
            throw new IOException(detail,e);
        }        
    }
    
    /**
     * Read in an image file and return the image as a Piccolo PImage object.
     * @param path Relative path to the image file to load,
     * e.g. /storymaps/functions/0.png
     */
    public static PImage readPImageFromFile(String path) throws IOException {
        return new PImage(readImageFromFile(path));
    }

    /**
     * Read in an image file and return the image as an Image object.
     * @param path Relative path to the image file to load,
     * e.g. /storymaps/functions/0.png
     */    
    public static Image readImageFromFile(String path) throws IOException {
        return readImageIconFromFile(path).getImage();
    }

    /**
     * Read in an image file and return the image as an ImageIcon object.
     * @param path Relative path to the image file to load,
     * e.g. /storymaps/functions/0.png
     */    
    public static ImageIcon readImageIconFromFile(String path) throws IOException {
        InputStream imagefile = Util.class.getResourceAsStream(path);
        if (imagefile == null) {
            String detail = "IOException when trying to read image from file at path: "+path;
            IOException e = new IOException(detail);
            reportException(detail, e);
            throw e;
        }
        try {
            Image image = ImageIO.read(imagefile);
            return new ImageIcon(image);
        } catch (IOException e) {
            String detail = "IOException when reading image from file at path: "+path;
            reportException(detail, e);
            throw new IOException(detail, e);
        }        
    }        
}