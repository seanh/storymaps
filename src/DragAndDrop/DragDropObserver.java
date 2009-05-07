package DragAndDrop;

/**
 * An interface to be implemented by classes that want to be notified when
 * drag-drops are accepted. A DragDropObserver can subscribe to Draggable or
 * Droppable objects by calling their attach methods. When a drag-drop is
 * accepted all DragDropObservers subscribed to the draggee and the droppee are
 * notified via the notify method.
 * 
 * Note that if one DragDropObserver is subscribed to both the draggee and the
 * droppee of a drop event, then its notify method will be called twice.
 * 
 * @author seanh
 */
public interface DragDropObserver {

    /**
     * Notifies that a drag-drop was accepted. Note that DragDropObservers must
     * _not_ unsubscribe themselves from the calling draggable or droppable in
     * this method or an exception will occur. Instead return false and you will
     * be unsubscribed.
     * 
     * @param An object that stores information about the drag-drop.
     * @return true to remain subscribed to the draggable or droppable that sent
     * the notification, false to unsubscribe.
     */
    public boolean notify(DropEvent de);
    
}