package storymaps;

import java.awt.Image;
import java.util.ArrayList;

/**
 * A Function is a simple immutable object that represents one of Propp's
 * functions.
 * 
 * The Function class also has a public static member functions that is a list
 * of all functions read in from the functions.xml file by XStream.
 * 
 * Function objects are also created when saved story XML files are read in,
 * so it is possible to have more than one Function object with the same fields,
 * or even with some fields the same but others different. Maybe this should be
 * changed.
 * 
 * The Function class constructor is package-private so that FunctionConverter
 * can use it, but it should not be used otherwise.
 * 
 * @author seanh
 */
final class Function implements Comparable {

    private final int number;
    private final String propp_name;
    private final String friendly_name;
    private final String description;
    private final String friendly_description;
    private final String image_path;
    private final Image image;

    /**
     * A singleton list containing a Function object for every function
     * represented in the functions.xml file.
     */
    public static final ArrayList<Function> functions = (ArrayList<Function>)
            XMLHandler.getInstance().readXMLRelative("/storymaps/data/functions.xml");    
        
    Function(int number, String propp_name, String friendly_name,
                    String description, String friendly_description,
                    String image_path) {
        this.number = number;
        this.propp_name = propp_name;
        this.friendly_name = friendly_name;
        this.description = description;
        this.friendly_description = friendly_description;
        this.image_path = image_path;
        this.image = ResourceLoader.loadImage(image_path);
    }
    
    public int getNumber() { return number; }
    public String getProppName() { return propp_name; }
    public String getFriendlyName() { return friendly_name; }
    public String getDescription() { return description; }
    public String getFriendlyDescription() { return friendly_description; }
    // FIXME: Should I make a defensive copy of image here?
    public Image getImage() { return image; }
    public String getImagePath() { return image_path; }
    
    @Override
    public String toString() {
        return friendly_name + " (" + friendly_description + " )";
    }
    
    /**
     * Return true if obj is equivalent to this function, false otherwise.
     * 
     * (This is in place of overriding equals, which is a PITA.)
     */
    public boolean compare(Object obj) {
        if (!(obj instanceof Function)) {
            return false;
        } else {
            Function f = (Function) obj;
            // A function's symbol is supposed to uniquely identify that
            // function, so we just check if the symbol's are the same.
            return f.number == this.number;
        }        
    }
    
    /**
     * Implement Comparable.
     */
    public int compareTo(Object arg) {
        Function other = (Function) arg;
        if (other.number > this.number) {
            return 1;
        } else if (other.number == this.number) {
            return 0;
        }
        return -1;
    }
}