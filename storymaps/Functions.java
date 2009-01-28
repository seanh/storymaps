package storymaps;

import java.util.ArrayList;

/**
 * A singleton class that reads the functions.xml file and returns a list of
 * Function objects, one for each function in functions.xml, via the
 * getFunctions() static method.
 *  
 * @author seanh
 */
public class Functions {
    
    /**
     * The single object-instance of Functions.
     */
    private static Functions f = new Functions();    
    
    /**
     * The list of function objects held by the singleton object-instance.
     */
    private ArrayList<Function> functions = new ArrayList<Function>();
    
    /**
     * Return the list of functions.
     */
    public static ArrayList<Function> getFunctions() {
        return f.functions;
    }
    
    /**
     * Construct the singleton Functions instance, parse the functions.xml file
     * and instantiate the Function objects.
     */
    private Functions() {
        parse_xml();            
    }
    
    /**
     * Parse the functions.xml file, instantiate Function objects, and add them
     * to the list functions.
     * 
     */     
    private void parse_xml() {
        // Deserialize the list of function mementos from the functions.xml
        // file.
        Object o = Util.readXMLRelative("/storymaps/data/functions.xml");
            
        // Cast the object back to its original type.
        ArrayList<Function.Memento> mementos = (ArrayList<Function.Memento>) o;
            
        // Instantiate Function objects from the deserialized list of
        // mementos and add them to the list of functions.
        for (Function.Memento m : mementos) {
            functions.add(Function.newFromMemento(m));
        }
    }        
    
    /**
     * Write the list of functions out to an XML file.
     */
    public static void write_xml() {
        ArrayList<Function.Memento> mementos = new ArrayList<Function.Memento>();
        for (Function function : getFunctions()) {
            mementos.add((Function.Memento)function.saveToMemento());
        }        
        Util.writeXML(mementos,"/home/seanh/Desktop/functions.xml");
    }            

    public static void main(String args[]) {
        Functions.getFunctions();
    }
}