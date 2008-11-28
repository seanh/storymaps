package storymaps;

import java.util.ArrayList;

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
}
