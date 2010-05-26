package DragAndDrop;

import java.util.HashSet;

/**
 * Superclass for Draggable and Droppable. Implements the Observer interface
 * that is common to both.
 * 
 * @author seanh
 */
public class DragDropSubject {

    /**
     * The observers currently subscribed to this subject.
     */
    private HashSet<DragDropObserver> observers =
            new HashSet<DragDropObserver>();

    /**
     * Subscribe a new observer.
     */
    public void attach(DragDropObserver o) {
        observers.add(o);
    }

    /**
     * Unsubscribe an observer.
     */
    public void detach(DragDropObserver o) {
        observers.remove(o);
    }

    /**
     * Called by Draggable and Droppable when they want to notify all of their
     * observer of a drop event. If an observer's notify method returns false
     * it will be unsubscribed from the subject.
     */
    public void notify_observers(DropEvent de) {
        HashSet<DragDropObserver> to_detach = new HashSet<DragDropObserver>();
        for (DragDropObserver observer : observers) {
            if (!observer.notify(de)) {
                to_detach.add(observer);
            }
        }
        for (DragDropObserver observer : to_detach) {
            detach(observer);
        }
    }
}