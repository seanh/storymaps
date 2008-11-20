package DragAndDrop;

import java.awt.geom.Point2D;

/**
 * A passive class to record details about a drop event (when a draggable is
 * dropped onto a droppable).
 * 
 * @author seanh
 */
public class DropEvent {
    private Draggable draggee;
    private Droppable droppee;
    private Point2D position;
    public DropEvent(Draggable draggee, Droppable droppee, Point2D position) {
        this.draggee = draggee;
        this.droppee = droppee;
        this.position = position;
    }
    public Draggable getDraggee() {
        return draggee;
    }
    public Droppable getDroppee() {
        return droppee;
    }
    public Point2D getPosition() {
        return position;
    }
}