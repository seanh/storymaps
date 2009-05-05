package storymaps;

import org.json.JSONArray;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * The number that defines the order of this function relative to other
     * functions, and that uniquely identifies this function. Used to implement
     * equals and comparable.
     */
    private final int number;
    
    /**
     * One or two word title for this function. Plain text.
     */
    private final String name;
    
    /**
     * Short one sentence description of this function. HTML-formatted.
     */
    private final String description;
    
    /**
     * Longer (multi-paragraph) structured instructions for writing this
     * function. Includes lists of options or examples where appropriate.
     * HTML-formatted.
     */
    private final String instructions;
    
    private final String imagePath;
    private final Image image;
    
    /**
     * A singleton list containing a Function object for every function
     * represented in the functions.xml file.
     */
    private static List<Function> functions = null;
    
    private static void initialiseFunctionsIfNecessary() {
        if (functions == null) {
            try {
                String jsonString = Util.readTextFromFileRelative("/storymaps/functions/functions.json");
                JSONArray jsonArray = new JSONArray(jsonString);
                functions = new ArrayList<Function>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int number = jsonObject.getInt("number");
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String instructions = jsonObject.getString("instructions");
                    Function function = new Function(number,name,description,instructions);
                    functions.add(function);
                }
            } catch (IOException e) {
                // If we can't read the functions.xml file then the application
                // can't work.
                throw new RuntimeException("Could not read functions.json file.",e);
            } catch (JSONException e) {
                throw new RuntimeException("Exception when reading functions.json file.",e);
            }
        }
    }
    
    static List<Function> getFunctions() {
        initialiseFunctionsIfNecessary();
        return functions;
    }
    
    Function(int number, String name, String description, String instructions) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.imagePath = "/storymaps/functions/"+number+".svg-512.png";
        try {
            this.image = Util.readImageFromFile(imagePath);
        } catch (IOException e) {
            System.out.println("Couldn't load image for function "+imagePath);
            // FIXME: shouldn't need to crash here.
            throw new RuntimeException("Couldn't load image for function "+imagePath,e);
        }
    }
        
    public int getNumber() { return number; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getInstructions() { return instructions; }
    public Image getImage() { return image; }
    public String getImagePath() { return imagePath; }
    
    @Override
    public String toString() {
        return "Function: "+number+", "+name;
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
        private final String name;
        private final String description;
        private final String instructions;
        FunctionMemento (Function f) {
            this.number = f.getNumber();
            this.name = f.getName();
            this.description = f.getDescription();
            this.instructions = f.getInstructions();
        }
        int getNumber() { return number; }
        String getName() { return name; }
        String getDescription() { return description; }
        String getInstructions() { return instructions; }
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
        return new Function(f.getNumber(),f.getName(),f.getDescription(),
                f.getInstructions());
    }
}