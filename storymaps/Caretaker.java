package storymaps;

import java.util.ArrayList;
import java.io.*;

/**
 * Only one Caretaker object is constructed, by Main (although I haven't
 * bothered to explicitly make this class a Singleton). The Caretaker object
 * manages saving and restoring of application state. It maintains a runtime
 * list of saved states, any of which the application can be restored to, which
 * can be used to implement undo and redo. It can also write saved states to
 * file, read them in from file, and export saved states as human-readable plain
 * text (and potentially into other formats as well).
 * 
 * @author seanh
 */
public class Caretaker {

    /**
     * List of saved states of the application. The application can be restored
     * to any of these saved states, or a saved state can be written out to
     * disk and read in again.
     */
    private ArrayList<Object> mementos = new ArrayList<Object>();    
        
    /**
     * Add a new memento object to the runtime list of saved states.
     */
    public void addMemento(Object m) {
        mementos.add(m);
    }

    /**
     * Get the most recent memento object from the runtime list of saved states.
     */
    public Object getMemento() {
        return getMemento(mementos.size()-1);
    }
    
    /**
     * Get a memento object from the runtime list of saved states.
     */
    public Object getMemento(int index) {
        return mementos.get(index);
    }
    
    /**
     * Write a memento object to a file. Write a valid XHTML file (assuming the
     * toString() method of the memento object produces a valid snippet of
     * XHTML).
     */
    public void writeMemento(Object m, File f)
    {
        // First the XHTML 1.0 Strict header.
        String string = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n";
        string += "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";
        string += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n";
        string += "<head>\n";
        //FIXME: Where should the page title come from?
        string += "<title>Conforming XHTML 1.0 Strict Template</title>\n";
        string += "</head>\n";
        string += "<body>\n";

        // Now append the XHTML from the memento.
        string += m.toString();
        
        // And finally append an XHTML footer.
        string += "</body>\n";
        string += "</html>";
                        
        // Then write the string to file.
        try {
            PrintWriter out = new PrintWriter(new FileWriter(f));
            out.print(string);
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }                        
    }
    
    /**
     * Return a memento object read in from a file.
     * @return
     */
    public Object readMemento(File f) {
        Object copy = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            copy = ois.readObject();
            ois.close();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("ClassNotFoundException when reading from file!\n"+cnfe);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException when reading from file!\n"+e);
        } catch (IOException e) {
            System.out.println("IOException when reading from file!\n"+e);
        }
        return copy;
    }
}