package storymaps;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A Function is a simple immutable object that represents one of Propp's
 * functions.
 * 
 * The Function class has a public static member functions that is a list of all
 * functions read in from the functions.xml file by XStream.
 * 
 * Function objects are also created when saved stories are read in from file,
 * so it is possible to have more than one Function object with the same fields,
 * or even with some fields the same but others different.
 * 
 * The Function class constructor is package-private so that FunctionConverter
 * can use it, but it should not be used otherwise.
 * 
 * @author seanh
 */
final class Function implements Comparable, Originator {

    private final int number;
    private final String proppName;
    private final String friendlyName;
    private final String description;
    private final String friendlyDescription;
    private final String imagePath;
    private final Image image;

    /**
     * A singleton list containing a Function object for every function
     * represented in the functions.xml file.
     */
    private static ArrayList<Function> functions = null;
    
    private static void initialiseFunctionsIfNecessary() {
        if (functions == null) {
            try {
                functions = (ArrayList<Function>) XMLHandler.getInstance().readXMLRelative("/storymaps/functions/functions.xml");
            } catch (IOException e) {
                // If we can't read the functions.xml file then the application
                // can't work.
                throw new RuntimeException("Could not read functions.xml file.",e);
            }
        }
    }
    
    static ArrayList<Function> getFunctions() {
        initialiseFunctionsIfNecessary();
        return functions;
    }
    
    Function(int number, String proppName, String friendlyName,
                    String description, String friendlyDescription) {
        this.number = number;
        this.proppName = proppName;
        this.friendlyName = friendlyName;
        this.description = description;
        this.friendlyDescription = friendlyDescription;
        this.imagePath = "/storymaps/functions/"+number+".svg-512.png";
        System.out.println(this.imagePath);
        try {
            this.image = Util.readImageFromFile(imagePath);
        } catch (IOException e) {
            System.out.println("Couldn't load image for function "+imagePath);
            // FIXME: shouldn't need to crash here.
            throw new RuntimeException("Couldn't load image for function "+imagePath,e);
        }
    }
        
    public int getNumber() { return number; }
    public String getProppName() { return proppName; }
    public String getFriendlyName() { return friendlyName; }
    public String getDescription() { return description; }
    public String getFriendlyDescription() { return friendlyDescription; }
    // FIXME: Should I make a defensive copy of image here?
    public Image getImage() { return image; }
    public String getImagePath() { return imagePath; }
    
    @Override
    public String toString() {
        return friendlyName + " (" + friendlyDescription + " )";
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
    
    // Implement Originator
    // --------------------
    
    private static final class FunctionMemento implements Memento {
        // No need to defensively copy anything as int is a primitive type
        // and strings are immutable.
        private final int number;
        private final String proppName;
        private final String friendlyName;
        private final String description;
        private final String friendlyDescription;
        FunctionMemento (Function f) {
            this.number = f.getNumber();
            this.proppName = f.getProppName();
            this.friendlyName = f.getFriendlyName();
            this.description = f.getDescription();
            this.friendlyDescription = f.getFriendlyDescription();
        }
        int getNumber() { return number; }
        String getProppName() { return proppName; }
        String getFriendlyName() { return friendlyName; }
        String getDescription() { return description; }
        String getFriendlyDescription() { return friendlyDescription; }
    }
    
    public Memento createMemento() {
        return new FunctionMemento(this);
    }
    
    public static Function newInstanceFromMemento(Memento m) throws MementoException {
        if (m == null) {
            String detail = "Null memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        if (!(m instanceof FunctionMemento)) {
            String detail = "Wrong type of memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        FunctionMemento f = (FunctionMemento) m;
        return new Function(f.getNumber(),f.getProppName(),f.getFriendlyName(),
                f.getDescription(),f.getFriendlyDescription());
    }
}