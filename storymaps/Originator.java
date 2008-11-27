package storymaps;

/**
 * The Memento design pattern is used here for Save, Restore, Undo & Redo
 * operations. An object that implements the Originator interface is an object
 * that has some state that can be saved and restored.
 * 
 * The idea is that a Caretaker object (see interface Caretaker) calls
 * saveToMemento() to get a memento object that holds the current state of an
 * originator object. Memento objects can be held onto by the caretaker object,
 * saved to file, etc. Later the caretaker calls restoreFromMemento(memento),
 * passing one of the memento objects retreived from saveToMemento(), to restore
 * an originator to an earlier state.
 * 
 * Note that there is no Memento interface, we just use opaque objects
 * (instances of Object) as mementos, but any class implementing the Originator
 * interface will probably want to have its own private memento class that it
 * uses for memento objects. Other classes don't need to know anything about
 * this memento class.
 * 
 * @author seanh
 */
public interface Originator {
    
    /** Return a memento object for the current state of this originator. */
    public Object saveToMemento();                          

    /** 
     * Restore the state of this originator from a memento object. 
     * Should first check if m is an instance of private memento class using
     * instanceof, then get state from m and restore it.
     * 
     * @throws IllegalArgumentException if the object m cannot be cast to the
     * private memento type used by the originator (i.e. the object m is not
     * an object returned by the saveToMemento method of the same originator
     * class).
     */
    public void restoreFromMemento(Object m);
}
