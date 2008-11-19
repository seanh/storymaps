package DragAndDrop;

/**
 * A passive class to record details about a drop event (when a draggable is
 * dropped onto a droppable).
 * 
 * @author seanh
 */
public class DropEvent {
    private Draggable draggee;
    private Droppable droppee;
    public DropEvent(Draggable draggee, Droppable droppee) {
        this.draggee = draggee;
        this.droppee = droppee;
    }
    public Draggable getDraggee() {
        return draggee;
    }
    public Droppable getDroppee() {
        return droppee;
    }
}