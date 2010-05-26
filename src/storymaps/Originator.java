package storymaps;

/**
 * An exception to be thrown by newInstanceFromMemento method implementations if
 * there is something wrong with the memento they receive (e.g. it's null, or
 * it's not an instanceof the right concrete subtype of Memento).
 * @author seanh
 */
class MementoException extends Exception {
    MementoException(String detail) {
        super(detail);
    }
}

/**
 * Memento design pattern.
 * 
 * An originator object can create a memento containing a snapshot of its
 * current internal state. (And a static factory method, not specified in this
 * interface, can create a new originator from the state recorded in a
 * memento.)
 * 
 * (See interface Memento.)
 * 
 * @author seanh
 */
public interface Originator {

    /**
     * Return a memento object recording the current internal state of this
     * originator.
     */
    Memento createMemento();
    
    // FIXME: classes that implement Originator also implement a
    // `static newInstanceFromMemento(Memento m)` method, but this method cannot
    // be specified in an interface or base class because it's static. May need
    // to use generics to fix this.
}