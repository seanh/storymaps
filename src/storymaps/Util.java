
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Miscellaneous static utility methods.
 * 
 * FIXME: this and some other files should probably move to a Util package.
 * 
 * @author seanh
 */
public class Util {

    public static <T> String join(final Iterable<T> objs, final String delimiter) {
        Iterator<T> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer(String.valueOf(iter.next()));
        while (iter.hasNext()) {
            buffer.append(delimiter).append(String.valueOf(iter.next()));
        }
        return buffer.toString();
    }
    
    /**
     * Join path1 and path2 in a system-dependent way and return the result.
     *
     * DO NOT use this method to join classpath paths, e.g. paths to resources
     * distributed in a JAR file. Classpath paths appear to work with forward
     * slashes only, but on a Windows system (for example) this method will
     * return backward slashes.
     * 
     * @param path1
     * @param path2
     * @return
     */
    public static String joinPaths(String path1, String path2) {
        return new File(path1,path2).getPath();
    }

    public static String joinClassPaths(String path1, String path2) {
        // I cannot fucking believe what I had to do in java to achieve this.
        String[] path1Array = path1.split("/");
        String[] path2Array = path2.split("/");
        List combined = new ArrayList();
        for (int i=0; i<path1Array.length; i++) {
            combined.add(path1Array[i]);
        }
        for (int i=0; i<path2Array.length; i++) {
            combined.add(path2Array[i]);
        }
        return join(combined,"/");
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
            IOException ee = new IOException(detail,e);
            Logger.getLogger(Util.class.getName()).throwing("Util", "readTextFileFromClassPath", ee);
            throw ee;
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
            IOException ee = new IOException(detail,e);
            Logger.getLogger(Util.class.getName()).throwing("Util", "readTextFileFromSystem", ee);
            throw ee;
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
            IOException ee = new IOException(detail,e);
            Logger.getLogger(Util.class.getName()).throwing("Util", "writeTextToFile", ee);
            throw ee;
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
            Logger.getLogger(Util.class.getName()).throwing("Util", "readImageIconFromClassPath", e);
            throw e;
        }
        try {
            Image image = ImageIO.read(imagefile);
            return new ImageIcon(image);
        } catch (IOException e) {
            String detail = "IOException when reading image from file at path: "+path;
            Logger.getLogger(Util.class.getName()).throwing("Util", "readImageIconFromClassPath", e);
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
            IOException ee = new IOException(detail,e);
            Logger.getLogger(Util.class.getName()).throwing("Util", "serializeObjectToFile", ee);
            throw ee;
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
            IOException ee = new IOException(detail, e);
            Logger.getLogger(Util.class.getName()).throwing("Util", "deserializeObjectFromFile", ee);
            throw ee;
        } catch (ClassNotFoundException e) {
            String detail = "ClassNotFoundException when deserializing object from file.\n";
            detail = detail +"Path: "+path;
            IOException ee = new IOException(detail, e);
            Logger.getLogger(Util.class.getName()).throwing("Util", "deserializeObjectFromFile", ee);
            throw ee;
        }
        return o;
    }

    /**
     * Convert an Image to a BufferedImage.
     *
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = true; //hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
}