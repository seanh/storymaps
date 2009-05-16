package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.*;
import java.text.SimpleDateFormat;
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date());
    }
    
    /**
     * Use the Java ClassLoader to read the text file at the given resource path
     * and return the contents as a String.
     * @param path The resource path to the text file to read.
     * @return The contents of the text file as a String.
     * @throws java.io.IOException
     */
    public static String readTextFileFromClassPath(String path) throws IOException {
        InputStream is = Util.class.getResourceAsStream(path);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String text = "";
        String line;
        try {
            while ((line = br.readLine()) != null)
            {
                text = text + line;
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            String detail = "IOException when reading-in text file from path: "+path;
            reportException(detail,e);
            throw new IOException(detail,e);
        }
        return text;
    }

    /**
     * Read in a text file from a canonical and absolute sytem path and return
     * the contents as a string.
     * @param path The canonical and absolute path to the text file to read.
     * @return The contents of the text file as a String.
     * @throws java.io.IOException
     */
    public static String readTextFileFromSystem(String path) throws IOException {
        String contents = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line;            
            while ((line = in.readLine()) != null) {
                contents = contents + line;
            }
            in.close();
        } catch (IOException e) {
            String detail = "IOException when reading-in text file from path: "+path;
            reportException(detail,e);
            throw new IOException(detail,e);
        }
        return contents;
    }

    /**
     * Write a string out to a text file at a canonical and absolute system
     * path.
     * 
     * @param s The string to be written.
     * @param path The canonical and absolute system path to the file to write.
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
     * Use the Java ClassLoader to read in an image file from a resource path
     * and return the image as a Piccolo PImage object.
     * @param path The resource path to the image file to read.
     */
    public static PImage readPImageFromClassPath(String path) throws IOException {
        return new PImage(readImageFromClassPath(path));
    }

    /**
     * Use the Java ClassLoader to read in an image from a resource path and
     * return the image as an Image object.
     * @param path The resource path to the image file to read.
     */    
    public static Image readImageFromClassPath(String path) throws IOException {
        return readImageIconFromClassPath(path).getImage();
    }

    /**
     * Use the Java ClassLoader to read in an image from a resoure path and
     * return the image as an ImageIcon object.
     * @param path The resource path to the image file to read.
     */    
    public static ImageIcon readImageIconFromClassPath(String path) throws IOException {
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
    
    public static void serializeObjectToFile(String path, Object o) throws IOException {
        File f = new File(path);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            String detail = "IOException when serializing object to file.\n";
            detail = detail +"Path: "+path+"\n";
            detail = detail +"Object: "+o;
            reportException(detail, e);
            throw new IOException(detail, e);            
        }
    }

    public static Object deserializeObjectFromFile(String path) throws IOException, ClassNotFoundException {
        File f = new File(path);
        Object o;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            o = ois.readObject();
            ois.close();
        } catch (IOException e) {
            String detail = "IOException when deserializing object from file.\n";
            detail = detail +"Path: "+path;
            reportException(detail, e);
            throw new IOException(detail, e);
        } catch (ClassNotFoundException e) {
            String detail = "ClassNotFoundException when deserializing object from file.\n";
            detail = detail +"Path: "+path;
            reportException(detail, e);
            throw new ClassNotFoundException(detail, e);            
        }
        return o;
    }
}