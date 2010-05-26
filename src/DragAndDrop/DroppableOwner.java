package DragAndDrop;

/**
 * An interface to be implemented by classes that want to own droppables. Each
 * droppable must have exactly one owner. When a node is dragged and dropped
 * onto a droppable, the droppable delegates the decision of whether or not to
 * accept the drop to its owner, via this interface.
 * 
 * @author seanh
 */
public interface DroppableOwner {
   
    /**
     * This method is called whenever a node is dropped onto a droppable owned
     * by this DroppableOwner. Any action to be taken when a drop occurs should
     * be implemented here.
     * 
     * @param draggee The PNode that was dropped.
     * @return true to accept the drop (all observers of the draggable and the
     * droppable will be notified), false to refuse the drop.
     * 
     */    
    public boolean dropped_onto(DropEvent de);

}
