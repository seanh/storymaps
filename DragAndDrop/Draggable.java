// FIXME: only one event handler, attached to the canvas, is needed to implement
// drag & drop, not one event handler per draggable instance attached to the
// draggable node. The event handler could be a static field. But how to access
//the canvas to add the event handler?
package DragAndDrop;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPickPath;
import java.awt.geom.Point2D;

/**
 * A wrapper class for PNode that makes a PNode 'draggable' (i.e. it can be
 * dragged and dropped).
 * 
 * @author seanh
 */
public class Draggable extends DragDropSubject {

    /**
     * The PNode that is wrapped by this draggable.
     */
    private PNode node;
    
    /**
     * Construct a new Draggable instance.
     * 
     * @param node The PNode to be wrapped by this draggable.
     * @throws NodeAlreadyDraggableException If an attribute with key
     * "Draggable" already exists on the given PNode.
     */    
    public Draggable(PNode node) throws NodeAlreadyDraggableException {
        if (node.getAttribute("Draggable") != null) {
            throw new NodeAlreadyDraggableException();
        } else {
            node.addAttribute("Draggable", this);
            this.node = node;
            node.addInputEventListener(createEventHandler());            
        }
    }

    /**
     * Return a new drag & drop event handler, that can then be added to a node.
     * It's in this event handler that most of the actual work of dragging and
     * dropping is handled.
     * 
     * @return A new drag & drop event handler.
     */
    private static PDragEventHandler createEventHandler() {       
        PDragEventHandler dragEventHandler = new PDragEventHandler() {
            private Point2D startPos;
            @Override
            protected void startDrag(PInputEvent e) {
                super.startDrag(e);
                startPos = getDraggedNode().getOffset();
            }
            @Override
            protected void endDrag(PInputEvent e) {
                // Find the draggable that was dragged.
                PNode dragNode = getDraggedNode();
                Draggable draggable = (Draggable)
                                             dragNode.getAttribute("Draggable");
                if (draggable == null) {
                    System.out.println("Dragged a node that wasn't wrapped in a draggable?");
                }
                
                // Find the droppable that was dropped onto.
                PPickPath path = e.getInputManager().getMouseOver();
                PNode dropNode = path.getPickedNode();
                // The first node under the mouse is going to be the dragged
                // node, so we move down the pick path until we find the first
                // picked node that is not the dragged node.
                while (dropNode == dragNode) {                    
                    dropNode = path.nextPickedNode();
                }                
                // Find the Droppable object attached to the dropNode
                Droppable droppable =
                                 (Droppable) dropNode.getAttribute("Droppable");                                                
                if (droppable == null) {
                    // No droppable attribute, just return the dragged node to
                    // where it was dragged from.
                    dragNode.setOffset(startPos);
                } else {
                    // Construct a DropEvent to represent the drop.
                    DropEvent de = new DropEvent(draggable,droppable);
                    boolean accepted = droppable.dropped_onto(de);                                        
                    if (accepted) {
                        // The droppable accepted the drop, notify observers.                       
                        draggable.notify_observers(de);
                        droppable.notify_observers(de);
                    }
                    else {
                        // The Droppable refused the dragged node, just return
                        // the dragged node to where it came from.
                        dragNode.setOffset(startPos);
                    }
                }
                super.endDrag(e);
            }
        };
        dragEventHandler.setMoveToFrontOnPress(true);
        return dragEventHandler;
    }
        
    public PNode getNode() {
        return node;
    }
}