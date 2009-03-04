package storymaps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

/**
 * Miscellaneous static utility methods.
 * 
 * @author seanh
 */
class Util {
        
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
    public static String readFileRelative(String relativePath) {
        String absolutePath = Util.class.getResource(relativePath).getPath();
        return readFileAbsolute(absolutePath);
    }

    /**
     * Given an absolute path read in a file and return the contents as a
     * string.
     */
    public static String readFileAbsolute(String absolutePath) {
        String contents = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(absolutePath));
            String line;            
            while ((line = in.readLine()) != null) {
                contents = contents + line;
            }
            in.close();
        } catch (IOException e) {
            System.out.println("IOException when reading "+absolutePath+" "+e);
        }
        return contents;
    }

    /**
     * Write a string out to a text file.
     * 
     * @param s The string to be written.
     * @param absolutePath The absolute path to the file to write to.
     */
    public static void writeFileAbsolute(String s, String absolutePath) {        
        try {
            PrintWriter out = new PrintWriter(new FileWriter(absolutePath));
            out.print(s);
            out.close();
        } catch (IOException e) {
            System.out.println("IOException when writing "+absolutePath+" "+e);
        }        
    }    
}